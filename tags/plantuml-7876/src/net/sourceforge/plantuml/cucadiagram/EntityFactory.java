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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UniqueSequence;
import net.sourceforge.plantuml.skin.VisibilityModifier;

public class EntityFactory implements IEntityFactory {

	private final LinkedHashMap<String, IEntity> entities = new LinkedHashMap<String, IEntity>();
	private final List<Link> links = new ArrayList<Link>();
	private final Map<String, IEntityMutable> groups = new LinkedHashMap<String, IEntityMutable>();

	public IEntity createEntity(String code, String display, EntityType type, IEntityMutable container,
			Set<VisibilityModifier> hides) {
		return createEntity("cl", UniqueSequence.getValue(), code, display, type, container, hides);
	}

	public IEntity createEntity(String uid1, int uid2, String code, String display, EntityType type,
			IEntityMutable container, Set<VisibilityModifier> hides) {
		return create(uid1, uid2, code, StringUtils.getWithNewlines(display), type, container, hides);
	}

	public IEntity create(String uid1, int uid2, String code, List<? extends CharSequence> display, EntityType type,
			IEntityMutable container, Set<VisibilityModifier> hides) {
		final Bodier bodier = new Bodier(type, hides);
		return new EntityMutable(EntityImpl.createInternal(uid1, uid2, code, display, type, container, bodier), this,
				bodier);
	}

	final LinkedHashMap<String, IEntity> getEntities() {
		return entities;
	}

	final List<Link> getLinks() {
		return links;
	}

	final Map<String, IEntityMutable> getGroups() {
		return groups;
	}

	// No used for SVEK
	public void overideGroup(EntityMutable entityMutable, EntityMutable proxy) {
		if (entities.containsKey(proxy.getCode())) {
			throw new IllegalArgumentException();
		}
		if (entities.containsValue(proxy)) {
			throw new IllegalArgumentException();
		}
		// for (final ListIterator<Link> it = links.listIterator(); it.hasNext();) {
		// final Link link = it.next();
		// if (link.insideGroup(entityMutable.asGroup())) {
		// it.remove();
		// }
		// }
		for (final ListIterator<Link> it = links.listIterator(); it.hasNext();) {
			final Link link = it.next();
			final Link newLink = link.mute(entityMutable, proxy);
			if (newLink == null) {
				it.remove();
			} else if (newLink != link) {
				it.set(newLink);
			}
		}
		groups.remove(entityMutable.zgetGroupCode());

		for (final Iterator<IEntity> it = entities.values().iterator(); it.hasNext();) {
			final IEntity ent = it.next();
			if (EntityUtils.equals(EntityUtils.getContainerOrEquivalent(ent), entityMutable)) {
				it.remove();
			}
		}
		entities.put(proxy.getCode(), proxy);
	}

	// public void overideGroup2(EntityMutable entityMutable) {
	// for (final ListIterator<Link> it = links.listIterator(); it.hasNext();) {
	// final Link link = it.next();
	// final Link newLink = link.mute2(entityMutable);
	// if (newLink == null) {
	// it.remove();
	// }
	// }
	// groups.remove(entityMutable.zgetGroupCode());
	//
	// for (final Iterator<IEntity> it = entities.values().iterator(); it.hasNext();) {
	// final IEntity ent = it.next();
	// if (ent!=entityMutable && EntityUtils.equals(EntityUtils.getContainerOrEquivalent(ent), entityMutable)) {
	// it.remove();
	// }
	// }
	// entities.put(entityMutable.getCode(), entityMutable);
	// }

	public void overideImage42(EntityMutable entityMutable) {
		for (final ListIterator<Link> it = links.listIterator(); it.hasNext();) {
			final Link link = it.next();
			final Link newLink = link.mute2(entityMutable);
			if (newLink == null) {
				it.remove();
			}
		}
		groups.remove(entityMutable.zgetGroupCode());

		for (final Iterator<IEntity> it = entities.values().iterator(); it.hasNext();) {
			final IEntity ent = it.next();
			if (ent != entityMutable && EntityUtils.equals(EntityUtils.getContainerOrEquivalent(ent), entityMutable)) {
				it.remove();
			}
		}
		entities.put(entityMutable.getCode(), entityMutable);

	}

	public final IEntityMutable getOrCreateGroupInternal(String code, String display, String namespace, GroupType type,
			IEntityMutable parent, final Set<VisibilityModifier> hides, boolean isSvek) {
		EntityMutable gg = (EntityMutable) getGroups().get(code);
		if (gg == null) {
			gg = isSvek ? createGroupSvek(code, display, namespace, type, parent, hides) : createGroup(code, display,
					namespace, type, parent, hides);
			getGroups().put(code, gg);
		}
		return gg;
	}

	private EntityMutable createGroup(String code, String display, String namespace, GroupType type,
			IEntityMutable parent, final Set<VisibilityModifier> hides) {

		final GroupImpl g = new GroupImpl(code, display, namespace, type, parent, getEntities().values(), getGroups()
				.values());
		if (getEntities().containsKey(code)) {
			final EntityMutable entityGroup = (EntityMutable) getEntities().get(code);
			final EntityMutable gg = new EntityMutable(g, this);
			entityGroup.muteToGroup99(g);
			gg.zsetEntityCluster(entityGroup.getUid());
			entities.put(code, gg);
			gg.overidesFieldsToDisplay(entityGroup);
			entityGroup.invalidateNow();
			return gg;
		}
		final EntityMutable gg = new EntityMutable(g, this);
		// final EntityMutable entityGroup = (EntityMutable) createEntity("$$" + code, code, EntityType.GROUP, gg,
		// hides);
		final EntityMutable entityGroup = (EntityMutable) createEntity(code, code, null, gg, hides);
		entityGroup.muteToGroup99(g);
		gg.zsetEntityCluster(entityGroup.getUid());
		gg.overidesFieldsToDisplay(entityGroup);
		return gg;
	}

	private EntityMutable createGroupSvek(String code, String display, String namespace, GroupType type,
			IEntityMutable parent, final Set<VisibilityModifier> hides) {

		final GroupImpl g = new GroupImpl(code, display, namespace, type, parent, getEntities().values(), getGroups()
				.values());
		if (getEntities().containsKey(code)) {
			final EntityMutable entityGroup = (EntityMutable) getEntities().get(code);
			//final EntityMutable gg = new EntityMutable(g, this);
			entityGroup.muteToGroup99(g);
			// gg.zsetEntityCluster(entityGroup.getUid());
			entities.remove(code);
			// entities.put(code, gg);
			// gg.overidesFieldsToDisplay(entityGroup);
			entityGroup.setDisplay2(display);
			entityGroup.invalidateNow();
			return entityGroup;
		}
		// final EntityMutable gg = new EntityMutable(g, this);
		// final EntityMutable entityGroup = (EntityMutable) createEntity("$$" + code, code, EntityType.GROUP, gg,
		// hides);
		final EntityMutable entityGroup = (EntityMutable) createEntity(code, code, null, parent, hides);
		entityGroup.muteToGroup99(g);
		//gg.zsetEntityCluster(entityGroup.getUid());
		//gg.overidesFieldsToDisplay(entityGroup);
		return entityGroup;
	}

}
