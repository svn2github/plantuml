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
import net.sourceforge.plantuml.ugraphic.UShape;

class USymbolComponent2 extends USymbolAbstract {

	private void drawNode(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition, double widthTotal,
			double heightTotal, boolean shadowing) {

		final URectangle form = new URectangle(widthTotal, heightTotal);
		if (shadowing) {
			form.setDeltaShadow(4);
		}

		final UShape small = new URectangle(15, 10);
		final UShape tiny = new URectangle(4, 2);

		ug.draw(xTheoricalPosition, yTheoricalPosition, form);

		// UML 2 Component Notation
		ug.draw(xTheoricalPosition + widthTotal - 20, yTheoricalPosition + 5, small);
		ug.draw(xTheoricalPosition + widthTotal - 22, yTheoricalPosition + 7, tiny);
		ug.draw(xTheoricalPosition + widthTotal - 22, yTheoricalPosition + 11, tiny);

	}

	private Margin getMargin() {
		return new Margin(10 + 5, 20 + 5, 15 + 5, 5 + 5);
	}

	public TextBlock asSmall(final TextBlock label, TextBlock stereotype, final SymbolContext symbolContext) {
		return new TextBlock() {

			public void drawU(UGraphic ug, double x, double y) {
				final Dimension2D dim = calculateDimension(ug.getStringBounder());
				symbolContext.apply(ug);
				drawNode(ug, x, y, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing());
				final Margin margin = getMargin();
				label.drawU(ug, x + margin.getX1(), y + margin.getY1());

			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final Dimension2D dim = label.calculateDimension(stringBounder);
				return getMargin().addDimension(dim);
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
				drawNode(ug, x, y, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing());
				title.drawU(ug, x + 3, y + 13);

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