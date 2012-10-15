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
 * Revision $Revision: 7715 $
 *
 */
package net.sourceforge.plantuml.descdiagram.command;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagram;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.USymbol;

public class CommandCreateElementFull extends SingleLineCommand2<DescriptionDiagram> {

	public CommandCreateElementFull(DescriptionDiagram system) {
		super(system, getRegexConcat());
	}

	private static RegexConcat getRegexConcat() {
		return new RegexConcat(
				new RegexLeaf("^"), //
				new RegexLeaf("SYMBOL", "(?:(artifact|actor|usecase|component|boundary|control|entity|interface)\\s+)?"), //
				new RegexLeaf("\\s*"), //
				new RegexOr(//
						new RegexLeaf("CODE3", CODE), //
						new RegexConcat(//
								new RegexLeaf("DISPLAY1", DISPLAY), //
								new RegexLeaf("\\s*as\\s+"), //
								new RegexLeaf("CODE1", CODE)), //
						new RegexConcat(//
								new RegexLeaf("CODE2", CODE), //
								new RegexLeaf("\\s*as\\s+"), //
								new RegexLeaf("DISPLAY2", DISPLAY))), //
				new RegexLeaf("STEREOTYPE", "(?:\\s*([\\<\\[]{2}.*[\\>\\]]{2}))?"), //
				new RegexLeaf("COLOR", "\\s*(#\\w+[-\\\\|/]?\\w+)?"), //
				new RegexLeaf("$"));
	}

	private static final String CODE = "([\\p{L}0-9_.]+|:[^:]+:|\\([^()]+\\)|\\[[^\\[\\]]+\\])";
	private static final String DISPLAY = "(\"[^\"]+\"|:[^:]+:|\\[[^\\[\\]]+\\])";

	@Override
	protected CommandExecutionResult executeArg(RegexResult arg) {
		final String codeRaw = arg.getLazzy("CODE", 0);
		final String displayRaw = arg.getLazzy("DISPLAY", 0);
		final char codeChar = getCharEncoding(codeRaw);
		final char codeDisplay = getCharEncoding(displayRaw);
		final String symbol;
		if (codeChar == '(' || codeDisplay == '(') {
			symbol = "usecase";
		} else if (codeChar == ':' || codeDisplay == ':') {
			symbol = "actor";
		} else if (codeChar == '[' || codeDisplay == '[') {
			symbol = "component";
		} else {
			symbol = arg.get("SYMBOL", 0);
		}

		final LeafType type;
		final USymbol usymbol;

		if (symbol == null) {
			type = LeafType.COMPONENT2;
			usymbol = USymbol.ACTOR;
		} else if (symbol.equalsIgnoreCase("artifact")) {
			type = LeafType.COMPONENT2;
			usymbol = USymbol.ARTIFACT;
		} else if (symbol.equalsIgnoreCase("actor")) {
			type = LeafType.COMPONENT2;
			usymbol = USymbol.ACTOR;
		} else if (symbol.equalsIgnoreCase("component")) {
			type = LeafType.COMPONENT2;
			usymbol = USymbol.COMPONENT1;
		} else if (symbol.equalsIgnoreCase("boundary")) {
			type = LeafType.COMPONENT2;
			usymbol = USymbol.BOUNDARY;
		} else if (symbol.equalsIgnoreCase("control")) {
			type = LeafType.COMPONENT2;
			usymbol = USymbol.CONTROL;
		} else if (symbol.equalsIgnoreCase("entity")) {
			type = LeafType.COMPONENT2;
			usymbol = USymbol.ENTITY_DOMAIN;
		} else if (symbol.equalsIgnoreCase("interface")) {
			type = LeafType.COMPONENT2;
			usymbol = USymbol.INTERFACE;
		} else if (symbol.equalsIgnoreCase("usecase")) {
			type = LeafType.USECASE;
			usymbol = null;
		} else {
			throw new IllegalStateException();
		}

		final String code = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(codeRaw);
		String display = displayRaw;
		if (display == null) {
			display = code;
		}
		display = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(display);
		final String stereotype = arg.get("STEREOTYPE", 0);
		final IEntity entity = getSystem().getOrCreateLeaf(code, type);
		entity.setDisplay(StringUtils.getWithNewlines(display));
		entity.setUSymbol(usymbol);
		if (stereotype != null) {
			entity.setStereotype(new Stereotype(stereotype, getSystem().getSkinParam().getCircledCharacterRadius(),
					getSystem().getSkinParam().getFont(FontParam.CIRCLED_CHARACTER, null)));
		}
		entity.setSpecificBackcolor(HtmlColorUtils.getColorIfValid(arg.get("COLOR", 0)));
		return CommandExecutionResult.ok();
	}

	private char getCharEncoding(final String codeRaw) {
		return codeRaw != null && codeRaw.length() > 2 ? codeRaw.charAt(0) : 0;
	}
}
