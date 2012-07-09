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
 */
package net.sourceforge.plantuml.ugraphic.html5;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.ugraphic.AbstractUGraphic;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UText;

public class UGraphicHtml5 extends AbstractUGraphic<Html5Drawer> implements ClipContainer {

	final static Graphics2D imDummy = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB).createGraphics();
	private UClip clip;

	private final StringBounder stringBounder;

	public UGraphicHtml5(ColorMapper colorMapper) {
		super(colorMapper, new Html5Drawer());
		stringBounder = StringBounderUtils.asStringBounder(imDummy);
		registerDriver(URectangle.class, new DriverRectangleHtml5(this));
//		registerDriver(UText.class, new DriverTextEps(imDummy, this, strategy));
		registerDriver(UText.class, new DriverNopHtml5());
		registerDriver(ULine.class, new DriverLineHtml5(this));
//		registerDriver(UPolygon.class, new DriverPolygonEps(this));
		registerDriver(UPolygon.class, new DriverNopHtml5());
//		registerDriver(UEllipse.class, new DriverEllipseEps());
//		registerDriver(UImage.class, new DriverImageEps());
//		registerDriver(UPath.class, new DriverPathEps());
//		registerDriver(DotPath.class, new DriverDotPathEps());
	}

	public StringBounder getStringBounder() {
		return stringBounder;
	}

	public void centerChar(double x, double y, char c, UFont font) {
		throw new UnsupportedOperationException();
		
	}

	public void setAntiAliasing(boolean trueForOn) {
		throw new UnsupportedOperationException();
		
	}

	public void startUrl(Url url) {
//		throw new UnsupportedOperationException();
		
	}

	public void closeAction() {
//		throw new UnsupportedOperationException();
		
	}

//	public void close() {
//		getEpsGraphics().close();
//	}


	public void setClip(UClip clip) {
		this.clip = clip == null ? null : clip.translate(getTranslateX(), getTranslateY());
	}

	public UClip getClip() {
		return clip;
	}

	public String generateHtmlCode() {
		return getGraphicObject().generateHtmlCode();
	}

//	public void centerChar(double x, double y, char c, UFont font) {
//		final UnusedSpace unusedSpace = UnusedSpace.getUnusedSpace(font, c);
//
//		final double xpos = x - unusedSpace.getCenterX() - 0.5;
//		final double ypos = y - unusedSpace.getCenterY() - 0.5;
//
//		final TextLayout t = new TextLayout("" + c, font.getFont(), imDummy.getFontRenderContext());
//		getGraphicObject().setStrokeColor(getColorMapper().getMappedColor(getParam().getColor()));
//		DriverTextEps.drawPathIterator(getGraphicObject(), xpos + getTranslateX(), ypos + getTranslateY(), t
//				.getOutline(null).getPathIterator(null));
//
//	}
//
//	static public String getEpsString(ColorMapper colorMapper, EpsStrategy epsStrategy, UDrawable udrawable)
//			throws IOException {
//		final UGraphicHtml5 ug = new UGraphicHtml5(colorMapper, epsStrategy);
//		udrawable.drawU(ug);
//		return ug.getEPSCode();
//	}
//
//	static public void copyEpsToFile(ColorMapper colorMapper, UDrawable udrawable, File f) throws IOException {
//		final PrintWriter pw = new PrintWriter(f);
//		final EpsStrategy epsStrategy = EpsStrategy.getDefault2();
//		pw.print(UGraphicHtml5.getEpsString(colorMapper, epsStrategy, udrawable));
//		pw.close();
//	}
//
//	public void setAntiAliasing(boolean trueForOn) {
//	}
//
//	public void startUrl(String url, String tooltip) {
//		getGraphicObject().openLink(url);
//	}
//
//	public void closeAction() {
//		getGraphicObject().closeLink();
//	}

}
