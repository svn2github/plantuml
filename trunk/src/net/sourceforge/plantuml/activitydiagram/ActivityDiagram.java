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
 * Revision $Revision: 8515 $
 *
 */
package net.sourceforge.plantuml.activitydiagram;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.UniqueSequence;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;

public class ActivityDiagram extends CucaDiagram {

	private IEntity lastEntityConsulted;
	private IEntity lastEntityBrancheConsulted;
	private ConditionalContext currentContext;

	private String getAutoBranch() {
		return "#" + UniqueSequence.getValue();
	}

	public IEntity getOrCreate(String code, List<? extends CharSequence> display, LeafType type) {
		final IEntity result;
		if (leafExist(code)) {
			result = super.getOrCreateLeaf(code, type);
			if (result.getEntityType() != type) {
				// throw new IllegalArgumentException("Already known: " + code + " " + result.getType() + " " + type);
				return null;
			}
		} else {
			result = createLeaf(code, display, type);
		}
		updateLasts(result);
		return result;
	}

	public void startIf(String optionalCode) {
		final IEntity br = createLeaf(optionalCode == null ? getAutoBranch() : optionalCode, Arrays.asList(""), LeafType.BRANCH);
		currentContext = new ConditionalContext(currentContext, br, Direction.DOWN);
	}

	public void endif() {
		currentContext = currentContext.getParent();
	}

	public ILeaf getStart() {
		return (ILeaf) getOrCreate("start", StringUtils.getWithNewlines("start"), LeafType.CIRCLE_START);
	}

	public ILeaf getEnd() {
		return (ILeaf) getOrCreate("end", StringUtils.getWithNewlines("end"), LeafType.CIRCLE_END);
	}

	private void updateLasts(final IEntity result) {
		if (result.getEntityType() == LeafType.NOTE) {
			return;
		}
		this.lastEntityConsulted = result;
		if (result.getEntityType() == LeafType.BRANCH) {
			lastEntityBrancheConsulted = result;
		}
	}

	@Override
	public ILeaf createLeaf(String code, List<? extends CharSequence> display, LeafType type) {
		final ILeaf result = super.createLeaf(code, display, type);
		updateLasts(result);
		return result;
	}

	public IEntity createNote(String code, List<? extends CharSequence> display) {
		return super.createLeaf(code, display, LeafType.NOTE);
	}

	final protected List<String> getDotStrings() {
		return Arrays.asList("nodesep=.20;", "ranksep=0.4;", "edge [fontsize=11,labelfontsize=11];",
				"node [fontsize=11];");
	}

	public String getDescription() {
		return "(" + getLeafs().size() + " activities)";
	}

	public IEntity getLastEntityConsulted() {
		return lastEntityConsulted;
	}

	@Deprecated
	public IEntity getLastEntityBrancheConsulted() {
		return lastEntityBrancheConsulted;
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.ACTIVITY;
	}

	public final ConditionalContext getCurrentContext() {
		return currentContext;
	}

	public final void setLastEntityConsulted(IEntity lastEntityConsulted) {
		this.lastEntityConsulted = lastEntityConsulted;
	}

	public IEntity createInnerActivity() {
		// Log.println("createInnerActivity A");
		final String code = "##" + UniqueSequence.getValue();
		final IEntity g = getOrCreateGroup(code, StringUtils.getWithNewlines(code), null, GroupType.INNER_ACTIVITY, getCurrentGroup());
		// g.setRankdir(Rankdir.LEFT_TO_RIGHT);
		lastEntityConsulted = null;
		lastEntityBrancheConsulted = null;
		// Log.println("createInnerActivity B "+getCurrentGroup());
		return g;
	}

	public void concurrentActivity(String name) {
		// Log.println("concurrentActivity A name=" + name+" "+getCurrentGroup());
		if (getCurrentGroup().zgetGroupType() == GroupType.CONCURRENT_ACTIVITY) {
			// getCurrentGroup().setRankdir(Rankdir.LEFT_TO_RIGHT);
			endGroup();
			//Log.println("endgroup");
		}
		// Log.println("concurrentActivity A name=" + name+" "+getCurrentGroup());
		final String code = "##" + UniqueSequence.getValue();
		if (getCurrentGroup().zgetGroupType() != GroupType.INNER_ACTIVITY) {
			throw new IllegalStateException("type=" + getCurrentGroup().zgetGroupType());
		}
		getOrCreateGroup(code, StringUtils.getWithNewlines("code"), null, GroupType.CONCURRENT_ACTIVITY, getCurrentGroup());
		lastEntityConsulted = null;
		lastEntityBrancheConsulted = null;
	}

}
