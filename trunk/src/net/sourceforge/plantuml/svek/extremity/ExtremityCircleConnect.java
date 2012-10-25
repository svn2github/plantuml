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
package net.sourceforge.plantuml.svek.extremity;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;

class ExtremityCircleConnect extends Extremity implements UDrawable {

	private final double px;
	private final double py;
	private final Point2D dest;
	private final double radius = 6;
	private final double radius2 = 10;
	private final double ortho;

	public ExtremityCircleConnect(Point2D p1, double ortho) {
		this.px = p1.getX() - radius;
		this.py = p1.getY() - radius;
		this.dest = new Point2D.Double(p1.getX(), p1.getY());
		this.ortho = ortho;
	}

	public void drawU(UGraphic ug, double x, double y) {
		ug.getParam().setBackcolor(HtmlColorUtils.WHITE);
		ug.getParam().setStroke(new UStroke(1.5));
		ug.draw(x + dest.getX() - radius, y + dest.getY() - radius, new UEllipse(radius * 2, radius * 2));
		// Point2D pp1 = getPointOnCircle(ortho - Math.PI / 4);
		// Point2D pp2 = getPointOnCircle(ortho + Math.PI / 4);

		final double deg = -ortho * 180 / Math.PI + 90 - 45;
		final UEllipse arc1 = new UEllipse(2 * radius2, 2 * radius2, deg, 90);
		ug.draw(x + dest.getX() - radius2, y + dest.getY() - radius2, arc1);

		// drawLine(ug, x, y, pp1, pp2);
		ug.getParam().setBackcolor(null);
		ug.getParam().setStroke(new UStroke());
	}

	// private Point2D getPointOnCircle(double angle) {
	// final double x = px + radius + radius2 * Math.cos(angle);
	// final double y = py + radius + radius2 * Math.sin(angle);
	// return new Point2D.Double(x, y);
	// }
	//
	// static private void drawLine(UGraphic ug, double x, double y, Point2D p1, Point2D p2) {
	// final double dx = p2.getX() - p1.getX();
	// final double dy = p2.getY() - p1.getY();
	// ug.draw(x + p1.getX(), y + p1.getY(), new ULine(dx, dy));
	//
	// }

}
