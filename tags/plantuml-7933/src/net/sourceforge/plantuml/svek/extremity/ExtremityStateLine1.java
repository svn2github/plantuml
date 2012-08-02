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
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;

class ExtremityStateLine1 extends Extremity implements UDrawable {

	private UPolygon polygon = new UPolygon();
	private final Point2D dest;
	private final double radius = 7;
	private final double angle;

	public ExtremityStateLine1(double angle, Point2D center) {
		this.angle = manageround(angle);
		polygon.addPoint(0, 0);
		this.dest = new Point2D.Double(center.getX(), center.getY());
		final int xAile = 9;
		final int yOuverture = 4;
		polygon.addPoint(-xAile, -yOuverture);
		final int xContact = 5;
		polygon.addPoint(-xContact, 0);
		polygon.addPoint(-xAile, yOuverture);
		polygon.addPoint(0, 0);
		polygon.rotate(this.angle);
		polygon = polygon.translate(center.getX(), center.getY());
	}

	public void drawU(UGraphic ug, double x, double y) {
		ug.getParam().setBackcolor(ug.getParam().getColor());
		ug.draw(x - radius * Math.cos(angle), y - radius * Math.sin(angle), polygon);
		ug.getParam().setBackcolor(HtmlColorUtils.WHITE);
		ug.getParam().setStroke(new UStroke(1.5));
		ug.draw(x + dest.getX() - radius, y + dest.getY() - radius, new UEllipse(radius * 2, radius * 2));
		ug.getParam().setStroke(new UStroke());
		drawLine(ug, getPointOnCircle(x + dest.getX(), y + dest.getY(), Math.PI / 4),
				getPointOnCircle(x + dest.getX(), y + dest.getY(), Math.PI + Math.PI / 4));
		drawLine(ug, getPointOnCircle(x + dest.getX(), y + dest.getY(), -Math.PI / 4),
				getPointOnCircle(x + dest.getX(), y + dest.getY(), Math.PI - Math.PI / 4));
		ug.getParam().setBackcolor(null);
	}

	private Point2D getPointOnCircle(double centerX, double centerY, double angle) {
		final double x = centerX + radius * Math.cos(angle);
		final double y = centerY + radius * Math.sin(angle);
		return new Point2D.Double(x, y);
	}

	static private void drawLine(UGraphic ug, Point2D p1, Point2D p2) {
		final double dx = p2.getX() - p1.getX();
		final double dy = p2.getY() - p1.getY();
		ug.draw(p1.getX(), p1.getY(), new ULine(dx, dy));

	}

}
