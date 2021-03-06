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
 * Revision $Revision: 6104 $
 *
 */
package net.sourceforge.plantuml.project2;

class TaskMerge implements Task {

	private final String code;
	private final String name;
	private final Task task1;
	private final Task task2;

	TaskMerge(String code, String name, Task task1, Task task2) {
		this.code = code;
		this.name = name;
		this.task1 = task1;
		this.task2 = task2;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public long getLoad() {
		throw new UnsupportedOperationException();
	}

	public TimeElement getStart() {
		return TimeUtils.min(task1.getStart(), task2.getStart());
	}

	public TimeElement getEnd() {
		return TimeUtils.max(task1.getEnd(), task2.getEnd());
	}

	public TimeElement getCompleted() {
		return TimeUtils.max(task1.getCompleted(), task2.getCompleted());
	}

}
