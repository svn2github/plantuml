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
 * Revision $Revision: 5183 $
 *
 */
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;

class Boundary implements TextBlock {

	private final double margin = 4;

	private final double radius = 12;
	private final double left = 17;
	private final HtmlColor backgroundColor;
	private final HtmlColor foregroundColor;
	private final float thickness = 2;

	private final double deltaShadow;

	public Boundary(HtmlColor backgroundColor, HtmlColor foregroundColor, double deltaShadow) {
		this.backgroundColor = backgroundColor;
		this.foregroundColor = foregroundColor;
		this.deltaShadow = deltaShadow;
	}

	public void drawU(UGraphic ug, double x, double y) {
		x += margin;
		y += margin;
		ug.getParam().setStroke(new UStroke(thickness));
		ug.getParam().setBackcolor(backgroundColor);
		ug.getParam().setColor(foregroundColor);
		final UEllipse circle = new UEllipse(radius * 2, radius * 2);
		circle.setDeltaShadow(deltaShadow);
		final ULine line1 = new ULine(0, radius * 2);
		line1.setDeltaShadow(deltaShadow);
		ug.draw(x, y, line1);
		final ULine line2 = new ULine(left, 0);
		line2.setDeltaShadow(deltaShadow);
		ug.draw(x, y + radius, line2);
		ug.draw(x + left, y, circle);

		ug.getParam().setStroke(new UStroke());

	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(radius * 2 + left + 2 * margin, radius * 2 + 2 * margin);
	}

	public List<Url> getUrls() {
		return Collections.emptyList();
	}

}
