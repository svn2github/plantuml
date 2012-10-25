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
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;

class USymbolArtifact extends USymbol {

	private void drawArtifact(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition, double widthTotal,
			double heightTotal, boolean shadowing) {

		final URectangle form = new URectangle(widthTotal, heightTotal);
		if (shadowing) {
			form.setDeltaShadow(4);
		}

		ug.draw(xTheoricalPosition, yTheoricalPosition, form);

		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);
		final double heightSymbol = 14;
		polygon.addPoint(0, heightSymbol);
		final double widthSymbol = 12;
		polygon.addPoint(widthSymbol, heightSymbol);
		final int cornersize = 6;
		polygon.addPoint(widthSymbol, cornersize);
		polygon.addPoint(widthSymbol - cornersize, 0);
		polygon.addPoint(0, 0);
		// if (shadowing) {
		// polygon.setDeltaShadow(3.0);
		// }

		final double xSymbol = xTheoricalPosition + widthTotal - widthSymbol - 5;
		final double ySymbol = yTheoricalPosition + 5;

		ug.draw(xSymbol, ySymbol, polygon);
		ug.draw(xSymbol + widthSymbol - cornersize, ySymbol, new ULine(0, cornersize));
		ug.draw(xSymbol + widthSymbol, ySymbol + cornersize, new ULine(-cornersize, 0));

	}

	private Margin getMargin() {
		return new Margin(10, 10 + 10, 10 + 3, 10);
	}

	public TextBlock asSmall(final TextBlock label, final TextBlock stereotype, final SymbolContext symbolContext) {
		return new TextBlock() {

			public void drawU(UGraphic ug, double x, double y) {
				final Dimension2D dim = calculateDimension(ug.getStringBounder());
				symbolContext.apply(ug);
				drawArtifact(ug, x, y, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing());
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

	public TextBlock asBig(final TextBlock title, TextBlock stereotype, final double width,
			final double height, final SymbolContext symbolContext) {
		return new TextBlock() {

			public void drawU(UGraphic ug, double x, double y) {
				final Dimension2D dim = calculateDimension(ug.getStringBounder());
				symbolContext.apply(ug);
				drawArtifact(ug, x, y, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing());
				title.drawU(ug, x, y);

			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(width, height);
			}

			public List<Url> getUrls() {
				return Collections.emptyList();
			}
		};
	}

}