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
 */
package net.sourceforge.plantuml.compositediagram.command;

import java.util.List;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.compositediagram.CompositeDiagram;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;

public class CommandLinkBlock extends SingleLineCommand<CompositeDiagram> {

	public CommandLinkBlock(CompositeDiagram diagram) {
		super(diagram, "(?i)^([\\p{L}0-9_.]+)\\s*(\\[\\]|\\*\\))?([=-]+|\\.+)(\\[\\]|\\(\\*)?\\s*([\\p{L}0-9_.]+)\\s*(?::\\s*(\\S*+))?$");
	}

	@Override
	protected CommandExecutionResult executeArg(List<String> arg) {
		final IEntity cl1 = getSystem().getOrCreateClass(arg.get(0));
		final IEntity cl2 = getSystem().getOrCreateClass(arg.get(4));

		final String deco1 = arg.get(1);
		final String deco2 = arg.get(3);
		LinkType linkType = new LinkType(getLinkDecor(deco1), getLinkDecor(deco2));
		
		if ("*)".equals(deco1)) {
			linkType = linkType.getInterfaceProvider();
		} else if ("(*".equals(deco2)) {
			linkType = linkType.getInterfaceUser();
		}

		final String queue = arg.get(2);

		final Link link = new Link(cl1, cl2, linkType, arg.get(5), queue.length());
		getSystem().addLink(link);
		return CommandExecutionResult.ok();
	}

	private LinkDecor getLinkDecor(String s) {
		if ("[]".equals(s)) {
			return LinkDecor.SQUARRE_toberemoved;
		}
		return LinkDecor.NONE;
	}

}
