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
 * Modified by : Arno Peterson
 * 
 * Revision $Revision: 5183 $
 *
 */
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.PackageStyle;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class EntityImageComponent extends AbstractEntityImage {

	final private TextBlock desc;
	final private List<Url> url;

	private TextBlock stereo = TextBlockUtils.empty(0, 0);;
	private PackageStyle style;
	private USymbol symbol;

	public EntityImageComponent(ILeaf entity, ISkinParam skinParam) {
		super(entity, skinParam);
		final Stereotype stereotype = entity.getStereotype();
		this.desc = TextBlockUtils.create(
				entity.getDisplay(),
				new FontConfiguration(getFont(FontParam.COMPONENT, stereotype), getFontColor(FontParam.COMPONENT,
						stereotype)), HorizontalAlignement.CENTER, skinParam);

		this.style = stereotype == null ? null : stereotype.getPackageStyle();
		if (this.style == null) {
			this.style = skinParam.useUml2ForComponent() ? PackageStyle.COMPONENT2 : PackageStyle.COMPONENT1;
			if (stereotype != null && stereotype.getLabel() != null) {
				this.stereo = TextBlockUtils.create(
						StringUtils.getWithNewlines(stereotype.getLabel()),
						new FontConfiguration(getFont(FontParam.COMPONENT_STEREOTYPE, stereotype), getFontColor(
								FontParam.COMPONENT_STEREOTYPE, null)), HorizontalAlignement.CENTER, skinParam);
			}
		}
		this.symbol = getUSymbol();
		this.url = entity.getUrls();

	}

	static class Margin {
		private final double x1;
		private final double x2;
		private final double y1;
		private final double y2;

		private Margin(double x1, double x2, double y1, double y2) {
			this.x1 = x1;
			this.x2 = x2;
			this.y1 = y1;
			this.y2 = y2;
		}

		private double getWidth() {
			return x1 + x2;
		}

		private double getHeight() {
			return y1 + y2;
		}
	}

	private USymbol getUSymbol() {
		if (style == PackageStyle.NODE) {
			return USymbol.NODE;
		}
		return null;
	}

	private Margin getSuppDimension() {
		if (style == PackageStyle.COMPONENT1) {
			return new Margin(10, 10, 10, 10);
		} else if (style == PackageStyle.COMPONENT2) {
			return new Margin(10, 14, 16, 11);
		} else if (style == PackageStyle.NODE) {
			return new Margin(10 + 5, 20 + 5, 15 + 5, 5 + 5);
		} else if (style == PackageStyle.FOLDER) {
			return new Margin(10, 10, 15, 5);
		} else if (style == PackageStyle.RECT) {
			return new Margin(10, 10, 10, 10);
		} else if (style == PackageStyle.FRAME) {
			return new Margin(10, 10, 15, 5);
		} else if (style == PackageStyle.CLOUD) {
			return new Margin(15, 15, 15, 15);
		} else if (style == PackageStyle.DATABASE) {
			return new Margin(10, 10, 25, 10);
		} else if (style == PackageStyle.STORAGE) {
			return new Margin(10 + 5, 10 + 5, 10 + 5, 10 + 5);
		} else if (style == PackageStyle.ARTIFACT) {
			return new Margin(10, 10, 10, 10);
		} else if (style == PackageStyle.AGENT) {
			return new Margin(10, 10, 10, 10);
		}
		return new Margin(0, 0, 0, 0);
	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D dim = Dimension2DDouble.mergeTB(desc.calculateDimension(stringBounder),
				stereo.calculateDimension(stringBounder));
		final Margin margin = getSuppDimension();
		return Dimension2DDouble.delta(dim, margin.getWidth(), margin.getHeight());
	}

	private void drawStyled(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
		final StringBounder stringBounder = ug.getStringBounder();

		final Dimension2D dim = getDimension(stringBounder);
		final double width = dim.getWidth();
		final double height = dim.getHeight();
		style.drawU(ug, xTheoricalPosition, yTheoricalPosition, new Dimension2DDouble(width, height), null,
				getSkinParam().shadowing());

		ug.getParam().setStroke(new UStroke());

		final Dimension2D dimTotal = getDimension(stringBounder);
		final Dimension2D dimDesc = desc.calculateDimension(stringBounder);
		final Dimension2D dimStereo = stereo.calculateDimension(stringBounder);
		final Dimension2D dimTwoText = Dimension2DDouble.mergeTB(dimStereo, dimDesc);
		final Margin margin = getSuppDimension();
		final double x = xTheoricalPosition + margin.x1
				+ (dimTotal.getWidth() - margin.getWidth() - dimDesc.getWidth()) / 2;
		final double y = yTheoricalPosition + margin.y1
				+ (dimTotal.getHeight() - margin.getHeight() - dimTwoText.getHeight()) / 2;
		desc.drawU(ug, x, y + dimStereo.getHeight());

		final double stereoX = xTheoricalPosition + margin.x1
				+ (dimTotal.getWidth() - margin.getWidth() - dimStereo.getWidth()) / 2;
		stereo.drawU(ug, stereoX, y);

	}

	public void drawU(UGraphic ug, final double xTheoricalPosition, final double yTheoricalPosition) {
		if (url.size() > 0) {
			ug.startUrl(url.get(0));
		}
		HtmlColor backcolor = getEntity().getSpecificBackColor();
		if (backcolor == null) {
			backcolor = getColor(ColorParam.componentBackground, getStereo());
		}
		ug.getParam().setBackcolor(backcolor);
		if (symbol == null) {
			ug.getParam().setStroke(new UStroke(1.5));
			ug.getParam().setColor(getColor(ColorParam.componentBorder, getStereo()));
			drawStyled(ug, xTheoricalPosition, yTheoricalPosition);
		} else {
			final SymbolContext ctx = new SymbolContext(backcolor, getColor(ColorParam.componentBorder, getStereo()))
					.withStroke(new UStroke(1.5)).withShadow(getSkinParam().shadowing());
			symbol.asSmall(TextBlockUtils.mergeTB(desc, stereo, HorizontalAlignement.CENTER), ctx).drawU(ug,
					xTheoricalPosition, yTheoricalPosition);
		}
		if (url.size() > 0) {
			ug.closeAction();
		}

	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public int getShield() {
		return 0;
	}

}
