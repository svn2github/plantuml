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
 * Revision $Revision: 9448 $
 *
 */
package net.sourceforge.plantuml.classdiagram;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.classdiagram.command.CommandAddMethod;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateClass;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateClassMultilines;
import net.sourceforge.plantuml.classdiagram.command.CommandDiamondAssociation;
import net.sourceforge.plantuml.classdiagram.command.CommandHideShow;
import net.sourceforge.plantuml.classdiagram.command.CommandHideShow3;
import net.sourceforge.plantuml.classdiagram.command.CommandImport;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkClass;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkLollipop;
import net.sourceforge.plantuml.classdiagram.command.CommandMouseOver;
import net.sourceforge.plantuml.classdiagram.command.CommandNamespaceSeparator;
import net.sourceforge.plantuml.classdiagram.command.CommandStereotype;
import net.sourceforge.plantuml.classdiagram.command.CommandUrl;
import net.sourceforge.plantuml.command.AbstractUmlSystemCommandFactory;
import net.sourceforge.plantuml.command.CommandEndNamespace;
import net.sourceforge.plantuml.command.CommandEndPackage;
import net.sourceforge.plantuml.command.CommandNamespace;
import net.sourceforge.plantuml.command.CommandPackage;
import net.sourceforge.plantuml.command.CommandPackageEmpty;
import net.sourceforge.plantuml.command.CommandPage;
import net.sourceforge.plantuml.command.CommandRankDir;
import net.sourceforge.plantuml.command.note.FactoryNoteCommand;
import net.sourceforge.plantuml.command.note.FactoryNoteOnEntityCommand;
import net.sourceforge.plantuml.command.note.FactoryNoteOnLinkCommand;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;

public class ClassDiagramFactory extends AbstractUmlSystemCommandFactory {

	private ClassDiagram system;

	public ClassDiagram getSystem() {
		return system;
	}

	@Override
	protected void initCommands() {
		system = new ClassDiagram();

		addCommonCommands(system);

		addCommand(new CommandRankDir(system));
		addCommand(new CommandPage(system));
		addCommand(new CommandAddMethod(system));

		addCommand(new CommandCreateClass(system));
		final FactoryNoteCommand factoryNoteCommand = new FactoryNoteCommand();
		addCommand(factoryNoteCommand.createSingleLine(system));

		addCommand(new CommandPackage(system));
		addCommand(new CommandEndPackage(system));
		addCommand(new CommandPackageEmpty(system));

		addCommand(new CommandNamespace(system));
		addCommand(new CommandEndNamespace(system));
		addCommand(new CommandStereotype(system));

		addCommand(new CommandLinkClass(system));
		addCommand(new CommandLinkLollipop(system));

		addCommand(new CommandImport(system));
		final FactoryNoteOnEntityCommand factoryNoteOnEntityCommand = new FactoryNoteOnEntityCommand(new RegexLeaf(
				"ENTITY", "(" + CommandCreateClass.CODE + "|\"[^\"]+\")"));
		addCommand(factoryNoteOnEntityCommand.createSingleLine(system));
		addCommand(new CommandUrl(system));

		addCommand(factoryNoteOnEntityCommand.createMultiLine(system));
		addCommand(factoryNoteCommand.createMultiLine(system));
		addCommand(new CommandCreateClassMultilines(system));

		final FactoryNoteOnLinkCommand factoryNoteOnLinkCommand = new FactoryNoteOnLinkCommand();
		addCommand(factoryNoteOnLinkCommand.createSingleLine(system));
		addCommand(factoryNoteOnLinkCommand.createMultiLine(system));

		addCommand(new CommandDiamondAssociation(system));
		addCommand(new CommandMouseOver(system));

		addCommand(new CommandHideShow3(system));
		addCommand(new CommandHideShow(system));
		
		addCommand(new CommandNamespaceSeparator(system));

	}

	@Override
	public String checkFinalError() {
		for (IGroup g : system.getGroups(true)) {
			final List<ILeaf> standalones = new ArrayList<ILeaf>();
			for (ILeaf ent : g.getLeafsDirect()) {
				if (system.isStandalone(ent)) {
					standalones.add(ent);
				}
			}
			if (standalones.size() < 3) {
				continue;
			}
			putInSquare(standalones);
		}
		return super.checkFinalError();
	}

	private void putInSquare(List<ILeaf> standalones) {
		final LinkType linkType = new LinkType(LinkDecor.NONE, LinkDecor.NONE).getInvisible();
		final int branch = computeBranch(standalones.size());
		int headBranch = 0;
		for (int i = 1; i < standalones.size(); i++) {
			final int dist = i - headBranch;
			final IEntity ent2 = standalones.get(i);
			final Link link;
			if (dist == branch) {
				final IEntity ent1 = standalones.get(headBranch);
				link = new Link(ent1, ent2, linkType, null, 2);
				headBranch = i;
			} else {
				final IEntity ent1 = standalones.get(i - 1);
				link = new Link(ent1, ent2, linkType, null, 1);
			}
			system.addLink(link);
		}

	}

	static int computeBranch(int size) {
		final double sqrt = Math.sqrt(size);
		final int r = (int) sqrt;
		if (r * r == size) {
			return r;
		}
		return r + 1;
	}
}
