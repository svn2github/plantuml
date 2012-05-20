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
 * Revision $Revision: 7738 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UniqueSequence;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.svek.PackageStyle;

public class GroupImpl implements Group {

	private final Collection<IEntity> allEntities;
	private final Collection<? extends Group> allGroups;
	private final String code;
	private final String display;
	private final String namespace;

	private HtmlColor backColor;
	private Group parent;

	private final GroupType type;

	private boolean autonom = true;
	private Rankdir rankdir = Rankdir.TOP_TO_BOTTOM;

	private final int cpt = UniqueSequence.getValue();

	public GroupImpl(String code, String display, String namespace, GroupType type, Group parent,
			Collection<IEntity> allEntities, Collection<? extends Group> allGroups) {
		if (type != GroupType.ROOT) {
			if (type == null) {
				throw new IllegalArgumentException();
			}
			if (code == null || code.length() == 0) {
				throw new IllegalArgumentException();
			}
		}
		this.namespace = namespace;
		this.type = type;
		this.parent = parent;
		this.code = code;
		this.display = display;
		this.allEntities = allEntities;
		this.allGroups = allGroups;
	}

	@Override
	public String toString() {
		return "G[code=" + code + "] autonom=" + zisAutonom();
	}

	public boolean zcontains(IEntity entity) {
		if (entity == null) {
			throw new IllegalArgumentException();
		}
		final Group toTest = type == GroupType.ROOT ? null : this;
		if (EntityUtils.equals(entity.getContainer(), toTest)) {
			return true;
		}
		for (Group child : zgetChildren()) {
			if (child.zcontains(entity)) {
				return true;
			}
		}
		return false;
	}

	public Collection<IEntity> zentities() {
		final List<IEntity> result = new ArrayList<IEntity>();
		final Group toTest = type == GroupType.ROOT ? null : this;
		for (IEntity ent : allEntities) {
			if (((IEntityMutable) ent).isGroup()) {
				continue;
			}
			if (EntityUtils.equals(ent.getContainer(), toTest)) {
				result.add(ent);
			}
		}
		return Collections.unmodifiableCollection(result);
	}

	public int zsize() {
		return zentities().size();
	}

	public String zgetGroupCode() {
		return code;
	}

	public String zgetUid() {
		return StringUtils.getUid(zgetUid1(), zgetUid2());
	}

	public String zgetUid1() {
		return "cluster";
	}

	public int zgetUid2() {
		return cpt;
	}

	public final HtmlColor zgetBackColor() {
		return backColor;
	}

	public final void zsetBackColor(HtmlColor backColor) {
		this.backColor = backColor;
	}

	public final Group zgetParent() {
		return parent;
	}

	public GroupType zgetGroupType() {
		return type;
	}

	public String zgetDisplay() {
		return display;
	}

	public void zmoveEntitiesTo(IEntityMutable dest) {
		for (IEntity ent : zentities()) {
			ent.setContainer(dest);
		}
		for (Group g : dest.zgetChildren()) {
			((GroupImpl) g).parent = (GroupImpl) dest;
		}

	}

	public String zgetNamespace() {
		return namespace;
	}

	public final Collection<Group> zgetChildren() {
		final Collection<Group> result = new ArrayList<Group>();
		for (Group g : allGroups) {
			if (g.zgetUid2() != this.zgetUid2() && g.zgetParent() != null
					&& g.zgetParent().zgetUid2() == this.zgetUid2()) {
				result.add(g);
			}
		}
		return Collections.unmodifiableCollection(result);
	}

	public final boolean zisAutonom() {
		return autonom;
	}

	public final void zsetAutonom(boolean autonom) {
		this.autonom = autonom;
	}

	public final Rankdir zgetRankdir() {
		return rankdir;
	}

	public final void zsetRankdir(Rankdir rankdir) {
		this.rankdir = rankdir;
	}

	private Stereotype stereotype;

	public final void zsetStereotype(Stereotype stereotype) {
		this.stereotype = stereotype;
	}

	public final Stereotype zgetStereotype() {
		return stereotype;
	}

	public PackageStyle zgetPackageStyle() {
		if (stereotype == null) {
			return null;
		}
		return stereotype.getPackageStyle();
	}

}
