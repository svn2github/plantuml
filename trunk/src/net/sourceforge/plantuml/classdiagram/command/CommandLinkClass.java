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
 * Revision $Revision: 5436 $
 *
 */
package net.sourceforge.plantuml.classdiagram.command;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkArrow;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.objectdiagram.AbstractClassOrObjectDiagram;

final public class CommandLinkClass extends SingleLineCommand2<AbstractClassOrObjectDiagram> {

	public CommandLinkClass(AbstractClassOrObjectDiagram diagram) {
		super(diagram, getRegexConcat(diagram.getUmlDiagramType()));
	}

	static private RegexConcat getRegexConcat(UmlDiagramType umlDiagramType) {
		return new RegexConcat(
				new RegexLeaf("HEADER", "^(?:@([\\d.]+)\\s+)?"),
				new RegexOr(
						new RegexLeaf("ENT1", "(?:" + optionalKeywords(umlDiagramType) + "\\s+)?"
								+ "(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*|\"[^\"]+\")\\s*(\\<\\<.*\\>\\>)?"),
						new RegexLeaf("COUPLE1",
								"\\(\\s*(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)\\s*,\\s*(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)\\s*\\)")),
				new RegexLeaf("\\s*"),
				new RegexLeaf("FIRST_LABEL", "(?:\"([^\"]+)\")?"),
				new RegexLeaf("\\s*"),

				new RegexConcat(
						//
						new RegexLeaf("ARROW_HEAD1", "( +o|[\\[<*+^]|[<\\[]\\|)?"), //
						new RegexLeaf("ARROW_BODY1", "([-=.]+)"), //
						new RegexLeaf("ARROW_STYLE1",
						"(?:\\[((?:#\\w+|dotted|dashed|bold|hidden)(?:,#\\w+|,dotted|,dashed|,bold|,hidden)*)\\])?"),
						new RegexLeaf("ARROW_DIRECTION", "(left|right|up|down|le?|ri?|up?|do?)?"), //
						new RegexLeaf("ARROW_STYLE2",
						"(?:\\[((?:#\\w+|dotted|dashed|bold|hidden)(?:,#\\w+|,dotted|,dashed|,bold|,hidden)*)\\])?"),
						new RegexLeaf("ARROW_BODY2", "([-=.]*)"), //
						new RegexLeaf("ARROW_HEAD2", "(o +|[\\]>*+^]|\\|[>\\]])?")), //

				new RegexLeaf("\\s*"),
				new RegexLeaf("SECOND_LABEL", "(?:\"([^\"]+)\")?"),
				new RegexLeaf("\\s*"),
				new RegexOr(
						new RegexLeaf("ENT2", "(?:" + optionalKeywords(umlDiagramType) + "\\s+)?"
								+ "(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*|\"[^\"]+\")\\s*(\\<\\<.*\\>\\>)?"),
						new RegexLeaf("COUPLE2",
								"\\(\\s*(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)\\s*,\\s*(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)\\s*\\)")),
				new RegexLeaf("\\s*"), new RegexLeaf("LABEL_LINK", "(?::\\s*(.+))?"), new RegexLeaf("$"));
	}

	private static String optionalKeywords(UmlDiagramType type) {
		if (type == UmlDiagramType.CLASS) {
			return "(interface|enum|abstract\\s+class|abstract|class)";
		}
		if (type == UmlDiagramType.OBJECT) {
			return "(object)";
		}
		throw new IllegalArgumentException();
	}

	@Override
	protected CommandExecutionResult executeArg(RegexResult arg) {
		Code ent1 = Code.of(arg.get("ENT1", 1));
		Code ent2 = Code.of(arg.get("ENT2", 1));
		if (ent1 == null) {
			return executeArgSpecial1(arg);
		}
		if (ent2 == null) {
			return executeArgSpecial2(arg);
		}
		ent1 = ent1.eventuallyRemoveStartingAndEndingDoubleQuote();
		ent2 = ent2.eventuallyRemoveStartingAndEndingDoubleQuote();
		if (getSystem().isGroup(ent1) && getSystem().isGroup(ent2)) {
			return executePackageLink(arg);
		}
		if (getSystem().isGroup(ent1) || getSystem().isGroup(ent2)) {
			return CommandExecutionResult.error("Package can be only linked to other package");
		}

		final ILeaf cl1 = (ILeaf) getSystem().getOrCreateLeaf1(ent1, null);
		final ILeaf cl2 = (ILeaf) getSystem().getOrCreateLeaf1(ent2, null);

		if (arg.get("ENT1", 0) != null) {
			final LeafType type = LeafType.getLeafType(arg.get("ENT1", 0));
			if (type != LeafType.OBJECT) {
				cl1.muteToType(type);
			}
		}
		if (arg.get("ENT2", 0) != null) {
			final LeafType type = LeafType.getLeafType(arg.get("ENT2", 0));
			if (type != LeafType.OBJECT) {
				cl2.muteToType(type);
			}
		}
		if (arg.get("ENT1", 2) != null) {
			cl1.setStereotype(new Stereotype(arg.get("ENT1", 2),
					getSystem().getSkinParam().getCircledCharacterRadius(), getSystem().getSkinParam().getFont(
							FontParam.CIRCLED_CHARACTER, null)));
		}
		if (arg.get("ENT2", 2) != null) {
			cl2.setStereotype(new Stereotype(arg.get("ENT2", 2),
					getSystem().getSkinParam().getCircledCharacterRadius(), getSystem().getSkinParam().getFont(
							FontParam.CIRCLED_CHARACTER, null)));
		}

		final LinkType linkType = getLinkType(arg);
		final Direction dir = getDirection(arg);
		final int queue;
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			queue = 1;
		} else {
			queue = getQueueLength(arg);
		}
		// if (dir != null && linkType.isExtendsOrAgregationOrCompositionOrPlus()) {
		// dir = dir.getInv();
		// }

		String firstLabel = arg.get("FIRST_LABEL", 0);
		String secondLabel = arg.get("SECOND_LABEL", 0);

		String labelLink = null;

		if (arg.get("LABEL_LINK", 0) != null) {
			labelLink = arg.get("LABEL_LINK", 0);
			if (firstLabel == null && secondLabel == null) {
				final Pattern p1 = Pattern.compile("^\"([^\"]+)\"([^\"]+)\"([^\"]+)\"$");
				final Matcher m1 = p1.matcher(labelLink);
				if (m1.matches()) {
					firstLabel = m1.group(1);
					labelLink = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(m1.group(2).trim()).trim();
					secondLabel = m1.group(3);
				} else {
					final Pattern p2 = Pattern.compile("^\"([^\"]+)\"([^\"]+)$");
					final Matcher m2 = p2.matcher(labelLink);
					if (m2.matches()) {
						firstLabel = m2.group(1);
						labelLink = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(m2.group(2).trim()).trim();
						secondLabel = null;
					} else {
						final Pattern p3 = Pattern.compile("^([^\"]+)\"([^\"]+)\"$");
						final Matcher m3 = p3.matcher(labelLink);
						if (m3.matches()) {
							firstLabel = null;
							labelLink = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(m3.group(1).trim())
									.trim();
							secondLabel = m3.group(2);
						}
					}
				}
			}
			labelLink = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(labelLink);
		}

		LinkArrow linkArrow = LinkArrow.NONE;
		if ("<".equals(labelLink)) {
			linkArrow = LinkArrow.BACKWARD;
			labelLink = null;
		} else if (">".equals(labelLink)) {
			linkArrow = LinkArrow.DIRECT_NORMAL;
			labelLink = null;
		} else if (labelLink != null && labelLink.startsWith("< ")) {
			linkArrow = LinkArrow.BACKWARD;
			labelLink = labelLink.substring(2).trim();
		} else if (labelLink != null && labelLink.startsWith("> ")) {
			linkArrow = LinkArrow.DIRECT_NORMAL;
			labelLink = labelLink.substring(2).trim();
		} else if (labelLink != null && labelLink.endsWith(" >")) {
			linkArrow = LinkArrow.DIRECT_NORMAL;
			labelLink = labelLink.substring(0, labelLink.length() - 2).trim();
		} else if (labelLink != null && labelLink.endsWith(" <")) {
			linkArrow = LinkArrow.BACKWARD;
			labelLink = labelLink.substring(0, labelLink.length() - 2).trim();
		}

		Link link = new Link(cl1, cl2, linkType, labelLink, queue, firstLabel, secondLabel, getSystem()
				.getLabeldistance(), getSystem().getLabelangle());

		if (dir == Direction.LEFT || dir == Direction.UP) {
			link = link.getInv();
		}
		link.setLinkArrow(linkArrow);
		applyStyle(arg.getLazzy("ARROW_STYLE", 0), link);

		addLink(link, arg.get("HEADER", 0));

		return CommandExecutionResult.ok();
	}

	private void addLink(Link link, String weight) {
		getSystem().addLink(link);
		if (weight == null) {
			// final LinkType type = link.getType();
			// --|> highest
			// --*, -->, --o normal
			// ..*, ..>, ..o lowest
			// if (type.isDashed() == false) {
			// if (type.contains(LinkDecor.EXTENDS)) {
			// link.setWeight(3);
			// }
			// if (type.contains(LinkDecor.ARROW) ||
			// type.contains(LinkDecor.COMPOSITION)
			// || type.contains(LinkDecor.AGREGATION)) {
			// link.setWeight(2);
			// }
			// }
		} else {
			link.setWeight(Double.parseDouble(weight));
		}
	}

	private CommandExecutionResult executePackageLink(RegexResult arg) {
		final String ent1 = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("ENT1", 1));
		final String ent2 = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("ENT2", 1));
		final IEntity cl1 = getSystem().getGroup(Code.of(ent1));
		final IEntity cl2 = getSystem().getGroup(Code.of(ent2));

		final LinkType linkType = getLinkType(arg);
		final Direction dir = getDirection(arg);
		final int queue;
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			queue = 1;
		} else {
			queue = getQueueLength(arg);
		}

		final String labelLink = arg.get("LABEL_LINK", 0);
		final String firstLabel = arg.get("FIRST_LABEL", 0);
		final String secondLabel = arg.get("SECOND_LABEL", 0);
		final Link link = new Link(cl1, cl2, linkType, labelLink, queue, firstLabel, secondLabel, getSystem()
				.getLabeldistance(), getSystem().getLabelangle());
		// if (dir == Direction.LEFT || dir == Direction.UP) {
		// link = link.getInv();
		// }

		getSystem().resetPragmaLabel();
		addLink(link, arg.get("HEADER", 0));
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeArgSpecial1(RegexResult arg) {
		final Code clName1 = Code.of(arg.get("COUPLE1", 0));
		final Code clName2 = Code.of(arg.get("COUPLE1", 1));
		if (getSystem().leafExist(clName1) == false) {
			return CommandExecutionResult.error("No class " + clName1);
		}
		if (getSystem().leafExist(clName2) == false) {
			return CommandExecutionResult.error("No class " + clName2);
		}

		final Code ent2 = Code.of(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("ENT2", 1)));
		final IEntity cl2 = getSystem().getOrCreateLeaf1(ent2, null);

		final LinkType linkType = getLinkType(arg);
		final String label = arg.get("LABEL_LINK", 0);
		// final int length = getQueueLength(arg);
		// final String weight = arg.get("HEADER").get(0);

		final boolean result = getSystem().associationClass(1, clName1, clName2, cl2, linkType, label);
		if (result == false) {
			return CommandExecutionResult.error("Cannot have more than 2 assocications");
		}

		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeArgSpecial2(RegexResult arg) {
		final Code clName1 = Code.of(arg.get("COUPLE2", 0));
		final Code clName2 = Code.of(arg.get("COUPLE2", 1));
		if (getSystem().leafExist(clName1) == false) {
			return CommandExecutionResult.error("No class " + clName1);
		}
		if (getSystem().leafExist(clName2) == false) {
			return CommandExecutionResult.error("No class " + clName2);
		}

		final Code ent1 = Code.of(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("ENT1", 1)));
		final IEntity cl1 = getSystem().getOrCreateLeaf1(ent1, null);

		final LinkType linkType = getLinkType(arg);
		final String label = arg.get("LABEL_LINK", 0);
		// final int length = getQueueLength(arg);
		// final String weight = arg.get("HEADER").get(0);

		final boolean result = getSystem().associationClass(2, clName1, clName2, cl1, linkType, label);
		if (result == false) {
			return CommandExecutionResult.error("Cannot have more than 2 assocications");
		}

		return CommandExecutionResult.ok();
	}

	private LinkDecor getDecors1(String s) {
		if (s == null) {
			return LinkDecor.NONE;
		}
		s = s.trim();
		if ("<|".equals(s)) {
			return LinkDecor.EXTENDS;
		}
		if ("<".equals(s)) {
			return LinkDecor.ARROW;
		}
		if ("^".equals(s)) {
			return LinkDecor.EXTENDS;
		}
		if ("+".equals(s)) {
			return LinkDecor.PLUS;
		}
		if ("o".equals(s)) {
			return LinkDecor.AGREGATION;
		}
		if ("*".equals(s)) {
			return LinkDecor.COMPOSITION;
		}
		return LinkDecor.NONE;
	}

	private LinkDecor getDecors2(String s) {
		if (s == null) {
			return LinkDecor.NONE;
		}
		s = s.trim();
		if ("|>".equals(s)) {
			return LinkDecor.EXTENDS;
		}
		if (">".equals(s)) {
			return LinkDecor.ARROW;
		}
		if ("^".equals(s)) {
			return LinkDecor.EXTENDS;
		}
		if ("+".equals(s)) {
			return LinkDecor.PLUS;
		}
		if ("o".equals(s)) {
			return LinkDecor.AGREGATION;
		}
		if ("*".equals(s)) {
			return LinkDecor.COMPOSITION;
		}
		return LinkDecor.NONE;
	}

	private LinkType getLinkType(RegexResult arg) {
		final LinkDecor decors1 = getDecors1(arg.get("ARROW_HEAD1", 0));
		final LinkDecor decors2 = getDecors2(arg.get("ARROW_HEAD2", 0));

		LinkType result = new LinkType(decors2, decors1);
		if (arg.get("ARROW_BODY1", 0).contains(".") || arg.get("ARROW_BODY2", 0).contains(".")) {
			result = result.getDashed();
		}
		return result;
	}

	private int getQueueLength(RegexResult arg) {
		String s = getFullArrow(arg);
		s = s.replaceAll("[^-.=]", "");
		return s.length();
	}

	private Direction getDirection(RegexResult arg) {
		final LinkDecor decors1 = getDecors1(arg.get("ARROW_HEAD1", 0));
		final LinkDecor decors2 = getDecors2(arg.get("ARROW_HEAD2", 0));

		String s = getFullArrow(arg);
		s = s.replaceAll("[^-.=\\w]", "");
		if (s.startsWith("o")) {
			s = s.substring(1);
		}
		if (s.endsWith("o")) {
			s = s.substring(0, s.length() - 1);
		}

		Direction result = StringUtils.getQueueDirection(s);
		if (isInversed(decors1, decors2) && s.matches(".*\\w.*")) {
			result = result.getInv();
		}

		return result;
	}

	private String getFullArrow(RegexResult arg) {
		return notNull(arg.get("ARROW_HEAD1", 0)) + notNull(arg.get("ARROW_BODY1", 0)) + notNull(arg.get("ARROW_DIRECTION", 0))
				+ notNull(arg.get("ARROW_BODY2", 0)) + notNull(arg.get("ARROW_HEAD2", 0));
	}

	private String notNull(String s) {
		if (s == null) {
			return "";
		}
		return s;
	}
	
	private void applyStyle(String arrowStyle, Link link) {
		if (arrowStyle == null) {
			return;
		}
		final StringTokenizer st = new StringTokenizer(arrowStyle, ",");
		while (st.hasMoreTokens()) {
			final String s = st.nextToken();
			if (s.equalsIgnoreCase("dashed")) {
				link.goDashed();
			} else if (s.equalsIgnoreCase("bold")) {
				link.goBold();
			} else if (s.equalsIgnoreCase("dotted")) {
				link.goDotted();
			} else if (s.equalsIgnoreCase("hidden")) {
				link.goHidden();
			} else {
				link.setSpecificColor(s);
			}
		}
	}



	private boolean isInversed(LinkDecor decors1, LinkDecor decors2) {
		if (decors1 == LinkDecor.ARROW && decors2 != LinkDecor.ARROW) {
			return true;
		}
		if (decors2 == LinkDecor.AGREGATION) {
			return true;
		}
		if (decors2 == LinkDecor.COMPOSITION) {
			return true;
		}
		if (decors2 == LinkDecor.PLUS) {
			return true;
		}
		if (decors2 == LinkDecor.EXTENDS) {
			return true;
		}
		return false;
	}

}
