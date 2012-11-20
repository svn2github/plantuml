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
 * Revision $Revision: 8151 $
 *
 */
package net.sourceforge.plantuml.skin.rose;

import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.svek.Boundary;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class ComponentRoseBoundary extends AbstractTextualComponent {

	private final TextBlock stickman;
	private final boolean head;

	public ComponentRoseBoundary(HtmlColor yellow, HtmlColor red, HtmlColor fontColor, UFont font,
			List<? extends CharSequence> stringsToDisplay, boolean head, SpriteContainer spriteContainer,
			double deltaShadow) {
		super(stringsToDisplay, fontColor, font, HorizontalAlignement.CENTER, 3, 3, 0, spriteContainer);
		this.head = head;
		this.stickman = new Boundary(yellow, red, deltaShadow);
	}

	@Override
	protected void drawInternalU(UGraphic ug, Area area) {
		ug.getParam().setColor(getFontColor());
		final TextBlock textBlock = getTextBlock();
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimStickman = stickman.calculateDimension(stringBounder);
		final double delta = (getPreferredWidth(stringBounder) - dimStickman.getWidth()) / 2;

		if (head) {
			textBlock.drawU(ug, getTextMiddlePostion(stringBounder), dimStickman.getHeight());
			ug.translate(delta, 0);
		} else {
			textBlock.drawU(ug, getTextMiddlePostion(stringBounder), 0);
			ug.translate(delta, getTextHeight(stringBounder));
		}
		stickman.drawU(ug, 0, 0);
	}

	private double getTextMiddlePostion(StringBounder stringBounder) {
		return (getPreferredWidth(stringBounder) - getTextWidth(stringBounder)) / 2.0;
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		final Dimension2D dimStickman = stickman.calculateDimension(stringBounder);
		return dimStickman.getHeight() + getTextHeight(stringBounder);
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		final Dimension2D dimStickman = stickman.calculateDimension(stringBounder);
		return Math.max(dimStickman.getWidth(), getTextWidth(stringBounder));
	}

}
