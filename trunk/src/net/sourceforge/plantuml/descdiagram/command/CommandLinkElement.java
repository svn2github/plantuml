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
package net.sourceforge.plantuml.descdiagram.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagram;

public class CommandLinkElement extends SingleLineCommand2<DescriptionDiagram> {

	public CommandLinkElement(DescriptionDiagram diagram) {
		super(diagram, getRegexConcat());
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(
				new RegexLeaf("^"), //
				getGroup("ENT1"), //
				new RegexLeaf("\\s*"), new RegexLeaf("LABEL1", "(?:\"([^\"]+)\")?"), new RegexLeaf("\\s*"),
				new RegexLeaf("HEAD2", "([<^*]|<\\|| +o)?"), //
				new RegexLeaf("BODY", "([-=.~]+)(?:(left|right|up|down|le?|ri?|up?|do?)(?=[-=.~]))?([-=.~]*)"), //
				new RegexLeaf("HEAD1", "([>^*]|\\|>|o +)?"), //
				new RegexLeaf("\\s*"), new RegexLeaf("LABEL2", "(?:\"([^\"]+)\")?"), new RegexLeaf("\\s*"), //
				getGroup("ENT2"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("LABEL_LINK", "(?::\\s*(.+))?$"));
	}

	private LinkType getLinkType(RegexResult arg) {
		final String head1 = trimAndLowerCase(arg.get("HEAD1", 0));
		final String head2 = trimAndLowerCase(arg.get("HEAD2", 0));
		LinkDecor d1 = LinkDecor.NONE;
		LinkDecor d2 = LinkDecor.NONE;
		if (head1.equals(">")) {
			d1 = LinkDecor.ARROW;
		} else if (head1.equals("*")) {
			d1 = LinkDecor.COMPOSITION;
		} else if (head1.equals("o")) {
			d1 = LinkDecor.AGREGATION;
		} else if (head1.equals("^")) {
			d1 = LinkDecor.EXTENDS;
		} else if (head1.equals("|>")) {
			d1 = LinkDecor.EXTENDS;
		}
		if (head2.equals("<")) {
			d2 = LinkDecor.ARROW;
		} else if (head2.equals("*")) {
			d2 = LinkDecor.COMPOSITION;
		} else if (head2.equals("o")) {
			d2 = LinkDecor.AGREGATION;
		} else if (head2.equals("^")) {
			d2 = LinkDecor.EXTENDS;
		} else if (head2.equals("<|")) {
			d2 = LinkDecor.EXTENDS;
		}

		LinkType result = new LinkType(d1, d2);
		final String body = trimAndLowerCase(arg.get("BODY", 0));
		if (body.contains(".")) {
			result = result.getDashed();
		} else if (body.contains("~")) {
			result = result.getDotted();
		}
		return result;
	}

	private static String trimAndLowerCase(String s) {
		if (s == null) {
			return "";
		}
		return s.trim().toLowerCase();
	}

	private LinkDecor getDecors1(String head1) {
		if (head1 == null) {
			return LinkDecor.NONE;
		}
		if (head1.equals(">")) {
			return LinkDecor.ARROW;
		}
		return LinkDecor.NONE;
	}

	private LinkDecor getDecors2(String head2) {
		if (head2 == null) {
			return LinkDecor.NONE;
		}
		if (head2.equals("*")) {
			return LinkDecor.COMPOSITION;
		}
		return LinkDecor.NONE;
	}

	private Direction getDirection(RegexResult arg) {
		final String dir = arg.get("BODY", 1);
		if (dir == null) {
			return Direction.DOWN;
		}
		return StringUtils.getQueueDirection(dir);
	}

	private String getQueue(RegexResult arg) {
		return arg.get("BODY", 0).trim() + arg.get("BODY", 2).trim();
	}

	private static RegexLeaf getGroup(String name) {
		return new RegexLeaf(
				name,
				"([\\p{L}0-9_.]+|\\(\\)\\s*[\\p{L}0-9_.]+|:[^:]+:|(?!\\[\\*\\])\\[[^\\[\\]]+\\]|\\((?!\\*\\))[^)]+\\))(?:\\s*(\\<\\<.*\\>\\>))?");
	}

	static class Labels {
		private String firstLabel;
		private String secondLabel;
		private String labelLink;

		Labels(RegexResult arg) {
			firstLabel = arg.get("LABEL1", 0);
			secondLabel = arg.get("LABEL2", 0);
			labelLink = arg.get("LABEL_LINK", 0);

			if (labelLink != null) {
				if (firstLabel == null && secondLabel == null) {
					init();
				}
				labelLink = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(labelLink);
			}

		}

		private void init() {
			final Pattern p1 = Pattern.compile("^\"([^\"]+)\"([^\"]+)\"([^\"]+)\"$");
			final Matcher m1 = p1.matcher(labelLink);
			if (m1.matches()) {
				firstLabel = m1.group(1);
				labelLink = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(m1.group(2).trim()).trim();
				secondLabel = m1.group(3);
				return;
			}
			final Pattern p2 = Pattern.compile("^\"([^\"]+)\"([^\"]+)$");
			final Matcher m2 = p2.matcher(labelLink);
			if (m2.matches()) {
				firstLabel = m2.group(1);
				labelLink = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(m2.group(2).trim()).trim();
				secondLabel = null;
				return;
			}
			final Pattern p3 = Pattern.compile("^([^\"]+)\"([^\"]+)\"$");
			final Matcher m3 = p3.matcher(labelLink);
			if (m3.matches()) {
				firstLabel = null;
				labelLink = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(m3.group(1).trim()).trim();
				secondLabel = m3.group(2);
			}
		}

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

		final IEntity cl1 = getOrCreateLeaf(ent1);
		final IEntity cl2 = getOrCreateLeaf(ent2);

		if (arg.get("ENT1", 1) != null) {
			cl1.setStereotype(new Stereotype(arg.get("ENT1", 1)));
		}
		if (arg.get("ENT2", 1) != null) {
			cl2.setStereotype(new Stereotype(arg.get("ENT2", 1)));
		}

		final LinkType linkType = getLinkType(arg);
		final Direction dir = getDirection(arg);
		final String queue;
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			queue = "-";
		} else {
			queue = getQueue(arg);
		}
		// if (dir != null && linkType.isExtendsOrAgregationOrCompositionOrPlus()) {
		// dir = dir.getInv();
		// }

		final Labels labels = new Labels(arg);

		Link link = new Link(cl1, cl2, linkType, labels.labelLink, queue.length(), labels.firstLabel,
				labels.secondLabel, getSystem().getLabeldistance(), getSystem().getLabelangle());

		if (dir == Direction.LEFT || dir == Direction.UP) {
			link = link.getInv();
		}

		getSystem().addLink(link);
		return CommandExecutionResult.ok();
	}

	private ILeaf getOrCreateLeaf(final String code) {
		if (code.startsWith("()")) {
			return getSystem().getOrCreateLeaf(code.substring(2).trim(), LeafType.CIRCLE_INTERFACE);
		}
		final char codeChar = code.length() > 2 ? code.charAt(0) : 0;
		if (codeChar == '(') {
			return getSystem().getOrCreateLeaf(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(code),
					LeafType.USECASE);
		} else if (codeChar == ':') {
			return getSystem().getOrCreateLeaf(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(code),
					LeafType.ACTOR);
		} else if (codeChar == '[') {
			return getSystem().getOrCreateLeaf(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(code),
					LeafType.COMPONENT);
		}

		return getSystem().getOrCreateClass(code);
	}

	private CommandExecutionResult executePackageLink(RegexResult arg) {
		final String ent1 = arg.get("ENT1", 0);
		final String ent2 = arg.get("ENT2", 0);
		final IEntity cl1 = getSystem().getGroup(ent1);
		final IEntity cl2 = getSystem().getGroup(ent2);

		final LinkType linkType = getLinkType(arg);
		final Direction dir = getDirection(arg);
		final String queue;
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			queue = "-";
		} else {
			queue = getQueue(arg);
		}
		// if (dir != null && linkType.isExtendsOrAgregationOrCompositionOrPlus()) {
		// dir = dir.getInv();
		// }

		Link link = new Link(cl1, cl2, linkType, arg.get("LABEL_LINK", 0), queue.length());
		if (dir == Direction.LEFT || dir == Direction.UP) {
			link = link.getInv();
		}
		getSystem().addLink(link);
		return CommandExecutionResult.ok();
	}

	// private LinkType getLinkTypeNormal(RegexPartialMatch regexPartialMatch) {
	// final String queue = regexPartialMatch.get(1).trim() + regexPartialMatch.get(3).trim();
	// final String key = regexPartialMatch.get(4);
	// return getLinkType(queue, key);
	// }
	//
	// private LinkType getLinkTypeInv(RegexPartialMatch regexPartialMatch) {
	// final String queue = regexPartialMatch.get(2).trim() + regexPartialMatch.get(4).trim();
	// final String key = regexPartialMatch.get(1);
	// return getLinkType(queue, key).getInversed();
	// }

	// private LinkType getLinkType(String queue, String key) {
	// if (key != null) {
	// key = key.trim();
	// }
	// LinkType linkType = getLinkTypeFromKey(key);
	//
	// if (queue.startsWith(".")) {
	// linkType = linkType.getDashed();
	// }
	// return linkType;
	// }

	// private LinkType getLinkTypeFromKey(String k) {
	// if (k == null) {
	// return new LinkType(LinkDecor.NONE, LinkDecor.NONE);
	// }
	// if (k.equals("<") || k.equals(">")) {
	// return new LinkType(LinkDecor.ARROW, LinkDecor.NONE);
	// }
	// if (k.equals("<|") || k.equals("|>")) {
	// return new LinkType(LinkDecor.EXTENDS, LinkDecor.NONE);
	// }
	// if (k.equals("^")) {
	// return new LinkType(LinkDecor.EXTENDS, LinkDecor.NONE);
	// }
	// return null;
	// }

}
