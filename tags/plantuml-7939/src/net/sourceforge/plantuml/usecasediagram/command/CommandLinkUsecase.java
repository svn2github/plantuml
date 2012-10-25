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

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.usecasediagram.UsecaseDiagram;

public class CommandLinkUsecase extends SingleLineCommand2<UsecaseDiagram> {

	public CommandLinkUsecase(UsecaseDiagram diagram) {
		super(diagram, getRegexConcat());
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(
				new RegexLeaf("^"),
				getGroup("ENT1"),
				new RegexLeaf("\\s*"),
				new RegexOr(
						new RegexLeaf("LEFT_TO_RIGHT", "(([-=.]+)(?:(left|right|up|down|le?|ri?|up?|do?)(?=[-=.]))?([-=.]*)([\\]>^]|\\|[>\\]])?)"),
						new RegexLeaf("RIGHT_TO_LEFT", "(([\\[<^]|[<\\[]\\|)?([-=.]*)(left|right|up|down|le?|ri?|up?|do?)?([-=.]+))")),
				new RegexLeaf("\\s*"),
				getGroup("ENT2"),
				new RegexLeaf("\\s*"),
				new RegexLeaf("LABEL_LINK", "(?::\\s*([^\"]+))?$"));
	}

	private static RegexLeaf getGroup(String name) {
		return new RegexLeaf(name, "([\\p{L}0-9_.]+|:[^:]+:|\\((?!\\*\\))[^)]+\\))(?:\\s*(\\<\\<.*\\>\\>))?");
	}

	@Override
	protected CommandExecutionResult executeArg(RegexResult arg) {
		final String ent1 = arg.get("ENT1", 0);
		final String ent2 = arg.get("ENT2", 0);

		if (getSystem().isGroup(ent1) && getSystem().isGroup(ent2)) {
			return executePackageLink(arg);
		}
		if (getSystem().isGroup(ent1) || getSystem().isGroup(ent2)) {
			return CommandExecutionResult.error("Package can be only linked to other package");
		}

		final IEntity cl1 = getSystem().getOrCreateClass(ent1);
		final IEntity cl2 = getSystem().getOrCreateClass(ent2);
		
		if (arg.get("ENT1", 1) != null) {
			cl1.setStereotype(new Stereotype(arg.get("ENT1", 1)));
		}
		if (arg.get("ENT2", 1) != null) {
			cl2.setStereotype(new Stereotype(arg.get("ENT2", 1)));
		}

		final LinkType linkType = getLinkType(arg);
		Direction dir = getDirection(arg);
		final String queue;
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			queue = "-";
		} else {
			queue = getQueue(arg);
		}
		if (dir != null && linkType.isExtendsOrAgregationOrCompositionOrPlus()) {
			dir = dir.getInv();
		}

		Link link = new Link(cl1, cl2, linkType, arg.get("LABEL_LINK", 0), queue.length());

		if (dir == Direction.LEFT || dir == Direction.UP) {
			link = link.getInv();
		}

		getSystem().addLink(link);
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executePackageLink(RegexResult arg) {
		final String ent1 = arg.get("ENT1", 0);
		final String ent2 = arg.get("ENT2", 0);
		final IEntity cl1 = getSystem().getGroup(ent1);
		final IEntity cl2 = getSystem().getGroup(ent2);

		final LinkType linkType = getLinkType(arg);
		Direction dir = getDirection(arg);
		final String queue;
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			queue = "-";
		} else {
			queue = getQueue(arg);
		}
		if (dir != null && linkType.isExtendsOrAgregationOrCompositionOrPlus()) {
			dir = dir.getInv();
		}

		Link link = new Link(cl1, cl2, linkType, arg.get("LABEL_LINK", 0),
				queue.length());
		if (dir == Direction.LEFT || dir == Direction.UP) {
			link = link.getInv();
		}
		getSystem().addLink(link);
		return CommandExecutionResult.ok();
	}

	private String getQueue(RegexResult arg) {
		if (arg.get("LEFT_TO_RIGHT", 1) != null) {
			return arg.get("LEFT_TO_RIGHT", 1).trim() + arg.get("LEFT_TO_RIGHT", 3).trim();
		}
		if (arg.get("RIGHT_TO_LEFT", 2) != null) {
			return arg.get("RIGHT_TO_LEFT", 2).trim() + arg.get("RIGHT_TO_LEFT", 4).trim();
		}
		throw new IllegalArgumentException();
	}

	private Direction getDirection(RegexResult arg) {
		if (arg.get("LEFT_TO_RIGHT", 2) != null) {
			return StringUtils.getQueueDirection(arg.get("LEFT_TO_RIGHT", 2));
		}
		if (arg.get("RIGHT_TO_LEFT", 3) != null) {
			return StringUtils.getQueueDirection(arg.get("RIGHT_TO_LEFT", 3)).getInv();
		}
		return null;
	}

	private LinkType getLinkType(RegexResult arg) {
		if (arg.get("LEFT_TO_RIGHT", 0) != null) {
			return getLinkTypeNormal(arg.get("LEFT_TO_RIGHT"));
		}
		if (arg.get("RIGHT_TO_LEFT", 0) != null) {
			return getLinkTypeInv(arg.get("RIGHT_TO_LEFT"));
		}
		throw new IllegalArgumentException();
	}

	private LinkType getLinkTypeNormal(RegexPartialMatch regexPartialMatch) {
		final String queue = regexPartialMatch.get(1).trim() + regexPartialMatch.get(3).trim();
		final String key = regexPartialMatch.get(4);
		return getLinkType(queue, key);
	}

	private LinkType getLinkTypeInv(RegexPartialMatch regexPartialMatch) {
		final String queue = regexPartialMatch.get(2).trim() + regexPartialMatch.get(4).trim();
		final String key = regexPartialMatch.get(1);
		return getLinkType(queue, key).getInversed();
	}

	private LinkType getLinkType(String queue, String key) {
		if (key != null) {
			key = key.trim();
		}
		LinkType linkType = getLinkTypeFromKey(key);

		if (queue.startsWith(".")) {
			linkType = linkType.getDashed();
		}
		return linkType;
	}

	private LinkType getLinkTypeFromKey(String k) {
		if (k == null) {
			return new LinkType(LinkDecor.NONE, LinkDecor.NONE);
		}
		if (k.equals("<") || k.equals(">")) {
			return new LinkType(LinkDecor.ARROW, LinkDecor.NONE);
		}
		if (k.equals("<|") || k.equals("|>")) {
			return new LinkType(LinkDecor.EXTENDS, LinkDecor.NONE);
		}
		if (k.equals("^")) {
			return new LinkType(LinkDecor.EXTENDS, LinkDecor.NONE);
		}
		return null;
	}

}
