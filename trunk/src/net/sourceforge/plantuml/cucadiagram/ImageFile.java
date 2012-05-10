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
 * Revision $Revision: 7755 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.plantuml.cucadiagram.dot.DrawFile;

public class ImageFile {

	private DrawFile imageFile;
	private final Set<DrawFile> subImages = new HashSet<DrawFile>();

	public final DrawFile getImageFile() {
		return imageFile;
	}

	public final void setImageFile(DrawFile imageFile) {
		this.imageFile = imageFile;
	}

	public final Set<DrawFile> getSubImages() {
		return subImages;
	}

	public void addSubImage(DrawFile subImage) {
		if (subImage == null) {
			throw new IllegalArgumentException();
		}
		subImages.add(subImage);
	}

	public void addSubImage(IEntity other) {
		subImages.addAll(other.getSubImages());
	}

	public DrawFile getImageFile(File searched) throws IOException {
		if (imageFile != null && imageFile.getPng().getCanonicalFile().equals(searched)) {
			return imageFile;
		}
		for (DrawFile f : subImages) {
			if (f.getPng().getCanonicalFile().equals(searched)) {
				return f;
			}
		}
		return null;
	}

	public void cleanSubImage() {
		for (DrawFile f : subImages) {
			f.deleteDrawFile();
		}
	}

}
