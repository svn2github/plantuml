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
 * Revision $Revision: 7812 $
 *
 */
package net.sourceforge.plantuml.graph;

import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.IEntity;

public class EntityImageFactory {

	public AbstractEntityImage createEntityImage(IEntity entity) {
		if (entity.getEntityType() == EntityType.CLASS || entity.getEntityType() == EntityType.ABSTRACT_CLASS
				|| entity.getEntityType() == EntityType.INTERFACE || entity.getEntityType() == EntityType.ENUM) {
			return new EntityImageClass(entity);
		}
		if (entity.getEntityType() == EntityType.ACTIVITY) {
			return new EntityImageActivity(entity);
		}
		if (entity.getEntityType() == EntityType.NOTE) {
			return new EntityImageNote(entity);
		}
		if (entity.getEntityType() == EntityType.POINT_FOR_ASSOCIATION) {
			return new EntityImageActivityCircle(entity, 4, 4);
		}
		if (entity.getEntityType() == EntityType.CIRCLE_START) {
			return new EntityImageActivityCircle(entity, 18, 18);
		}
		if (entity.getEntityType() == EntityType.CIRCLE_END) {
			return new EntityImageActivityCircle(entity, 18, 11);
		}
		if (entity.getEntityType() == EntityType.BRANCH) {
			return new EntityImageActivityBranch(entity);
		}
		if (entity.getEntityType() == EntityType.SYNCHRO_BAR) {
			return new EntityImageActivityBar(entity);
		}
		if (entity.getEntityType() == EntityType.USECASE) {
			return new EntityImageUsecase(entity);
		}
		if (entity.getEntityType() == EntityType.ACTOR) {
			return new EntityImageActor(entity);
		}
		if (entity.getEntityType() == EntityType.CIRCLE_INTERFACE) {
			return new EntityImageCircleInterface(entity);
		}
		if (entity.getEntityType() == EntityType.COMPONENT) {
			return new EntityImageComponent(entity);
		}
		return new EntityImageDefault(entity);
	}

}
