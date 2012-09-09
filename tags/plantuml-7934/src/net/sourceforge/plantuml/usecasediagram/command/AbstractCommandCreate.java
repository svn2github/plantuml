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

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.usecasediagram.UsecaseDiagram;

abstract class AbstractCommandCreate extends SingleLineCommand2<UsecaseDiagram> {

	public AbstractCommandCreate(UsecaseDiagram usecaseDiagram, RegexConcat regexConcat) {
		super(usecaseDiagram, regexConcat);
	}

	@Override
	final protected boolean isForbidden(String line) {
		if (line.matches("^[\\p{L}0-9_.]+$")) {
			return true;
		}
		return false;
	}

	final protected CommandExecutionResult executeArg(Map<String, RegexPartialMatch> arg, LeafType type) {
		final String code;
		final String display;
		final String string1 = arg.get("STRING1").get(0);
		final String string2 = arg.get("STRING2").get(0);
		if (string2 == null) {
			code = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(string1);
			display = code;
		} else if (defineCode(string1) && defineCode(string2)==false) {
			display = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(string2);
			code = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(string1);
		} else {
			display = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(string1);
			code = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(string2);
		}

		final IEntity entity = getSystem().getOrCreateLeaf(code, type);
		entity.setDisplay(StringUtils.getWithNewlines(display));
		final String stereotype = arg.get("STEREO").get(0);
		if (stereotype != null) {
			entity.setStereotype(new Stereotype(stereotype, getSystem().getSkinParam().getCircledCharacterRadius(),
					getSystem().getSkinParam().getFont(FontParam.CIRCLED_CHARACTER, null)));
		}
		return CommandExecutionResult.ok();
	}

	
	abstract protected boolean defineCode(String s);

}
