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
 * Revision $Revision: 5183 $
 *
 */
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.EntityPosition;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.Cluster;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class EntityImageStateBorder extends AbstractEntityImage {

	public static final double RADIUS = 6;
	private final TextBlock desc;
	private final Cluster stateParent;
	private final EntityPosition entityPosition;

	public EntityImageStateBorder(ILeaf leaf, ISkinParam skinParam, Cluster stateParent) {
		super(leaf, skinParam);

		this.entityPosition = leaf.getEntityPosition();
		if (entityPosition == EntityPosition.NORMAL) {
			throw new IllegalArgumentException();
		}
		this.stateParent = stateParent;
		final Stereotype stereotype = leaf.getStereotype();

		this.desc = TextBlockUtils.create(leaf.getDisplay(), new FontConfiguration(
				getFont(FontParam.STATE, stereotype), getFontColor(FontParam.STATE, stereotype)),
				HorizontalAlignement.CENTER, skinParam);
	}

	private boolean upPosition(double yTheoricalPosition) {
		final Point2D clusterCenter = stateParent.getClusterPosition().getPointCenter();
		return yTheoricalPosition < clusterCenter.getY();
	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(RADIUS * 2, RADIUS * 2);
	}

	public double getMaxWidthFromLabelForEntryExit(StringBounder stringBounder) {
		final Dimension2D dimDesc = desc.calculateDimension(stringBounder);
		return dimDesc.getWidth();
	}

	public void drawU(UGraphic ug, final double xTheoricalPosition, final double yTheoricalPosition) {
		final Shadowable circle = new UEllipse(RADIUS * 2, RADIUS * 2);
		// if (getSkinParam().shadowing()) {
		// circle.setDeltaShadow(4);
		// }

		double y = yTheoricalPosition;
		final Dimension2D dimDesc = desc.calculateDimension(ug.getStringBounder());
		final double x = xTheoricalPosition - (dimDesc.getWidth() - 2 * RADIUS) / 2;
		if (upPosition(yTheoricalPosition)) {
			y -= 2 * RADIUS + dimDesc.getHeight();
		} else {
			y += 2 * RADIUS;
		}
		desc.drawU(ug, x, y);

		ug.getParam().setStroke(new UStroke(1.5));
		ug.getParam().setColor(getColor(ColorParam.stateBorder, getStereo()));
		HtmlColor backcolor = getEntity().getSpecificBackColor();
		if (backcolor == null) {
			backcolor = getColor(ColorParam.stateBackground, getStereo());
		}
		ug.getParam().setBackcolor(backcolor);

		ug.draw(xTheoricalPosition, yTheoricalPosition, circle);
		if (entityPosition == EntityPosition.EXIT_POINT) {
			final double xc = xTheoricalPosition + RADIUS + .5;
			final double yc = yTheoricalPosition + RADIUS + .5;
			final double radius = RADIUS - .5;
			drawLine(ug, getPointOnCircle(xc, yc, Math.PI / 4, radius),
					getPointOnCircle(xc, yc, Math.PI + Math.PI / 4, radius));
			drawLine(ug, getPointOnCircle(xc, yc, -Math.PI / 4, radius),
					getPointOnCircle(xc, yc, Math.PI - Math.PI / 4, radius));
			ug.getParam().setStroke(new UStroke());
		}

	}

	private Point2D getPointOnCircle(double xc, double yc, double angle, double radius) {
		final double x = xc + radius * Math.cos(angle);
		final double y = yc + radius * Math.sin(angle);
		return new Point2D.Double(x, y);
	}

	static private void drawLine(UGraphic ug, Point2D p1, Point2D p2) {
		final double dx = p2.getX() - p1.getX();
		final double dy = p2.getY() - p1.getY();
		ug.draw(p1.getX(), p1.getY(), new ULine(dx, dy));

	}

	public ShapeType getShapeType() {
		return ShapeType.CIRCLE;
	}

	public int getShield() {
		return 0;
	}

}
