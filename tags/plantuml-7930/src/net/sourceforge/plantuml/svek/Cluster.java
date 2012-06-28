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
 * Revision $Revision: 4236 $
 * 
 */
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.EntityMutable;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.cucadiagram.MethodsOrFieldsArea;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockEmpty;
import net.sourceforge.plantuml.graphic.TextBlockWidth;
import net.sourceforge.plantuml.posimo.Moveable;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class Cluster implements Moveable {

	private final Cluster parent;
	private final Group group;
	private final List<Shape> shapes = new ArrayList<Shape>();
	private final List<Cluster> children = new ArrayList<Cluster>();
	private final boolean special;
	private final int color;
	private final int colorTitle;
	private final ISkinParam skinParam;

	private int titleWidth;
	private int titleHeight;
	private TextBlock title;

	private double xTitle;
	private double yTitle;

	private double minX;
	private double minY;
	private double maxX;
	private double maxY;

	public void moveSvek(double deltaX, double deltaY) {
		this.xTitle += deltaX;
		this.minX += deltaX;
		this.maxX += deltaX;
		this.yTitle += deltaY;
		this.minY += deltaY;
		this.maxY += deltaY;

	}

	public Cluster(ColorSequence colorSequence, ISkinParam skinParam) {
		this(null, null, false, colorSequence, skinParam);
	}

	private int getUid2() {
		return group.zgetUid2();
	}

	private Cluster(Cluster parent, Group group, boolean special, ColorSequence colorSequence, ISkinParam skinParam) {
		this.parent = parent;
		this.group = group;
		this.special = special;
		this.color = colorSequence.getValue();
		this.colorTitle = colorSequence.getValue();
		this.skinParam = skinParam;
	}

	@Override
	public String toString() {
		return super.toString() + " " + group;
	}

	public final Cluster getParent() {
		return parent;
	}

	public void addShape(Shape sh) {
		if (sh == null) {
			throw new IllegalArgumentException();
		}
		this.shapes.add(sh);
		sh.setCluster(this);
	}

	public final List<Shape> getShapes() {
		return Collections.unmodifiableList(shapes);
	}

	private List<Shape> getShapesOrderedTop(Collection<Line> lines) {
		final List<Shape> firsts = new ArrayList<Shape>();
		final Set<String> tops = new HashSet<String>();
		final Map<String, Shape> shs = new HashMap<String, Shape>();

		for (final Iterator<Shape> it = shapes.iterator(); it.hasNext();) {
			final Shape sh = it.next();
			shs.put(sh.getUid(), sh);
			if (sh.isTop()) {
				firsts.add(sh);
				tops.add(sh.getUid());
			}
		}

		for (Line l : lines) {
			if (tops.contains(l.getStartUid())) {
				final Shape sh = shs.get(l.getEndUid());
				if (sh != null) {
					firsts.add(0, sh);
				}
			}

			if (l.isInverted()) {
				final Shape sh = shs.get(l.getStartUid());
				if (sh != null) {
					firsts.add(0, sh);
				}
			}
		}

		return firsts;
	}

	private List<Shape> getShapesOrderedWithoutTop(Collection<Line> lines) {
		final List<Shape> all = new ArrayList<Shape>(shapes);
		final Set<String> tops = new HashSet<String>();
		final Map<String, Shape> shs = new HashMap<String, Shape>();

		for (final Iterator<Shape> it = all.iterator(); it.hasNext();) {
			final Shape sh = it.next();
			shs.put(sh.getUid(), sh);
			if (sh.isTop()) {
				tops.add(sh.getUid());
				it.remove();
			}
		}

		for (Line l : lines) {
			if (tops.contains(l.getStartUid())) {
				final Shape sh = shs.get(l.getEndUid());
				if (sh != null) {
					all.remove(sh);
				}
			}

			if (l.isInverted()) {
				final Shape sh = shs.get(l.getStartUid());
				if (sh != null) {
					all.remove(sh);
				}
			}
		}

		return all;
	}

	public final List<Cluster> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public Cluster createChild(Group g, int titleWidth, int titleHeight, TextBlock title, boolean special,
			ColorSequence colorSequence, ISkinParam skinParam) {
		final Cluster child = new Cluster(this, g, special, colorSequence, skinParam);
		child.titleWidth = titleWidth;
		child.titleHeight = titleHeight;
		child.title = title;
		this.children.add(child);
		return child;
	}

	public final Group getGroup() {
		return group;
	}

	public final int getTitleWidth() {
		return titleWidth;
	}

	public final int getTitleHeight() {
		return titleHeight;
	}

	public double getWidth() {
		return maxX - minX;
	}

	public double getMinX() {
		return minX;
	}

	public void setTitlePosition(double x, double y) {
		this.xTitle = x;
		this.yTitle = y;
	}

	public void drawU(UGraphic ug, double x, double y, HtmlColor borderColor, DotData dotData) {
		if (skinParam.useSwimlanes()) {
			drawSwinLinesState(ug, x, y, borderColor, dotData);
			return;
		}
		final boolean isState = dotData.getUmlDiagramType() == UmlDiagramType.STATE;
		if (isState) {
			drawUState(ug, x, y, borderColor, dotData);
			return;
		}
		PackageStyle style = group.zgetPackageStyle();
		if (style == null) {
			style = dotData.getSkinParam().getPackageStyle();
		}
		if (title != null) {
			final HtmlColor stateBack = getStateBackColor(getBackColor(), dotData.getSkinParam(), group
					.zgetStereotype() == null ? null : group.zgetStereotype().getLabel());
			final ClusterDecoration decoration = new ClusterDecoration(style, title, stateBack, minX, minY, maxX, maxY);
			decoration.drawU(ug, x, y, borderColor, dotData.getSkinParam().shadowing());
			return;
		}
		final URectangle rect = new URectangle(maxX - minX, maxY - minY);
		if (dotData.getSkinParam().shadowing()) {
			rect.setDeltaShadow(3.0);
		}
		final HtmlColor stateBack = getStateBackColor(getBackColor(), dotData.getSkinParam(),
				group.zgetStereotype() == null ? null : group.zgetStereotype().getLabel());
		ug.getParam().setBackcolor(stateBack);
		ug.getParam().setColor(borderColor);
		ug.getParam().setStroke(new UStroke(2));
		ug.draw(x + minX, y + minY, rect);
		ug.getParam().setStroke(new UStroke());
	}

	private void drawSwinLinesState(UGraphic ug, double x, double y, HtmlColor borderColor, DotData dotData) {
		if (title != null) {
			title.drawU(ug, x + xTitle, y);
		}
		final ULine line = new ULine(0, maxY - minY);
		ug.getParam().setColor(borderColor);
		ug.draw(x + minX, y, line);
		ug.draw(x + maxX, y, line);

	}

	private HtmlColor getColor(DotData dotData, ColorParam colorParam, String stereo) {
		return new Rose().getHtmlColor(dotData.getSkinParam(), colorParam, stereo);
	}

	private void drawUState(UGraphic ug, double x, double y, HtmlColor borderColor, DotData dotData) {

		final Dimension2D total = new Dimension2DDouble(maxX - minX, maxY - minY);
		final double suppY;
		if (title == null) {
			suppY = 0;
		} else {
			suppY = title.calculateDimension(ug.getStringBounder()).getHeight() + EntityImageState.MARGIN
					+ EntityImageState.MARGIN_LINE;
		}
		HtmlColor stateBack = getBackColor();
		if (stateBack == null) {
			stateBack = getColor(dotData, ColorParam.stateBackground, group.zgetStereotype() == null ? null : group
					.zgetStereotype().getLabel());
		}
		final HtmlColor background = getColor(dotData, ColorParam.background, null);
		final List<Member> members = ((IEntity) group).getFieldsToDisplay();
		final TextBlockWidth attribute;
		if (members.size() == 0) {
			attribute = new TextBlockEmpty();
		} else {
			attribute = new MethodsOrFieldsArea(members, FontParam.STATE_ATTRIBUTE, dotData.getSkinParam());
		}
		final double attributeHeight = attribute.calculateDimension(ug.getStringBounder()).getHeight();
		final RoundedContainer r = new RoundedContainer(total, suppY, attributeHeight
				+ (attributeHeight > 0 ? EntityImageState.MARGIN : 0), borderColor, stateBack, background);
		r.drawU(ug, x + minX, y + minY, dotData.getSkinParam().shadowing());

		if (title != null) {
			title.drawU(ug, x + xTitle, y + yTitle);
		}

		if (attributeHeight > 0) {
			attribute.drawU(ug, x + minX + EntityImageState.MARGIN, y + minY + suppY + EntityImageState.MARGIN / 2.0,
					total.getWidth());
		}

	}

	public void setPosition(double minX, double minY, double maxX, double maxY) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;

	}

	public boolean isSpecial() {
		return special;
	}

	public void printCluster1(StringBuilder sb, Collection<Line> lines) {
		for (Shape sh : getShapesOrderedTop(lines)) {
			sh.appendShape(sb);
		}
	}

	public void printCluster2(StringBuilder sb, Collection<Line> lines) {
		// Log.println("Cluster::printCluster " + this);

		final Set<String> rankSame = new HashSet<String>();
		for (Line l : lines) {
			final String startUid = l.getStartUid();
			final String endUid = l.getEndUid();
			if (isInCluster(startUid) && isInCluster(endUid)) {
				final String same = l.rankSame();
				if (same != null) {
					rankSame.add(same);
				}
			}
		}

		for (Shape sh : getShapesOrderedWithoutTop(lines)) {
			sh.appendShape(sb);
		}

		for (String same : rankSame) {
			sb.append(same);
			SvekUtils.println(sb);
		}

		for (Cluster child : getChildren()) {
			child.printInternal(sb, lines);
		}
	}

	public void fillRankMin(Set<String> rankMin) {
		for (Shape sh : getShapes()) {
			if (sh.isTop()) {
				rankMin.add(sh.getUid());
			}
		}

		for (Cluster child : getChildren()) {
			child.fillRankMin(rankMin);
		}
	}

	private boolean isInCluster(String uid) {
		for (Shape sh : shapes) {
			if (sh.getUid().equals(uid)) {
				return true;
			}
		}
		return false;
	}

	public String getClusterId() {
		return "cluster" + color;
	}

	public String getSpecialPointId() {
		return CENTER_ID + getUid2();
	}

	public final static String CENTER_ID = "za";

	private boolean protection0() {
		if (skinParam.useSwimlanes()) {
			return false;
		}
		return true;
	}

	private boolean protection1() {
		if (skinParam.useSwimlanes()) {
			return false;
		}
		return true;
	}

	public String getMinPoint() {
		if (skinParam.useSwimlanes()) {
			return "minPoint" + color;
		}
		return null;
	}

	public String getMaxPoint() {
		if (skinParam.useSwimlanes()) {
			return "maxPoint" + color;
		}
		return null;
	}

	private String getSourceInPoint() {
		if (skinParam.useSwimlanes()) {
			return "sourceIn" + color;
		}
		return null;
	}

	private String getSinkInPoint() {
		if (skinParam.useSwimlanes()) {
			return "sinkIn" + color;
		}
		return null;
	}

	private void printInternal(StringBuilder sb, Collection<Line> lines) {
		if (isSpecial()) {
			subgraphCluster(sb, "a");
		}
		if (protection0()) {
			subgraphCluster(sb, "p0");
		}
		sb.append("subgraph " + getClusterId() + " {");
		sb.append("style=solid;");
		sb.append("color=\"" + StringUtils.getAsHtml(color) + "\";");

		final int titleWidth = getTitleWidth();
		final int titleHeight = getTitleHeight();
		if (titleHeight > 0 && titleWidth > 0) {
			// if (skinParam.getPackageStyle() != PackageStyle.RECT) {
			// titleWidth += ClusterDecoration.marginTitleX1 +
			// ClusterDecoration.marginTitleX2
			// + ClusterDecoration.marginTitleX3;
			// titleHeight += ClusterDecoration.marginTitleY0 +
			// ClusterDecoration.marginTitleY1
			// + ClusterDecoration.marginTitleY2;
			// }
			sb.append("label=<");
			Line.appendTable(sb, titleWidth, titleHeight - 5, colorTitle);
			sb.append(">;");
		}
		SvekUtils.println(sb);

		if (isSpecial()) {
			sb.append(getSpecialPointId() + " [shape=point,width=.01,label=\"\"];");
			subgraphCluster(sb, "i");
		}
		if (protection1()) {
			subgraphCluster(sb, "p1");
		}
		if (skinParam.useSwimlanes()) {
			sb.append("{rank = source; ");
			sb.append(getSourceInPoint());
			sb.append(" [shape=point,width=.01,label=\"\"];");
			sb.append(getMinPoint() + "->" + getSourceInPoint() + "  [weight=999];");
			sb.append("}");
			SvekUtils.println(sb);
			sb.append("{rank = sink; ");
			sb.append(getSinkInPoint());
			sb.append(" [shape=point,width=.01,label=\"\"];");
			sb.append("}");
			sb.append(getSinkInPoint() + "->" + getMaxPoint() + "  [weight=999];");
			SvekUtils.println(sb);
		}
		SvekUtils.println(sb);
		printCluster1(sb, lines);
		printCluster2(sb, lines);
		if (protection1()) {
			sb.append("}");
		}
		if (isSpecial()) {
			sb.append("}");
			// subgraphCluster(sb, "zb");
			// if (OptionFlags.getInstance().isDebugDot()) {
			// sb.append("zb" + child.getUid2() + ";");
			// } else {
			// sb.append("zb" + child.getUid2() + "
			// [shape=point,width=.01,label=\"\"];");
			// }
			// sb.append("}");
		}
		sb.append("}");
		if (protection0()) {
			sb.append("}");
		}
		if (this.isSpecial()) {
			// a
			sb.append("}");
		}
		SvekUtils.println(sb);
	}

	private void subgraphCluster(StringBuilder sb, String id) {
		final String uid = getClusterId() + id;
		sb.append("subgraph " + uid + " {");
		sb.append("style=invis;");
		sb.append("label=\"\";");
	}

	public int getColor() {
		return color;
	}

	public int getTitleColor() {
		return colorTitle;
	}

	private final HtmlColor getBackColor() {
		if (group == null) {
			return null;
		}
		final HtmlColor result = group.zgetBackColor();
		if (result != null) {
			return result;
		}
		if (parent == null) {
			return null;
		}
		return parent.getBackColor();
	}

	public boolean isClusterOf(IEntity ent) {
		if (((EntityMutable) ent).isGroup() == false) {
			return false;
		}
		return group == ent;
	}

	static HtmlColor getStateBackColor(HtmlColor stateBack, ISkinParam skinParam, String stereotype) {
		if (stateBack == null) {
			stateBack = skinParam.getHtmlColor(ColorParam.packageBackground, stereotype);
		}
		if (stateBack == null) {
			stateBack = skinParam.getHtmlColor(ColorParam.background, stereotype);
		}
		if (stateBack == null) {
			stateBack = HtmlColorUtils.WHITE;
		}
		return stateBack;
	}

	// public Point2D projection(double x, double y) {
	// final double v1 = Math.abs(minX - x);
	// final double v2 = Math.abs(maxX - x);
	// final double v3 = Math.abs(minY - y);
	// final double v4 = Math.abs(maxY - y);
	// if (v1 <= v2 && v1 <= v3 && v1 <= v4) {
	// return new Point2D.Double(minX, y);
	// }
	// if (v2 <= v1 && v2 <= v3 && v2 <= v4) {
	// return new Point2D.Double(maxX, y);
	// }
	// if (v3 <= v1 && v3 <= v2 && v3 <= v4) {
	// return new Point2D.Double(x, minY);
	// }
	// if (v4 <= v1 && v4 <= v1 && v4 <= v3) {
	// return new Point2D.Double(x, maxY);
	// }
	// throw new IllegalStateException();
	// }

}
