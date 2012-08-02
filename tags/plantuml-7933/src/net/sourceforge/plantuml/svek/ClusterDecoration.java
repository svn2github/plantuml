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
 * Revision $Revision: 4236 $
 * 
 */
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class ClusterDecoration {

	final private PackageStyle style;
	final private TextBlock title;
	final private HtmlColor stateBack;

	final private double minX;
	final private double minY;
	final private double maxX;
	final private double maxY;

	public ClusterDecoration(PackageStyle style, TextBlock title, HtmlColor stateBack, double minX, double minY,
			double maxX, double maxY) {
		this.style = style;
		this.title = title;
		this.stateBack = stateBack;
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}

	public void drawU(UGraphic ug, double x, double y, HtmlColor borderColor, boolean shadowing) {
		if (style == PackageStyle.NODE) {
			drawWithTitleNode(ug, x, y, borderColor, shadowing);
		} else if (style == PackageStyle.DATABASE) {
			drawWithTitleDatabase(ug, x, y, borderColor, shadowing);
		} else if (style == PackageStyle.CLOUD) {
			drawWithTitleCloud(ug, x, y, borderColor, shadowing);
		} else if (style == PackageStyle.FRAME) {
			drawWithTitleCorner(ug, x, y, borderColor, shadowing);
		} else if (style == PackageStyle.RECT) {
			drawWithTitleRect(ug, x, y, borderColor, shadowing);
		} else {
			drawWithTitleFolder(ug, x, y, borderColor, shadowing);
		}
	}

	private final int cornersize = 10;

	// Cloud
	private void drawWithTitleCloud(UGraphic ug, double x, double y, HtmlColor borderColor, boolean shadowing) {
		// final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
		final double width = maxX - minX;
		final double height = maxY - minY;
		final UPath shape = getSpecificFrontierForCloud(width, height);
		if (shadowing) {
			shape.setDeltaShadow(3.0);
		}

		ug.getParam().setBackcolor(stateBack);
		ug.getParam().setColor(borderColor);
		ug.getParam().setStroke(new UStroke(2));
		ug.draw(x + minX, y + minY, shape);
		ug.getParam().setStroke(new UStroke());
		title.drawU(ug, x + minX + marginTitleX1, y + minY + 10);

	}

	private UPath getSpecificFrontierForCloud(double width, double height) {
		final UPath path = new UPath();
		path.moveTo(0, 10);
		double x = 0;
		for (int i = 0; i < width - 9; i += 10) {
			path.cubicTo(0 + i, -3 + 10, 2 + i, -5 + 10, 5 + i, -5 + 10);
			path.cubicTo(8 + i, -5 + 10, 10 + i, -3 + 10, 10 + i, 0 + 10);
			x = i + 10;
		}
		double y = 0;
		for (int j = 10; j < height - 9; j += 10) {
			path.cubicTo(x + 3, 0 + j, x + 5, 2 + j, x + 5, 5 + j);
			path.cubicTo(x + 5, 8 + j, x + 3, 10 + j, x, 10 + j);
			y = j + 10;
		}
		for (int i = 0; i < width - 9; i += 10) {
			path.cubicTo(x - i, y + 3, x - 3 - i, y + 5, x - 5 - i, y + 5);
			path.cubicTo(x - 8 - i, y + 5, x - 10 - i, y + 3, x - 10 - i, y);
		}
		for (int j = 0; j < height - 9 - 10; j += 10) {
			path.cubicTo(-3, y - j, -5, y - 2 - j, -5, y - 5 - j);
			path.cubicTo(-5, y - 8 - j, -3, y - 10 - j, 0, y - 10 - j);
		}
		return path;
	}

	// Database
	private void drawWithTitleDatabase(UGraphic ug, double x, double y, HtmlColor borderColor, boolean shadowing) {
		// final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
		final double width = maxX - minX;
		final double height = maxY - minY;
		final UPath shape = new UPath();
		if (shadowing) {
			shape.setDeltaShadow(3.0);
		}
		shape.moveTo(0, 0);
		shape.cubicTo(10, -10, width / 2 - 10, -10, width / 2, -10);
		shape.cubicTo(width / 2 + 10, -10, width - 10, -10, width, 0);
		shape.lineTo(width, height);
		shape.cubicTo(width - 10, height + 10, width / 2 - 10, height + 10, width / 2, height + 10);
		shape.cubicTo(width / 2 + 10, height + 10, 10, height + 10, 0, height);
		shape.lineTo(0, 0);

		ug.getParam().setBackcolor(stateBack);
		ug.getParam().setColor(borderColor);
		ug.getParam().setStroke(new UStroke(2));

		ug.draw(x + minX, y + minY, shape);

		final UPath closing = new UPath();
		closing.moveTo(0, 0);
		closing.cubicTo(10, 10, width / 2 - 10, 10, width / 2, 10);
		closing.cubicTo(width / 2 + 10, 10, width - 10, 10, width, 0);

		ug.draw(x + minX, y + minY, closing);

		ug.getParam().setStroke(new UStroke());
		title.drawU(ug, x + minX + marginTitleX1, y + minY + 10);

	}

	// Corner
	private void drawWithTitleCorner(UGraphic ug, double x, double y, HtmlColor borderColor, boolean shadowing) {
		final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
		final double width = maxX - minX;
		final double height = maxY - minY;
		final URectangle shape = new URectangle(width, height);
		if (shadowing) {
			shape.setDeltaShadow(3.0);
		}

		ug.getParam().setBackcolor(stateBack);
		ug.getParam().setColor(borderColor);
		ug.getParam().setStroke(new UStroke(2));

		ug.draw(x + minX, y + minY, shape);
		final double textWidth = dimTitle.getWidth() + 10;
		final double textHeight = dimTitle.getHeight() + 3;

		final UPath polygon = new UPath();
		polygon.moveTo(textWidth, 1);

		polygon.lineTo(textWidth, textHeight - cornersize);
		polygon.lineTo(textWidth - cornersize, textHeight);

		polygon.lineTo(0, textHeight);
		ug.draw(x + minX, y + minY, polygon);

		ug.getParam().setStroke(new UStroke());
		title.drawU(ug, x + minX + marginTitleX1, y + minY);

	}

	// Node
	private void drawWithTitleNode(UGraphic ug, double x, double y, HtmlColor borderColor, boolean shadowing) {
		final UPolygon shape = getSpecificFrontierForNode(ug.getStringBounder());
		if (shadowing) {
			shape.setDeltaShadow(3.0);
		}
		final double width = maxX - minX;
		final double height = maxY - minY;
		ug.getParam().setBackcolor(stateBack);
		ug.getParam().setColor(borderColor);
		ug.getParam().setStroke(new UStroke(2));
		ug.draw(x + minX, y + minY, shape);
		ug.draw(x + minX + width, y + minY + 10, new ULine(9, -9));
		final UPath path = new UPath();
		path.moveTo(0, 0);
		path.lineTo(width, 0);
		path.lineTo(width, height - 10);
		ug.draw(x + minX, y + minY + 10, path);
		ug.getParam().setStroke(new UStroke());
		title.drawU(ug, x + minX + marginTitleX1, y + minY + marginTitleY1 + 10);
	}

	private UPolygon getSpecificFrontierForNode(StringBounder stringBounder) {
		final double width = maxX - minX;
		final double height = maxY - minY;
		final UPolygon shape = new UPolygon();

		shape.addPoint(0, 10);
		shape.addPoint(10, 0);
		shape.addPoint(width + 10, 0);
		shape.addPoint(width + 10, height - 10);
		shape.addPoint(width, height);
		shape.addPoint(0, height);
		shape.addPoint(0, 10);
		return shape;
	}

	// Folder
	private UPolygon getSpecificFrontierForFolder(StringBounder stringBounder) {
		final double width = maxX - minX;
		final double height = maxY - minY;
		final Dimension2D dimTitle = title.calculateDimension(stringBounder);
		final double wtitle = dimTitle.getWidth() + marginTitleX1 + marginTitleX2;
		final double htitle = dimTitle.getHeight() + marginTitleY1 + marginTitleY2;
		final UPolygon shape = new UPolygon();
		shape.addPoint(0, 0);
		shape.addPoint(wtitle, 0);
		shape.addPoint(wtitle + marginTitleX3, htitle);
		shape.addPoint(width, htitle);
		shape.addPoint(width, height);
		shape.addPoint(0, height);
		shape.addPoint(0, 0);
		return shape;
	}

	private void drawWithTitleFolder(UGraphic ug, double x, double y, HtmlColor borderColor, boolean shadowing) {
		final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
		final double wtitle = dimTitle.getWidth() + marginTitleX1 + marginTitleX2;
		final double htitle = dimTitle.getHeight() + marginTitleY1 + marginTitleY2;
		final UPolygon shape = getSpecificFrontierForFolder(ug.getStringBounder());
		if (shadowing) {
			shape.setDeltaShadow(3.0);
		}

		ug.getParam().setBackcolor(stateBack);
		ug.getParam().setColor(borderColor);
		ug.getParam().setStroke(new UStroke(2));
		ug.draw(x + minX, y + minY, shape);
		ug.draw(x + minX, y + minY + htitle, new ULine(wtitle + marginTitleX3, 0));
		ug.getParam().setStroke(new UStroke());
		title.drawU(ug, x + minX + marginTitleX1, y + minY + marginTitleY1);
	}

	// Rect
	private void drawWithTitleRect(UGraphic ug, double x, double y, HtmlColor borderColor, boolean shadowing) {
		final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
		final double width = maxX - minX;
		final double height = maxY - minY;
		final URectangle shape = new URectangle(width, height);
		if (shadowing) {
			shape.setDeltaShadow(3.0);
		}

		ug.getParam().setBackcolor(stateBack);
		ug.getParam().setColor(borderColor);
		ug.getParam().setStroke(new UStroke(2));

		ug.draw(x + minX, y + minY, shape);
		ug.getParam().setStroke(new UStroke());
		final double deltax = width - dimTitle.getWidth();
		title.drawU(ug, x + minX + deltax / 2, y + minY + 5);
	}

	public final static int marginTitleX1 = 3;
	public final static int marginTitleX2 = 3;
	public final static int marginTitleX3 = 7;
	public final static int marginTitleY0 = 0;
	public final static int marginTitleY1 = 3;
	public final static int marginTitleY2 = 3;

}
