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
 * Revision $Revision: 4762 $
 *
 */
package net.sourceforge.plantuml.statediagram.command;

import java.util.Map;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.statediagram.StateDiagram;

public class CommandCreatePackageState extends SingleLineCommand2<StateDiagram> {

	public CommandCreatePackageState(StateDiagram diagram) {
		super(diagram, getRegexConcat());
		// super(diagram,
		// "(?i)^state\\s+([\\p{L}0-9_.]+)\\s+as\\s+\"([^\"]+)\"\\s*(\\<\\<.*\\>\\>)?\\s*(#\\w+)?(?:\\s*\\{|\\s+begin)$");
	}

	// "");

	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^state\\s+"), //
				new RegexOr(//
						new RegexConcat(//
								new RegexLeaf("CODE1", "([\\p{L}0-9_.]+)\\s+"), //
								new RegexLeaf("DISPLAY1", "as\\s+\"([^\"]+)\"")), //
						new RegexConcat(//
								new RegexLeaf("DISPLAY2", "(?:\"([^\"]+)\"\\s+as\\s+)?"), //
								new RegexLeaf("CODE2", "([\\p{L}0-9_.]+)"))), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("COLOR", "(#\\w+)?"), //
				new RegexLeaf("(?:\\s*\\{|\\s+begin)$"));
	}

	private String getNotNull(Map<String, RegexPartialMatch> arg, String v1, String v2) {
		if (arg.get(v1).get(0) == null) {
			return arg.get(v2).get(0);
		}
		return arg.get(v1).get(0);
	}

	@Override
	protected CommandExecutionResult executeArg(Map<String, RegexPartialMatch> arg) {
		final IGroup currentPackage = getSystem().getCurrentGroup();
		final String code = getNotNull(arg, "CODE1", "CODE2");
		String display = getNotNull(arg, "DISPLAY1", "DISPLAY2");
		if (display == null) {
			display = code;
		}
		final IEntity p = getSystem().getOrCreateGroup(code, StringUtils.getWithNewlines(display), null, GroupType.STATE, currentPackage);
		final String stereotype = arg.get("STEREOTYPE").get(0);
		if (stereotype != null) {
			p.setStereotype(new Stereotype(stereotype));
		}
		final String color = arg.get("COLOR").get(0);
		if (HtmlColorUtils.getColorIfValid(color) != null) {
			p.setSpecificBackcolor(HtmlColorUtils.getColorIfValid(color));
		}
		return CommandExecutionResult.ok();
	}

}
