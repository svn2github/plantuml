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
 * Revision $Revision: 4749 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.svek.PackageStyle;

public class EntityMutable implements IEntityMutable {

	private IEntity entity;
	private Bodier bodier;
	private Group group;
	private final EntityFactory entityFactory;

	private boolean isValid = true;

	public void invalidateNow() {
		isValid = false;
	}

	private void checkValid() {
		if (isValid == false) {
			// throw new IllegalStateException();
		}
	}

	@Override
	public String toString() {
		if (group == null) {
			return super.toString() + " " + entity;
		}
		return "GROUP**" + super.toString() + " " + entity + " " + group;
	}

	public boolean isGroup() {
		checkValid();
		return group != null;
	}

	public CrossingType getCrossingType(Link link) {
		checkValid();
		if (group == null) {
			throw new IllegalStateException();
		}
		if (link.getEntity1().equals(this) && link.getEntity2().equals(this)) {
			return CrossingType.SELF;
		}
		if (link.getEntity1().equals(this)) {
			if (zcontains(link.getEntity2())) {
				return CrossingType.TOUCH_INSIDE;
			}
			return CrossingType.TOUCH_OUTSIDE;
		}
		if (link.getEntity2().equals(this)) {
			if (zcontains(link.getEntity1())) {
				return CrossingType.TOUCH_INSIDE;
			}
			return CrossingType.TOUCH_OUTSIDE;
		}
		final boolean contains1 = zcontains(link.getEntity1());
		final boolean contains2 = zcontains(link.getEntity2());
		if (contains1 && contains2) {
			return CrossingType.INSIDE;
		}
		if (contains1 == false && contains2 == false) {
			return CrossingType.OUTSIDE;
		}
		return CrossingType.CUT;
	}

	public EntityMutable(EntityImpl entity, EntityFactory entityFactory, Bodier bodier) {
		this.entity = entity;
		this.entityFactory = entityFactory;
		this.bodier = bodier;
	}

	public EntityMutable(GroupImpl newGroup, EntityFactory entityFactory) {
		this.entity = null;
		this.group = newGroup;
		this.entityFactory = entityFactory;
	}

	public void overideImage42(IEntityImage img, List<Url> url) {
		entityFactory.overideImage42(this);
		this.entity = new MyEntity42(getContainer(), img, getStereotype(), getCode(), url);
		this.group = null;
	}

	static class MyEntity42 extends UnsupportedEntity {

		private final Group container;
		private final IEntityImage img;
		private final Stereotype stereotype;
		private final String code;
		private final List<Url> url;

		public MyEntity42(Group container, IEntityImage img, Stereotype stereotype, String code, List<Url> url) {
			this.container = container;
			this.img = img;
			this.code = code;
			this.stereotype = stereotype;
			this.url = url;
		}
		
//		@Override
//		public String getUid() {
//			// Only for CMAPX
//			return code;
//		}


		@Override
		public String getCode() {
			return code;
		}

		@Override
		public Stereotype getStereotype() {
			return stereotype;
		}

		@Override
		public HtmlColor getSpecificBackColor() {
			return null;
		}

		@Override
		public List<Url> getUrls() {
			return url;
		}

		@Override
		public EntityType getEntityType() {
			return null;
		}

		@Override
		public boolean isTop() {
			return false;
		}

		@Override
		public IEntityImage getSvekImage() {
			return img;
		}

		@Override
		public Group getContainer() {
			return container;
		}

	}

	public void muteToGroup99(Group newGroup) {
		checkValid();
		if (group != null) {
			throw new IllegalStateException();
		}
		this.group = newGroup;
	}

	private String zuid;

	public final void zsetEntityCluster(String uid) {
		checkValid();
		this.zuid = uid;

	}

	public void overidesFieldsToDisplay(EntityMutable blocDisplayProxy) {
		checkValid();
		bodier = blocDisplayProxy.bodier;
	}

	// --------

	public HtmlColor getSpecificBackColor() {
		checkValid();
		return entity.getSpecificBackColor();
	}

	public void setSpecificBackcolor(HtmlColor specificBackcolor) {
		checkValid();
		entity.setSpecificBackcolor(specificBackcolor);
	}

	// public int compareTo(IEntity arg0) {
	// return entity.compareTo(arg0);
	// }

	public Group getContainer() {
		checkValid();
		if (group != null) {
			if (this == group.zgetParent()) {
				throw new IllegalStateException();
			}
			return group.zgetParent();
		}
		if (this == entity.getContainer()) {
			throw new IllegalStateException();
		}
		return entity.getContainer();
	}

	public List<? extends CharSequence> getDisplay2() {
		checkValid();
		// if (entity == null) {
		if (group != null) {
			// return Arrays.asList(zgetGroupCode());
			return Arrays.asList(zgetDisplay());
		}
		return entity.getDisplay2();
	}

	public EntityType getEntityType() {
		checkValid();
		if (group != null) {
			return null;
		}
		return entity.getEntityType();
	}

	public String getUid() {
		checkValid();
		if (zuid != null) {
			return zuid;
		}
		return entity.getUid();
	}

	public List<Url> getUrls() {
		checkValid();
		if (entity == null) {
			return null;
		}
		return entity.getUrls();
	}

	public Stereotype getStereotype() {
		checkValid();
		if (entity == null) {
			return zgetStereotype();
		}
		return entity.getStereotype();
	}

	public void setStereotype(Stereotype stereotype) {
		checkValid();
		entity.setStereotype(stereotype);
	}

	// public List<Member> getFieldsToDisplay() {
	// return entity.getFieldsToDisplay();
	// }
	//
	// public List<Member> getMethodsToDisplay() {
	// return entity.getMethodsToDisplay();
	// }

	public BlockMember getBody(PortionShower portionShower) {
		checkValid();
		return entity.getBody(portionShower);
	}

	public String getCode() {
		checkValid();
		if (entity == null) {
			return zgetGroupCode();
		}
		return entity.getCode();
	}

	public boolean isTop() {
		checkValid();
		return entity.isTop();
	}

	public void setTop(boolean top) {
		checkValid();
		entity.setTop(top);
	}

	public boolean hasNearDecoration() {
		checkValid();
		if (entity == null) {
			return false;
		}
		return entity.hasNearDecoration();
	}

	public void setNearDecoration(boolean nearDecoration) {
		checkValid();
		entity.setNearDecoration(nearDecoration);
	}

	public int getXposition() {
		checkValid();
		return entity.getXposition();
	}

	public void setXposition(int pos) {
		checkValid();
		entity.setXposition(pos);
	}

	public IEntityImage getSvekImage() {
		checkValid();
		return entity.getSvekImage();
	}

	public String getGeneric() {
		checkValid();
		return entity.getGeneric();
	}

	public BlockMember getMouseOver() {
		checkValid();
		return entity.getMouseOver();
	}

	public void muteToType(EntityType newType) {
		checkValid();
		entity.muteToType(newType);
	}

	public void setGeneric(String generic) {
		checkValid();
		entity.setGeneric(generic);
	}

	// public void addFieldOrMethod(String s) {
	// entity.addFieldOrMethod(s);
	// }

	public void mouseOver(String s) {
		checkValid();
		entity.mouseOver(s);
	}

	public void addUrl(Url url) {
		checkValid();
		entity.addUrl(url);
	}

	public void setSvekImage(IEntityImage svekImage) {
		checkValid();
		entity.setSvekImage(svekImage);
	}

	public void setDisplay2(String display) {
		checkValid();
		entity.setDisplay2(display);
	}

	public void setContainer(IEntityMutable entityPackage) {
		checkValid();
		entity.setContainer(entityPackage);
	}

	// --------

	public boolean zcontains(IEntity entity) {
		checkValid();
		return group.zcontains(entity);
	}

	public Collection<IEntity> zentities() {
		checkValid();
		if (group == null) {
			return Collections.emptyList();
		}
		return group.zentities();
	}

	public int zsize() {
		checkValid();
		return group.zsize();
	}

	public String zgetGroupCode() {
		checkValid();
		return group.zgetGroupCode();
	}

	public String zgetUid() {
		checkValid();
		return group.zgetUid();
	}

	public String zgetUid1() {
		checkValid();
		return group.zgetUid1();
	}

	public int zgetUid2() {
		checkValid();
		if (group == null) {
			// if (isGroup()) {
			// return ((Group) entity).zgetUid2();
			// }
			if (entity.getContainer() == null) {
				throw new IllegalStateException();
			}
			return entity.getContainer().zgetUid2();
		}
		return group.zgetUid2();
	}

	public HtmlColor zgetBackColor() {
		checkValid();
		if (group == null) {
			return getSpecificBackColor();
		}
		return group.zgetBackColor();
	}

	public void zsetBackColor(HtmlColor backColor) {
		checkValid();
		group.zsetBackColor(backColor);
	}

	public Group zgetParent() {
		checkValid();
		if (group == null) {
			return null;
		}
		return group.zgetParent();
	}

	public GroupType zgetGroupType() {
		checkValid();
		return group.zgetGroupType();
	}

	public String zgetDisplay() {
		checkValid();
		return group.zgetDisplay();
	}

	public void zmoveEntitiesTo(IEntityMutable dest) {
		checkValid();
		group.zmoveEntitiesTo(dest);
	}

	public String zgetNamespace() {
		checkValid();
		return group.zgetNamespace();
	}

	public Collection<? extends Group> zgetChildren() {
		checkValid();
		return group.zgetChildren();
	}

	public boolean zisAutonom() {
		checkValid();
		return group.zisAutonom();
	}

	public void zsetAutonom(boolean autonom) {
		checkValid();
		group.zsetAutonom(autonom);
	}

	public Rankdir zgetRankdir() {
		checkValid();
		return group.zgetRankdir();
	}

	public void zsetRankdir(Rankdir rankdir) {
		checkValid();
		group.zsetRankdir(rankdir);
	}

	public void zsetStereotype(Stereotype stereotype) {
		checkValid();
		group.zsetStereotype(stereotype);
	}

	public PackageStyle zgetPackageStyle() {
		checkValid();
		return group.zgetPackageStyle();
	}

	public Stereotype zgetStereotype() {
		checkValid();
		if (group == null) {
			return getStereotype();
		}
		return group.zgetStereotype();
	}

	public List<Member> getFieldsToDisplay() {
		checkValid();
		return bodier.getFieldsToDisplay();
	}

	public List<Member> getMethodsToDisplay() {
		checkValid();
		return bodier.getMethodsToDisplay();
	}

	public void addFieldOrMethod(String s) {
		checkValid();
		bodier.addFieldOrMethod(s);
	}
}

class UnsupportedEntity implements IEntity {

	public HtmlColor getSpecificBackColor() {
		throw new UnsupportedOperationException();
	}

	public void setSpecificBackcolor(HtmlColor specificBackcolor) {
		throw new UnsupportedOperationException();

	}

	public Group getContainer() {
		throw new UnsupportedOperationException();
	}

	public void setContainer(IEntityMutable container) {
		throw new UnsupportedOperationException();

	}

	public List<? extends CharSequence> getDisplay2() {
		throw new UnsupportedOperationException();
	}

	public EntityType getEntityType() {
		throw new UnsupportedOperationException();
	}

	public String getUid() {
		throw new UnsupportedOperationException();
	}

	public List<Url> getUrls() {
		throw new UnsupportedOperationException();
	}

	public Stereotype getStereotype() {
		throw new UnsupportedOperationException();
	}

	public void setStereotype(Stereotype stereotype) {
		throw new UnsupportedOperationException();

	}

	public List<Member> getFieldsToDisplay() {
		throw new UnsupportedOperationException();
	}

	public List<Member> getMethodsToDisplay() {
		throw new UnsupportedOperationException();
	}

	public BlockMember getBody(PortionShower portionShower) {
		throw new UnsupportedOperationException();
	}

	public String getCode() {
		throw new UnsupportedOperationException();
	}

	public boolean isTop() {
		throw new UnsupportedOperationException();
	}

	public void setTop(boolean top) {
		throw new UnsupportedOperationException();

	}

	public boolean hasNearDecoration() {
		throw new UnsupportedOperationException();
	}

	public void setNearDecoration(boolean nearDecoration) {
		throw new UnsupportedOperationException();
	}

	public int getXposition() {
		throw new UnsupportedOperationException();
	}

	public void setXposition(int pos) {
		throw new UnsupportedOperationException();

	}

	public IEntityImage getSvekImage() {
		throw new UnsupportedOperationException();
	}

	public String getGeneric() {
		throw new UnsupportedOperationException();
	}

	public BlockMember getMouseOver() {
		throw new UnsupportedOperationException();
	}

	public void muteToType(EntityType newType) {
		throw new UnsupportedOperationException();

	}

	public void setGeneric(String generic) {
		throw new UnsupportedOperationException();

	}

	public void addFieldOrMethod(String s) {
		throw new UnsupportedOperationException();

	}

	public void mouseOver(String s) {
		throw new UnsupportedOperationException();

	}

	public void addUrl(Url url) {
		throw new UnsupportedOperationException();

	}

	public void setSvekImage(IEntityImage svekImage) {
		throw new UnsupportedOperationException();

	}

	public void setDisplay2(String display) {
		throw new UnsupportedOperationException();

	}

	public void cleanSubImage() {
		throw new UnsupportedOperationException();

	}

}