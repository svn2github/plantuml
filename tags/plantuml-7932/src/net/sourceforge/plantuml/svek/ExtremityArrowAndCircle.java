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

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;

class ExtremityArrowAndCircle extends Extremity implements UDrawable {

	private UPolygon polygon = new UPolygon();
	// private final ULine line;
	private final Point2D contact;
	private final Point2D dest;
	private final double radius = 5;

	public ExtremityArrowAndCircle(Point2D p1, double angle, Point2D center) {
		angle = manageround(angle);
		polygon.addPoint(0, 0);
		final int xAile = 9;
		final int yOuverture = 4;
		final int xContact = 5;
		this.dest = new Point2D.Double(p1.getX(), p1.getY());
		polygon.addPoint(-xAile, -yOuverture);
		polygon.addPoint(-xContact, 0);
		polygon.addPoint(-xAile, yOuverture);
		polygon.addPoint(0, 0);
		polygon.rotate(angle + Math.PI / 2);
		polygon = polygon.translate(p1.getX() + radius * Math.sin(angle), p1.getY() - radius * Math.cos(angle));
		contact = new Point2D.Double(p1.getX() - xContact * Math.cos(angle + Math.PI / 2), p1.getY() - xContact
				* Math.sin(angle + Math.PI / 2));
		// this.line = new ULine(center.getX() - contact.getX(), center.getY() - contact.getY());
	}

	public void drawU(UGraphic ug, double x, double y) {
		ug.getParam().setBackcolor(ug.getParam().getColor());
		ug.draw(x, y, polygon);
		// if (line.getLength() > 2) {
		// ug.draw(x + contact.getX(), y + contact.getY(), line);
		// }
		ug.getParam().setBackcolor(HtmlColorUtils.WHITE);
		ug.getParam().setStroke(new UStroke(1.5));
		ug.draw(x + dest.getX() - radius, y + dest.getY() - radius, new UEllipse(radius * 2, radius * 2));
		ug.getParam().setStroke(new UStroke());
		ug.getParam().setBackcolor(null);
	}

}
