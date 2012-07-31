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
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.statediagram.command;

import java.util.Map;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.statediagram.StateDiagram;

public class CommandCreateState extends SingleLineCommand2<StateDiagram> {

	public CommandCreateState(StateDiagram diagram) {
		super(diagram, getRegexConcat()); 
	}
	
	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("(?:state\\s+)"), //
				new RegexLeaf("DISPLAY", "(?:\"([^\"]+)\"\\s+as\\s+)?"), //
				new RegexLeaf("CODE", "([\\p{L}0-9_.]+)"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("COLOR", "(#\\w+[-\\\\|/]?\\w+)?"), //
				new RegexLeaf("$"));
	}

	@Override
	protected CommandExecutionResult executeArg(Map<String, RegexPartialMatch> arg2) {
		String display = arg2.get("DISPLAY").get(0);
		final String code = arg2.get("CODE").get(0);
		if (display == null) {
			display = code;
		}
		final IEntity ent = getSystem().getOrCreateClass(code);
		ent.setDisplay(StringUtils.getWithNewlines(display));

		final String stereotype = arg2.get("STEREOTYPE").get(0);
		if (stereotype != null) {
			ent.setStereotype(new Stereotype(stereotype));
		}
		final String color = arg2.get("COLOR").get(0);
		if (color!=null) {
			ent.setSpecificBackcolor(HtmlColorUtils.getColorIfValid(color));
		}
		return CommandExecutionResult.ok();
	}

}
