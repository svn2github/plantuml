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

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.EntityPosition;
import net.sourceforge.plantuml.cucadiagram.EntityUtils;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.cucadiagram.MethodsOrFieldsArea;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.cucadiagram.entity.EntityFactory;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockEmpty;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.TextBlockWidth;
import net.sourceforge.plantuml.skin.StickMan;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.svek.image.EntityImageActivity;
import net.sourceforge.plantuml.svek.image.EntityImageActor2;
import net.sourceforge.plantuml.svek.image.EntityImageArcCircle;
import net.sourceforge.plantuml.svek.image.EntityImageAssociation;
import net.sourceforge.plantuml.svek.image.EntityImageAssociationPoint;
import net.sourceforge.plantuml.svek.image.EntityImageBranch;
import net.sourceforge.plantuml.svek.image.EntityImageCircleEnd;
import net.sourceforge.plantuml.svek.image.EntityImageCircleInterface;
import net.sourceforge.plantuml.svek.image.EntityImageCircleStart;
import net.sourceforge.plantuml.svek.image.EntityImageClass;
import net.sourceforge.plantuml.svek.image.EntityImageComponent;
import net.sourceforge.plantuml.svek.image.EntityImageEmptyPackage2;
import net.sourceforge.plantuml.svek.image.EntityImageGroup;
import net.sourceforge.plantuml.svek.image.EntityImageLollipopInterface;
import net.sourceforge.plantuml.svek.image.EntityImageNote;
import net.sourceforge.plantuml.svek.image.EntityImageObject;
import net.sourceforge.plantuml.svek.image.EntityImagePseudoState;
import net.sourceforge.plantuml.svek.image.EntityImageState;
import net.sourceforge.plantuml.svek.image.EntityImageStateBorder;
import net.sourceforge.plantuml.svek.image.EntityImageSynchroBar;
import net.sourceforge.plantuml.svek.image.EntityImageUseCase;

public final class CucaDiagramFileMakerSvek2 {

	private final ColorSequence colorSequence = new ColorSequence();

	private final DotData dotData;
	private final EntityFactory entityFactory;
	private final boolean hasVerticalLine;

	static private final StringBounder stringBounder;

	static {
		final EmptyImageBuilder builder = new EmptyImageBuilder(10, 10, Color.WHITE);
		stringBounder = StringBounderUtils.asStringBounder(builder.getGraphics2D());
	}

	public CucaDiagramFileMakerSvek2(DotData dotData, EntityFactory entityFactory, boolean hasVerticalLine) {
		this.dotData = dotData;
		this.entityFactory = entityFactory;
		this.hasVerticalLine = hasVerticalLine;
	}

	private DotStringFactory dotStringFactory;

	public Bibliotekon getBibliotekon() {
		return dotStringFactory.getBibliotekon();
	}

	public IEntityImage createFile(String... dotStrings) throws IOException, InterruptedException {

		dotStringFactory = new DotStringFactory(colorSequence, stringBounder, dotData);

		printGroups(dotData.getRootGroup());
		printEntities(getUnpackagedEntities());

		for (Link link : dotData.getLinks()) {
			try {
				final String shapeUid1 = getBibliotekon().getShapeUid((ILeaf) link.getEntity1());
				final String shapeUid2 = getBibliotekon().getShapeUid((ILeaf) link.getEntity2());

				String ltail = null;
				if (shapeUid1.startsWith(Cluster.CENTER_ID)) {
					// final Group g1 = ((IEntityMutable) link.getEntity1()).getContainerOrEquivalent();
					ltail = getCluster2((IEntity) link.getEntity1()).getClusterId();
				}
				String lhead = null;
				if (shapeUid2.startsWith(Cluster.CENTER_ID)) {
					// final Group g2 = ((IEntityMutable) link.getEntity2()).getContainerOrEquivalent();
					lhead = getCluster2((IEntity) link.getEntity2()).getClusterId();
				}
				final FontConfiguration labelFont = new FontConfiguration(dotData.getSkinParam().getFont(
						FontParam.ACTIVITY_ARROW, null), HtmlColorUtils.BLACK);

				final Line line = new Line(shapeUid1, shapeUid2, link, colorSequence, ltail, lhead,
						dotData.getSkinParam(), stringBounder, labelFont, getBibliotekon());
				// if (OptionFlags.PIC_LINE && link.getLabel() != null && link.getLabel().startsWith("@")) {
				// final Group container1 = link.getEntity1().getContainer();
				// if (container1 != null) {
				// line.setPicLine1(getBibliotekon().getCluster((IEntity) container1));
				// }
				// final Group container2 = link.getEntity2().getContainer();
				// if (container2 != null) {
				// line.setPicLine2(getBibliotekon().getCluster((IEntity) container2));
				// }
				// }
				// if (link.getEntryPoint() != null) {
				// line.setPicLine2(getBibliotekon().getCluster((IEntity) link.getEntryPoint()));
				// }
				getBibliotekon().addLine(line);

				if (link.getEntity1().isGroup() == false && link.getEntity1().getEntityType() == LeafType.NOTE
						&& onlyOneLink(link.getEntity1())) {
					final Shape shape = getBibliotekon().getShape(link.getEntity1());
					((EntityImageNote) shape.getImage()).setOpaleLine(line, shape);
					line.setOpale(true);
				} else if (link.getEntity2().isGroup() == false && link.getEntity2().getEntityType() == LeafType.NOTE
						&& onlyOneLink(link.getEntity2())) {
					final Shape shape = getBibliotekon().getShape(link.getEntity2());
					((EntityImageNote) shape.getImage()).setOpaleLine(line, shape);
					line.setOpale(true);
				}
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}

		if (dotStringFactory.illegalDotExe()) {
			return error(dotStringFactory.getDotExe());
		}

		final ClusterPosition position = dotStringFactory.solve(dotStrings).delta(10, 10);
		final double minY = position.getMinY();
		final double minX = position.getMinX();
		if (minX > 0 || minY > 0) {
			throw new IllegalStateException();
		}
		final HtmlColor border;
		if (dotData.getUmlDiagramType() == UmlDiagramType.STATE) {
			border = getColor(ColorParam.stateBorder, null);
		} else {
			border = getColor(ColorParam.packageBorder, null);
		}
		final SvekResult result = new SvekResult(position, dotData, dotStringFactory, border, hasVerticalLine);
		result.moveSvek(6 - minX, -minY);
		return result;

	}

	private boolean onlyOneLink(IEntity ent) {
		int nb = 0;
		for (Link link : dotData.getLinks()) {
			if (link.isInvis()) {
				continue;
			}
			if (link.contains(ent)) {
				nb++;
			}
			if (nb > 1) {
				return false;
			}
		}
		return nb == 1;
	}

	private HtmlColor getColor(ColorParam colorParam, Stereotype stereo) {
		final String s = stereo == null ? null : stereo.getLabel();
		return new Rose().getHtmlColor(dotData.getSkinParam(), colorParam, s);
	}

	private Cluster getCluster(IEntity g) {
		for (Cluster cl : getBibliotekon().allCluster()) {
			if (EntityUtils.equals(cl.getGroup(), g)) {
				return cl;
			}
		}
		throw new IllegalArgumentException(g.toString());
	}

	private Cluster getCluster2(IEntity entityMutable) {
		for (Cluster cl : getBibliotekon().allCluster()) {
			if (entityMutable == cl.getGroup()) {
				return cl;
			}
		}
		throw new IllegalArgumentException();
	}

	private IEntityImage error(File dotExe) {

		final List<String> msg = new ArrayList<String>();
		msg.add("Dot Executable: " + dotExe);
		if (dotExe != null) {
			if (dotExe.exists() == false) {
				msg.add("File does not exist");
			} else if (dotExe.isDirectory()) {
				msg.add("It should be an executable, not a directory");
			} else if (dotExe.isFile() == false) {
				msg.add("Not a valid file");
			} else if (dotExe.canRead() == false) {
				msg.add("File cannot be read");
			}
		}
		msg.add("Cannot find Graphviz. You should try");
		msg.add(" ");
		msg.add("@startuml");
		msg.add("testdot");
		msg.add("@enduml");
		msg.add(" ");
		msg.add(" or ");
		msg.add(" ");
		msg.add("java -jar plantuml.jar -testdot");
		msg.add(" ");
		return new GraphicStrings(msg);
	}

	private void printEntities(Collection<ILeaf> entities2) {
		for (ILeaf ent : entities2) {
			printEntity(ent);
		}
	}

	private void printEntity(ILeaf ent) {
		final IEntityImage image = printEntity(ent, dotData);
		final Dimension2D dim = image.getDimension(stringBounder);
		final Shape shape = new Shape(image, image.getShapeType(), dim.getWidth(), dim.getHeight(), colorSequence,
				ent.isTop(), image.getShield(), ent.getUrls(), ent.getEntityPosition());
		dotStringFactory.addShape(shape);
		getBibliotekon().putShape(ent, shape);
	}

	private IEntityImage printEntity(ILeaf ent, DotData dotData) {
		if (ent.getSvekImage() == null) {
			return createEntityImageBlock(dotData, ent);
		}
		return ent.getSvekImage();
	}

	private IEntityImage createEntityImageBlock(DotData dotData, ILeaf leaf) {
		if (leaf.getEntityType() == LeafType.CLASS || leaf.getEntityType() == LeafType.ABSTRACT_CLASS
				|| leaf.getEntityType() == LeafType.INTERFACE || leaf.getEntityType() == LeafType.ENUM) {
			return new EntityImageClass((ILeaf) leaf, dotData.getSkinParam(), dotData);
		}
		if (leaf.getEntityType() == LeafType.NOTE) {
			return new EntityImageNote(leaf, dotData.getSkinParam());
		}
		if (leaf.getEntityType() == LeafType.ACTIVITY) {
			return new EntityImageActivity(leaf, dotData.getSkinParam());
		}
		if (leaf.getEntityType() == LeafType.STATE) {
			if (leaf.getEntityPosition() != EntityPosition.NORMAL) {
				final Cluster stateParent = getBibliotekon().getCluster(leaf.getParentContainer());
				return new EntityImageStateBorder(leaf, dotData.getSkinParam(), stateParent);
			}
			return new EntityImageState(leaf, dotData.getSkinParam());
		}
		if (leaf.getEntityType() == LeafType.CIRCLE_START) {
			return new EntityImageCircleStart(leaf, dotData.getSkinParam());
		}
		if (leaf.getEntityType() == LeafType.CIRCLE_END) {
			return new EntityImageCircleEnd(leaf, dotData.getSkinParam());
		}
		if (leaf.getEntityType() == LeafType.USECASE) {
			return new EntityImageUseCase(leaf, dotData.getSkinParam());
		}
		if (leaf.getEntityType() == LeafType.BRANCH) {
			return new EntityImageBranch(leaf, dotData.getSkinParam());
		}
		if (leaf.getEntityType() == LeafType.LOLLIPOP) {
			return new EntityImageLollipopInterface(leaf, dotData.getSkinParam());
		}
		if (leaf.getEntityType() == LeafType.ACTOR) {
			final TextBlock stickman = new StickMan(getColor(ColorParam.usecaseActorBackground, leaf.getStereotype()),
					getColor(ColorParam.usecaseActorBorder, leaf.getStereotype()),
					dotData.getSkinParam().shadowing() ? 4.0 : 0.0);
			return new EntityImageActor2(leaf, dotData.getSkinParam(), FontParam.USECASE_ACTOR_STEREOTYPE,
					FontParam.USECASE_ACTOR, stickman);
		}
		if (leaf.getEntityType() == LeafType.BOUNDARY) {
			final TextBlock stickman = new Boundary(getColor(ColorParam.usecaseActorBackground, leaf.getStereotype()),
					getColor(ColorParam.usecaseActorBorder, leaf.getStereotype()),
					dotData.getSkinParam().shadowing() ? 4.0 : 0.0);
			return new EntityImageActor2(leaf, dotData.getSkinParam(), FontParam.USECASE_ACTOR_STEREOTYPE,
					FontParam.USECASE_ACTOR, stickman);
		}
		if (leaf.getEntityType() == LeafType.CONTROL) {
			final TextBlock stickman = new Control(getColor(ColorParam.usecaseActorBackground, leaf.getStereotype()),
					getColor(ColorParam.usecaseActorBorder, leaf.getStereotype()),
					dotData.getSkinParam().shadowing() ? 4.0 : 0.0);
			return new EntityImageActor2(leaf, dotData.getSkinParam(), FontParam.USECASE_ACTOR_STEREOTYPE,
					FontParam.USECASE_ACTOR, stickman);
		}
		if (leaf.getEntityType() == LeafType.ENTITY_DOMAIN) {
			final TextBlock stickman = new EntityDomain(getColor(ColorParam.usecaseActorBackground,
					leaf.getStereotype()), getColor(ColorParam.usecaseActorBorder, leaf.getStereotype()), dotData
					.getSkinParam().shadowing() ? 4.0 : 0.0);
			return new EntityImageActor2(leaf, dotData.getSkinParam(), FontParam.USECASE_ACTOR_STEREOTYPE,
					FontParam.USECASE_ACTOR, stickman);
		}
		if (leaf.getEntityType() == LeafType.COMPONENT) {
			return new EntityImageComponent(leaf, dotData.getSkinParam());
		}
		if (leaf.getEntityType() == LeafType.OBJECT) {
			return new EntityImageObject(leaf, dotData.getSkinParam());
		}
		if (leaf.getEntityType() == LeafType.SYNCHRO_BAR) {
			return new EntityImageSynchroBar(leaf, dotData.getSkinParam());
		}
		if (leaf.getEntityType() == LeafType.CIRCLE_INTERFACE) {
			return new EntityImageCircleInterface(leaf, dotData.getSkinParam());
		}
		if (leaf.getEntityType() == LeafType.ARC_CIRCLE) {
			return new EntityImageArcCircle(leaf, dotData.getSkinParam());
		}
		if (leaf.getEntityType() == LeafType.POINT_FOR_ASSOCIATION) {
			return new EntityImageAssociationPoint(leaf, dotData.getSkinParam());
		}
		if (leaf.isGroup()) {
			return new EntityImageGroup(leaf, dotData.getSkinParam());
		}
		if (leaf.getEntityType() == LeafType.EMPTY_PACKAGE) {
			return new EntityImageEmptyPackage2(leaf, dotData.getSkinParam());
		}
		if (leaf.getEntityType() == LeafType.ASSOCIATION) {
			return new EntityImageAssociation(leaf, dotData.getSkinParam());
		}
		if (leaf.getEntityType() == LeafType.PSEUDO_STATE) {
			return new EntityImagePseudoState(leaf, dotData.getSkinParam());
		}
		throw new UnsupportedOperationException(leaf.getEntityType().toString());
	}

	private Collection<ILeaf> getUnpackagedEntities() {
		final List<ILeaf> result = new ArrayList<ILeaf>();
		for (ILeaf ent : dotData.getLeafs()) {
			if (dotData.getTopParent() == ent.getParentContainer()) {
				result.add(ent);
			}
		}
		return result;
	}

	private void printGroups(IGroup parent) throws IOException {
		for (IGroup g : dotData.getGroupHierarchy().getChildrenGroups(parent)) {
			if (dotData.isEmpty(g) && g.zgetGroupType() == GroupType.PACKAGE) {
				final ILeaf folder = entityFactory.createLeaf(g.getCode(), g.getDisplay(), LeafType.EMPTY_PACKAGE,
						g.getParentContainer(), null);
				folder.setSpecificBackcolor(g.getSpecificBackColor());
				printEntity(folder);
			} else {
				printGroup(g);
			}
		}
	}

	private void printGroup(IGroup g) throws IOException {
		if (g.zgetGroupType() == GroupType.CONCURRENT_STATE) {
			return;
		}
		// final String stereo = g.getStereotype();

		int titleAndAttributeWidth = 0;
		int titleAndAttributeHeight = 0;

		final List<? extends CharSequence> label = g.getDisplay();
		TextBlock title = null;
		if (label != null) {
			final FontParam fontParam = g.zgetGroupType() == GroupType.STATE ? FontParam.STATE : FontParam.PACKAGE;

			final String stereo = g.getStereotype() == null ? null : g.getStereotype().getLabel();

			title = TextBlockUtils.create(label, new FontConfiguration(dotData.getSkinParam()
					.getFont(fontParam, stereo), dotData.getSkinParam().getFontHtmlColor(fontParam, stereo)),
					HorizontalAlignement.CENTER, dotData.getSkinParam());

			final Dimension2D dimLabel = title.calculateDimension(stringBounder);

			final List<Member> members = ((IEntity) g).getFieldsToDisplay();
			final TextBlockWidth attribute;
			if (members.size() == 0) {
				attribute = new TextBlockEmpty();
			} else {
				attribute = new MethodsOrFieldsArea(members, FontParam.STATE_ATTRIBUTE, dotData.getSkinParam());
			}
			final Dimension2D dimAttribute = attribute.calculateDimension(stringBounder);
			final double attributeHeight = dimAttribute.getHeight();
			final double attributeWidth = dimAttribute.getWidth();
			final double marginForFields = attributeHeight > 0 ? IEntityImage.MARGIN : 0;

			titleAndAttributeWidth = (int) Math.max(dimLabel.getWidth(), attributeWidth);
			titleAndAttributeHeight = (int) (dimLabel.getHeight() + attributeHeight + marginForFields);
		}

		dotStringFactory.openCluster(g, titleAndAttributeWidth, titleAndAttributeHeight, title);
		this.printEntities(g.getLeafsDirect());

		printGroups(g);

		dotStringFactory.closeCluster();
	}

}
