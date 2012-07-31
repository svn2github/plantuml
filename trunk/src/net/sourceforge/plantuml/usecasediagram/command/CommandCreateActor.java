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
 */
package net.sourceforge.plantuml.usecasediagram.command;

import java.util.Map;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.usecasediagram.UsecaseDiagram;

public class CommandCreateActor extends AbstractCommandCreate {

	public CommandCreateActor(UsecaseDiagram usecaseDiagram) {
		super(usecaseDiagram, getRegexConcat());
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("(?:actor\\s+)?"), //
				new RegexLeaf("STRING1", getStringPattern()), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("STRING2", "(?:as\\s+" + getStringPattern() + ")?"), //
				new RegexLeaf("STEREO", "(?:\\s*([\\<\\[]{2}.*[\\>\\]]{2}))?"), //
				new RegexLeaf("$"));
	}

	private static String getStringPattern() {
		return "([\\p{L}0-9_.]+|:[^:]+:|\"[^\"]+\")";
	}

	@Override
	protected CommandExecutionResult executeArg(Map<String, RegexPartialMatch> arg) {
		return executeArg(arg, LeafType.ACTOR);
	}

	@Override
	protected boolean defineCode(String s) {
		return s.matches(":?[\\p{L}0-9_.]+:?");
	}

}
