/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 6285 $
 *
 */
package net.sourceforge.plantuml.eggs;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.graphic.GraphicStrings;

public class PSystemEgg extends AbstractPSystem {

	private final List<String> strings = new ArrayList<String>();

	PSystemEgg(String sentence) {
		final StringTokenizer st = new StringTokenizer(sentence, "|");
		while (st.hasMoreTokens()) {
			strings.add(st.nextToken());
		}
	}

//	public List<File> createFiles(File suggestedFile, FileFormatOption fileFormat) throws IOException, InterruptedException {
//		OutputStream os = null;
//		try {
//			os = new FileOutputStream(suggestedFile);
//			getGraphicStrings().writeImage(os, fileFormat);
//		} finally {
//			if (os != null) {
//				os.close();
//			}
//		}
//		return Arrays.asList(suggestedFile);
//	}

	public void exportDiagram(OutputStream os, int index, FileFormatOption fileFormat) throws IOException {
		getGraphicStrings().writeImage(os, fileFormat);
	}

	private GraphicStrings getGraphicStrings() throws IOException {
		final Font font = new Font("SansSerif", Font.PLAIN, 12);
		return new GraphicStrings(strings, font, Color.BLACK, Color.WHITE, false);
	}

	public String getDescription() {
		return "(Easter Eggs)";
	}

}