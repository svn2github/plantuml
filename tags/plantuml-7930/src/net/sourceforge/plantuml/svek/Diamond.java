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

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.graphic.UDrawable3;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;

class Diamond implements UDrawable3 {

	private int radius;
	private final Point2D center;
	private final Point2D p1;
	private final Point2D p2;
	private Point2D p3;
	private Point2D p4;
	private final boolean fill;
	private final double delta = 0.7;

	public Diamond(Point2D p1, Point2D p2, boolean fill) {
		this.center = new Point2D.Double((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
		final AffineTransform at = AffineTransform.getTranslateInstance(-center.getX(), -center.getY());
		final AffineTransform at2 = AffineTransform.getTranslateInstance(center.getX(), center.getY());
		radius = (int) (p1.distance(p2) / 2);
		if (radius % 2 == 0) {
			radius--;
		}
		this.p1 = p1;
		this.p2 = p2;

		this.p3 = at.transform(this.p1, null);
		this.p3 = new Point2D.Double(p3.getY() * delta, -p3.getX() * delta);
		this.p3 = at2.transform(p3, null);

		this.p4 = at.transform(this.p2, null);
		this.p4 = new Point2D.Double(p4.getY() * delta, -p4.getX() * delta);
		this.p4 = at2.transform(p4, null);
		this.fill = fill;
	}

	// private Point2D putOnCircle(Point2D p) {
	// p = at.transform(p, null);
	// final double coef = p.distance(new Point2D.Double()) / radius;
	// p = new Point2D.Double(p.getX() / coef, p.getY() / coef);
	// return at2.transform(p, null);
	// }

	public void drawU(UGraphic ug, double x, double y) {
		final UPolygon polygon = new UPolygon();
		polygon.addPoint(p1.getX(), p1.getY());
		polygon.addPoint(p3.getX(), p3.getY());
		polygon.addPoint(p2.getX(), p2.getY());
		polygon.addPoint(p4.getX(), p4.getY());
		// polygon.addPoint(p1.getX(), p1.getY());
//		drawLine(ug, x, y, p1, p3);
//		drawLine(ug, x, y, p3, p2);
//		drawLine(ug, x, y, p2, p4);
//		drawLine(ug, x, y, p4, p1);

		if (fill) {
			ug.getParam().setBackcolor(ug.getParam().getColor());
		}
		ug.draw(x, y, polygon);
		ug.getParam().setBackcolor(null);

	}

	static private void drawLine(UGraphic ug, double x, double y, Point2D p1, Point2D p2) {
		final double dx = p2.getX() - p1.getX();
		final double dy = p2.getY() - p1.getY();
		ug.draw(x + p1.getX(), y + p1.getY(), new ULine(dx, dy));

	}

}
