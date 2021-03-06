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
 * Revision $Revision: 7947 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.ugraphic.UGraphic;

class TextBlockSpotted extends TextBlockSimple {

	private final CircledCharacter circledCharacter;

	public TextBlockSpotted(CircledCharacter circledCharacter, List<? extends CharSequence> texts,
			FontConfiguration fontConfiguration, HorizontalAlignement horizontalAlignement, SpriteContainer spriteContainer) {
		super(texts, fontConfiguration, horizontalAlignement, spriteContainer);
		this.circledCharacter = circledCharacter;
	}

	@Override
	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final double widthCircledCharacter = getCircledCharacterWithAndMargin(stringBounder);
		final double heightCircledCharacter = circledCharacter.getPreferredHeight(stringBounder);

		final Dimension2D dim = super.calculateDimension(stringBounder);
		return new Dimension2DDouble(dim.getWidth() + widthCircledCharacter, Math.max(heightCircledCharacter,
				dim.getHeight()));
	}

	private double getCircledCharacterWithAndMargin(StringBounder stringBounder) {
		return circledCharacter.getPreferredWidth(stringBounder) + 6.0;
	}

	@Override
	public void drawU(UGraphic ug, double x, double y) {
		final double atX = ug.getTranslateX();
		final double atY = ug.getTranslateY();
		final StringBounder stringBounder = ug.getStringBounder();

		// final double deltaY = calculateDimension(stringBounder).getHeight()
		// - circledCharacter.getPreferredHeight(stringBounder);

		// circledCharacter.drawU(ug, (int) x, (int) (y + deltaY / 2.0));
		circledCharacter.drawU(ug, x, y);

		ug.setTranslate(atX, atY);
		final double widthCircledCharacter = getCircledCharacterWithAndMargin(stringBounder);
		ug.translate(widthCircledCharacter, 0);

		super.drawU(ug, x, y);

		ug.setTranslate(atX, atY);
	}

}
