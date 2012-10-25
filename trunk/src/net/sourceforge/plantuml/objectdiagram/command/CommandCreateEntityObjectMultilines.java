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
package net.sourceforge.plantuml.objectdiagram.command;

import java.util.List;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.objectdiagram.ObjectDiagram;
import net.sourceforge.plantuml.skin.VisibilityModifier;

public class CommandCreateEntityObjectMultilines extends CommandMultilines2<ObjectDiagram> {

	public CommandCreateEntityObjectMultilines(ObjectDiagram diagram) {
		super(diagram, getRegexConcat());
	}

	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("TYPE", "(object)\\s+"), //
				new RegexLeaf("NAME", "(?:\"([^\"]+)\"\\s+as\\s+)?([\\p{L}0-9_.]+)"), //
				new RegexLeaf("STEREO", "(?:\\s*([\\<\\[]{2}.*[\\>\\]]{2}))?"), //
				new RegexLeaf("COLOR", "\\s*(#\\w+[-\\\\|/]?\\w+)?"), //
				new RegexLeaf("\\s*\\{\\s*$"));
	}

	@Override
	public String getPatternEnd() {
		return "(?i)^\\s*\\}\\s*$";
	}

	public CommandExecutionResult execute(List<String> lines) {
		StringUtils.trim(lines, true);
		final RegexResult line0 = getStartingPattern().matcher(lines.get(0).trim());
		final IEntity entity = executeArg0(line0);
		if (entity == null) {
			return CommandExecutionResult.error("No such entity");
		}
		for (String s : lines.subList(1, lines.size() - 1)) {
			assert s.length() > 0;
			if (VisibilityModifier.isVisibilityCharacter(s.charAt(0))) {
				getSystem().setVisibilityModifierPresent(true);
			}
			entity.addFieldOrMethod(s);
		}
		return CommandExecutionResult.ok();
	}

	private IEntity executeArg0(RegexResult line0) {
		final String code = line0.get("NAME", 1);
		final String display = line0.get("NAME", 0);
		final String stereotype = line0.get("STEREO", 0);
		if (getSystem().leafExist(code)) {
			return getSystem().getOrCreateClass(code);
		}
		final IEntity entity = getSystem().createLeaf(code, StringUtils.getWithNewlines(display), LeafType.OBJECT);
		if (stereotype != null) {
			entity.setStereotype(new Stereotype(stereotype, getSystem().getSkinParam().getCircledCharacterRadius(),
					getSystem().getSkinParam().getFont(FontParam.CIRCLED_CHARACTER, null)));
		}
		entity.setSpecificBackcolor(HtmlColorUtils.getColorIfValid(line0.get("COLOR", 0)));
		return entity;
	}

}
