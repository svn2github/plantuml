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
 * Revision $Revision: 7715 $
 *
 */
package net.sourceforge.plantuml.classdiagram.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.cucadiagram.EntityType;

class JavaFile {

	private static final Pattern classDefinition = Pattern
			.compile("^(?:public\\s+|abstract\\s+|final\\s+)*(class|interface|enum)\\s+(\\w+)(?:.*\\b(extends|implements)\\s+([\\w\\s,]+))?");

	private static final Pattern packageDefinition = Pattern.compile("^package\\s+([\\w+.]+)\\s*;");

	private final List<JavaClass> all = new ArrayList<JavaClass>();

	public JavaFile(File f) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
			initFromReader(br);
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	private void initFromReader(BufferedReader br) throws IOException {
		String javaPackage = null;
		String s;
		while ((s = br.readLine()) != null) {
			s = s.trim();
			final Matcher matchPackage = packageDefinition.matcher(s);
			if (matchPackage.find()) {
				javaPackage = matchPackage.group(1);
			} else {
				final Matcher matchClassDefinition = classDefinition.matcher(s);
				if (matchClassDefinition.find()) {
					final String n = matchClassDefinition.group(2);
					final String p = matchClassDefinition.group(4);
					final EntityType type = EntityType.valueOf(matchClassDefinition.group(1).toUpperCase());
					final EntityType parentType = getParentType(type, matchClassDefinition.group(3));
					all.add(new JavaClass(javaPackage, n, p, type, parentType));
				}
			}
		}
	}

	static EntityType getParentType(EntityType type, String extendsOrImplements) {
		if (extendsOrImplements == null) {
			return null;
		}
		if (type == EntityType.CLASS) {
			if (extendsOrImplements.equals("extends")) {
				return EntityType.CLASS;
			}
			return EntityType.INTERFACE;
		}
		return EntityType.INTERFACE;
	}

	public List<JavaClass> getJavaClasses() {
		return Collections.unmodifiableList(all);

	}

}
