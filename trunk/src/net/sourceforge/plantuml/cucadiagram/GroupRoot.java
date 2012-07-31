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
 * Revision $Revision: 8218 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.svek.PackageStyle;


public class GroupRoot implements IGroup {
	
	private final EntityFactory entityFactory;
	
	public GroupRoot(EntityFactory entityFactory) {
		this.entityFactory = entityFactory;
	}

	public void setContainer(IEntity container) {
		throw new UnsupportedOperationException();
		
	}

	public List<? extends CharSequence> getDisplay() {
		throw new UnsupportedOperationException();
		
	}

	public void setDisplay(List<? extends CharSequence> display) {
		throw new UnsupportedOperationException();
		
	}

	public LeafType getEntityType() {
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

	public void muteToType(LeafType newType) {
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

	public HtmlColor getSpecificBackColor() {
		throw new UnsupportedOperationException();
		
	}

	public void setSpecificBackcolor(HtmlColor specificBackcolor) {
		throw new UnsupportedOperationException();
		
	}

	public IGroup getParentContainer() {
		throw new UnsupportedOperationException();
	}

	public boolean containsLeafRecurse(ILeaf entity) {
		throw new UnsupportedOperationException();
		
	}

	public Collection<ILeaf> getLeafsDirect() {
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
		throw new UnsupportedOperationException();
		
	}

	public void zmoveEntitiesTo(IGroup dest) {
		throw new UnsupportedOperationException();
	}

	public int zsize() {
		throw new UnsupportedOperationException();
	}

	public GroupType zgetGroupType() {
		throw new UnsupportedOperationException();
	}

	public String zgetNamespace() {
		throw new UnsupportedOperationException();
		
	}

	public boolean zisAutonom() {
		throw new UnsupportedOperationException();
		
	}

	public void zsetAutonom(boolean autonom) {
		throw new UnsupportedOperationException();
		
	}

	public Rankdir zgetRankdir() {
		throw new UnsupportedOperationException();
		
	}

	public void zsetRankdir(Rankdir rankdir) {
		throw new UnsupportedOperationException();
		
	}

	public PackageStyle zgetPackageStyle() {
		throw new UnsupportedOperationException();
		
	}

	public boolean isGroup() {
		return true;
	}

	public void muteToGroup99(IEntity newGroup) {
		throw new UnsupportedOperationException();
		
	}

	public void overideImage42(IEntityImage img, List<Url> url) {
		throw new UnsupportedOperationException();
		
	}

	public boolean getContainerOrEquivalentThenEquals(IEntity gr) {
		throw new UnsupportedOperationException();
		
	}

	public EntityPosition getEntityPosition() {
		throw new UnsupportedOperationException();
	}


}
