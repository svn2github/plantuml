/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 6104 $
 *
 */
package net.sourceforge.plantuml.project.graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.util.Arrays;

import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.project.Item;
import net.sourceforge.plantuml.project.Project;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;

class ItemHeader {

	private final Font font = new Font("Serif", Font.PLAIN, 9);
	private final Project project;
	private final FontConfiguration fontConfig = new FontConfiguration(font, Color.BLACK);

	public ItemHeader(Project project) {
		this.project = project;
	}

	public void draw(UGraphic ug, double x, double y) {

		final StringBounder stringBounder = ug.getStringBounder();

		ug.getParam().setColor(Color.BLACK);
		ug.draw(x, y, new URectangle(getWidth(stringBounder), getHeight(stringBounder)));

		for (Item it : project.getValidItems()) {
			final TextBlock b = TextBlockUtils.create(Arrays.asList("" + it.getCode()), fontConfig,
					HorizontalAlignement.LEFT);
			final Dimension2D dim = b.calculateDimension(stringBounder);
			b.drawU(ug, x, y);
			y += dim.getHeight();
			ug.draw(x, y, new ULine(getWidth(stringBounder), 0));
		}
	}

	public double getWidth(StringBounder stringBounder) {
		double width = 0;
		for (Item it : project.getValidItems()) {
			final Dimension2D dim = stringBounder.calculateDimension(font, it.getCode());
			width = Math.max(width, dim.getWidth());
		}
		return width;
	}

	public double getHeight(StringBounder stringBounder) {
		double height = 0;
		for (Item it : project.getValidItems()) {
			final Dimension2D dim = stringBounder.calculateDimension(font, it.getCode());
			height += dim.getHeight();

		}
		return height;
	}

	public double getPosition(StringBounder stringBounder, Item item) {
		double pos = 0;
		for (Item it : project.getValidItems()) {
			if (it == item) {
				return pos;
			}
			final Dimension2D dim = stringBounder.calculateDimension(font, it.getCode());
			pos += dim.getHeight();

		}
		throw new IllegalArgumentException();
	}

}