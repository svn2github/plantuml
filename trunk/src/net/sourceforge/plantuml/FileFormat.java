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
 * Revision $Revision: 9353 $
 *
 */
package net.sourceforge.plantuml;

import java.io.File;

public enum FileFormat {
	PNG, SVG, EPS, EPS_TEXT, ATXT, UTXT, XMI_STANDARD, XMI_STAR, XMI_ARGO, PDF, MJPEG, HTML, HTML5;

	public String getFileSuffix() {
		if (name().startsWith("XMI")) {
			return ".XMI";
		}
		if (this == EPS_TEXT) {
			return EPS.getFileSuffix();
		}
		return "." + name().toLowerCase();
	}

	public boolean isEps() {
		if (this == EPS) {
			return true;
		}
		if (this == EPS_TEXT) {
			return true;
		}
		return false;
	}

	public String changeName(String fileName, int cpt) {
		if (cpt == 0) {
			return fileName.replaceAll("\\.\\w+$", getFileSuffix());
		}
		return fileName.replaceAll("\\.\\w+$", "_" + String.format("%03d", cpt) + getFileSuffix());
	}

	public File computeFilename(File pngFile, int i) {
		if (i == 0) {
			return pngFile;
		}
		final File dir = pngFile.getParentFile();
		return new File(dir, computeFilename(pngFile.getName(), i));
//		String name = pngFile.getName();
//		name = name.replaceAll("\\" + getFileSuffix() + "$", "_" + String.format("%03d", i) + getFileSuffix());
//		return new File(dir, name);

	}

	public String computeFilename(String name, int i) {
		if (i == 0) {
			return name;
		}
		return name.replaceAll("\\" + getFileSuffix() + "$", "_" + String.format("%03d", i) + getFileSuffix());
	}

}
