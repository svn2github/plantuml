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
 * Revision $Revision: 7853 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import java.util.Collection;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.svek.PackageStyle;

public interface Group {

	public boolean zcontains(IEntity entity);

	public Collection<IEntity> zentities();

	public int zsize();

	public String zgetGroupCode();

	public String zgetUid();

	public String zgetUid1();

	public int zgetUid2();

	public HtmlColor zgetBackColor();

	public void zsetBackColor(HtmlColor backColor);

	public Group zgetParent();

	public boolean zisDashed();

	public void zsetDashed(boolean dashed);

	public boolean zisRounded();

	public void zsetRounded(boolean rounded);

	public GroupType zgetGroupType();

	public String zgetDisplay();

	public boolean zisBold();

	public void zsetBold(boolean bold);

	public void zmoveEntitiesTo(IEntityMutable dest);

	public String zgetNamespace();

	public Collection<? extends Group> zgetChildren();

	public boolean zisAutonom();

	public void zsetAutonom(boolean autonom);

	public Rankdir zgetRankdir();

	public void zsetRankdir(Rankdir rankdir);

	public void zsetStereotype(Stereotype stereotype);

	public Stereotype zgetStereotype();
	
	public PackageStyle zgetPackageStyle();
}
