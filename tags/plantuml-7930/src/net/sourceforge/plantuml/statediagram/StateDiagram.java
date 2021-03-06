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
 * Revision $Revision: 8019 $
 *
 */
package net.sourceforge.plantuml.statediagram;

import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.UniqueSequence;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IEntityMutable;

public class StateDiagram extends AbstractEntityDiagram {

	@Override
	public IEntity getOrCreateClass(String code) {
		if (code.startsWith("[*]")) {
			throw new IllegalArgumentException();
		}
		if (isGroup(code)) {
			return getGroup(code);
		}
		final IEntity result = getOrCreateEntity(code, EntityType.STATE);
		return result;
	}

	public IEntity getStart() {
		final IEntityMutable g = getCurrentGroup();
		if (g == null) {
			return getOrCreateEntity("*start", EntityType.CIRCLE_START);
		}
		return getOrCreateEntity("*start*" + g.zgetGroupCode(), EntityType.CIRCLE_START);
	}

	public IEntity getEnd() {
		final IEntityMutable p = getCurrentGroup();
		if (p == null) {
			return getOrCreateEntity("*end", EntityType.CIRCLE_END);
		}
		return getOrCreateEntity("*end*" + p.zgetGroupCode(), EntityType.CIRCLE_END);
	}

	public IEntity getHistorical() {
		final IEntityMutable g = getCurrentGroup();
		if (g == null) {
			return getOrCreateEntity("*historical", EntityType.PSEUDO_STATE);
		}
		return getOrCreateEntity("*historical*" + g.zgetGroupCode(), EntityType.PSEUDO_STATE);
	}

	public IEntity getHistorical(String codeGroup) {
		final IEntityMutable g = getOrCreateGroup(codeGroup, codeGroup, null, GroupType.STATE, null);
		final IEntity result = getOrCreateEntity("*historical*" + g.zgetGroupCode(), EntityType.PSEUDO_STATE);
		endGroup();
		return result;
	}

	public boolean concurrentState() {
		final IEntityMutable cur = getCurrentGroup();
//		printlink("BEFORE");
		if (cur != null && cur.zgetGroupType() == GroupType.CONCURRENT_STATE) {
			super.endGroup();
		}
		final IEntityMutable conc1 = getOrCreateGroup("CONC" + UniqueSequence.getValue(), "", null, GroupType.CONCURRENT_STATE,
				getCurrentGroup());
		if (cur != null && cur.zgetGroupType() == GroupType.STATE) {
			cur.zmoveEntitiesTo(conc1);
			super.endGroup();
			final IEntityMutable conc2 = getOrCreateGroup("CONC" + UniqueSequence.getValue(), "", null,
					GroupType.CONCURRENT_STATE, getCurrentGroup());
		}
//		printlink("AFTER");
		return true;
	}

//	private void printlink(String comment) {
// Log.println("COMMENT="+comment);
//		for (Link l : getLinks()) {
// Log.println(l);
//		}
//	}

	@Override
	public void endGroup() {
		final IEntityMutable cur = getCurrentGroup();
		if (cur != null && cur.zgetGroupType() == GroupType.CONCURRENT_STATE) {
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

	// @Override
	// final protected List<String> getDotStrings() {
	// return Arrays.asList("nodesep=1.95;", "ranksep=1.8;", "edge
	// [fontsize=11,labelfontsize=11];",
	// "node [fontsize=11,height=.35,width=.55];");
	// }

}
