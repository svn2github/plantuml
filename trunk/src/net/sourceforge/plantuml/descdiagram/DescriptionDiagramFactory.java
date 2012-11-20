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
 * Revision $Revision: 7920 $
 *
 */
package net.sourceforge.plantuml.descdiagram;

import net.sourceforge.plantuml.classdiagram.command.CommandUrl;
import net.sourceforge.plantuml.command.AbstractUmlSystemCommandFactory;
import net.sourceforge.plantuml.command.CommandEndPackage;
import net.sourceforge.plantuml.command.CommandPackage;
import net.sourceforge.plantuml.command.CommandPage;
import net.sourceforge.plantuml.command.CommandRankDir;
import net.sourceforge.plantuml.command.note.FactoryNoteCommand;
import net.sourceforge.plantuml.command.note.FactoryNoteOnEntityCommand;
import net.sourceforge.plantuml.command.note.FactoryNoteOnLinkCommand;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.descdiagram.command.CommandCreateElementFull;
import net.sourceforge.plantuml.descdiagram.command.CommandCreateElementMultilines;
import net.sourceforge.plantuml.descdiagram.command.CommandLinkElement;
import net.sourceforge.plantuml.descdiagram.command.CommandPackageWithUSymbol;

public class DescriptionDiagramFactory extends AbstractUmlSystemCommandFactory {

	private DescriptionDiagram system;

	public DescriptionDiagram getSystem() {
		return system;
	}

	@Override
	protected void initCommands() {
		system = new DescriptionDiagram();

		addCommand(new CommandRankDir(system));
		addCommonCommands(system);

		addCommand(new CommandPage(system));
		addCommand(new CommandLinkElement(system));
		// addCommand(new CommandLinkComponent2(system));
		//
		addCommand(new CommandPackageWithUSymbol(system));
		addCommand(new CommandPackage(system));
		addCommand(new CommandEndPackage(system));
		// addCommand(new CommandNamespace(system));
		// addCommand(new CommandEndNamespace(system));
		final FactoryNoteCommand factoryNoteCommand = new FactoryNoteCommand();
		addCommand(factoryNoteCommand.createMultiLine(system));

		final FactoryNoteOnEntityCommand factoryNoteOnEntityCommand = new FactoryNoteOnEntityCommand(new RegexOr(
				"ENTITY", //
				new RegexLeaf("[\\p{L}0-9_.]+"), //
				new RegexLeaf("\\(\\)\\s*[\\p{L}0-9_.]+"), //
				new RegexLeaf("\\(\\)\\s*\"[^\"]+\""), //
				new RegexLeaf("\\[[^\\]*]+[^\\]]*\\]"), //
				new RegexLeaf("\\((?!\\*\\))[^\\)]+\\)"), //
				new RegexLeaf(":[^:]+:"), //
				new RegexLeaf("\"[^\"]+\"") //
				));
		addCommand(factoryNoteOnEntityCommand.createSingleLine(system));

		addCommand(factoryNoteCommand.createSingleLine(system));
		addCommand(new CommandUrl(system));
		// addCommand(new CommandCreateComponent2(system));
		addCommand(new CommandCreateElementFull(system));
		addCommand(new CommandCreateElementMultilines(system));
		// addCommand(new CommandCreateElementTyped(system));
		// addCommand(new CommandCreateCircleInterface(system));
		// addCommand(new CommandCreateActorInComponent(system));

		addCommand(factoryNoteOnEntityCommand.createMultiLine(system));
		addCommand(factoryNoteCommand.createMultiLine(system));

		final FactoryNoteOnLinkCommand factoryNoteOnLinkCommand = new FactoryNoteOnLinkCommand();
		addCommand(factoryNoteOnLinkCommand.createSingleLine(system));
		addCommand(factoryNoteOnLinkCommand.createMultiLine(system));

	}

	@Override
	public String checkFinalError() {
		system.applySingleStrategy();
		return super.checkFinalError();
	}

}
