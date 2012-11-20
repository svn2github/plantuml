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
 * Revision $Revision: 4161 $
 *
 */
package net.sourceforge.plantuml.descdiagram.command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagram;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;

public class CommandCreateElementMultilines extends CommandMultilines2<DescriptionDiagram> {

	enum Mode {
		EXTENDS, IMPLEMENTS
	};

	public CommandCreateElementMultilines(DescriptionDiagram diagram) {
		super(diagram, getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE);
	}

	@Override
	public String getPatternEnd() {
		return "(?i)^(.*)\"$";
	}

	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("TYPE", "(usecase)\\s+"), //
				new RegexLeaf("CODE", "([\\p{L}0-9_.]+)"), //
				new RegexLeaf("STEREO", "(?:\\s*(\\<\\<.+\\>\\>))?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("COLOR", "(#\\w+[-\\\\|/]?\\w+)?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("DESC", "as\\s*\"(.*)$"));
	}

	public CommandExecutionResult executeNow(List<String> lines) {
		StringUtils.trim(lines, false);
		final RegexResult line0 = getStartingPattern().matcher(lines.get(0).trim());
		final LeafType type = LeafType.getLeafType(line0.get("TYPE", 0).toUpperCase());
		final Code code = Code.of(line0.get("CODE", 0));
		final List<String> display = new ArrayList<String>(lines.subList(1, lines.size() - 1));
		final String descStart = line0.get("DESC", 0);
		if (StringUtils.isNotEmpty(descStart)) {
			display.add(0, descStart);
		}

		final List<String> lineLast = StringUtils.getSplit(Pattern.compile(getPatternEnd()), lines
				.get(lines.size() - 1));
		if (StringUtils.isNotEmpty(lineLast.get(0))) {
			display.add(lineLast.get(0));
		}

		final String stereotype = line0.get("STEREO", 0);

		final ILeaf result = getSystem().createLeaf(code, display, type);
		if (stereotype != null) {
			result.setStereotype(new Stereotype(stereotype, getSystem().getSkinParam().getCircledCharacterRadius(),
					getSystem().getSkinParam().getFont(FontParam.CIRCLED_CHARACTER, null)));
		}

		result.setSpecificBackcolor(HtmlColorUtils.getColorIfValid(line0.get("COLOR", 0)));

		return CommandExecutionResult.ok();
	}

}
