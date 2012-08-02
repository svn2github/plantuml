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
 * Revision $Revision: 4129 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.eps.EpsStrategy;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.ugraphic.eps.UGraphicEps;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;
import net.sourceforge.plantuml.ugraphic.svg.UGraphicSvg;

public abstract class UGraphicUtils {

	public static UDrawable translate(final UDrawable d, final double dx, final double dy) {
		return new UDrawable() {
			public void drawU(UGraphic ug, double x, double y) {
				d.drawU(ug, x + dx, y + dy);
			}
		};

	}

	public static UGraphic translate(final UGraphic g, final double tx, final double ty) {
		return new UGraphic() {

			public void centerChar(double x, double y, char c, UFont font) {
				g.centerChar(tx + x, ty + y, c, font);
			}

			public void draw(double x, double y, UShape shape) {
				g.draw(tx + x, ty + y, shape);
			}

			public UParam getParam() {
				return g.getParam();
			}

			public StringBounder getStringBounder() {
				return g.getStringBounder();
			}

			public double getTranslateX() {
				return g.getTranslateX();
			}

			public double getTranslateY() {
				return g.getTranslateY();
			}

			public void setClip(UClip clip) {
				throw new UnsupportedOperationException();
			}

			public void setTranslate(double dx, double dy) {
				g.setTranslate(dx, dy);
			}

			public void translate(double dx, double dy) {
				g.translate(dx, dy);
			}

			public void setAntiAliasing(boolean trueForOn) {
				g.setAntiAliasing(trueForOn);
			}

			public void startUrl(Url url) {
				g.startUrl(url);
			}

			public void closeAction() {
				g.closeAction();
			}

			public ColorMapper getColorMapper() {
				return g.getColorMapper();
			}

			public UGroup createGroup() {
				return g.createGroup();
			}
		};
	}

	public static void writeImage(OutputStream os, String metadata, FileFormatOption fileFormatOption,
			ColorMapper colorMapper, HtmlColor background, TextBlock image) throws IOException {
		final FileFormat fileFormat = fileFormatOption.getFileFormat();
		if (fileFormat == FileFormat.PNG) {
			final BufferedImage im = createImage(colorMapper, background, image);
			PngIO.write(im, os, metadata, 96);
		} else if (fileFormat == FileFormat.SVG) {
			final UGraphicSvg svg = new UGraphicSvg(colorMapper, StringUtils.getAsHtml(colorMapper
					.getMappedColor(background)), false);
			image.drawU(svg, 0, 0);
			svg.createXml(os);
		} else if (fileFormat == FileFormat.EPS) {
			final UGraphicEps ug = new UGraphicEps(colorMapper, EpsStrategy.getDefault2());
			image.drawU(ug, 0, 0);
			os.write(ug.getEPSCode().getBytes());
		} else {
			throw new UnsupportedOperationException();
		}
	}

	private static BufferedImage createImage(ColorMapper colorMapper, HtmlColor background, TextBlock image) {
		EmptyImageBuilder builder = new EmptyImageBuilder(10, 10, colorMapper.getMappedColor(background));
		Graphics2D g2d = builder.getGraphics2D();

		final UGraphicG2d tmp = new UGraphicG2d(colorMapper, g2d, null, 1.0);
		final Dimension2D size = image.calculateDimension(tmp.getStringBounder());
		g2d.dispose();

		builder = new EmptyImageBuilder(size.getWidth(), size.getHeight(), colorMapper.getMappedColor(background));
		final BufferedImage im = builder.getBufferedImage();
		g2d = builder.getGraphics2D();
		// if (disableTextAliasing) {
		// g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		// }
		final UGraphicG2d ug = new UGraphicG2d(colorMapper, g2d, null, 1.0);
		image.drawU(ug, 0, 0);
		g2d.dispose();
		return im;
	}

}
