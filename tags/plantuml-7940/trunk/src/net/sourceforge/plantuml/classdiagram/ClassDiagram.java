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
 * Revision $Revision: 9063 $
 *
 */
package net.sourceforge.plantuml.classdiagram;

import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.EntityUtils;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.objectdiagram.AbstractClassOrObjectDiagram;

public class ClassDiagram extends AbstractClassOrObjectDiagram {

	@Override
	public ILeaf getOrCreateLeaf(Code code, LeafType defaultType) {
		assert defaultType == LeafType.ABSTRACT_CLASS || defaultType == LeafType.CLASS
				|| defaultType == LeafType.INTERFACE || defaultType == LeafType.ENUM
				|| defaultType == LeafType.LOLLIPOP || defaultType == LeafType.POINT_FOR_ASSOCIATION;
		code = getFullyQualifiedCode(code);
		if (super.leafExist(code)) {
			return super.getOrCreateLeaf(code, defaultType);
		}
		return createEntityWithNamespace(code, StringUtils.getWithNewlines(getShortName(code)), defaultType);
	}

	@Override
	public ILeaf createLeaf(Code code, List<? extends CharSequence> display, LeafType type) {
		if (type != LeafType.ABSTRACT_CLASS && type != LeafType.CLASS && type != LeafType.INTERFACE
				&& type != LeafType.ENUM && type != LeafType.LOLLIPOP) {
			return super.createLeaf(code, display, type);
		}
		code = getFullyQualifiedCode(code);
		if (super.leafExist(code)) {
			throw new IllegalArgumentException("Already known: " + code);
		}
		return createEntityWithNamespace(code, display, type);
	}

	private ILeaf createEntityWithNamespace(Code fullyCode, List<? extends CharSequence> display, LeafType type) {
		IGroup group = getCurrentGroup();
		final String namespace = getNamespace(fullyCode);
		if (namespace != null && (EntityUtils.groupRoot(group) || group.getCode().getCode().equals(namespace) == false)) {
			group = getOrCreateGroupInternal(Code.of(namespace), StringUtils.getWithNewlines(namespace), namespace, GroupType.PACKAGE, getRootGroup());
		}
		return createLeafInternal(fullyCode, display == null ? StringUtils.getWithNewlines(getShortName(fullyCode)) : display, type, group);
	}

	@Override
	public final boolean leafExist(Code code) {
		return super.leafExist(getFullyQualifiedCode(code));
	}

	@Override
	public ILeaf getOrCreateClass(Code code) {
		return getOrCreateLeaf(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(code), LeafType.CLASS);
	}

	final public IEntity getOrCreateClass(Code code, LeafType type) {
		if (type != LeafType.ABSTRACT_CLASS && type != LeafType.CLASS && type != LeafType.INTERFACE
				&& type != LeafType.ENUM && type != LeafType.LOLLIPOP) {
			throw new IllegalArgumentException();
		}
		return getOrCreateLeaf(code, type);
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.CLASS;
	}


}
