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

class USymbolDatabase extends USymbol {

	private void drawDatabase(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition, double width,
			double height, boolean shadowing) {
		final UPath shape = new UPath();
		if (shadowing) {
			shape.setDeltaShadow(3.0);
		}
		shape.moveTo(0, 10);
		shape.cubicTo(10, 0, width / 2 - 10, 0, width / 2, 0);
		shape.cubicTo(width / 2 + 10, 0, width - 10, 0, width, 10);
		shape.lineTo(width, height - 10);
		shape.cubicTo(width - 10, height, width / 2 - 10, height, width / 2, height);
		shape.cubicTo(width / 2 + 10, height, 10, height, 0, height - 10);
		shape.lineTo(0, 10);

		ug.draw(xTheoricalPosition, yTheoricalPosition, shape);

		final UPath closing = new UPath();
		closing.moveTo(0, 10);
		closing.cubicTo(10, 20, width / 2 - 10, 20, width / 2, 20);
		closing.cubicTo(width / 2 + 10, 20, width - 10, 20, width, 10);

		ug.draw(xTheoricalPosition, yTheoricalPosition, closing);

	}

	private Margin getMargin() {
		return new Margin(10, 10, 20, 5);
	}

	public TextBlock asSmall(final TextBlock label, final TextBlock stereotype, final SymbolContext symbolContext) {
		return new TextBlock() {

			public void drawU(UGraphic ug, double x, double y) {
				final Dimension2D dim = calculateDimension(ug.getStringBounder());
				symbolContext.apply(ug);
				drawDatabase(ug, x, y, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing());
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
				drawDatabase(ug, x, y, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing());
				final Dimension2D dimStereo = stereotype.calculateDimension(ug.getStringBounder());
				final double posStereo = (width - dimStereo.getWidth()) / 2;
				stereotype.drawU(ug, x + posStereo, y);

				final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
				final double posTitle = (width - dimTitle.getWidth()) / 2;
				title.drawU(ug, x + posTitle, y + 21);

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