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
 * Revision $Revision: 8600 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.posimo.Positionable;
import net.sourceforge.plantuml.posimo.PositionableImpl;
import net.sourceforge.plantuml.sequencediagram.MessageNumber;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class TextBlockUtils {

	public static TextBlock create(List<? extends CharSequence> texts, FontConfiguration fontConfiguration,
			HorizontalAlignement horizontalAlignement, SpriteContainer spriteContainer) {
		if (texts.size() > 0) {
			if (texts.get(0) instanceof Stereotype) {
				return createStereotype(texts, fontConfiguration, horizontalAlignement, spriteContainer, 0);
			}
			if (texts.get(texts.size() - 1) instanceof Stereotype) {
				return createStereotype(texts, fontConfiguration, horizontalAlignement, spriteContainer,
						texts.size() - 1);
			}
			if (texts.get(0) instanceof MessageNumber) {
				return createMessageNumber(texts, fontConfiguration, horizontalAlignement, spriteContainer);
			}
		}
		return new TextBlockSimple(texts, fontConfiguration, horizontalAlignement, spriteContainer);
	}

	private static TextBlock createMessageNumber(List<? extends CharSequence> texts,
			FontConfiguration fontConfiguration, HorizontalAlignement horizontalAlignement,
			SpriteContainer spriteContainer) {
		final MessageNumber number = (MessageNumber) texts.get(0);
		return new TextBlockWithNumber(number.getNumber(), texts.subList(1, texts.size()), fontConfiguration,
				horizontalAlignement, spriteContainer);

	}

	private static TextBlock createStereotype(List<? extends CharSequence> texts, FontConfiguration fontConfiguration,
			HorizontalAlignement horizontalAlignement, SpriteContainer spriteContainer, int position) {
		final Stereotype stereotype = (Stereotype) texts.get(position);
		if (stereotype.isSpotted()) {
			final CircledCharacter circledCharacter = new CircledCharacter(stereotype.getCharacter(),
					stereotype.getRadius(), stereotype.getCircledFont(), stereotype.getHtmlColor(), null,
					fontConfiguration.getColor());
			if (stereotype.getLabel() == null) {
				return new TextBlockSpotted(circledCharacter, texts.subList(1, texts.size()), fontConfiguration,
						horizontalAlignement, spriteContainer);
			}
			return new TextBlockSpotted(circledCharacter, texts, fontConfiguration, horizontalAlignement,
					spriteContainer);
		}
		return new TextBlockSimple(texts, fontConfiguration, horizontalAlignement, spriteContainer);
	}

	public static TextBlock withMargin(TextBlock textBlock, double marginX, double marginY) {
		return new TextBlockMarged(textBlock, marginX, marginX, marginY, marginY);
	}

	public static TextBlockWidth withMargin(TextBlockWidth textBlock, double marginX, double marginY) {
		return new TextBlocWidthMarged(textBlock, marginX, marginX, marginY, marginY);
	}

	public static TextBlockWidth withMargin(TextBlockWidth textBlock, double marginX, double marginY1, double marginY2) {
		return new TextBlocWidthMarged(textBlock, marginX, marginX, marginY1, marginY2);
	}

	public static TextBlock withMinWidth(TextBlock textBlock, double minWidth, HorizontalAlignement horizontalAlignement) {
		return new TextBlockMinWidth(textBlock, minWidth, horizontalAlignement);
	}

	public static TextBlock withMargin(TextBlock textBlock, double marginX1, double marginX2, double marginY1,
			double marginY2) {
		return new TextBlockMarged(textBlock, marginX1, marginX2, marginY1, marginY2);
	}

	public static TextBlock empty(final double width, final double height) {
		return new TextBlock() {
			public void drawU(UGraphic ug, double x, double y) {
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(width, height);
			}

			public List<Url> getUrls() {
				return Collections.emptyList();
			}
		};
	}

	public static Positionable asPositionable(TextBlock textBlock, StringBounder stringBounder, Point2D pt) {
		return new PositionableImpl(pt, textBlock.calculateDimension(stringBounder));
	}

	public static TextBlock fromIEntityImage(final IEntityImage image) {
		return new TextBlock() {
			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return image.getDimension(stringBounder);
			}

			public void drawU(UGraphic ug, double x, double y) {
				image.drawU(ug, x, y);
			}

			public List<Url> getUrls() {
				return Collections.emptyList();
			}
		};
	}

	public static TextBlock mergeLR(TextBlock b1, TextBlock b2) {
		return new TextBlockHorizontal(b1, b2);
	}

	public static TextBlock mergeTB(TextBlock b1, TextBlock b2, HorizontalAlignement horizontalAlignement) {
		return new TextBlockVertical(b1, b2, horizontalAlignement);
	}

}
