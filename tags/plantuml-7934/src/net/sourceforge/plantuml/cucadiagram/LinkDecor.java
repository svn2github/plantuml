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

import net.sourceforge.plantuml.svek.extremity.ExtremityFactory;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryArrow;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryArrowAndCircle;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryCircleCross;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryDiamond;
import net.sourceforge.plantuml.svek.extremity.ExtremityFactoryPlus;

public enum LinkDecor {

	NONE(2, false, 0), EXTENDS(30, false, 2), COMPOSITION(15, true, 1.3), AGREGATION(15, false, 1.3), ARROW(10, true,
			0.5), ARROW_AND_CIRCLE(10, false, 0.5), CIRCLE_CROSS(0, false, 0.5), PLUS(0, false, 1.5), SQUARRE(30, false, 0);

	private final double arrowSize;
	private final int margin;
	private final boolean fill;

	private LinkDecor(int margin, boolean fill, double arrowSize) {
		this.margin = margin;
		this.fill = fill;
		this.arrowSize = arrowSize;
	}

	public String getArrowDotSvek() {
		if (this == LinkDecor.NONE) {
			return "none";
		} else if (this == LinkDecor.EXTENDS) {
			return "empty";
		} else if (this == LinkDecor.COMPOSITION) {
			return "empty";
		} else if (this == LinkDecor.AGREGATION) {
			return "empty";
		} else if (this == LinkDecor.ARROW) {
			return "empty";
		} else if (this == LinkDecor.ARROW_AND_CIRCLE) {
			return "empty";
		} else if (this == LinkDecor.PLUS) {
			return "empty";
		} else if (this == LinkDecor.CIRCLE_CROSS) {
			return "empty";
		} else {
			throw new UnsupportedOperationException();
		}
	}

	public int getMargin() {
		return margin;
	}

	public boolean isFill() {
		return fill;
	}

	public double getArrowSize() {
		return arrowSize;
	}

	public ExtremityFactory getExtremityFactory() {
		if (this == LinkDecor.PLUS) {
			return new ExtremityFactoryPlus();
		} else if (this == LinkDecor.CIRCLE_CROSS) {
			return new ExtremityFactoryCircleCross();
		} else if (this == LinkDecor.ARROW) {
			return new ExtremityFactoryArrow();
		} else if (this == LinkDecor.ARROW_AND_CIRCLE) {
			return new ExtremityFactoryArrowAndCircle();
		} else if (this == LinkDecor.AGREGATION) {
			return new ExtremityFactoryDiamond(false);
		} else if (this == LinkDecor.COMPOSITION) {
			return new ExtremityFactoryDiamond(true);
		}
		return null;
	}

}
