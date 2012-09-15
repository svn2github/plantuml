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
 * Revision $Revision: 7755 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UniqueSequence;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.BlockMember;
import net.sourceforge.plantuml.cucadiagram.BlockMemberImpl;
import net.sourceforge.plantuml.cucadiagram.Bodier;
import net.sourceforge.plantuml.cucadiagram.BodyEnhanced;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.EntityPosition;
import net.sourceforge.plantuml.cucadiagram.EntityUtils;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.Rankdir;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.TextBlockWidth;
import net.sourceforge.plantuml.graphic.TextBlockWidthVertical;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.svek.PackageStyle;

class EntityImpl implements ILeaf, IGroup {

	private final EntityFactory entityFactory;

	// Entity
	private final String code;
	private final List<Url> urls = new ArrayList<Url>();

	private final Bodier bodier;
	private final String uid = StringUtils.getUid("cl", UniqueSequence.getValue());
	private final List<CharSequence> display = new ArrayList<CharSequence>();

	private LeafType leafType;
	private Stereotype stereotype;
	private String generic;
	private IGroup parentContainer;

	private boolean top;

	private final List<String> mouseOver = new ArrayList<String>();

	// Group
	private String namespace;

	private GroupType groupType;

	private boolean autonom = true;
	private Rankdir rankdir = Rankdir.TOP_TO_BOTTOM;

	// Other

	private HtmlColor specificBackcolor;
	private boolean nearDecoration = false;
	private int xposition;
	private IEntityImage svekImage;

	// Back to Entity
	public final boolean isTop() {
		checkNotGroup();
		return top;
	}

	public final void setTop(boolean top) {
		checkNotGroup();
		this.top = top;
	}

	private EntityImpl(EntityFactory entityFactory, String code, Bodier bodier, IGroup parentContainer) {
		if (code == null) {
			throw new IllegalArgumentException();
		}
		this.entityFactory = entityFactory;
		this.bodier = bodier;
		this.code = code;
		this.parentContainer = parentContainer;
	}

	EntityImpl(EntityFactory entityFactory, String code, Bodier bodier, IGroup parentContainer, LeafType leafType) {
		this(entityFactory, code, bodier, parentContainer);
		this.leafType = leafType;
	}

	EntityImpl(EntityFactory entityFactory, String code, Bodier bodier, IGroup parentContainer, GroupType groupType,
			String namespace) {
		this(entityFactory, code, bodier, parentContainer);
		this.groupType = groupType;
		this.namespace = namespace;
	}

	public void setContainer(IGroup container) {
		checkNotGroup();
		if (container == null) {
			throw new IllegalArgumentException();
		}
		this.parentContainer = container;
	}

	public LeafType getEntityType() {
		checkNotGroup();
		return leafType;
	}

	public void muteToType(LeafType newType) {
		checkNotGroup();
		if (newType == null) {
			throw new IllegalArgumentException();
		}
		if (leafType != LeafType.ABSTRACT_CLASS && leafType != LeafType.CLASS && leafType != LeafType.ENUM
				&& leafType != LeafType.INTERFACE) {
			throw new IllegalArgumentException("type=" + leafType);
		}
		if (newType != LeafType.ABSTRACT_CLASS && newType != LeafType.CLASS && newType != LeafType.ENUM
				&& newType != LeafType.INTERFACE) {
			throw new IllegalArgumentException("newtype=" + newType);
		}
		this.leafType = newType;
	}

	public String getCode() {
		return code;
	}

	public List<? extends CharSequence> getDisplay() {
		return Collections.unmodifiableList(display);
	}

	public void setDisplay(List<? extends CharSequence> display) {
		this.display.clear();
		this.display.addAll(display);
	}

	public String getUid() {
		return uid;
	}

	public Stereotype getStereotype() {
		return stereotype;
	}

	public final void setStereotype(Stereotype stereotype) {
		this.stereotype = stereotype;
	}

	public final IGroup getParentContainer() {
		if (parentContainer == null) {
			throw new IllegalArgumentException();
		}
		return parentContainer;
	}

	@Override
	public String toString() {
		return code + " " + display + "(" + leafType + ") " + xposition + " " + getUid();
	}

	public HtmlColor getSpecificBackColor() {
		return specificBackcolor;
	}

	public void setSpecificBackcolor(HtmlColor color) {
		this.specificBackcolor = color;
	}

	public final List<Url> getUrls() {
		final List<Url> result = new ArrayList<Url>(urls);
		for (Member m : getFieldsToDisplay()) {
			final Url u = m.getUrl();
			if (u != null) {
				result.add(u);
			}
		}
		for (Member m : getMethodsToDisplay()) {
			final Url u = m.getUrl();
			if (u != null) {
				result.add(u);
			}
		}
		return Collections.unmodifiableList(result);
	}

	public final void addUrl(Url url) {
		if (url == null) {
			throw new UnsupportedOperationException();
		}
		this.urls.add(url);
	}

	public final boolean hasNearDecoration() {
		checkNotGroup();
		return nearDecoration;
	}

	public final void setNearDecoration(boolean nearDecoration) {
		checkNotGroup();
		this.nearDecoration = nearDecoration;
	}

	public int getXposition() {
		checkNotGroup();
		return xposition;
	}

	public void setXposition(int pos) {
		checkNotGroup();
		xposition = pos;
	}

	public final IEntityImage getSvekImage() {
		checkNotGroup();
		return svekImage;
	}

	public final void setSvekImage(IEntityImage svekImage) {
		checkNotGroup();
		this.svekImage = svekImage;
	}

	public final void setGeneric(String generic) {
		checkNotGroup();
		this.generic = generic;
	}

	public final String getGeneric() {
		checkNotGroup();
		return generic;
	}

	public BlockMember getBody(final PortionShower portionShower) {
		checkNotGroup();
		if (getEntityType() == LeafType.CLASS && bodier.isBodyEnhanced()) {
			return bodier.getBodyEnhanced();
		}
		return new BlockMember() {
			public TextBlockWidth asTextBlock(FontParam fontParam, ISkinParam skinParam) {
				if (getEntityType() == LeafType.ABSTRACT_CLASS || getEntityType() == LeafType.CLASS
						|| getEntityType() == LeafType.INTERFACE || getEntityType() == LeafType.ENUM) {

					final boolean showMethods = portionShower.showPortion(EntityPortion.METHOD, EntityImpl.this);
					final boolean showFields = portionShower.showPortion(EntityPortion.FIELD, EntityImpl.this);

					if (showFields && showMethods) {
						return new TextBlockWidthVertical(new BlockMemberImpl(getFieldsToDisplay()).asTextBlock(
								fontParam, skinParam), new BlockMemberImpl(getMethodsToDisplay()).asTextBlock(
								fontParam, skinParam));
					} else if (showFields) {
						return new BlockMemberImpl(getFieldsToDisplay()).asTextBlock(fontParam, skinParam);
					} else if (showMethods) {
						return new BlockMemberImpl(getMethodsToDisplay()).asTextBlock(fontParam, skinParam);
					}
					return null;
				}
				if (getEntityType() == LeafType.OBJECT) {
					return new BlockMemberImpl(getFieldsToDisplay()).asTextBlock(fontParam, skinParam);
				}
				throw new UnsupportedOperationException();
			}
		};
	}

	public BlockMember getMouseOver() {
		if (mouseOver.size() == 0) {
			return null;
		}
		return new BlockMember() {
			public TextBlockWidth asTextBlock(FontParam fontParam, ISkinParam skinParam) {
				return new BodyEnhanced(mouseOver, fontParam, skinParam);
			}
		};
	}

	public void mouseOver(String s) {
		mouseOver.add(s);
	}

	public List<Member> getFieldsToDisplay() {
		// checkNotGroup();
		return bodier.getFieldsToDisplay();
	}

	public List<Member> getMethodsToDisplay() {
		// checkNotGroup();
		return bodier.getMethodsToDisplay();
	}

	public void addFieldOrMethod(String s) {
		// checkNotGroup();
		bodier.addFieldOrMethod(s);
	}

	public EntityPosition getEntityPosition() {
		checkNotGroup();
		if (leafType != LeafType.STATE) {
			return EntityPosition.NORMAL;
		}
		final Stereotype stereotype = getStereotype();
		if (stereotype == null) {
			return EntityPosition.NORMAL;
		}
		final String label = stereotype.getLabel();
		if ("<<entrypoint>>".equalsIgnoreCase(label)) {
			return EntityPosition.ENTRY_POINT;
		}
		if ("<<exitpoint>>".equalsIgnoreCase(label)) {
			return EntityPosition.EXIT_POINT;
		}
		return EntityPosition.NORMAL;

	}

	// ----------

	private void checkGroup() {
		if (isGroup() == false) {
			throw new UnsupportedOperationException();
		}
	}

	private void checkNotGroup() {
		if (isGroup()) {
			throw new UnsupportedOperationException();
		}
	}

	public boolean containsLeafRecurse(ILeaf leaf) {
		if (leaf == null) {
			throw new IllegalArgumentException();
		}
		if (leaf.isGroup()) {
			throw new IllegalArgumentException();
		}
		checkGroup();
		if (EntityUtils.equals(leaf.getParentContainer(), this)) {
			return true;
		}
		for (IGroup child : zgetChildren()) {
			if (child.containsLeafRecurse(leaf)) {
				return true;
			}
		}
		return false;
	}

	public Collection<ILeaf> getLeafsDirect() {
		checkGroup();
		final List<ILeaf> result = new ArrayList<ILeaf>();
		for (ILeaf ent : entityFactory.getLeafs().values()) {
			if (ent.isGroup()) {
				throw new IllegalStateException();
			}
			if (EntityUtils.equals(ent.getParentContainer(), this)) {
				result.add(ent);
			}
		}
		return Collections.unmodifiableCollection(result);
	}

	public Collection<IGroup> zgetChildren() {
		checkGroup();
		final Collection<IGroup> result = new ArrayList<IGroup>();
		for (IGroup g : entityFactory.getGroups().values()) {
			if (g != this && g.getParentContainer() == this) {
				result.add(g);
			}
		}
		return Collections.unmodifiableCollection(result);
	}

	public void zmoveEntitiesTo(IGroup dest) {
		checkGroup();
		if (dest.isGroup() == false) {
			throw new UnsupportedOperationException();
		}
		for (ILeaf ent : getLeafsDirect()) {
			((EntityImpl) ent).parentContainer = dest;
		}
		for (IGroup g : dest.zgetChildren()) {
			// ((EntityImpl) g).parentContainer = dest;
			throw new IllegalStateException();
		}

		for (IGroup g : zgetChildren()) {
			if (g == dest) {
				continue;
			}
			((EntityImpl) g).parentContainer = dest;
		}

	}

	public int zsize() {
		checkGroup();
		return getLeafsDirect().size();
	}

	public GroupType zgetGroupType() {
		checkGroup();
		return groupType;
	}

	public String zgetNamespace() {
		checkGroup();
		return namespace;
	}

	public boolean zisAutonom() {
		checkGroup();
		return autonom;
	}

	public void zsetAutonom(boolean autonom) {
		this.autonom = autonom;

	}

	public Rankdir zgetRankdir() {
		checkGroup();
		return rankdir;
	}

	public void zsetRankdir(Rankdir rankdir) {
		checkGroup();
		this.rankdir = rankdir;
	}

	public PackageStyle zgetPackageStyle() {
		checkGroup();
		if (stereotype == null) {
			return null;
		}
		return stereotype.getPackageStyle();
	}

	public boolean isGroup() {
		if (groupType != null && leafType != null) {
			throw new IllegalStateException();
		}
		if (groupType != null) {
			return true;
		}
		if (leafType != null) {
			return false;
		}
		throw new IllegalStateException();
	}

	// ---- other

	public void overideImage(IEntityImage img, List<Url> url, LeafType leafType) {
		checkGroup();
		// this.svekImage = new InnerStateConcurrent(img);
		this.svekImage = img;
		this.urls.clear();
		this.urls.addAll(url);

		for (final Link link : new ArrayList<Link>(entityFactory.getLinks())) {
			if (EntityUtils.isPureInnerLink12(this, link)) {
				entityFactory.removeLink(link);
			}
		}

		entityFactory.removeGroup(this.getCode());
		for (ILeaf ent : new ArrayList<ILeaf>(entityFactory.getLeafs().values())) {
			if (this != ent && this == ent.getParentContainer()) {
				entityFactory.removeLeaf(ent.getCode());
			}
		}

		entityFactory.addLeaf(this);
		this.groupType = null;
		this.leafType = leafType;
	}

	void muteToGroup(String namespace, GroupType groupType, IGroup parentContainer) {
		checkNotGroup();
		if (parentContainer.isGroup() == false) {
			throw new IllegalArgumentException();
		}
		this.namespace = namespace;
		this.groupType = groupType;
		this.leafType = null;
		this.parentContainer = parentContainer;
	}
}
