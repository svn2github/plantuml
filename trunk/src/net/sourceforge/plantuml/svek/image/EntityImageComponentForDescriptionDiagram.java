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
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class EntityImageComponentForDescriptionDiagram extends AbstractEntityImage {

	final private List<Url> url;

	private final TextBlock asSmall;

	public EntityImageComponentForDescriptionDiagram(ILeaf entity, ISkinParam skinParam) {
		super(entity, skinParam);
		final Stereotype stereotype = entity.getStereotype();
		final TextBlock desc = TextBlockUtils.create(
				entity.getDisplay(),
				new FontConfiguration(getFont(FontParam.COMPONENT, stereotype), getFontColor(FontParam.COMPONENT,
						stereotype)), HorizontalAlignement.CENTER, skinParam);

		final USymbol symbol = entity.getUSymbol();
		if (symbol == null) {
			throw new IllegalArgumentException();
		}
		this.url = entity.getUrls();

		HtmlColor backcolor = getEntity().getSpecificBackColor();
		if (backcolor == null) {
			backcolor = getColor(ColorParam.componentBackground, getStereo());
		}
		final SymbolContext ctx = new SymbolContext(backcolor, getColor(ColorParam.componentBorder, getStereo()))
				.withStroke(new UStroke(1.5)).withShadow(getSkinParam().shadowing());

		TextBlock stereo = TextBlockUtils.empty(0, 0);
		if (stereotype != null && stereotype.getLabel() != null) {
			stereo = TextBlockUtils.create(
					StringUtils.getWithNewlines(stereotype.getLabel()),
					new FontConfiguration(getFont(FontParam.COMPONENT_STEREOTYPE, stereotype), getFontColor(
							FontParam.COMPONENT_STEREOTYPE, null)), HorizontalAlignement.CENTER, skinParam);
		}

		asSmall = symbol.asSmall(desc, stereo, ctx);
	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		return asSmall.calculateDimension(stringBounder);
	}

	public void drawU(UGraphic ug, final double xTheoricalPosition, final double yTheoricalPosition) {
		if (url.size() > 0) {
			ug.startUrl(url.get(0));
		}
		asSmall.drawU(ug, xTheoricalPosition, yTheoricalPosition);
		ug.getParam().setStroke(new UStroke());

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
