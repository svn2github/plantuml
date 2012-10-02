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

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.posimo.Moveable;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;

public final class SvekResult implements IEntityImage, Moveable {

	private final Rose rose = new Rose();

	private final HtmlColor clusterBorder;
	private ClusterPosition dim;
	private final DotData dotData;
	private final DotStringFactory dotStringFactory;
	private final boolean hasVerticalLine;

	public SvekResult(ClusterPosition dim, DotData dotData, DotStringFactory dotStringFactory, HtmlColor clusterBorder,
			boolean hasVerticalLine) {
		this.dim = dim;
		this.dotData = dotData;
		this.dotStringFactory = dotStringFactory;
		this.clusterBorder = clusterBorder;
		this.hasVerticalLine = hasVerticalLine;
	}

	public void drawU(UGraphic ug, double x, double y) {
		// final Map<Group, Cluster> groups = new HashMap<Group, Cluster>();

		for (Cluster cluster : dotStringFactory.getBibliotekon().allCluster()) {
			cluster.drawU(ug, x, y, clusterBorder, dotData);
			// groups.put(cluster.getGroup(), cluster);
		}
		// assert groups.size() == dotStringFactory.getAllSubCluster().size();

		final Set<Double> xdots = new TreeSet<Double>();

		for (Shape shape : dotStringFactory.getBibliotekon().allShapes()) {
			final double minX = shape.getMinX();
			final double minY = shape.getMinY();
			shape.getImage().drawU(ug, x + minX, y + minY);
			if (hasVerticalLine) {
				final double xv = x + minX;
				xdots.add(xv);
				xdots.add(xv + shape.getWidth());
			}
		}

		for (Line line : dotStringFactory.getBibliotekon().allLines()) {
			// line.patchLineForCluster(dotStringFactory.getAllSubCluster());
			final HtmlColor color = rose.getHtmlColor(dotData.getSkinParam(), getArrowColorParam(), null);
			line.drawU(ug, x, y, color);
		}

		final double THICKNESS_BORDER = 1.5;
		final int DASH = 8;

		if (xdots.size() > 0) {
			final double height = getDimension(ug.getStringBounder()).getHeight();
			ug.getParam().setColor(clusterBorder);
			ug.getParam().setStroke(new UStroke(DASH, 10, THICKNESS_BORDER));
			for (Double xv : middeling(xdots)) {
				ug.draw(xv, y, new ULine(0, height));
			}
			ug.getParam().setStroke(new UStroke());
		}

	}

	private Collection<Double> middeling(Set<Double> xdots) {
		final List<Double> result = new ArrayList<Double>();
		final Iterator<Double> it = xdots.iterator();
		it.next();
		while (true) {
			if (it.hasNext() == false) {
				return result;
			}
			final double v1 = it.next();
			if (it.hasNext() == false) {
				return result;
			}
			final double v2 = it.next();
			result.add((v1 + v2) / 2);
		}
	}

	private ColorParam getArrowColorParam() {
		if (dotData.getUmlDiagramType() == UmlDiagramType.CLASS) {
			return ColorParam.classArrow;
		} else if (dotData.getUmlDiagramType() == UmlDiagramType.OBJECT) {
			return ColorParam.objectArrow;
		} else if (dotData.getUmlDiagramType() == UmlDiagramType.USECASE) {
			return ColorParam.usecaseArrow;
		} else if (dotData.getUmlDiagramType() == UmlDiagramType.ACTIVITY) {
			return ColorParam.activityArrow;
		} else if (dotData.getUmlDiagramType() == UmlDiagramType.COMPONENT) {
			return ColorParam.componentArrow;
		} else if (dotData.getUmlDiagramType() == UmlDiagramType.STATE) {
			return ColorParam.stateArrow;
		}
		throw new IllegalStateException();
	}

	public HtmlColor getBackcolor() {
		return dotData.getSkinParam().getBackgroundColor();
	}

	public Dimension2D getDimension(StringBounder stringBounder) {
		return dim.getDimension();
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public int getShield() {
		return 0;
	}

	public void moveSvek(double deltaX, double deltaY) {
		dotStringFactory.moveSvek(deltaX, deltaY);
		dim = dim.delta(deltaX > 0 ? deltaX : 0, deltaY > 0 ? deltaY : 0);
	}

}
