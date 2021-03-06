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
 * Revision $Revision: 4639 $
 * 
 */
package net.sourceforge.plantuml;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.plantuml.graphic.HtmlColor;

public class LanguageDescriptor {

	private final Set<String> type = new TreeSet<String>();
	private final Set<String> keyword = new TreeSet<String>();
	private final Set<String> preproc = new TreeSet<String>();

	public LanguageDescriptor() {
		type.add("actor");
		type.add("participant");
		type.add("usecase");
		type.add("class");
		type.add("interface");
		type.add("abstract");
		type.add("enum");
		type.add("component");
		type.add("state");
		type.add("object");

		keyword.add("@startuml");
		keyword.add("@enduml");
		keyword.add("as");
		keyword.add("also");
		keyword.add("autonumber");
		keyword.add("title");
		keyword.add("newpage");
		keyword.add("box");
		keyword.add("alt");
		keyword.add("else");
		keyword.add("opt");
		keyword.add("loop");
		keyword.add("par");
		keyword.add("break");
		keyword.add("critical");
		keyword.add("note");
		keyword.add("group");
		keyword.add("left");
		keyword.add("right");
		keyword.add("of");
		keyword.add("over");
		keyword.add("end");
		keyword.add("activate");
		keyword.add("deactivate");
		keyword.add("destroy");
		keyword.add("create");
		keyword.add("footbox");
		keyword.add("hide");
		keyword.add("show");
		keyword.add("skinparam");
		keyword.add("skin");
		keyword.add("top");
		keyword.add("bottom");
		keyword.add("top to bottom direction");
		keyword.add("package");
		keyword.add("namespace");
		keyword.add("page");
		keyword.add("up");
		keyword.add("down");
		keyword.add("if");
		keyword.add("else");
		keyword.add("endif");
		keyword.add("partition");
		keyword.add("footer");
		keyword.add("header");
		keyword.add("center");
		keyword.add("rotate");
		keyword.add("ref");
		keyword.add("return");

		
		preproc.add("!include");
		preproc.add("!define");
		preproc.add("!undef");
		preproc.add("!ifdef");
		preproc.add("!endif");
		preproc.add("!ifndef");
	}

	public void print(PrintStream ps) {
		print(ps, "type", type);
		print(ps, "keyword", keyword);
		print(ps, "preprocessor", preproc);
		print(ps, "skinparameter", SkinParam.getPossibleValues());
		print(ps, "color", HtmlColor.names());
		ps.println(";EOF");
	}

	private static void print(PrintStream ps, String name, Collection<String> data) {
		ps.println(";"+name);
		ps.println(";" + data.size());
		for (String k : data) {
			ps.println(k);
		}
		ps.println();
	}

}
