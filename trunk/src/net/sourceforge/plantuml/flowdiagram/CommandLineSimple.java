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
package net.sourceforge.plantuml.flowdiagram;

import java.util.Map;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
import net.sourceforge.plantuml.golem.TileGeometry;

public class CommandLineSimple extends SingleLineCommand2<FlowDiagram> {

	public CommandLineSimple(FlowDiagram diagram) {
		super(diagram, getRegexConcat());
	}

	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("ORIENTATION", "(?:([nsew])\\s+)?"), //
				new RegexLeaf("ID_DEST", "(\\w+)"), //
				new RegexLeaf("\\s+"), //
				new RegexLeaf("LABEL", "\"(.*)\""), //
				new RegexLeaf("$"));
	}

	@Override
	protected CommandExecutionResult executeArg(Map<String, RegexPartialMatch> arg) {
		final String idDest = arg.get("ID_DEST").get(0);
		final String label = arg.get("LABEL").get(0);
		final String orientationString = arg.get("ORIENTATION").get(0);
		TileGeometry orientation = TileGeometry.SOUTH;
		if (orientationString != null) {
			orientation = TileGeometry.fromString(orientationString);
		}
		getSystem().lineSimple(orientation, idDest, label);
		return CommandExecutionResult.ok();
	}

}
