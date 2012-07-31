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
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.EntityFactory;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.GroupHierarchy;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.Rankdir;
import net.sourceforge.plantuml.ugraphic.ColorMapper;

final public class DotData implements PortionShower {

	final private List<Link> links;
	final private Collection<ILeaf> leafs;
	final private UmlDiagramType umlDiagramType;
	final private ISkinParam skinParam;
	final private Rankdir rankdir;
	final private GroupHierarchy groupHierarchy;
	final private IEntity topParent;
	final private PortionShower portionShower;
	private int dpi = 96;

	private boolean visibilityModifierPresent;
	private final ColorMapper colorMapper;
	private final EntityFactory entityFactory;

	public DotData(IEntity topParent, List<Link> links, Collection<ILeaf> leafs,
			UmlDiagramType umlDiagramType, ISkinParam skinParam, Rankdir rankdir, GroupHierarchy groupHierarchy,
			PortionShower portionShower, ColorMapper colorMapper, EntityFactory entityFactory) {
		this.topParent = topParent;
		if (topParent == null) {
			throw new IllegalArgumentException();
		}
		this.colorMapper = colorMapper;
		this.links = links;
		this.leafs = leafs;
		this.umlDiagramType = umlDiagramType;
		this.skinParam = skinParam;
		this.rankdir = rankdir;
		this.groupHierarchy = groupHierarchy;
		this.portionShower = portionShower;
		this.entityFactory = entityFactory;
	}

	public DotData(IEntity topParent, List<Link> links, Collection<ILeaf> leafs,
			UmlDiagramType umlDiagramType, ISkinParam skinParam, Rankdir rankdir, GroupHierarchy groupHierarchy,
			ColorMapper colorMapper, EntityFactory entityFactory) {
		this(topParent, links, leafs, umlDiagramType, skinParam, rankdir, groupHierarchy, new PortionShower() {
			public boolean showPortion(EntityPortion portion, IEntity entity) {
				return true;
			}
		}, colorMapper, entityFactory);
	}

	public boolean hasUrl() {
		return true;
	}

	public boolean isThereVisibilityImages() {
		return visibilityModifierPresent;
	}

	public void setVisibilityModifierPresent(boolean b) {
		checkObjectOrClassDiagram();
		this.visibilityModifierPresent = b;
	}

	private void checkObjectOrClassDiagram() {
		if (umlDiagramType != UmlDiagramType.CLASS && umlDiagramType != UmlDiagramType.OBJECT) {
			throw new IllegalStateException();
		}
	}

	public UmlDiagramType getUmlDiagramType() {
		return umlDiagramType;
	}

	public ISkinParam getSkinParam() {
		return skinParam;
	}

	public Rankdir getRankdir() {
		return rankdir;
	}

	public GroupHierarchy getGroupHierarchy() {
		return groupHierarchy;
	}

	public List<Link> getLinks() {
		return links;
	}

	public Collection<ILeaf> getLeafs() {
		return leafs;
	}

	public final Set<ILeaf> getAllLinkedTo(final ILeaf ent1) {
		final Set<ILeaf> result = new HashSet<ILeaf>();
		result.add(ent1);
		int size = 0;
		do {
			size = result.size();
			for (ILeaf ent : leafs) {
				if (isDirectyLinked(ent, result)) {
					result.add(ent);
				}
			}
		} while (size != result.size());
		result.remove(ent1);
		return Collections.unmodifiableSet(result);
	}

	public final Set<ILeaf> getAllLinkedDirectedTo(final ILeaf ent1) {
		final Set<ILeaf> result = new HashSet<ILeaf>();
		for (ILeaf ent : leafs) {
			if (isDirectlyLinkedSlow(ent, ent1)) {
				result.add(ent);
			}
		}
		return Collections.unmodifiableSet(result);
	}

	private boolean isDirectyLinked(IEntity ent1, Collection<ILeaf> others) {
		for (ILeaf ent2 : others) {
			if (isDirectlyLinkedSlow(ent1, ent2)) {
				return true;
			}
		}
		return false;
	}

	private boolean isDirectlyLinkedSlow(IEntity ent1, IEntity ent2) {
		for (Link link : links) {
			if (link.isBetween(ent1, ent2)) {
				return true;
			}
		}
		return false;
	}

	public List<Link> getAutoLinks(IEntity g) {
		final List<Link> result = new ArrayList<Link>();
		for (Link l : links) {
			if (l.isAutolink(g)) {
				result.add(l);
			}
		}
		return Collections.unmodifiableList(result);
	}

	public List<Link> getToEdgeLinks(IEntity g) {
		final List<Link> result = new ArrayList<Link>();
		for (Link l : links) {
			if (l.isToEdgeLink(g)) {
				result.add(l);
			}
		}
		return Collections.unmodifiableList(result);
	}

	public List<Link> getFromEdgeLinks(IEntity g) {
		final List<Link> result = new ArrayList<Link>();
		for (Link l : links) {
			if (l.isFromEdgeLink(g)) {
				result.add(l);
			}
		}
		return Collections.unmodifiableList(result);
	}

	public final IEntity getTopParent() {
		return topParent;
	}

	public boolean isEmpty(IGroup g) {
		return groupHierarchy.isEmpty(g);
	}

	public boolean showPortion(EntityPortion portion, IEntity entity) {
		return portionShower.showPortion(portion, entity);
	}

	public final int getDpi() {
		return dpi;
	}

	public double getDpiFactor() {
		if (dpi == 96) {
			return 1.0;
		}
		return dpi / 96.0;
	}

	public final void setDpi(int dpi) {
		this.dpi = dpi;
	}

	private boolean hideEmptyDescription = false;

	public final void setHideEmptyDescription(boolean hideEmptyDescription) {
		this.hideEmptyDescription = hideEmptyDescription;
	}

	public final boolean isHideEmptyDescription() {
		return hideEmptyDescription;
	}

	public final ColorMapper getColorMapper() {
		return colorMapper;
	}

	public final EntityFactory getEntityFactory() {
		return entityFactory;
	}

}
