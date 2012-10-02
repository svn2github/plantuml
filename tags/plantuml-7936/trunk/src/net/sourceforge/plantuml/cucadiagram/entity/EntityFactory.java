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
package net.sourceforge.plantuml.cucadiagram.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.cucadiagram.Bodier;
import net.sourceforge.plantuml.cucadiagram.GroupRoot;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.skin.VisibilityModifier;

public class EntityFactory {

	private final Map<String, ILeaf> leafs = new LinkedHashMap<String, ILeaf>();
	private final List<Link> links = new ArrayList<Link>();
	private final Map<String, IGroup> groups = new LinkedHashMap<String, IGroup>();

	private final IGroup rootGroup = new GroupRoot(this);

	public ILeaf createLeaf(String code, List<? extends CharSequence> display, LeafType entityType,
			IGroup parentContainer, Set<VisibilityModifier> hides) {
		if (entityType == null) {
			throw new IllegalArgumentException();
		}
		final Bodier bodier = new Bodier(entityType, hides);
		final EntityImpl result = new EntityImpl(this, code, bodier, parentContainer, entityType);
		result.setDisplay(display);
		return result;
	}

	public IGroup createGroup(String code, List<? extends CharSequence> display, String namespace,
			GroupType groupType, IGroup parentContainer, Set<VisibilityModifier> hides) {
		if (groupType == null) {
			throw new IllegalArgumentException();
		}
		final Bodier bodier = new Bodier(null, hides);
		final EntityImpl result = new EntityImpl(this, code, bodier, parentContainer, groupType, namespace);
		if (display != null) {
			result.setDisplay(display);
		}
		return result;
	}

	public IGroup getRootGroup() {
		return rootGroup;
	}

	public final Map<String, ILeaf> getLeafs() {
		return Collections.unmodifiableMap(leafs);
	}

	public void addLeaf(ILeaf entity) {
		leafs.put(entity.getCode(), entity);
	}

	void removeLeaf(String code) {
		final IEntity removed = leafs.remove(code);
		if (removed == null) {
			throw new IllegalArgumentException();
		}
	}

	public void addGroup(IGroup group) {
		groups.put(group.getCode(), group);
	}

	void removeGroup(String code) {
		final IEntity removed = groups.remove(code);
		if (removed == null) {
			throw new IllegalArgumentException();
		}
	}

	public final Map<String, IGroup> getGroups() {
		return Collections.unmodifiableMap(groups);
	}

	public final List<Link> getLinks() {
		return Collections.unmodifiableList(links);
	}

	public void addLink(Link link) {
		links.add(link);
	}

	public void removeLink(Link link) {
		final boolean ok = links.remove(link);
		if (ok == false) {
			throw new IllegalArgumentException();
		}
	}

	public IGroup muteToGroup(String code, String namespace, GroupType type, IGroup parent) {
		final ILeaf leaf = getLeafs().get(code);
		((EntityImpl) leaf).muteToGroup(namespace, type, parent);
		final IGroup result = (IGroup) leaf;
		removeLeaf(code);
		return result;
	}

}
