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

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;

class MiddleCircleCircled extends Extremity implements UDrawable {

	private final double angle;
	private final MiddleCircleCircledMode mode;
	private final double radius1 = 6;
	private final UEllipse circle = new UEllipse(2 * radius1, 2 * radius1);

	private final double radius2 = 10;
	private final UEllipse bigcircle = new UEllipse(2 * radius2, 2 * radius2);

	public MiddleCircleCircled(double angle, MiddleCircleCircledMode mode) {
		this.angle = angle;
		this.mode = mode;
	}

	public void drawU(UGraphic ug, double x, double y) {

		ug.getParam().setBackcolor(HtmlColorUtils.WHITE);
		if (mode == MiddleCircleCircledMode.BOTH) {
			final HtmlColor color = ug.getParam().getColor();
			ug.getParam().setColor(HtmlColorUtils.WHITE);
			ug.draw(x - radius2, y - radius2, bigcircle);
			ug.getParam().setColor(color);
		}

		ug.getParam().setStroke(new UStroke(1.5));

		final double d = 0;
		if (mode == MiddleCircleCircledMode.MODE1 || mode == MiddleCircleCircledMode.BOTH) {
			final UEllipse arc1 = new UEllipse(2 * radius2, 2 * radius2, angle, 90);
			ug.draw(x - radius2 + d, y - radius2 + d, arc1);
		}
		if (mode == MiddleCircleCircledMode.MODE2 || mode == MiddleCircleCircledMode.BOTH) {
			final UEllipse arc2 = new UEllipse(2 * radius2, 2 * radius2, angle + 180, 90);
			ug.draw(x - radius2 + d, y - radius2 + d, arc2);
		}
		ug.draw(x - radius1, y - radius1, circle);
		ug.getParam().setStroke(new UStroke());

	}

}
