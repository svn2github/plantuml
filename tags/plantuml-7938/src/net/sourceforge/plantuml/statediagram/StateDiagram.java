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
 * Revision $Revision: 8536 $
 *
 */
package net.sourceforge.plantuml.statediagram;

import java.util.Arrays;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.UniqueSequence;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.cucadiagram.EntityUtils;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.LeafType;

public class StateDiagram extends AbstractEntityDiagram {

	@Override
	public IEntity getOrCreateClass(String code) {
		if (code.startsWith("[*]")) {
			throw new IllegalArgumentException();
		}
		if (isGroup(code)) {
			return getGroup(code);
		}
		final IEntity result = getOrCreateLeaf(code, LeafType.STATE);
		return result;
	}

	public IEntity getStart() {
		final IGroup g = getCurrentGroup();
		if (EntityUtils.groupNull(g)) {
			return getOrCreateLeaf("*start", LeafType.CIRCLE_START);
		}
		return getOrCreateLeaf("*start*" + g.getCode(), LeafType.CIRCLE_START);
	}

	public IEntity getEnd() {
		final IGroup p = getCurrentGroup();
		if (EntityUtils.groupNull(p)) {
			return getOrCreateLeaf("*end", LeafType.CIRCLE_END);
		}
		return getOrCreateLeaf("*end*" + p.getCode(), LeafType.CIRCLE_END);
	}

	public IEntity getHistorical() {
		final IGroup g = getCurrentGroup();
		if (EntityUtils.groupNull(g)) {
			return getOrCreateLeaf("*historical", LeafType.PSEUDO_STATE);
		}
		return getOrCreateLeaf("*historical*" + g.getCode(), LeafType.PSEUDO_STATE);
	}

	public IEntity getHistorical(String codeGroup) {
		final IEntity g = getOrCreateGroup(codeGroup, StringUtils.getWithNewlines(codeGroup), null, GroupType.STATE, getRootGroup());
		final IEntity result = getOrCreateLeaf("*historical*" + g.getCode(), LeafType.PSEUDO_STATE);
		endGroup();
		return result;
	}

	public boolean concurrentState() {
		final IGroup cur = getCurrentGroup();
		// printlink("BEFORE");
		if (EntityUtils.groupNull(cur) == false && cur.zgetGroupType() == GroupType.CONCURRENT_STATE) {
			super.endGroup();
		}
		final IGroup conc1 = getOrCreateGroup("CONC" + UniqueSequence.getValue(), Arrays.asList(""), null,
				GroupType.CONCURRENT_STATE, getCurrentGroup());
		if (EntityUtils.groupNull(cur) == false && cur.zgetGroupType() == GroupType.STATE) {
			cur.zmoveEntitiesTo(conc1);
			super.endGroup();
			getOrCreateGroup("CONC" + UniqueSequence.getValue(), Arrays.asList(""), null,
					GroupType.CONCURRENT_STATE, getCurrentGroup());
		}
		// printlink("AFTER");
		return true;
	}

	// private void printlink(String comment) {
	// Log.println("COMMENT="+comment);
	// for (Link l : getLinks()) {
	// Log.println(l);
	// }
	// }

	@Override
	public void endGroup() {
		final IGroup cur = getCurrentGroup();
		if (EntityUtils.groupNull(cur) == false && cur.zgetGroupType() == GroupType.CONCURRENT_STATE) {
			super.endGroup();
		}
		super.endGroup();
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.STATE;
	}

	private boolean hideEmptyDescription = false;

	public final void setHideEmptyDescription(boolean hideEmptyDescription) {
		this.hideEmptyDescription = hideEmptyDescription;
	}

	public final boolean isHideEmptyDescription() {
		return hideEmptyDescription;
	}

//	public Link isEntryPoint(IEntity ent) {
//		final Stereotype stereotype = ent.getStereotype();
//		if (stereotype == null) {
//			return null;
//		}
//		final String label = stereotype.getLabel();
//		if ("<<entrypoint>>".equalsIgnoreCase(label) == false) {
//			return null;
//		}
//		Link inLink = null;
//		Link outLink = null;
//		for (Link link : getLinks()) {
//			if (link.getEntity1() == ent) {
//				if (outLink != null) {
//					return null;
//				}
//				outLink = link;
//			}
//			if (link.getEntity2() == ent) {
//				if (inLink != null) {
//					return null;
//				}
//				inLink = link;
//			}
//		}
//		if (inLink == null || outLink == null) {
//			return null;
//		}
//		final Link result = Link.mergeForEntryPoint(inLink, outLink);
//		result.setEntryPoint(ent.getContainer());
//		return result;
//	}
//
//	public void manageExitAndEntryPoints() {
//		for (IEntity ent : getEntities().values()) {
//			final Link entryPointLink = isEntryPoint(ent);
//			if (entryPointLink != null) {
//				addLink(entryPointLink);
//				for (Link link : new ArrayList<Link>(getLinks())) {
//					if (link.contains(ent)) {
//						removeLink(link);
//					}
//				}
//			}
//		}
//
//	}

}
