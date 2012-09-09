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
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkArrow;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockArrow;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.posimo.BezierUtils;
import net.sourceforge.plantuml.posimo.DotPath;
import net.sourceforge.plantuml.posimo.Moveable;
import net.sourceforge.plantuml.posimo.Positionable;
import net.sourceforge.plantuml.posimo.PositionableUtils;
import net.sourceforge.plantuml.svek.SvekUtils.PointListIterator;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactory;
import net.sourceforge.plantuml.svek.image.EntityImageNoteLink;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class Line implements Moveable {

	private final String ltail;
	private final String lhead;
	private final Link link;

	private final String startUid;
	private final String endUid;

	private final TextBlock startTailText;
	private final TextBlock endHeadText;
	private final TextBlock noteLabelText;

	private final int lineColor;
	private final int noteLabelColor;
	private final int startTailColor;
	private final int endHeadColor;

	private final StringBounder stringBounder;
	private final Bibliotekon bibliotekon;

	private DotPath dotPath;

	private Positionable startTailLabelXY;
	private Positionable endHeadLabelXY;
	private Positionable noteLabelXY;

	private UDrawable endHead;
	private UDrawable startTail;

	private double dx;
	private double dy;

	private boolean opale;
	private Cluster projectionCluster;

	class DirectionalTextBlock implements TextBlock {

		private final TextBlock direct;
		private final TextBlock reverse;

		DirectionalTextBlock(TextBlock direct, TextBlock reverse) {
			this.direct = direct;
			this.reverse = reverse;
		}

		public void drawU(UGraphic ug, double x, double y) {
			boolean isDirect = isDirect();
			if (getLinkArrow() == LinkArrow.BACKWARD) {
				isDirect = !isDirect;
			}
			if (isDirect) {
				direct.drawU(ug, x, y);
			} else {
				reverse.drawU(ug, x, y);
			}
		}

		public List<Url> getUrls() {
			if (isDirect()) {
				return direct.getUrls();
			}
			return reverse.getUrls();
		}

		public Dimension2D calculateDimension(StringBounder stringBounder) {
			return direct.calculateDimension(stringBounder);
		}

		private boolean isDirect() {
			final Point2D start = dotPath.getStartPoint();
			final Point2D end = dotPath.getEndPoint();
			if (end.getX() == start.getX()) {
				return end.getY() > start.getY();
			}
			return end.getX() > start.getX();
		}

	}

	// private boolean projectionStart() {
	// return startUid.startsWith(Cluster.CENTER_ID);
	// }

	public Line(String startUid, String endUid, Link link, ColorSequence colorSequence, String ltail, String lhead,
			ISkinParam skinParam, StringBounder stringBounder, FontConfiguration labelFont, Bibliotekon bibliotekon) {
		if (startUid == null || endUid == null || link == null) {
			throw new IllegalArgumentException();
		}
		this.bibliotekon = bibliotekon;
		this.stringBounder = stringBounder;
		this.link = link;
		this.startUid = startUid;
		this.endUid = endUid;
		this.ltail = ltail;
		this.lhead = lhead;

		this.lineColor = colorSequence.getValue();
		this.noteLabelColor = colorSequence.getValue();
		this.startTailColor = colorSequence.getValue();
		this.endHeadColor = colorSequence.getValue();

		final TextBlock labelOnly;
		if (link.getLabel() == null) {
			if (getLinkArrow() == LinkArrow.NONE) {
				labelOnly = null;
			} else {
				labelOnly = new DirectionalTextBlock(new TextBlockArrow(LinkArrow.DIRECT_NORMAL, labelFont),
						new TextBlockArrow(LinkArrow.BACKWARD, labelFont));
			}
		} else {
			final double marginLabel = startUid.equals(endUid) ? 6 : 1;
			final TextBlock label = TextBlockUtils.withMargin(TextBlockUtils.create(
					StringUtils.getWithNewlines(link.getLabel()), labelFont, HorizontalAlignement.CENTER, skinParam),
					marginLabel, marginLabel);
			if (getLinkArrow() == LinkArrow.NONE) {
				labelOnly = label;
			} else {
				final TextBlock direct = TextBlockUtils.mergeLR(label, new TextBlockArrow(LinkArrow.DIRECT_NORMAL,
						labelFont));
				final TextBlock reverse = TextBlockUtils.mergeLR(new TextBlockArrow(LinkArrow.BACKWARD, labelFont),
						label);

				labelOnly = new DirectionalTextBlock(direct, reverse);
			}
		}

		final TextBlock noteOnly;
		if (link.getNote() == null) {
			noteOnly = null;
		} else {
			noteOnly = TextBlockUtils.fromIEntityImage(new EntityImageNoteLink(link.getNote(), link.getNoteColor(),
					skinParam));
		}

		if (labelOnly != null && noteOnly != null) {
			if (link.getNotePosition() == Position.LEFT) {
				noteLabelText = TextBlockUtils.mergeLR(noteOnly, labelOnly);
			} else if (link.getNotePosition() == Position.RIGHT) {
				noteLabelText = TextBlockUtils.mergeLR(labelOnly, noteOnly);
			} else if (link.getNotePosition() == Position.TOP) {
				noteLabelText = TextBlockUtils.mergeTB(noteOnly, labelOnly, HorizontalAlignement.CENTER);
			} else {
				noteLabelText = TextBlockUtils.mergeTB(labelOnly, noteOnly, HorizontalAlignement.CENTER);
			}
		} else if (labelOnly != null) {
			noteLabelText = labelOnly;
		} else if (noteOnly != null) {
			noteLabelText = noteOnly;
		} else {
			noteLabelText = null;
		}

		if (link.getQualifier1() == null) {
			startTailText = null;
		} else {
			startTailText = TextBlockUtils.create(StringUtils.getWithNewlines(link.getQualifier1()), labelFont,
					HorizontalAlignement.CENTER, skinParam);
		}

		if (link.getQualifier2() == null) {
			endHeadText = null;
		} else {
			endHeadText = TextBlockUtils.create(StringUtils.getWithNewlines(link.getQualifier2()), labelFont,
					HorizontalAlignement.CENTER, skinParam);
		}

	}

	private LinkArrow getLinkArrow() {
		return link.getLinkArrow();
	}

	public void appendLine(StringBuilder sb) {
		// Log.println("inverted=" + isInverted());
		// if (isInverted()) {
		// sb.append(endUid);
		// sb.append("->");
		// sb.append(startUid);
		// } else {
		sb.append(startUid);
		sb.append("->");
		sb.append(endUid);
		// }
		sb.append("[");
		String decoration = link.getType().getSpecificDecorationSvek();
		if (decoration.endsWith(",") == false) {
			decoration += ",";
		}
		sb.append(decoration);

		if (OptionFlags.HORIZONTAL_LINE_BETWEEN_DIFFERENT_PACKAGE_ALLOWED || link.getLength() != 1) {
			sb.append("minlen=" + (link.getLength() - 1));
			sb.append(",");
		}
		sb.append("color=\"" + StringUtils.getAsHtml(lineColor) + "\"");
		if (noteLabelText != null) {
			sb.append(",");
			sb.append("label=<");
			appendTable(sb, noteLabelText.calculateDimension(stringBounder), noteLabelColor);
			sb.append(">");
		}

		if (startTailText != null) {
			sb.append(",");
			sb.append("taillabel=<");
			appendTable(sb, startTailText.calculateDimension(stringBounder), startTailColor);
			sb.append(">");
		}
		if (endHeadText != null) {
			sb.append(",");
			sb.append("headlabel=<");
			appendTable(sb, endHeadText.calculateDimension(stringBounder), endHeadColor);
			sb.append(">");
		}

		if (ltail != null) {
			sb.append(",");
			sb.append("ltail=");
			sb.append(ltail);
		}
		if (lhead != null) {
			sb.append(",");
			sb.append("lhead=");
			sb.append(lhead);
		}
		if (link.isInvis()) {
			sb.append(",");
			sb.append("style=invis");
		}

		if (link.isConstraint() == false || link.hasTwoEntryPointsSameContainer()) {
			sb.append("constraint=false,");
		}

		sb.append("];");
		SvekUtils.println(sb);
	}

	public String rankSame() {
		if (OptionFlags.HORIZONTAL_LINE_BETWEEN_DIFFERENT_PACKAGE_ALLOWED == false && link.getLength() == 1) {
			return "{rank=same; " + getStartUid() + "; " + getEndUid() + "}";
		}
		return null;
	}

	public static void appendTable(StringBuilder sb, Dimension2D dim, int col) {
		final int w = (int) dim.getWidth();
		final int h = (int) dim.getHeight();
		appendTable(sb, w, h, col);
	}

	public static void appendTable(StringBuilder sb, int w, int h, int col) {
		sb.append("<TABLE ");
		sb.append("BGCOLOR=\"" + StringUtils.getAsHtml(col) + "\" ");
		sb.append("FIXEDSIZE=\"TRUE\" WIDTH=\"" + w + "\" HEIGHT=\"" + h + "\">");
		sb.append("<TR");
		sb.append(">");
		sb.append("<TD");
		// sb.append(" FIXEDSIZE=\"TRUE\" WIDTH=\"" + 0 + "\" HEIGHT=\"" + 0 +
		// "\"");
		sb.append(">");
		sb.append("</TD>");
		sb.append("</TR>");
		sb.append("</TABLE>");
	}

	public final String getStartUid() {
		if (startUid.endsWith(":h")) {
			return startUid.substring(0, startUid.length() - 2);
		}
		return startUid;
	}

	public final String getEndUid() {
		if (endUid.endsWith(":h")) {
			return endUid.substring(0, endUid.length() - 2);
		}
		return endUid;
	}

	public UDrawable getExtremity(LinkDecor decor, PointListIterator pointListIterator) {
		final ExtremityFactory extremityFactory2 = decor.getExtremityFactory();

		if (extremityFactory2 != null) {
			final List<Point2D.Double> points = pointListIterator.next();
			final Point2D p0 = points.get(0);
			final Point2D p1 = points.get(1);
			final Point2D p2 = points.get(2);
			return extremityFactory2.createUDrawable(p0, p1, p2);
		} else if (decor != LinkDecor.NONE) {
			final UShape sh = new UPolygon(pointListIterator.next());
			return new UDrawable() {
				public void drawU(UGraphic ug, double x, double y) {
					ug.draw(x, y, sh);
				}
			};
		}
		return null;

	}

	public void solveLine(final String svg, final int fullHeight, MinFinder corner1) {
		if (this.link.isInvis()) {
			return;
		}

		int idx = getIndexFromColor(svg, this.lineColor);
		idx = svg.indexOf("d=\"", idx);
		if (idx == -1) {
			throw new IllegalStateException();
		}
		final int end = svg.indexOf("\"", idx + 3);
		final String path = svg.substring(idx + 3, end);
		dotPath = new DotPath(path, fullHeight);

		final PointListIterator pointListIterator = new PointListIterator(svg.substring(end), fullHeight);

		this.endHead = getExtremity(link.getType().getDecor2(), pointListIterator);
		this.startTail = getExtremity(link.getType().getDecor1(), pointListIterator);

		if (this.noteLabelText != null) {
			final Point2D pos = getXY(svg, this.noteLabelColor, fullHeight);
			corner1.manage(pos);
			this.noteLabelXY = TextBlockUtils.asPositionable(noteLabelText, stringBounder, pos);
		}

		if (this.startTailText != null) {
			final Point2D pos = getXY(svg, this.startTailColor, fullHeight);
			corner1.manage(pos);
			this.startTailLabelXY = TextBlockUtils.asPositionable(startTailText, stringBounder, pos);
		}

		if (this.endHeadText != null) {
			final Point2D pos = getXY(svg, this.endHeadColor, fullHeight);
			corner1.manage(pos);
			this.endHeadLabelXY = TextBlockUtils.asPositionable(endHeadText, stringBounder, pos);
		}

		if (isOpalisable() == false) {
			setOpale(false);
		}
	}

	private boolean isOpalisable() {
		return dotPath.getBeziers().size() <= 1;
	}

	private Point2D.Double getXY(String svg, int color, int height) {
		final int idx = getIndexFromColor(svg, color);
		return SvekUtils.getMinXY(SvekUtils.extractPointsList(svg, idx, height));

	}

	private int getIndexFromColor(String svg, int color) {
		String s = "stroke=\"" + StringUtils.getAsHtml(color).toLowerCase() + "\"";
		int idx = svg.indexOf(s);
		if (idx != -1) {
			return idx;
		}
		s = ";stroke:" + StringUtils.getAsHtml(color).toLowerCase() + ";";
		idx = svg.indexOf(s);
		if (idx != -1) {
			return idx;
		}
		throw new IllegalStateException();

	}

	public void drawU(UGraphic ug, double x, double y, HtmlColor color) {
		if (opale) {
			return;
		}

		final Url url = link.getUrl();
		if (url != null) {
			ug.startUrl(url);
		}

		if (link.isAutoLinkOfAGroup()) {
			final Cluster cl = bibliotekon.getCluster((IGroup) link.getEntity1());
			x += cl.getWidth();
			x -= dotPath.getStartPoint().getX() - cl.getMinX();
		}

		x += dx;
		y += dy;

		if (link.isInvis()) {
			return;
		}
		if (this.link.getSpecificColor() != null) {
			color = this.link.getSpecificColor();
		}

		ug.getParam().setColor(color);
		ug.getParam().setBackcolor(null);
		ug.getParam().setStroke(link.getType().getStroke());
		double moveStartX = 0;
		double moveStartY = 0;
		double moveEndX = 0;
		double moveEndY = 0;
		if (projectionCluster != null && link.getEntity1() == projectionCluster.getGroup()) {
			final DotPath copy = new DotPath(dotPath);
			final Point2D start = copy.getStartPoint();
			final Point2D proj = projectionCluster.getClusterPosition().getProjectionOnFrontier(start);
			moveStartX = proj.getX() - start.getX();
			moveStartY = proj.getY() - start.getY();
			copy.forceStartPoint(proj.getX(), proj.getY());
			ug.draw(x, y, copy);
		} else if (projectionCluster != null && link.getEntity2() == projectionCluster.getGroup()) {
			final DotPath copy = new DotPath(dotPath);
			final Point2D end = copy.getEndPoint();
			final Point2D proj = projectionCluster.getClusterPosition().getProjectionOnFrontier(end);
			moveEndX = proj.getX() - end.getX();
			moveEndY = proj.getY() - end.getY();
			copy.forceEndPoint(proj.getX(), proj.getY());
			ug.draw(x, y, copy);
		} else {
			ug.draw(x, y, dotPath);
		}

		// if (picLine1 != null) {
		// final ClusterPosition clusterPosition = picLine1.getClusterPosition();
		// final PointDirected inters = dotPath.getIntersection(clusterPosition);
		// ExtremityStateLine1 extr1 = new ExtremityStateLine1(inters.getAngle(), inters.getPoint2D());
		// extr1.drawU(ug, x, y);
		// } else if (picLine2 != null) {
		// final ClusterPosition clusterPosition = picLine2.getClusterPosition();
		// final PointDirected inters = dotPath.getIntersection(clusterPosition);
		// ExtremityStateLine2 extr2 = new ExtremityStateLine2(inters.getAngle(), inters.getPoint2D());
		// extr2.drawU(ug, x, y);
		// }

		ug.getParam().setStroke(new UStroke());

		if (this.startTail != null) {
			ug.getParam().setColor(color);
			if (this.link.getType().getDecor1().isFill()) {
				ug.getParam().setBackcolor(color);
			} else {
				ug.getParam().setBackcolor(null);
			}
			this.startTail.drawU(ug, x + moveEndX, y + moveEndY);
		}
		if (this.endHead != null) {
			ug.getParam().setColor(color);
			if (this.link.getType().getDecor2().isFill()) {
				ug.getParam().setBackcolor(color);
			} else {
				ug.getParam().setBackcolor(null);
			}
			this.endHead.drawU(ug, x + moveStartX, y + moveStartY);
		}
		if (this.noteLabelText != null) {
			this.noteLabelText.drawU(ug, x + this.noteLabelXY.getPosition().getX(), y
					+ this.noteLabelXY.getPosition().getY());
		}
		if (this.startTailText != null) {
			this.startTailText.drawU(ug, x + this.startTailLabelXY.getPosition().getX(), y
					+ this.startTailLabelXY.getPosition().getY());
		}
		if (this.endHeadText != null) {
			this.endHeadText.drawU(ug, x + this.endHeadLabelXY.getPosition().getX(), y
					+ this.endHeadLabelXY.getPosition().getY());
		}

		if (url != null) {
			ug.closeAction();
		}
	}

	public boolean isInverted() {
		return link.isInverted();
	}

	private double getDecorDzeta() {
		final int size1 = link.getType().getDecor1().getMargin();
		final int size2 = link.getType().getDecor2().getMargin();
		return size1 + size2;
	}

	public double getHorizontalDzeta(StringBounder stringBounder) {
		if (startUid.equals(endUid)) {
			return getDecorDzeta();
		}
		final ArithmeticStrategy strategy;
		if (isHorizontal()) {
			strategy = new ArithmeticStrategySum();
		} else {
			return 0;
		}
		if (noteLabelText != null) {
			strategy.eat(noteLabelText.calculateDimension(stringBounder).getWidth());
		}
		if (startTailText != null) {
			strategy.eat(startTailText.calculateDimension(stringBounder).getWidth());
		}
		if (endHeadText != null) {
			strategy.eat(endHeadText.calculateDimension(stringBounder).getWidth());
		}
		return strategy.getResult() + getDecorDzeta();
	}

	private boolean isHorizontal() {
		return link.getLength() == 1;
	}

	public double getVerticalDzeta(StringBounder stringBounder) {
		if (startUid.equals(endUid)) {
			return getDecorDzeta();
		}
		if (isHorizontal()) {
			return 0;
		}
		final ArithmeticStrategy strategy = new ArithmeticStrategySum();
		if (noteLabelText != null) {
			strategy.eat(noteLabelText.calculateDimension(stringBounder).getHeight());
		}
		if (startTailText != null) {
			strategy.eat(startTailText.calculateDimension(stringBounder).getHeight());
		}
		if (endHeadText != null) {
			strategy.eat(endHeadText.calculateDimension(stringBounder).getHeight());
		}
		return strategy.getResult() + getDecorDzeta();
	}

	public void manageCollision(Collection<Shape> allShapes) {

		for (Shape sh : allShapes) {
			final Positionable cl = PositionableUtils.addMargin(sh, 8, 8);
			if (startTailText != null && PositionableUtils.intersect(cl, startTailLabelXY)) {
				startTailLabelXY = PositionableUtils.moveAwayFrom(cl, startTailLabelXY);
			}
			if (endHeadText != null && PositionableUtils.intersect(cl, endHeadLabelXY)) {
				endHeadLabelXY = PositionableUtils.moveAwayFrom(cl, endHeadLabelXY);
			}
		}

		// final Positionable start = getStartTailPositionnable();
		// if (start != null) {
		// for (Shape sh : allShapes) {
		// if (cut(start, sh)) {
		// avoid(startTailLabelXY, start, sh);
		// }
		// }
		// }
		//
		// final Positionable end = getEndHeadPositionnable();
		// if (end != null) {
		// for (Shape sh : allShapes) {
		// if (cut(end, sh)) {
		// avoid(endHeadLabelXY, end, sh);
		// }
		// }
		// }

	}

	private void avoid(Point2D.Double move, Positionable pos, Shape sh) {
		final Oscillator oscillator = new Oscillator();
		final Point2D.Double orig = new Point2D.Double(move.x, move.y);
		while (cut(pos, sh)) {
			final Point2D.Double m = oscillator.nextPosition();
			move.setLocation(orig.x + m.x, orig.y + m.y);
		}
	}

	private boolean cut(Positionable pos, Shape sh) {
		return BezierUtils.intersect(pos, sh) || tooClose(pos);
	}

	private boolean tooClose(Positionable pos) {
		final double dist = dotPath.getMinDist(BezierUtils.getCenter(pos));
		final Dimension2D dim = pos.getSize();
		// Log.println("dist=" + dist);
		return dist < (dim.getWidth() / 2 + 2) || dist < (dim.getHeight() / 2 + 2);
	}

	public void moveSvek(double deltaX, double deltaY) {
		this.dx += deltaX;
		this.dy += deltaY;
	}

	public final DotPath getDotPath() {
		final DotPath result = new DotPath(dotPath);
		result.moveSvek(dx, dy);
		return result;
	}

	public int getLength() {
		return link.getLength();
	}

	public void setOpale(boolean opale) {
		this.link.setOpale(opale);
		this.opale = opale;

	}

	public boolean isOpale() {
		return opale;
	}

	public boolean isHorizontalSolitary() {
		return link.isHorizontalSolitary();
	}

	public boolean isLinkFromOrToGroup(IEntity group) {
		return link.getEntity1() == group || link.getEntity2() == group;
	}

	public boolean hasEntryPoint() {
		return link.hasEntryPoint();
	}

	public void setProjectionCluster(Cluster cluster) {
		this.projectionCluster = cluster;

	}

	// private Cluster picLine1 = null;
	// private Cluster picLine2 = null;
	//
	// public void setPicLine1(Cluster cluster) {
	// this.picLine1 = cluster;
	// }
	//
	// public void setPicLine2(Cluster cluster) {
	// this.picLine2 = cluster;
	// }

}
