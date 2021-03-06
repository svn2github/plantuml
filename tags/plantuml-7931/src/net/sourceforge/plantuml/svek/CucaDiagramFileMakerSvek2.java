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
 * Revision $Revision: 6711 $
 *
 */
package net.sourceforge.plantuml.svek;

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.EntityUtils;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.cucadiagram.MethodsOrFieldsArea;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockEmpty;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.TextBlockWidth;
import net.sourceforge.plantuml.skin.rose.Rose;

public final class CucaDiagramFileMakerSvek2 {

	private final ColorSequence colorSequence = new ColorSequence();

	private final DotData dotData;

	static private final StringBounder stringBounder;

	static {
		final EmptyImageBuilder builder = new EmptyImageBuilder(10, 10, Color.WHITE);
		stringBounder = StringBounderUtils.asStringBounder(builder.getGraphics2D());
	}

	public CucaDiagramFileMakerSvek2(DotData dotData) {
		this.dotData = dotData;
	}

	private DotStringFactory dotStringFactory;

	public Bibliotekon getBibliotekon() {
		return dotStringFactory.getBibliotekon();
	}

	public IEntityImage createFile(String... dotStrings) throws IOException, InterruptedException {

		dotStringFactory = new DotStringFactory(colorSequence, stringBounder, dotData);

		printGroups(null);
		printEntities(getUnpackagedEntities());

		for (Link link : dotData.getLinks()) {
			try {
				final String shapeUid1 = getBibliotekon().getShapeUid(link.getEntity1());
				final String shapeUid2 = getBibliotekon().getShapeUid(link.getEntity2());

				String ltail = null;
				if (shapeUid1.startsWith(Cluster.CENTER_ID)) {
					final Group g1 = EntityUtils.getContainerOrEquivalent(link.getEntity1());
					ltail = getCluster(g1).getClusterId();
				}
				String lhead = null;
				if (shapeUid2.startsWith(Cluster.CENTER_ID)) {
					final Group g2 = EntityUtils.getContainerOrEquivalent(link.getEntity2());
					lhead = getCluster(g2).getClusterId();
				}
				final FontConfiguration labelFont = new FontConfiguration(dotData.getSkinParam().getFont(
						FontParam.ACTIVITY_ARROW, null), HtmlColorUtils.BLACK);

				final Line line = new Line(shapeUid1, shapeUid2, link, colorSequence, ltail, lhead,
						dotData.getSkinParam(), stringBounder, labelFont, getBibliotekon());
				getBibliotekon().addLine(line);

				if (link.getEntity1().getEntityType() == EntityType.NOTE && onlyOneLink(link.getEntity1())) {
					final Shape shape = getBibliotekon().getShape(link.getEntity1());
					((EntityImageNote) shape.getImage()).setOpaleLine(line, shape);
					line.setOpale(true);
				} else if (link.getEntity2().getEntityType() == EntityType.NOTE && onlyOneLink(link.getEntity2())) {
					final Shape shape = getBibliotekon().getShape(link.getEntity2());
					((EntityImageNote) shape.getImage()).setOpaleLine(line, shape);
					line.setOpale(true);
				}
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}

		if (dotStringFactory.illegalDotExe()) {
			return error(dotStringFactory.getDotExe());
		}

		final Dimension2D dim = Dimension2DDouble.delta(dotStringFactory.solve(dotStrings), 10);
		final HtmlColor border;
		if (dotData.getUmlDiagramType() == UmlDiagramType.STATE) {
			border = getColor(ColorParam.stateBorder, null);
		} else {
			border = getColor(ColorParam.packageBorder, null);
		}
		final SvekResult result = new SvekResult(dim, dotData, dotStringFactory, border);
		result.moveSvek(6, 0);
		return result;

	}

	private boolean onlyOneLink(IEntity ent) {
		return Link.onlyOneLink(ent, dotData.getLinks());
	}

	protected final HtmlColor getColor(ColorParam colorParam, String stereo) {
		return new Rose().getHtmlColor(dotData.getSkinParam(), colorParam, stereo);
	}

	private Cluster getCluster(Group g) {
		for (Cluster cl : getBibliotekon().allCluster()) {
			if (EntityUtils.equals(cl.getGroup(), g)) {
				return cl;
			}
		}
		throw new IllegalArgumentException(g.toString());
	}

	private IEntityImage error(File dotExe) {

		final List<String> msg = new ArrayList<String>();
		msg.add("Dot Executable: " + dotExe);
		if (dotExe != null) {
			if (dotExe.exists() == false) {
				msg.add("File does not exist");
			} else if (dotExe.isDirectory()) {
				msg.add("It should be an executable, not a directory");
			} else if (dotExe.isFile() == false) {
				msg.add("Not a valid file");
			} else if (dotExe.canRead() == false) {
				msg.add("File cannot be read");
			}
		}
		msg.add("Cannot find Graphviz. You should try");
		msg.add(" ");
		msg.add("@startuml");
		msg.add("testdot");
		msg.add("@enduml");
		msg.add(" ");
		msg.add(" or ");
		msg.add(" ");
		msg.add("java -jar plantuml.jar -testdot");
		msg.add(" ");
		return new GraphicStrings(msg);
	}

	private void printEntities(Collection<? extends IEntity> entities2) {
		for (IEntity ent : entities2) {
			printEntity(ent);
		}
	}

	private void printEntity(IEntity ent) {
		final IEntityImage image = Shape.printEntity(ent, dotData);
		final Dimension2D dim = image.getDimension(stringBounder);
		final Shape shape = new Shape(image, image.getShapeType(), dim.getWidth(), dim.getHeight(), colorSequence,
				ent.isTop(), image.getShield(), ent.getUrls());
		dotStringFactory.addShape(shape);
		getBibliotekon().putShape(ent, shape);
	}

	private Collection<IEntity> getUnpackagedEntities() {
		final List<IEntity> result = new ArrayList<IEntity>();
		for (IEntity ent : dotData.getEntities()) {
			if (EntityUtils.equals(EntityUtils.getContainerOrEquivalent(ent), dotData.getTopParent())) {
				result.add(ent);
			}
		}
		return result;
	}

	private void printGroups(Group parent) throws IOException {
		for (Group g : dotData.getGroupHierarchy().getChildrenGroups(parent)) {
			if (dotData.isEmpty(g) && g.zgetGroupType() == GroupType.PACKAGE) {
				final IEntity folder = dotData.getEntityFactory().createEntity(g.zgetUid1(), g.zgetUid2(),
						g.zgetGroupCode(), g.zgetDisplay(), EntityType.EMPTY_PACKAGE, null, null);
				folder.setSpecificBackcolor(g.zgetBackColor());
				printEntity(folder);
			} else {
				printGroup(g);
			}
		}
	}

	private void printGroup(Group g) throws IOException {
		if (g.zgetGroupType() == GroupType.CONCURRENT_STATE) {
			return;
		}
		// final String stereo = g.getStereotype();

		int titleWidth = 0;
		int titleHeight = 0;

		final String label = g.zgetDisplay();
		TextBlock title = null;
		if (label != null) {
			final FontParam fontParam = g.zgetGroupType() == GroupType.STATE ? FontParam.STATE : FontParam.PACKAGE;

			final String stereo = g.zgetStereotype() == null ? null : g.zgetStereotype().getLabel();

			title = TextBlockUtils.create(StringUtils.getWithNewlines(label),
					new FontConfiguration(dotData.getSkinParam().getFont(fontParam, stereo), dotData.getSkinParam()
							.getFontHtmlColor(fontParam, stereo)), HorizontalAlignement.CENTER, dotData.getSkinParam());

			final Dimension2D dimLabel = title.calculateDimension(stringBounder);

			final List<Member> members = ((IEntity) g).getFieldsToDisplay();
			final TextBlockWidth attribute;
			if (members.size() == 0) {
				attribute = new TextBlockEmpty();
			} else {
				attribute = new MethodsOrFieldsArea(members, FontParam.STATE_ATTRIBUTE, dotData.getSkinParam());
			}
			final double attributeHeight = attribute.calculateDimension(stringBounder).getHeight();
			final double marginForFields = attributeHeight > 0 ? EntityImageState.MARGIN : 0;

			titleWidth = (int) dimLabel.getWidth();
			titleHeight = (int) (dimLabel.getHeight() + attributeHeight + marginForFields);
		}

		dotStringFactory.openCluster(g, titleWidth, titleHeight, title, isSpecialGroup(g));
		this.printEntities(g.zentities());

		// sb.append("subgraph " + g.getUid() + " {");
		//
		// final UFont font =
		// getData().getSkinParam().getFont(getFontParamForGroup(), stereo);
		// sb.append("fontsize=\"" + font.getSize() + "\";");
		// final String fontFamily = font.getFamily(null);
		// if (fontFamily != null) {
		// sb.append("fontname=\"" + fontFamily + "\";");
		// }
		//
		// if (g.getDisplay() != null) {
		// sb.append("label=<" + manageHtmlIB(g.getDisplay(),
		// getFontParamForGroup(), stereo) + ">;");
		// }
		// final String fontColor =
		// getAsHtml(getData().getSkinParam().getFontHtmlColor(getFontParamForGroup(),
		// stereo));
		// sb.append("fontcolor=\"" + fontColor + "\";");
		//
		// if (getGroupBackColor(g) != null) {
		// sb.append("fillcolor=\"" + getAsHtml(getGroupBackColor(g)) + "\";");
		// }
		//
		// if (g.getType() == GroupType.STATE) {
		// sb.append("color=" + getColorString(ColorParam.stateBorder, stereo) +
		// ";");
		// } else {
		// sb.append("color=" + getColorString(ColorParam.packageBorder, stereo)
		// + ";");
		// }
		// sb.append("style=\"" + getStyle(g) + "\";");
		//
		printGroups(g);
		//
		// this.printEntities(sb, g.entities().values());
		// for (Link link : getData().getLinks()) {
		// eventuallySameRank(sb, g, link);
		// }
		// sb.append("}");

		dotStringFactory.closeCluster();
	}

	private boolean isSpecialGroup(Group g) {
		if (g.zgetGroupType() == GroupType.STATE) {
			return true;
		}
		if (g.zgetGroupType() == GroupType.CONCURRENT_STATE) {
			throw new IllegalStateException();
		}
		if (dotData.isThereLink(g)) {
			return true;
		}
		return false;
	}

}
