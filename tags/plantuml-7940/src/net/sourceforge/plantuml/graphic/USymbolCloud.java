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
import net.sourceforge.plantuml.ugraphic.UPath;

class USymbolCloud extends USymbol {

	private void drawCloud(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition, double width,
			double height, boolean shadowing) {
		final UPath shape = getSpecificFrontierForCloud(width, height);
		if (shadowing) {
			shape.setDeltaShadow(3.0);
		}
		ug.draw(xTheoricalPosition + 3, yTheoricalPosition - 3, shape);
	}

	private UPath getSpecificFrontierForCloud(double width, double height) {
		final UPath path = new UPath();
		path.moveTo(0, 10);
		double x = 0;
		for (int i = 0; i < width - 9; i += 10) {
			path.cubicTo(0 + i, -3 + 10, 2 + i, -5 + 10, 5 + i, -5 + 10);
			path.cubicTo(8 + i, -5 + 10, 10 + i, -3 + 10, 10 + i, 0 + 10);
			x = i + 10;
		}
		double y = 0;
		for (int j = 10; j < height - 9; j += 10) {
			path.cubicTo(x + 3, 0 + j, x + 5, 2 + j, x + 5, 5 + j);
			path.cubicTo(x + 5, 8 + j, x + 3, 10 + j, x, 10 + j);
			y = j + 10;
		}
		for (int i = 0; i < width - 9; i += 10) {
			path.cubicTo(x - i, y + 3, x - 3 - i, y + 5, x - 5 - i, y + 5);
			path.cubicTo(x - 8 - i, y + 5, x - 10 - i, y + 3, x - 10 - i, y);
		}
		for (int j = 0; j < height - 9 - 10; j += 10) {
			path.cubicTo(-3, y - j, -5, y - 2 - j, -5, y - 5 - j);
			path.cubicTo(-5, y - 8 - j, -3, y - 10 - j, 0, y - 10 - j);
		}
		return path;
	}

	private Margin getMargin() {
		return new Margin(10, 10, 10, 10);
	}

	public TextBlock asSmall(final TextBlock label, final TextBlock stereotype, final SymbolContext symbolContext) {
		return new TextBlock() {

			public void drawU(UGraphic ug, double x, double y) {
				final Dimension2D dim = calculateDimension(ug.getStringBounder());
				symbolContext.apply(ug);
				drawCloud(ug, x, y, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing());
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

	public TextBlock asBig(final TextBlock title, final TextBlock stereotype, final double width, final double height,
			final SymbolContext symbolContext) {
		return new TextBlock() {

			public void drawU(UGraphic ug, double x, double y) {
				final Dimension2D dim = calculateDimension(ug.getStringBounder());
				symbolContext.apply(ug);
				drawCloud(ug, x, y, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing());
				final Dimension2D dimStereo = stereotype.calculateDimension(ug.getStringBounder());
				final double posStereo = (width - dimStereo.getWidth()) / 2;
				stereotype.drawU(ug, x + posStereo, y + 13);
				final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
				final double posTitle = (width - dimTitle.getWidth()) / 2;
				title.drawU(ug, x + posTitle, y + 13 + dimStereo.getHeight());

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