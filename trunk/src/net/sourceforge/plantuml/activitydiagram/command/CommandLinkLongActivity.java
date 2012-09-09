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
 * Revision $Revision: 5031 $
 *
 */
package net.sourceforge.plantuml.activitydiagram.command;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;

public class CommandLinkLongActivity extends CommandMultilines2<ActivityDiagram> {

	public CommandLinkLongActivity(final ActivityDiagram diagram) {
		super(diagram, getRegexConcat());
	}

	@Override
	public String getPatternEnd() {
		return "(?i)^\\s*([^\"]*)\"(?:\\s+as\\s+([\\p{L}0-9][\\p{L}0-9_.]*))?\\s*(\\<\\<.*\\>\\>)?\\s*(?:in\\s+(\"[^\"]+\"|\\S+))?\\s*(#\\w+)?$";
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexOr("FIRST", true, //
						new RegexLeaf("STAR", "(\\(\\*(top)?\\))"), //
						new RegexLeaf("CODE", "([\\p{L}0-9][\\p{L}0-9_.]*)"), //
						new RegexLeaf("BAR", "(?:==+)\\s*([\\p{L}0-9_.]+)\\s*(?:==+)"), //
						new RegexLeaf("QUOTED", "\"([^\"]+)\"(?:\\s+as\\s+([\\p{L}0-9_.]+))?")), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("BACKCOLOR", "(#\\w+)?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("URL", "(" + StringUtils.URL_PATTERN + ")?"), //
				new RegexLeaf("ARROW", "([-=.]+(?:(left|right|up|down|le?|ri?|up?|do?)(?=[-=.]))?[-=.]*\\>)"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("BRACKET", "(?:\\[([^\\]*]+[^\\]]*)\\])?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("DESC", "\"([^\"]*?)"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("$"));
	}

	public CommandExecutionResult execute(List<String> lines) {
		StringUtils.trim(lines, false);
		final Map<String, RegexPartialMatch> line0 = getStartingPattern().matcher(lines.get(0).trim());

		final IEntity entity1 = CommandLinkActivity.getEntity(getSystem(), line0, true);

		if (line0.get("STEREOTYPE").get(0) != null) {
			entity1.setStereotype(new Stereotype(line0.get("STEREOTYPE").get(0)));
		}
		if (line0.get("BACKCOLOR").get(0) != null) {
			entity1.setSpecificBackcolor(HtmlColorUtils.getColorIfValid(line0.get("BACKCOLOR").get(0)));
		}
		final StringBuilder sb = new StringBuilder();

		final String desc0 = line0.get("DESC").get(0);
		Url urlActivity = null;
		if (StringUtils.isNotEmpty(desc0)) {
			urlActivity = StringUtils.extractUrl(getSystem().getSkinParam().getValue("topurl"), desc0, true);
			if (urlActivity == null) {
				sb.append(desc0);
				sb.append("\\n");
			}
		}
		for (int i = 1; i < lines.size() - 1; i++) {
			if (i == 1 && urlActivity == null) {
				urlActivity = StringUtils.extractUrl(getSystem().getSkinParam().getValue("topurl"), lines.get(i), true);
				if (urlActivity != null) {
					continue;
				}
			}
			sb.append(lines.get(i));
			if (i < lines.size() - 2) {
				sb.append("\\n");
			}
		}

		final List<String> lineLast = StringUtils.getSplit(Pattern.compile(getPatternEnd()),
				lines.get(lines.size() - 1));
		if (StringUtils.isNotEmpty(lineLast.get(0))) {
			if (sb.length() > 0 && sb.toString().endsWith("\\n") == false) {
				sb.append("\\n");
			}
			sb.append(lineLast.get(0));
		}

		final String display = sb.toString();
		final String code = lineLast.get(1) == null ? display : lineLast.get(1);

		String partition = null;
		if (lineLast.get(3) != null) {
			partition = lineLast.get(3);
			partition = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(partition);
		}
		if (partition != null) {
			getSystem().getOrCreateGroup(partition, StringUtils.getWithNewlines(partition), null, GroupType.PACKAGE,
					null);
		}
		final IEntity entity2 = getSystem().createLeaf(code, StringUtils.getWithNewlines(display), LeafType.ACTIVITY);
		if (partition != null) {
			getSystem().endGroup();
		}
		if (urlActivity != null) {
			entity2.addUrl(urlActivity);
		}

		if (lineLast.get(2) != null) {
			entity2.setStereotype(new Stereotype(lineLast.get(2)));
		}
		if (lineLast.get(4) != null) {
			entity2.setSpecificBackcolor(HtmlColorUtils.getColorIfValid(lineLast.get(4)));
		}

		if (entity1 == null || entity2 == null) {
			return CommandExecutionResult.error("No such entity");
		}

		final String arrow = StringUtils.manageArrowForCuca(line0.get("ARROW").get(0));
		final int lenght = arrow.length() - 1;

		final String linkLabel = line0.get("BRACKET").get(0);

		LinkType type = new LinkType(LinkDecor.ARROW, LinkDecor.NONE);
		if (line0.get("ARROW").get(0).contains(".")) {
			type = type.getDotted();
		}
		Link link = new Link(entity1, entity2, type, linkLabel, lenght);
		final Direction direction = StringUtils.getArrowDirection(line0.get("ARROW").get(0));
		if (direction == Direction.LEFT || direction == Direction.UP) {
			link = link.getInv();
		}

		if (line0.get("URL").get(0) != null) {
			final Url urlLink = StringUtils.extractUrl(getSystem().getSkinParam().getValue("topurl"), line0.get("URL")
					.get(0), true);
			link.setUrl(urlLink);
		}

		getSystem().addLink(link);

		return CommandExecutionResult.ok();
	}

}
