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

public abstract class EntityUtils {

	public static boolean equals(Group g1, Group g2) {
		if (g1 == null && g2 == null) {
			return true;
		}
		if (g1 == null || g2 == null) {
			assert g1 == null ^ g2 == null;
			return false;
		}
		if (g1 instanceof IEntityMutable && g2 instanceof IEntityMutable) {
			final IEntityMutable gg1 = (IEntityMutable) g1;
			final IEntityMutable gg2 = (IEntityMutable) g2;
			if (gg1.isGroup() == false && gg2.isGroup()) {
				return false;
			}
			if (gg2.isGroup() == false && gg1.isGroup()) {
				return false;
			}
		}
//		if ((g1 == g2) != (g1.zgetUid2() == g2.zgetUid2())) {
//			throw new IllegalStateException();
//
//		}
		return g1.zgetUid2() == g2.zgetUid2();
	}

	public static boolean doesContains(IEntityMutable container, IEntityMutable element) {
		while (true) {
			if (element.getContainer() == container) {
				return true;
			}
			element = (IEntityMutable) element.getContainer();
			if (element == null) {
				return false;
			}
		}
	}

	public static Group getContainerOrEquivalent(IEntity entity) {
		if (((IEntityMutable) entity).isGroup()) {
			return (Group) entity;
		}
		return entity.getContainer();
	}

}
