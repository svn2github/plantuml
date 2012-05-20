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
 * Revision $Revision: 6711 $
 *
 */
package net.sourceforge.plantuml.svek;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.EntityUtils;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.GroupHierarchy;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IEntityMutable;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.cucadiagram.MethodsOrFieldsArea;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockEmpty;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.TextBlockWidth;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UFont;

public final class GroupPngMakerState {

	private final CucaDiagram diagram;
	private final Group group;

	class InnerGroupHierarchy implements GroupHierarchy {

		public Collection<? extends Group> getChildrenGroups(Group parent) {
			if (parent == null) {
				return diagram.getChildrenGroups(group);
			}
			return diagram.getChildrenGroups(parent);
		}

		public boolean isEmpty(Group g) {
			return diagram.isEmpty(g);
		}

	}

	public GroupPngMakerState(CucaDiagram diagram, Group group) {
		this.diagram = diagram;
		this.group = group;
	}

	private List<Link> getPureInnerLinks() {
		final List<Link> result = new ArrayList<Link>();
		for (Link link : diagram.getLinks()) {
			final IEntityMutable e1 = (IEntityMutable) link.getEntity1();
			final IEntityMutable e2 = (IEntityMutable) link.getEntity2();
			if (isParent(e1, group) && e1.isGroup() == false && isParent(e2, group) && e2.isGroup() == false) {
				result.add(link);
			}
		}
		return result;
	}

	private boolean isParent(IEntity toTest, Group parent) {
		Group g = toTest.getContainer();
		while (g != null) {
			if (EntityUtils.equals(g, parent)) {
				return true;
			}
			g = g.zgetParent();
			// if (g!=null && g.isAutonom()==false) {
			// return false;
			// }
		}
		return false;
	}

	public IEntityImage getImage() throws IOException, InterruptedException {
		final String display = group.zgetDisplay();
		final TextBlock title = TextBlockUtils.create(StringUtils.getWithNewlines(display), new FontConfiguration(
				getFont(FontParam.STATE), HtmlColor.BLACK), HorizontalAlignement.CENTER, diagram.getSkinParam());

		if (group.zsize() == 0) {
			return new EntityImageState((IEntity) group, diagram.getSkinParam());
		}
		final List<Link> links = getPureInnerLinks();
		ISkinParam skinParam = diagram.getSkinParam();
		if (OptionFlags.PBBACK && group.zgetBackColor() != null) {
			skinParam = new SkinParamBackcolored(skinParam, null, group.zgetBackColor());
		}
		final DotData dotData = new DotData(group, links, group.zentities(), diagram.getUmlDiagramType(), skinParam,
				group.zgetRankdir(), new InnerGroupHierarchy(), diagram.getColorMapper(), diagram.getEntityFactory());

		final CucaDiagramFileMakerSvek2 svek2 = new CucaDiagramFileMakerSvek2(dotData);

		if (group.zgetGroupType() == GroupType.CONCURRENT_STATE) {
			return new InnerStateConcurrent(svek2.createFile());
		} else if (group.zgetGroupType() == GroupType.STATE) {
			final HtmlColor borderColor = getColor(ColorParam.stateBorder, null);
			final HtmlColor backColor = group.zgetBackColor() == null ? getColor(ColorParam.stateBackground, null)
					: group.zgetBackColor();
			final List<Member> members = ((IEntity) group).getFieldsToDisplay();
			final TextBlockWidth attribute;
			if (members.size() == 0) {
				attribute = new TextBlockEmpty();
			} else {
				attribute = new MethodsOrFieldsArea(members, FontParam.STATE_ATTRIBUTE, diagram.getSkinParam());
			}
			return new InnerStateAutonom(svek2.createFile(), title, attribute, borderColor, backColor,
					skinParam.shadowing());
		}

		throw new UnsupportedOperationException(group.zgetGroupType().toString());

	}

	private UFont getFont(FontParam fontParam) {
		final ISkinParam skinParam = diagram.getSkinParam();
		return skinParam.getFont(fontParam, null);
	}

	private final Rose rose = new Rose();

	protected final HtmlColor getColor(ColorParam colorParam, Stereotype stereo) {
		final String s = stereo == null ? null : stereo.getLabel();
		final ISkinParam skinParam = diagram.getSkinParam();
		return rose.getHtmlColor(skinParam, colorParam, s);
	}
}
