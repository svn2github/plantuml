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
 * Revision $Revision: 7736 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EntityFactory {

	private final Map<String, ILeaf> leafs = new LinkedHashMap<String, ILeaf>();
	private final List<Link> links = new ArrayList<Link>();
	private final Map<String, IGroup> groups = new LinkedHashMap<String, IGroup>();

	private final IGroup rootGroup = new GroupRoot(this);

	public IGroup getRootGroup() {
		return rootGroup;
	}

	final Map<String, ILeaf> getLeafs() {
		return Collections.unmodifiableMap(leafs);
	}

	void addLeaf(ILeaf entity) {
		leafs.put(entity.getCode(), entity);
	}

	void removeLeaf(String code) {
		final IEntity removed = leafs.remove(code);
		if (removed == null) {
			throw new IllegalArgumentException();
		}
	}

	void addGroup(IGroup group) {
		groups.put(group.getCode(), group);
	}

	void removeGroup(String code) {
		final IEntity removed = groups.remove(code);
		if (removed == null) {
			throw new IllegalArgumentException();
		}
	}

	final Map<String, IGroup> getGroups() {
		return Collections.unmodifiableMap(groups);
	}

	final List<Link> getLinks() {
		return Collections.unmodifiableList(links);
	}

	void addLink(Link link) {
		links.add(link);
	}

	void removeLink(Link link) {
		final boolean ok = links.remove(link);
		if (ok == false) {
			throw new IllegalArgumentException();
		}
	}


}
