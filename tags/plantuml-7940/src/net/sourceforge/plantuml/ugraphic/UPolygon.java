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
 * Revision $Revision: 8056 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class UPolygon extends AbstractShadowable {

	private final List<Point2D.Double> all = new ArrayList<Point2D.Double>();

	private double minX = Double.MAX_VALUE;
	private double minY = Double.MAX_VALUE;
	private double maxX = -Double.MAX_VALUE;
	private double maxY = -Double.MAX_VALUE;

	public UPolygon() {
	}

	public UPolygon(List<Point2D.Double> points) {
		all.addAll(points);
		for (Point2D.Double pt : all) {
			manageMinMax(pt.getX(), pt.getY());
		}
	}

	public void addPoint(double x, double y) {
		all.add(new Point2D.Double(x, y));
		manageMinMax(x, y);
	}

	private void manageMinMax(double x, double y) {
		if (x > maxX) {
			maxX = x;
		}
		if (x < minX) {
			minX = x;
		}
		if (y > maxY) {
			maxY = y;
		}
		if (y < minY) {
			minY = y;
		}
	}

	public List<Point2D.Double> getPoints() {
		return all;
	}

	public UPolygon translate(double dx, double dy) {
		final UPolygon result = new UPolygon();
		for (Point2D.Double pt : all) {
			result.addPoint(pt.x + dx, pt.y + dy);
		}
		return result;
	}

	public void rotate(double theta) {
		final AffineTransform rotate = AffineTransform.getRotateInstance(theta);
		for (Point2D.Double pt : all) {
			rotate.transform(pt, pt);
		}
	}

	@Override
	public String toString() {
		return super.toString() + " " + all;
	}

	public double getHeight() {
		return maxY - minY;
	}

	public double getWidth() {
		return maxX - minX;
	}
}
