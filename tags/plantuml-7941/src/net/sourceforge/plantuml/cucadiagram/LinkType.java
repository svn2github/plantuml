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
 * Revision $Revision: 4604 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import net.sourceforge.plantuml.ugraphic.UStroke;

public class LinkType {

	private final LinkHat hat1;
	private final LinkDecor decor1;
	private final LinkStyle style;
	private final LinkDecor decor2;
	private final LinkHat hat2;
	private final LinkMiddleDecor middleDecor;

	public LinkType(LinkDecor decor1, LinkDecor decor2) {
		this(LinkHat.NONE, decor1, decor2, LinkHat.NONE);
	}

	public LinkType(LinkHat hat1, LinkDecor decor1, LinkDecor decor2, LinkHat hat2) {
		this(hat1, decor1, LinkStyle.NORMAL, LinkMiddleDecor.NONE, decor2, hat2);
	}

	// public boolean contains(LinkDecor decors) {
	// return decor1 == decors || decor2 == decors;
	// }

	@Override
	public String toString() {
		return decor1 + "-" + style + "-" + decor2;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		final LinkType other = (LinkType) obj;
		return this.decor1 == other.decor1 && this.decor2 == other.decor2 && this.style == other.style;
	}

	private LinkType(LinkHat hat1, LinkDecor decor1, LinkStyle style, LinkMiddleDecor middleDecor, LinkDecor decor2,
			LinkHat hat2) {
		this.decor1 = decor1;
		this.style = style;
		this.decor2 = decor2;
		this.middleDecor = middleDecor;
		this.hat1 = hat1;
		this.hat2 = hat2;
	}

	public boolean isDashed() {
		return style == LinkStyle.DASHED;
	}

	public boolean isDotted() {
		return style == LinkStyle.DOTTED;
	}

	public boolean isBold() {
		return style == LinkStyle.BOLD;
	}

	public boolean isInvisible() {
		return style == LinkStyle.INVISIBLE;
	}

	public LinkType getDashed() {
		return new LinkType(hat1, decor1, LinkStyle.DASHED, middleDecor, decor2, hat2);
	}

	public LinkType getDotted() {
		return new LinkType(hat1, decor1, LinkStyle.DOTTED, middleDecor, decor2, hat2);
	}

	public LinkType getBold() {
		return new LinkType(hat1, decor1, LinkStyle.BOLD, middleDecor, decor2, hat2);
	}

	public LinkType getInterfaceProvider() {
		return new LinkType(hat1, decor1, LinkStyle.__toremove_INTERFACE_PROVIDER, middleDecor, decor2, hat2);
	}

	public LinkType getInterfaceUser() {
		return new LinkType(hat1, decor1, LinkStyle.__toremove_INTERFACE_USER, middleDecor, decor2, hat2);
	}

	public LinkType getInversed() {
		return new LinkType(hat2, decor2, style, middleDecor, decor1, hat1);
	}

	public LinkType withMiddleCircle() {
		return new LinkType(hat1, decor1, style, LinkMiddleDecor.CIRCLE, decor2, hat2);
	}

	public LinkType withMiddleCircleCircled() {
		return new LinkType(hat1, decor1, style, LinkMiddleDecor.CIRCLE_CIRCLED, decor2, hat2);
	}

	public LinkType withMiddleCircleCircled1() {
		return new LinkType(hat1, decor1, style, LinkMiddleDecor.CIRCLE_CIRCLED1, decor2, hat2);
	}

	public LinkType withMiddleCircleCircled2() {
		return new LinkType(hat1, decor1, style, LinkMiddleDecor.CIRCLE_CIRCLED2, decor2, hat2);
	}

	public LinkType getInvisible() {
		return new LinkType(hat1, decor1, LinkStyle.INVISIBLE, middleDecor, decor2, hat2);
	}

	public String getSpecificDecorationSvek() {
		final StringBuilder sb = new StringBuilder();

		final boolean isEmpty1 = decor1 == LinkDecor.NONE && hat1 == LinkHat.NONE;
		final boolean isEmpty2 = decor2 == LinkDecor.NONE && hat2 == LinkHat.NONE;

		if (isEmpty1 && isEmpty2) {
			sb.append("arrowtail=none");
			sb.append(",arrowhead=none");
		} else if (isEmpty1 == false && isEmpty2 == false) {
			sb.append("dir=both,");
			sb.append("arrowtail=empty");
			sb.append(",arrowhead=empty");
		} else if (isEmpty1 && isEmpty2 == false) {
			sb.append("arrowtail=empty");
			sb.append(",arrowhead=none");
			sb.append(",dir=back");
		} else if (isEmpty1 == false && isEmpty2) {
			sb.append("arrowtail=none");
			sb.append(",arrowhead=empty");
		}

		// if (decor1 == LinkDecor.NONE && decor2 != LinkDecor.NONE) {
		// sb.append("dir=back,");
		// }
		// if (decor1 != LinkDecor.NONE && decor2 != LinkDecor.NONE) {
		// sb.append("dir=both,");
		// }
		//
		// sb.append("dir=both,");
		//
		// sb.append("arrowtail=");
		// sb.append(decor2.getArrowDotSvek());
		// sb.append(",arrowhead=");
		// sb.append(decor1.getArrowDotSvek());

		final double arrowsize = Math.max(decor1.getArrowSize(), decor2.getArrowSize());
		if (arrowsize > 0) {
			sb.append(",arrowsize=" + arrowsize);
		}
		return sb.toString();
	}

	public final LinkDecor getDecor1() {
		return decor1;
	}

	public final LinkStyle getStyle() {
		return style;
	}

	public final LinkDecor getDecor2() {
		return decor2;
	}

	public boolean isExtendsOrAgregationOrCompositionOrPlus() {
		return isExtends() || isAgregationOrComposition() || isPlus();
	}

	private boolean isExtends() {
		return decor1 == LinkDecor.EXTENDS || decor2 == LinkDecor.EXTENDS;
	}

	private boolean isPlus() {
		return decor1 == LinkDecor.PLUS || decor2 == LinkDecor.PLUS;
	}

	private boolean isAgregationOrComposition() {
		return decor1 == LinkDecor.AGREGATION || decor2 == LinkDecor.AGREGATION || decor1 == LinkDecor.COMPOSITION
				|| decor2 == LinkDecor.COMPOSITION;
	}

	public LinkType getPart1() {
		return new LinkType(hat1, decor1, style, middleDecor, LinkDecor.NONE, LinkHat.NONE);
	}

	public LinkType getPart2() {
		return new LinkType(LinkHat.NONE, LinkDecor.NONE, style, middleDecor, decor2, hat2);
	}

	public UStroke getStroke() {
		if (style == LinkStyle.DASHED) {
			return new UStroke(7, 7, 1);
		}
		if (style == LinkStyle.DOTTED) {
			return new UStroke(1, 3, 1);
		}
		if (style == LinkStyle.BOLD) {
			return new UStroke(2);
		}
		return new UStroke();
	}

	public LinkMiddleDecor getMiddleDecor() {
		return middleDecor;
	}

	public LinkHat getHat1() {
		return hat1;
	}

	public LinkHat getHat2() {
		return hat2;
	}

}
