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

import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.svek.PackageStyle;

public interface IGroup extends IEntity {

	public boolean containsLeafRecurse(ILeaf entity);

	public Collection<ILeaf> getLeafsDirect();

	public Collection<IGroup> zgetChildren();

	public void zmoveEntitiesTo(IGroup dest);

	public int zsize();

	public GroupType zgetGroupType();

	public String zgetNamespace();

	public boolean zisAutonom();

	public void zsetAutonom(boolean autonom);

	public Rankdir zgetRankdir();

	public void zsetRankdir(Rankdir rankdir);

	public PackageStyle zgetPackageStyle();

	public void overideImage(IEntityImage img, List<Url> urls, LeafType state);
}
