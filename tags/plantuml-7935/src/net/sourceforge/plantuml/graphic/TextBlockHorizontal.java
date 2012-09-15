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
 * Revision $Revision: 6577 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.ugraphic.UGraphic;

class TextBlockHorizontal implements TextBlock {

	private final TextBlock b1;
	private final TextBlock b2;

	public TextBlockHorizontal(TextBlock b1, TextBlock b2) {
		this.b1 = b1;
		this.b2 = b2;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim1 = b1.calculateDimension(stringBounder);
		final Dimension2D dim2 = b2.calculateDimension(stringBounder);
		return Dimension2DDouble.mergeLR(dim1, dim2);
	}

	public void drawU(UGraphic ug, double x, double y) {
		final Dimension2D dim = calculateDimension(ug.getStringBounder());
		final Dimension2D dimb1 = b1.calculateDimension(ug.getStringBounder());
		final Dimension2D dimb2 = b2.calculateDimension(ug.getStringBounder());
		b1.drawU(ug, x, y + (dim.getHeight() - dimb1.getHeight()) / 2);
		final Dimension2D dim1 = b1.calculateDimension(ug.getStringBounder());
		b2.drawU(ug, x + dim1.getWidth(), y + (dim.getHeight() - dimb2.getHeight()) / 2);
	}
	
	public List<Url> getUrls() {
		return StringUtils.merge(b1.getUrls(), b2.getUrls());
	}


}