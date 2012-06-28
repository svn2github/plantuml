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

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

import net.sourceforge.plantuml.version.Version;

public class PlantumlDriver extends TraceObject implements Driver {
	
	public static final DiagramTable DiagramTable = new DiagramTable();
	public static final PlantumlVersionTable PlantumlVersionTable = new PlantumlVersionTable();

	public boolean acceptsURL(String arg0) throws SQLException {
		trace(1, arg0);
		return true;
	}

	public Connection connect(String url, Properties info) throws SQLException {
		trace(1, url);
		log(1, "info=" + info);
		return new PConnection(info.getProperty("user"));
	}

	public int getMajorVersion() {
		trace(0);
		return 1;
	}

	public int getMinorVersion() {
		trace(0);
		return Version.version();
	}

	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		trace(1);
		return null;
	}

	public boolean jdbcCompliant() {
		return false;
	}
}
