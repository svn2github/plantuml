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
 * Revision $Revision: 6711 $
 *
 */
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockWidth;
import net.sourceforge.plantuml.svek.image.EntityImageState;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public final class InnerStateAutonom implements IEntityImage {

	private final IEntityImage im;
	private final TextBlock title;
	private final TextBlockWidth attribute;
	private final HtmlColor borderColor;
	private final HtmlColor backColor;
	private final boolean shadowing;
	private final List<Url> url;
	private final List<Url> suburl;
	private final boolean withSymbol;

	public InnerStateAutonom(final IEntityImage im, final TextBlock title, TextBlockWidth attribute,
			HtmlColor borderColor, HtmlColor backColor, boolean shadowing, List<Url> suburl, List<Url> url,
			boolean withSymbol) {
		this.im = im;
		this.withSymbol = withSymbol;
		this.title = title;
		this.borderColor = borderColor;
		this.backColor = backColor;
		this.shadowing = shadowing;
		this.attribute = attribute;
		this.url = url;
		this.suburl = suburl;
	}

	public final static double THICKNESS_BORDER = 1.5;

	public void drawU(UGraphic ug, double x, double y) {
		final Dimension2D text = title.calculateDimension(ug.getStringBounder());
		final Dimension2D attr = attribute.calculateDimension(ug.getStringBounder());
		final Dimension2D total = getDimension(ug.getStringBounder());
		final double marginForFields = attr.getHeight() > 0 ? IEntityImage.MARGIN : 0;

		final double titreHeight = IEntityImage.MARGIN + text.getHeight() + IEntityImage.MARGIN_LINE;
		final RoundedContainer r = new RoundedContainer(total, titreHeight, attr.getHeight() + marginForFields,
				borderColor, backColor, im.getBackcolor());

		if (url.size() > 0) {
			ug.startUrl(url.get(0));
		}

		r.drawU(ug, x, y, shadowing);
		title.drawU(ug, x + (total.getWidth() - text.getWidth()) / 2, y + IEntityImage.MARGIN);
		attribute.drawU(ug, x + IEntityImage.MARGIN, y + IEntityImage.MARGIN + text.getHeight() + IEntityImage.MARGIN,
				total.getWidth());

		final double spaceYforURL = getSpaceYforURL(ug.getStringBounder());
		im.drawU(ug, x + IEntityImage.MARGIN, y + spaceYforURL);

		if (withSymbol) {
			ug.getParam().setColor(borderColor);
			EntityImageState.drawSymbol(ug, x + total.getWidth(), y + total.getHeight());

		}

		if (url.size() > 0) {
			ug.closeAction();
		}
	}

	private double getSpaceYforURL(StringBounder stringBounder) {
		final Dimension2D text = title.calculateDimension(stringBounder);
		final Dimension2D attr = attribute.calculateDimension(stringBounder);
		final double marginForFields = attr.getHeight() > 0 ? IEntityImage.MARGIN : 0;
		final double titreHeight = IEntityImage.MARGIN + text.getHeight() + IEntityImage.MARGIN_LINE;
		final double suppY = titreHeight + marginForFields + attr.getHeight();
		return suppY + IEntityImage.MARGIN_LINE;
	}

	public HtmlColor getBackcolor() {
		return null;
	}

	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D img = im.getDimension(stringBounder);
		final Dimension2D text = title.calculateDimension(stringBounder);
		final Dimension2D attr = attribute.calculateDimension(stringBounder);

		final Dimension2D dim = Dimension2DDouble.mergeTB(text, attr, img);
		final double marginForFields = attr.getHeight() > 0 ? IEntityImage.MARGIN : 0;

		final Dimension2D result = Dimension2DDouble.delta(dim, IEntityImage.MARGIN * 2 + 2 * IEntityImage.MARGIN_LINE
				+ marginForFields);

		return result;
	}

	public ShapeType getShapeType() {
		return ShapeType.ROUND_RECTANGLE;
	}

	public int getShield() {
		return 0;
	}

}
