/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2012, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 5075 $
 *
 */
package net.sourceforge.plantuml.classdiagram.command;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;

public class CommandCreateClass extends SingleLineCommand2<ClassDiagram> {

	enum Mode {
		EXTENDS, IMPLEMENTS
	};

	public CommandCreateClass(ClassDiagram diagram) {
		super(diagram, getRegexConcat());
	}

	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("TYPE", //
						"(interface|enum|abstract\\s+class|abstract|class)\\s+"), //
				new RegexOr(new RegexLeaf("NAME1",
						"(?:\"([^\"]+)\"\\s+as\\s+)?((?:\\.|::)?[\\p{L}0-9_]+(?:(?:\\.|::)[\\p{L}0-9_]+)*)"), //
						new RegexLeaf("NAME2", "((?:\\.|::)?[\\p{L}0-9_]+(?:(?:\\.|::)[\\p{L}0-9_]+)*)\\s+as\\s+\"([^\"]+)\""), //
						new RegexLeaf("NAME3", "\"([^\"]+)\"")), //
				new RegexLeaf("GENERIC", "(?:\\s*\\<(" + GenericRegexProducer.PATTERN + ")\\>)?"), //
				new RegexLeaf("STEREO", "(?:\\s*(\\<{2}.*\\>{2}))?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("COLOR", "(#\\w+[-\\\\|/]?\\w+)?"), //
				new RegexLeaf("EXTENDS", "(\\s+(extends|implements)\\s+(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*))?"), //
				new RegexLeaf("$"));
	}

	@Override
	protected CommandExecutionResult executeArg(RegexResult arg) {
		final LeafType type = LeafType.getLeafType(arg.get("TYPE", 0).toUpperCase());
		final Code code;
		final String display;
		if (arg.get("NAME1", 1) != null) {
			code = Code.of(arg.get("NAME1", 1));
			display = arg.get("NAME1", 0);
		} else if (arg.get("NAME3", 0) != null) {
			code = Code.of(arg.get("NAME3", 0));
			display = arg.get("NAME3", 0);
		} else {
			code = Code.of(arg.get("NAME2", 0));
			display = arg.get("NAME2", 1);
		}
		final String stereotype = arg.get("STEREO", 0);
		final String generic = arg.get("GENERIC", 0);
		final ILeaf entity;
		if (getSystem().leafExist(code)) {
			entity = getSystem().getOrCreateLeaf(code, type);
			entity.muteToType(type);
		} else {
			entity = getSystem().createLeaf(code, StringUtils.getWithNewlines(display), type);
		}
		if (stereotype != null) {
			entity.setStereotype(new Stereotype(stereotype, getSystem().getSkinParam().getCircledCharacterRadius(),
					getSystem().getSkinParam().getFont(FontParam.CIRCLED_CHARACTER, null)));
		}
		if (generic != null) {
			entity.setGeneric(generic);
		}

		final String urlString = arg.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(getSystem().getSkinParam().getValue("topurl"), true);
			final Url url = urlBuilder.getUrl(urlString);
			entity.addUrl(url);
		}

		entity.setSpecificBackcolor(HtmlColorUtils.getColorIfValid(arg.get("COLOR", 0)));
		manageExtends(getSystem(), arg, entity);

		return CommandExecutionResult.ok();
	}

	public static void manageExtends(ClassDiagram system, RegexResult arg, final IEntity entity) {
		if (arg.get("EXTENDS", 1) != null) {
			final Mode mode = arg.get("EXTENDS", 1).equalsIgnoreCase("extends") ? Mode.EXTENDS : Mode.IMPLEMENTS;
			final Code other = Code.of(arg.get("EXTENDS", 2));
			LeafType type2 = LeafType.CLASS;
			if (mode == Mode.IMPLEMENTS) {
				type2 = LeafType.INTERFACE;
			}
			if (mode == Mode.EXTENDS && entity.getEntityType() == LeafType.INTERFACE) {
				type2 = LeafType.INTERFACE;
			}
			final IEntity cl2 = system.getOrCreateClass(other, type2);
			LinkType typeLink = new LinkType(LinkDecor.NONE, LinkDecor.EXTENDS);
			if (type2 == LeafType.INTERFACE && entity.getEntityType() != LeafType.INTERFACE) {
				typeLink = typeLink.getDashed();
			}
			final Link link = new Link(cl2, entity, typeLink, null, 2, null, null, system.getLabeldistance(),
					system.getLabelangle());
			system.addLink(link);
		}
	}


}
