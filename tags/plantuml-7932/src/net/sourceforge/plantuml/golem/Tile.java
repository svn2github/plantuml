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
 * Revision $Revision: 7715 $
 *
 */
package net.sourceforge.plantuml.golem;

import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;

public class Tile implements TextBlock {

	private static double SIZE = 40;
	private final int num;

	private final UFont numberFont = new UFont("Monospaced", Font.PLAIN, 11);
	private final FontConfiguration fc = new FontConfiguration(numberFont, HtmlColorUtils.BLACK);
	private final Map<TileGeometry, TileArea> geometries;

	Tile(int num) {
		this.num = num;
		final Map<TileGeometry, TileArea> tmp = new EnumMap<TileGeometry, TileArea>(TileGeometry.class);
		for (TileGeometry g : TileGeometry.values()) {
			tmp.put(g, new TileArea(this, g));
		}
		this.geometries = Collections.unmodifiableMap(tmp);
	}

	public TileArea getArea(TileGeometry geometry) {
		return this.geometries.get(geometry);
	}

	public void drawU(UGraphic ug, double x, double y) {
		ug.getParam().setColor(HtmlColorUtils.BLACK);
		final TextBlock n = TextBlockUtils.create(Arrays.asList("" + num), fc, HorizontalAlignement.LEFT,
				new SpriteContainerEmpty());
		final Dimension2D dimNum = n.calculateDimension(ug.getStringBounder());
		final Dimension2D dimTotal = calculateDimension(ug.getStringBounder());
		final double diffx = dimTotal.getWidth() - dimNum.getWidth();
		final double diffy = dimTotal.getHeight() - dimNum.getHeight();
		final double radius = Math.max(dimNum.getWidth(), dimNum.getHeight());
		final double diffx2 = dimTotal.getWidth() - radius;
		final double diffy2 = dimTotal.getHeight() - radius;
		n.drawU(ug, x + diffx / 2, y + diffy / 2);
		ug.draw(x, y, new URectangle(SIZE, SIZE));
		ug.draw(x + diffx2 / 2, y + diffy2 / 2, new UEllipse(radius, radius));
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(SIZE, SIZE);
	}

	public List<Url> getUrls() {
		return Collections.emptyList();
	}
}
