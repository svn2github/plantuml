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
 * Revision $Revision: 8066 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;

class USymbolRect extends USymbol {

	private void drawRect(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition, double width,
			double height, boolean shadowing) {
		final URectangle shape = new URectangle(width, height);
		if (shadowing) {
			shape.setDeltaShadow(3.0);
		}
		ug.draw(xTheoricalPosition, yTheoricalPosition, shape);
	}

	private Margin getMargin() {
		return new Margin(10, 10, 10, 10);
	}

	public TextBlock asSmall(final TextBlock label, final TextBlock stereotype, final SymbolContext symbolContext) {
		return new TextBlock() {

			public void drawU(UGraphic ug, double x, double y) {
				final Dimension2D dim = calculateDimension(ug.getStringBounder());
				symbolContext.apply(ug);
				drawRect(ug, x, y, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing());
				final Margin margin = getMargin();
				final TextBlock tb = TextBlockUtils.mergeTB(stereotype, label, HorizontalAlignement.CENTER);
				tb.drawU(ug, x + margin.getX1(), y + margin.getY1());

			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final Dimension2D dimLabel = label.calculateDimension(stringBounder);
				final Dimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				return getMargin().addDimension(Dimension2DDouble.mergeTB(dimStereo, dimLabel));
			}

			public List<Url> getUrls() {
				return Collections.emptyList();
			}
		};
	}

	public TextBlock asBig(final TextBlock title, TextBlock stereotype, final double width, final double height,
			final SymbolContext symbolContext) {
		throw new UnsupportedOperationException();
		// return new TextBlock() {
		//
		// public void drawU(UGraphic ug, double x, double y) {
		// final Dimension2D dim = calculateDimension(ug.getStringBounder());
		// symbolContext.apply(ug);
		// drawArtifact(ug, x, y, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing());
		// title.drawU(ug, x, y);
		//
		// }
		//
		// public Dimension2D calculateDimension(StringBounder stringBounder) {
		// return new Dimension2DDouble(width, height);
		// }
		//
		// public List<Url> getUrls() {
		// return Collections.emptyList();
		// }
		// };
	}

}