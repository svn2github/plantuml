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
 * Revision $Revision: 7558 $
 *
 */
package net.sourceforge.plantuml.command.note;

import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UniqueSequence;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;

public final class FactoryNoteActivityCommand implements SingleMultiFactoryCommand<ActivityDiagram> {

	private RegexConcat getRegexConcatMultiLine() {
		return new RegexConcat(new RegexLeaf("^note\\s+"), //
				new RegexLeaf("POSITION", "(right|left|top|bottom)\\s*"), //
				new RegexLeaf("COLOR", "(#\\w+)?\\s*"), //
				new RegexLeaf("$"));
	}

	private RegexConcat getRegexConcatSingleLine() {
		return new RegexConcat(new RegexLeaf("^note\\s+"), //
				new RegexLeaf("POSITION", "(right|left|top|bottom)\\s*"), //
				new RegexLeaf("COLOR", "(#\\w+)?\\s*:\\s*"), //
				new RegexLeaf("NOTE", "(.*)"), //
				new RegexLeaf("$"));
	}

	public Command createMultiLine(final ActivityDiagram system) {
		return new CommandMultilines2<ActivityDiagram>(system, getRegexConcatMultiLine()) {

			@Override
			public String getPatternEnd() {
				return "(?i)^end ?note$";
			}

			public final CommandExecutionResult execute(List<String> lines) {
				// StringUtils.trim(lines, true);
				final RegexResult arg = getStartingPattern().matcher(lines.get(0).trim());
				List<String> strings = StringUtils.removeEmptyColumns(lines.subList(1, lines.size() - 1));

				Url url = null;
				if (strings.size() > 0) {
					url = StringUtils.extractUrl(getSystem().getSkinParam().getValue("topurl"), strings.get(0), true);
				}
				if (url != null) {
					strings = strings.subList(1, strings.size());
				}

				// final String s = StringUtils.getMergedLines(strings);

				final IEntity note = getSystem().createLeaf("GMN" + UniqueSequence.getValue(), strings, LeafType.NOTE);
				if (url != null) {
					note.addUrl(url);
				}
				return executeInternal(getSystem(), arg, note);
			}
		};
	}

	public Command createSingleLine(final ActivityDiagram system) {
		return new SingleLineCommand2<ActivityDiagram>(system, getRegexConcatSingleLine()) {

			@Override
			protected CommandExecutionResult executeArg(RegexResult arg) {
				final IEntity note = getSystem().createNote("GN" + UniqueSequence.getValue(),
						StringUtils.getWithNewlines(arg.get("NOTE", 0)));
				return executeInternal(getSystem(), arg, note);
			}
		};
	}

	private CommandExecutionResult executeInternal(ActivityDiagram system, RegexResult arg,
			IEntity note) {

		note.setSpecificBackcolor(HtmlColorUtils.getColorIfValid(arg.get("COLOR", 0)));

		IEntity activity = system.getLastEntityConsulted();
		if (activity == null) {
			activity = system.getStart();
		}

		final Link link;

		final Position position = Position.valueOf(arg.get("POSITION", 0).toUpperCase()).withRankdir(
				system.getRankdir());

		final LinkType type = new LinkType(LinkDecor.NONE, LinkDecor.NONE).getDashed();

		if (position == Position.RIGHT) {
			link = new Link(activity, note, type, null, 1);
		} else if (position == Position.LEFT) {
			link = new Link(note, activity, type, null, 1);
		} else if (position == Position.BOTTOM) {
			link = new Link(activity, note, type, null, 2);
		} else if (position == Position.TOP) {
			link = new Link(note, activity, type, null, 2);
		} else {
			throw new IllegalArgumentException();
		}
		system.addLink(link);
		return CommandExecutionResult.ok();
	}

}
