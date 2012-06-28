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
package net.sourceforge.plantuml.jdbc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public abstract class TraceObject {

	static private final File f = new File("c:/tracejdbc/trace" + System.currentTimeMillis() + ".txt");
	
	static private final boolean TRACE = false;

	protected void log(int level, String... args) {
		if (TRACE==false) {
			return;
		}
		if (level == 0) {
			return;
		}
		synchronized (f) {
			f.getParentFile().mkdirs();
			PrintWriter pw = null;
			try {
				final FileWriter fw = new FileWriter(f, true);
				pw = new PrintWriter(fw);
				if (args.length > 0) {
					final Exception tr = new Exception();
					tr.fillInStackTrace();
					final StackTraceElement functionName = tr.getStackTrace()[1];
					pw.println("LOG " + functionName + " " + Arrays.asList(args));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				pw.close();
			}
		}

	}

	protected void trace(int level, String... args) {
		if (TRACE==false) {
			return;
		}
		if (level == 0) {
			return;
		}
		synchronized (f) {
			f.getParentFile().mkdirs();
			PrintWriter pw = null;
			try {
				final FileWriter fw = new FileWriter(f, true);
				pw = new PrintWriter(fw);
				final Exception tr = new Exception();
				tr.fillInStackTrace();
				final StackTraceElement functionName = tr.getStackTrace()[1];
				pw.println("CALLING " + functionName);
				if (args.length > 0) {
					pw.println(Arrays.asList(args));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				pw.close();
			}
		}
	}

}
