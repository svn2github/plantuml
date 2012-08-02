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
 * Revision $Revision: 7800 $
 *
 */
package net.sourceforge.plantuml.command;

import java.util.Map;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UniqueSequence;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;

public class CommandPackage extends SingleLineCommand2<AbstractEntityDiagram> {

	public CommandPackage(AbstractEntityDiagram diagram) {
		super(diagram, getRegexConcat());
	}
	
	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^package\\s+"), //
				new RegexLeaf("NAME", "(\"[^\"]+\"|[^#\\s{}]*)"), //
				new RegexLeaf("AS", "(?:\\s+as\\s+([\\p{L}0-9_.]+))?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("COLOR", "(#[0-9a-fA-F]{6}|#?\\w+)?"), //
				new RegexLeaf("\\s*\\{?$"));
	}

	@Override
	protected CommandExecutionResult executeArg(Map<String, RegexPartialMatch> arg) {
		final String code;
		final String display;
		final String name = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("NAME").get(0));
		if (arg.get("AS").get(0) == null) {
			if (name.length() == 0) {
				code = "##" + UniqueSequence.getValue();
				display = null;
			} else {
				code = name;
				display = code;
			}
		} else {
			display = name;
			code = arg.get("AS").get(0);
		}
		final IGroup currentPackage = getSystem().getCurrentGroup();
		final IEntity p = getSystem().getOrCreateGroup(code, StringUtils.getWithNewlines(display), null, GroupType.PACKAGE, currentPackage);
		final String stereotype = arg.get("STEREOTYPE").get(0);
		if (stereotype != null) {
			p.setStereotype(new Stereotype(stereotype));
		}
		final String color = arg.get("COLOR").get(0);
		if (color != null) {
			p.setSpecificBackcolor(HtmlColorUtils.getColorIfValid(color));
		}
		return CommandExecutionResult.ok();
	}

}
