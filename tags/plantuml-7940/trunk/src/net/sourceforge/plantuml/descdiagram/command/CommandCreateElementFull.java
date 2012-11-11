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
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
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
				new RegexLeaf("SYMBOL", "(?:(artifact|actor|folder|rect|node|frame|cloud|database|storage|agent|usecase|component|boundary|control|entity|interface|\\(\\))\\s+)?"), //
				new RegexLeaf("\\s*"), //
				new RegexOr(//
						new RegexLeaf("CODE1", CODE_WITH_QUOTE), //
						new RegexConcat(//
								new RegexLeaf("DISPLAY2", DISPLAY), //
								new RegexLeaf("\\s*as\\s+"), //
								new RegexLeaf("CODE2", CODE)), //
						new RegexConcat(//
								new RegexLeaf("CODE3", CODE), //
								new RegexLeaf("\\s+as\\s*"), //
								new RegexLeaf("DISPLAY3", DISPLAY)), //
						new RegexConcat(//
								new RegexLeaf("DISPLAY4", DISPLAY_WITHOUT_QUOTE), //
								new RegexLeaf("\\s*as\\s+"), //
								new RegexLeaf("CODE4", CODE)) //
				), //
				new RegexLeaf("STEREOTYPE", "(?:\\s*(\\<\\<.+\\>\\>))?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("COLOR", "(#\\w+[-\\\\|/]?\\w+)?"), //
				new RegexLeaf("$"));
	}

	private static final String CODE_CORE = "[\\p{L}0-9_.]+|\\(\\)\\s*[\\p{L}0-9_.]+|\\(\\)\\s*\"[^\"]+\"|:[^:]+:|\\([^()]+\\)|\\[[^\\[\\]]+\\]";
	private static final String CODE = "(" + CODE_CORE + ")";
	private static final String CODE_WITH_QUOTE = "(" + CODE_CORE + "|\"[^\"]+\")";

	private static final String DISPLAY_CORE = "\"[^\"]+\"|:[^:]+:|\\([^()]+\\)|\\[[^\\[\\]]+\\]";
	private static final String DISPLAY = "(" + DISPLAY_CORE + ")";
	private static final String DISPLAY_WITHOUT_QUOTE = "(" + DISPLAY_CORE + "|[\\p{L}0-9_.]+)";

	@Override
	final protected boolean isForbidden(String line) {
		if (line.matches("^[\\p{L}0-9_.]+$")) {
			return true;
		}
		return false;
	}

	@Override
	protected CommandExecutionResult executeArg(RegexResult arg) {
		String codeRaw = arg.getLazzy("CODE", 0);
		final String displayRaw = arg.getLazzy("DISPLAY", 0);
		final char codeChar = getCharEncoding(codeRaw);
		final char codeDisplay = getCharEncoding(displayRaw);
		final String symbol;
		if (codeRaw.startsWith("()")) {
			symbol = "interface";
			codeRaw = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(codeRaw.substring(2).trim());
		} else if (codeChar == '(' || codeDisplay == '(') {
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
		} else if (symbol.equalsIgnoreCase("folder")) {
			type = LeafType.COMPONENT2;
			usymbol = USymbol.FOLDER;
		} else if (symbol.equalsIgnoreCase("rect")) {
			type = LeafType.COMPONENT2;
			usymbol = USymbol.RECT;
		} else if (symbol.equalsIgnoreCase("node")) {
			type = LeafType.COMPONENT2;
			usymbol = USymbol.NODE;
		} else if (symbol.equalsIgnoreCase("frame")) {
			type = LeafType.COMPONENT2;
			usymbol = USymbol.FRAME;
		} else if (symbol.equalsIgnoreCase("cloud")) {
			type = LeafType.COMPONENT2;
			usymbol = USymbol.CLOUD;
		} else if (symbol.equalsIgnoreCase("database")) {
			type = LeafType.COMPONENT2;
			usymbol = USymbol.DATABASE;
		} else if (symbol.equalsIgnoreCase("storage")) {
			type = LeafType.COMPONENT2;
			usymbol = USymbol.STORAGE;
		} else if (symbol.equalsIgnoreCase("agent")) {
			type = LeafType.COMPONENT2;
			usymbol = USymbol.RECT;
		} else if (symbol.equalsIgnoreCase("actor")) {
			type = LeafType.COMPONENT2;
			usymbol = USymbol.ACTOR;
		} else if (symbol.equalsIgnoreCase("component")) {
			type = LeafType.COMPONENT2;
			usymbol = getSystem().getSkinParam().useUml2ForComponent() ? USymbol.COMPONENT2 : USymbol.COMPONENT1;
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
		} else if (symbol.equalsIgnoreCase("()")) {
			type = LeafType.COMPONENT2;
			usymbol = USymbol.INTERFACE;
		} else if (symbol.equalsIgnoreCase("usecase")) {
			type = LeafType.USECASE;
			usymbol = null;
		} else {
			throw new IllegalStateException();
		}

		final Code code = Code.of(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(codeRaw));
		String display = displayRaw;
		if (display == null) {
			display = code.getCode();
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

		final String urlString = arg.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(getSystem().getSkinParam().getValue("topurl"), true);
			final Url url = urlBuilder.getUrl(urlString);
			entity.addUrl(url);
		}

		entity.setSpecificBackcolor(HtmlColorUtils.getColorIfValid(arg.get("COLOR", 0)));
		return CommandExecutionResult.ok();
	}

	private char getCharEncoding(final String codeRaw) {
		return codeRaw != null && codeRaw.length() > 2 ? codeRaw.charAt(0) : 0;
	}
}
