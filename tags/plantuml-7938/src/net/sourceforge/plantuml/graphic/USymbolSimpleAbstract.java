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

abstract class USymbolSimpleAbstract extends USymbolAbstract {

	public TextBlock asSmall(final TextBlock label, final TextBlock stereotype, final SymbolContext symbolContext) {
		if (stereotype == null) {
			throw new IllegalArgumentException();
		}
		final TextBlock stickman = getDrawing(symbolContext);
		return new TextBlock() {

			public void drawU(UGraphic ug, double x, double y) {
				final StringBounder stringBounder = ug.getStringBounder();
				final Dimension2D dimName = label.calculateDimension(stringBounder);
				final Dimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				final Dimension2D dimStickMan = stickman.calculateDimension(stringBounder);
				final Dimension2D dimTotal = calculateDimension(stringBounder);
				final double stickmanX = (dimTotal.getWidth() - dimStickMan.getWidth()) / 2;
				final double stickmanY = dimStereo.getHeight();
				symbolContext.apply(ug);
				stickman.drawU(ug, x + stickmanX, y + stickmanY);
				final double labelX = (dimTotal.getWidth() - dimName.getWidth()) / 2;
				final double labelY = dimStickMan.getHeight() + dimStereo.getHeight();
				label.drawU(ug, x + labelX, y + labelY);

				final double stereoX = (dimTotal.getWidth() - dimStereo.getWidth()) / 2;
				stereotype.drawU(ug, x + stereoX, y);

			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final Dimension2D dimName = label.calculateDimension(stringBounder);
				final Dimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				final Dimension2D dimActor = stickman.calculateDimension(stringBounder);
				return Dimension2DDouble.mergeLayoutT12B3(dimStereo, dimActor, dimName);
			}

			public List<Url> getUrls() {
				return Collections.emptyList();
			}
		};
	}

	abstract protected TextBlock getDrawing(final SymbolContext symbolContext);


	public TextBlock asBig(final TextBlock title, TextBlock stereotype, final double width, final double height,
			final SymbolContext symbolContext) {
		throw new UnsupportedOperationException();
	}

}