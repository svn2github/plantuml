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
 * Revision $Revision: 6710 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.EntityMutable;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntityMutable;
import net.sourceforge.plantuml.svek.GroupPngMakerState;
import net.sourceforge.plantuml.svek.IEntityImage;

public final class CucaDiagramSimplifierState {

	private final CucaDiagram diagram;

	public CucaDiagramSimplifierState(CucaDiagram diagram, List<String> dotStrings) throws IOException,
			InterruptedException {
		this.diagram = diagram;
		boolean changed;
		do {
			changed = false;
			final Collection<IEntityMutable> groups = new ArrayList<IEntityMutable>(diagram.getGroups(false));
			for (IEntityMutable g : groups) {
				if (diagram.isAutarkic(g)) {
					final EntityType type;
					if (g.zgetGroupType() == GroupType.CONCURRENT_STATE) {
						type = EntityType.STATE_CONCURRENT;
					} else if (g.zgetGroupType() == GroupType.STATE) {
						type = EntityType.STATE;
					} else {
						throw new IllegalStateException();
					}
					
					final IEntityImage img = computeImage(g);
					g.overideImage42(img);

//					final String code = "#" + g.zgetGroupCode();
//					final EntityMutable proxy = (EntityMutable) diagram.getEntityFactory().createEntity(code,
//							g.zgetDisplay(), type, (IEntityMutable) g.zgetParent(), diagram.getHides());
//					if (g.zgetBackColor() != null) {
//						proxy.setSpecificBackcolor(g.zgetBackColor());
//					}
//					proxy.overidesFieldsToDisplay((EntityMutable) g);
//					computeImageGroup((EntityMutable) g, proxy, dotStrings);
//					g.overideGroup(proxy);
//					((IEntityMutable) g).setSvekImage(proxy.getSvekImage());

					changed = true;
				}
			}
		} while (changed);
	}

	private void computeImageGroup(EntityMutable g, EntityMutable proxy, List<String> dotStrings) throws IOException,
			InterruptedException {
		final GroupPngMakerState maker = new GroupPngMakerState(diagram, g);
		proxy.setSvekImage(maker.getImage());
	}

	private IEntityImage computeImage(IEntityMutable g) throws IOException, InterruptedException {
		final GroupPngMakerState maker = new GroupPngMakerState(diagram, g);
		return maker.getImage();
	}

}
