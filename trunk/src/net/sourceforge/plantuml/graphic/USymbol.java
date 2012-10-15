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
 * Revision $Revision: 8066 $
 *
 */
package net.sourceforge.plantuml.graphic;

public interface USymbol {

	public static USymbol NODE = new USymbolNode();
	public static USymbol ARTIFACT = new USymbolArtifact();
	public static USymbol ACTOR = new USymbolActor();
	public static USymbol COMPONENT1 = new USymbolComponent1();
	public static USymbol COMPONENT2 = new USymbolComponent2();
	public static USymbol BOUNDARY = new USymbolBoundary();
	public static USymbol ENTITY_DOMAIN = new USymbolEntityDomain();
	public static USymbol CONTROL = new USymbolControl();
	public static USymbol INTERFACE = new USymbolInterface();

	TextBlock asSmall(TextBlock label, TextBlock stereotype, SymbolContext symbolContext);

	TextBlock asBig(TextBlock label, TextBlock stereotype, double width, double height, SymbolContext symbolContext);

}