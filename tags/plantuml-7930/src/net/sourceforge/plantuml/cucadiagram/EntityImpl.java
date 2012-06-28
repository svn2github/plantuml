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
package net.sourceforge.plantuml.cucadiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.TextBlockWidth;
import net.sourceforge.plantuml.graphic.TextBlockWidthVertical;
import net.sourceforge.plantuml.svek.IEntityImage;

public class EntityImpl implements IEntity {

	private final String code;
	private List<? extends CharSequence> display2;

	private final String uid;
	private EntityType type;

	private Stereotype stereotype;
	private String generic;

	private IEntityMutable container;

	private final List<Url> urls = new ArrayList<Url>();

	private final Bodier bodier;

	private boolean top;

	public final boolean isTop() {
		return top;
	}

	public final void setTop(boolean top) {
		this.top = top;
	}

	static EntityImpl createInternal(String uid1, int uid2, String code, List<? extends CharSequence> display,
			EntityType type, IEntityMutable entityPackage, Bodier bodier) {
		return new EntityImpl(uid1, uid2, code, display, type, entityPackage, bodier);
	}

	private EntityImpl(String uid1, int uid2, String code, List<? extends CharSequence> display, EntityType type,
			IEntityMutable container, Bodier bodier) {
		if (code == null || code.length() == 0) {
			throw new IllegalArgumentException();
		}
		if (display == null) {
			throw new IllegalArgumentException();
		}
		// this.bodier = new Bodier(type, hides);
		this.bodier = bodier;
		this.uid = StringUtils.getUid(uid1, uid2);
		this.type = type;
		this.code = code;
		this.display2 = display;
		this.container = container;
	}

	public void setContainer(IEntityMutable container) {
		if (container == null) {
			throw new IllegalArgumentException();
		}
		this.container = container;
	}

	public EntityType getEntityType() {
		return type;
	}

	public void muteToType(EntityType newType) {
		if (type != EntityType.ABSTRACT_CLASS && type != EntityType.CLASS && type != EntityType.ENUM
				&& type != EntityType.INTERFACE) {
			throw new IllegalArgumentException("type=" + type);
		}
		if (newType != EntityType.ABSTRACT_CLASS && newType != EntityType.CLASS && newType != EntityType.ENUM
				&& newType != EntityType.INTERFACE) {
			throw new IllegalArgumentException("newtype=" + newType);
		}
		this.type = newType;
	}

	public String getCode() {
		return code;
	}

	public List<? extends CharSequence> getDisplay2() {
		return display2;
	}

	public void setDisplay2(String display) {
		this.display2 = StringUtils.getWithNewlines(display);
	}

	public void setDisplay2(List<? extends CharSequence> display) {
		this.display2 = display;
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

	public final Group getContainer() {
		return container;
	}

	@Override
	public String toString() {
		// if (type == EntityType.GROUP) {
		// return "GROUP " + display2 + "(" + getEntityType() + ")" + this.container;
		// }
		return display2 + "(" + getEntityType() + ") " + xposition + " " + getUid();
	}

	private HtmlColor specificBackcolor;

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

	private boolean nearDecoration = false;

	public final boolean hasNearDecoration() {
		return nearDecoration;
	}

	public final void setNearDecoration(boolean nearDecoration) {
		this.nearDecoration = nearDecoration;
	}

	// public int compareTo(IEntity other) {
	// return getUid().compareTo(other.getUid());
	// }

	private int xposition;

	public int getXposition() {
		return xposition;
	}

	public void setXposition(int pos) {
		xposition = pos;
	}

	private IEntityImage svekImage;

	public final IEntityImage getSvekImage() {
		return svekImage;
	}

	public final void setSvekImage(IEntityImage svekImage) {
		this.svekImage = svekImage;
	}

	public final void setGeneric(String generic) {
		this.generic = generic;
	}

	public final String getGeneric() {
		return generic;
	}

	public BlockMember getBody(final PortionShower portionShower) {
		if (getEntityType() == EntityType.CLASS && bodier.isBodyEnhanced()) {
			return bodier.getBodyEnhanced();
		}
		return new BlockMember() {
			public TextBlockWidth asTextBlock(FontParam fontParam, ISkinParam skinParam) {
				if (getEntityType() == EntityType.ABSTRACT_CLASS || getEntityType() == EntityType.CLASS
						|| getEntityType() == EntityType.INTERFACE || getEntityType() == EntityType.ENUM) {

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
				if (getEntityType() == EntityType.OBJECT) {
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

	private final List<String> mouseOver = new ArrayList<String>();

	public void mouseOver(String s) {
		mouseOver.add(s);
	}

	public List<Member> getFieldsToDisplay() {
		return bodier.getFieldsToDisplay();
	}

	public List<Member> getMethodsToDisplay() {
		return bodier.getMethodsToDisplay();
	}

	public void addFieldOrMethod(String s) {
		bodier.addFieldOrMethod(s);
	}
}
