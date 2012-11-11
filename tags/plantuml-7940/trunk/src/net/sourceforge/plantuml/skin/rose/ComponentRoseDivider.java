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
 * Revision $Revision: 3837 $
 *
 */
package net.sourceforge.plantuml.skin.rose;

import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class ComponentRoseDivider extends AbstractTextualComponent {

	// private final int outMargin = 5;
	private final HtmlColor background;
	private final boolean empty;
	private final boolean withShadow;

	public ComponentRoseDivider(HtmlColor fontColor, UFont font, HtmlColor background,
			List<? extends CharSequence> stringsToDisplay, SpriteContainer spriteContainer, boolean withShadow) {
		super(stringsToDisplay, fontColor, font, HorizontalAlignement.CENTER, 4, 4, 4, spriteContainer);
		this.background = background;
		this.empty = stringsToDisplay.get(0).length() == 0;
		this.withShadow = withShadow;
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		final TextBlock textBlock = getTextBlock();
		final StringBounder stringBounder = ug.getStringBounder();
		final double textWidth = getTextWidth(stringBounder);
		final double textHeight = getTextHeight(stringBounder);

		final double deltaX = 6;
		final double xpos = (dimensionToUse.getWidth() - textWidth - deltaX) / 2;
		final double ypos = (dimensionToUse.getHeight() - textHeight) / 2;

		// if (empty) {
		ug.getParam().setColor(background);
		ug.getParam().setBackcolor(background);
		final URectangle rectLong = new URectangle(dimensionToUse.getWidth(), 3);
		if (withShadow) {
			rectLong.setDeltaShadow(2);
		}
		ug.draw(0, dimensionToUse.getHeight() / 2 - 1, rectLong);
		// }
		ug.getParam().setColor(HtmlColorUtils.BLACK);
		ug.draw(0, dimensionToUse.getHeight() / 2 - 1, new ULine(dimensionToUse.getWidth(), 0));
		ug.draw(0, dimensionToUse.getHeight() / 2 + 2, new ULine(dimensionToUse.getWidth(), 0));

		if (empty == false) {
			ug.getParam().setColor(HtmlColorUtils.BLACK);
			ug.getParam().setBackcolor(background);

			ug.getParam().setStroke(new UStroke(2));
			final URectangle rect = new URectangle(textWidth + deltaX, textHeight);
			if (withShadow) {
				rect.setDeltaShadow(4);
			}
			ug.draw(xpos, ypos, rect);
			ug.getParam().setStroke(new UStroke());

			textBlock.drawU(ug, xpos + deltaX, ypos + getMarginY());
		}
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + 20;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getTextWidth(stringBounder) + 30;
	}

}
