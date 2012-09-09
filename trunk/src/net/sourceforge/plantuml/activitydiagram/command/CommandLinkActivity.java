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
 * Revision $Revision: 5024 $
 *
 */
package net.sourceforge.plantuml.activitydiagram.command;

import java.util.Map;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;

public class CommandLinkActivity extends SingleLineCommand2<ActivityDiagram> {

	public CommandLinkActivity(ActivityDiagram diagram) {
		super(diagram, getRegexConcat());
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexOr("FIRST", true, //
						new RegexLeaf("STAR", "(\\(\\*(top)?\\))"), //
						new RegexLeaf("CODE", "([\\p{L}0-9][\\p{L}0-9_.]*)"), //
						new RegexLeaf("BAR", "(?:==+)\\s*([\\p{L}0-9_.]+)\\s*(?:==+)"), //
						new RegexLeaf("QUOTED", "\"([^\"]+)\"(?:\\s+as\\s+([\\p{L}0-9_.]+))?")), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"), //
				new RegexLeaf("\\s*"), //
				// new RegexLeaf("BACKCOLOR", "(#\\w+)?"), //
				new RegexLeaf("BACKCOLOR", "(#\\w+[-\\\\|/]?\\w+)?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("URL", "(" + StringUtils.URL_PATTERN + ")?"), //
				new RegexLeaf("ARROW", "([-=.]+(?:\\*|left|right|up|down|le?|ri?|up?|do?)?[-=.]*\\>)"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("BRACKET", "(?:\\[([^\\]*]+[^\\]]*)\\])?"), //
				new RegexLeaf("\\s*"), //
				new RegexOr("FIRST2", //
						new RegexLeaf("STAR2", "(\\(\\*(top)?\\))"), //
						new RegexLeaf("OPENBRACKET2", "(\\{)"), //
						new RegexLeaf("CODE2", "([\\p{L}0-9][\\p{L}0-9_.]*)"), //
						new RegexLeaf("BAR2", "(?:==+)\\s*([\\p{L}0-9_.]+)\\s*(?:==+)"), //
						new RegexLeaf("QUOTED2", "\"([^\"]+)\"(?:\\s+as\\s+([\\p{L}0-9][\\p{L}0-9_.]*))?"), //
						new RegexLeaf("QUOTED_INVISIBLE2", "(\\w.*?)")), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("STEREOTYPE2", "(\\<\\<.*\\>\\>)?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("PARTITION2", "(?:in\\s+(\"[^\"]+\"|\\S+))?"), //
				new RegexLeaf("\\s*"), //
				// new RegexLeaf("BACKCOLOR2", "(#\\w+)?"), //
				new RegexLeaf("BACKCOLOR2", "(#\\w+[-\\\\|/]?\\w+)?"), //
				new RegexLeaf("$"));
	}

	@Override
	protected CommandExecutionResult executeArg(Map<String, RegexPartialMatch> arg2) {
		final IEntity entity1 = getEntity(getSystem(), arg2, true);
		if (entity1 == null) {
			return CommandExecutionResult.error("No such activity");
		}
		if (arg2.get("STEREOTYPE").get(0) != null) {
			entity1.setStereotype(new Stereotype(arg2.get("STEREOTYPE").get(0)));
		}
		if (arg2.get("BACKCOLOR").get(0) != null) {
			entity1.setSpecificBackcolor(HtmlColorUtils.getColorIfValid(arg2.get("BACKCOLOR").get(0)));
		}

		final IEntity entity2 = getEntity(getSystem(), arg2, false);
		if (entity2 == null) {
			return CommandExecutionResult.error("No such activity");
		}
		if (arg2.get("BACKCOLOR2").get(0) != null) {
			entity2.setSpecificBackcolor(HtmlColorUtils.getColorIfValid(arg2.get("BACKCOLOR2").get(0)));
		}
		if (arg2.get("STEREOTYPE2").get(0) != null) {
			entity2.setStereotype(new Stereotype(arg2.get("STEREOTYPE2").get(0)));
		}

		final String linkLabel = arg2.get("BRACKET").get(0);

		final String arrow = StringUtils.manageArrowForCuca(arg2.get("ARROW").get(0));
		int lenght = arrow.length() - 1;
		if (arg2.get("ARROW").get(0).contains("*")) {
			lenght = 2;
		}

		LinkType type = new LinkType(LinkDecor.ARROW, LinkDecor.NONE);
		if (arg2.get("ARROW").get(0).contains(".")) {
			type = type.getDotted();
		}

		Link link = new Link(entity1, entity2, type, linkLabel, lenght);
		if (arg2.get("ARROW").get(0).contains("*")) {
			link.setConstraint(false);
		}
		final Direction direction = StringUtils.getArrowDirection(arg2.get("ARROW").get(0));
		if (direction == Direction.LEFT || direction == Direction.UP) {
			link = link.getInv();
		}
		if (arg2.get("URL").get(0) != null) {
			final Url urlLink = StringUtils.extractUrl(getSystem().getSkinParam().getValue("topurl"), arg2.get("URL")
					.get(0), true);
			link.setUrl(urlLink);
		}

		getSystem().addLink(link);

		return CommandExecutionResult.ok();

	}

	static IEntity getEntity(ActivityDiagram system, Map<String, RegexPartialMatch> arg, final boolean start) {
		final String suf = start ? "" : "2";

		final RegexPartialMatch openBracket = arg.get("OPENBRACKET" + suf);
		if (openBracket != null && openBracket.get(0) != null) {
			return system.createInnerActivity();
		}
		if (arg.get("STAR" + suf).get(0) != null) {
			if (start) {
				if (arg.get("STAR" + suf).get(1) != null) {
					system.getStart().setTop(true);
				}
				return system.getStart();
			}
			return system.getEnd();
		}
		String partition = null;
		if (arg.get("PARTITION" + suf) != null) {
			partition = arg.get("PARTITION" + suf).get(0);
			if (partition != null) {
				partition = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(partition);
			}
		}
		final String code = arg.get("CODE" + suf).get(0);
		if (code != null) {
			if (partition != null) {
				system.getOrCreateGroup(partition, StringUtils.getWithNewlines(partition), null, GroupType.PACKAGE,
						system.getRootGroup());
			}
			final IEntity result = system.getOrCreate(code, StringUtils.getWithNewlines(code),
					getTypeIfExisting(system, code));
			if (partition != null) {
				system.endGroup();
			}
			return result;
		}
		final String bar = arg.get("BAR" + suf).get(0);
		if (bar != null) {
			return system.getOrCreate(bar, StringUtils.getWithNewlines(bar), LeafType.SYNCHRO_BAR);
		}
		final RegexPartialMatch quoted = arg.get("QUOTED" + suf);
		if (quoted.get(0) != null) {
			final String quotedCode = quoted.get(1) == null ? quoted.get(0) : quoted.get(1);
			if (partition != null) {
				system.getOrCreateGroup(partition, StringUtils.getWithNewlines(partition), null, GroupType.PACKAGE,
						system.getRootGroup());
			}
			final IEntity result = system.getOrCreate(quotedCode, StringUtils.getWithNewlines(quoted.get(0)),
					getTypeIfExisting(system, quotedCode));
			if (partition != null) {
				system.endGroup();
			}
			return result;
		}
		final RegexPartialMatch quotedInvisible = arg.get("QUOTED_INVISIBLE" + suf);
		if (quotedInvisible != null && quotedInvisible.get(0) != null) {
			final String s = quotedInvisible.get(0);
			if (partition != null) {
				system.getOrCreateGroup(partition, StringUtils.getWithNewlines(partition), null, GroupType.PACKAGE,
						system.getRootGroup());
			}
			final IEntity result = system.getOrCreate(s, StringUtils.getWithNewlines(s), LeafType.ACTIVITY);
			if (partition != null) {
				system.endGroup();
			}
			return result;
		}
		final String first = arg.get("FIRST" + suf).get(0);
		if (first == null) {
			return system.getLastEntityConsulted();
		}

		return null;
	}

	static LeafType getTypeIfExisting(ActivityDiagram system, String code) {
		if (system.leafExist(code)) {
			final IEntity ent = system.getLeafs().get(code);
			if (ent.getEntityType() == LeafType.BRANCH) {
				return LeafType.BRANCH;
			}
		}
		return LeafType.ACTIVITY;
	}

	static LeafType getTypeFromString(String type, final LeafType circle) {
		if (type == null) {
			return LeafType.ACTIVITY;
		}
		if (type.equals("*")) {
			return circle;
		}
		if (type.startsWith("=")) {
			return LeafType.SYNCHRO_BAR;
		}
		throw new IllegalArgumentException();
	}

}
