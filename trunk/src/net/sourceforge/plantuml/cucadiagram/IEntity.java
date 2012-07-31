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

import java.util.List;

import net.sourceforge.plantuml.SpecificBackcolorable;
import net.sourceforge.plantuml.Url;

public interface IEntity extends SpecificBackcolorable {

	public LeafType getEntityType();

	public List<? extends CharSequence> getDisplay();

	public IGroup getParentContainer();

	public void setDisplay(List<? extends CharSequence> display);

	public String getUid();

	public List<Url> getUrls();

	public Stereotype getStereotype();

	public void setStereotype(Stereotype stereotype);

	public List<Member> getFieldsToDisplay();

	public List<Member> getMethodsToDisplay();

	public BlockMember getBody(PortionShower portionShower);

	public String getCode();

	public BlockMember getMouseOver();

	public void addFieldOrMethod(String s);

	public void mouseOver(String s);

	public void addUrl(Url url);

	public boolean isGroup();

}
