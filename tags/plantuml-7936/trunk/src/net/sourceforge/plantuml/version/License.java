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
package net.sourceforge.plantuml.version;

import java.util.ArrayList;
import java.util.List;

public class License {

	private final List<String> text = new ArrayList<String>();

	private License() {
		text.add("========================================================================");
		text.add("PlantUML : a free UML diagram generator");
		text.add("========================================================================");
		text.add("");
		text.add("(C) Copyright 2009-2012, Arnaud Roques");
		text.add("");
		text.add("Project Info:  http://plantuml.sourceforge.net");
		text.add("");
		if (isCloseSource()) {
			text.add("PlantUML is free of charge software; you can redistribute it as is freely");
			text.add("in a free or commercial product, but without modifying it.");
			text.add("");
			text.add("PlantUML distributed in the hope that it will be useful, but");
			text.add("WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY");
			text.add("or FITNESS FOR A PARTICULAR PURPOSE.");
			text.add("");
			text.add("Note that images (whatever their format : PNG, SVG...) generated by running PlantUML");
			text.add("are owned by the author of their corresponding sources code (that is, their");
			text.add("textual description in PlantUML language).");
		} else {
			text.add("PlantUML is free software; you can redistribute it and/or modify it");
			text.add("under the terms of the GNU General Public License as published by");
			text.add("the Free Software Foundation, either version 3 of the License, or");
			text.add("(at your option) any later version.");
			text.add("");
			text.add("PlantUML distributed in the hope that it will be useful, but");
			text.add("WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY");
			text.add("or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public");
			text.add("License for more details.");
			text.add("");
			text.add("You should have received a copy of the GNU General Public");
			text.add("License along with this library; if not, write to the Free Software");
			text.add("Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,");
			text.add("USA.");
			text.add("");
			text.add("Note that images (whatever their format : PNG, SVG...) generated by running PlantUML");
			text.add("are owned by the author of their corresponding sources code (that is, their");
			text.add("textual description in PlantUML language). Those images are not covered by");
			text.add("the GPL license.");
			text.add("");
			text.add("The generated images can then be used without any reference to the GPL license.");
			text.add("It is not even necessary to stipulate that they have been generated with PlantUML,");
			text.add("also this will be appreciate by PlantUML team.");
			text.add("");
			text.add("There is an exception : if the textual description in PlantUML language is also covered");
			text.add("by a license (like the GPL), then the generated images are logically covered");
			text.add("by the very same license.");
		}
	}

	public static List<String> getText() {
		return new License().text;
	}

	public static boolean isCloseSource() {
		try {
			Class.forName("com.ctreber.acearth.ACearth");
			return false;
		} catch (ClassNotFoundException e) {
			return true;
		}
	}
}