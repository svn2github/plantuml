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
 * Revision $Revision: 4236 $
 * 
 */
package net.sourceforge.plantuml.svek;

import java.awt.geom.Point2D;
import java.util.Collection;

public class FrontierCalculator {

	private ClusterPosition core;

	public FrontierCalculator(final ClusterPosition cluster, Collection<ClusterPosition> insides,
			Collection<Point2D> points) {
		for (ClusterPosition in : insides) {
			if (core == null) {
				core = in;
			} else {
				core = core.merge(in);
			}
		}
		if (core == null) {
			final Point2D center = cluster.getPointCenter();
			core = new ClusterPosition(center.getX() - 1, center.getY() - 1, center.getX() + 1, center.getY() + 1);
		}
		for (Point2D p : points) {
			core = core.merge(p);
		}
		boolean touchMinX = false;
		boolean touchMaxX = false;
		boolean touchMinY = false;
		boolean touchMaxY = false;
		for (Point2D p : points) {
			if (p.getX() == core.getMinX()) {
				touchMinX = true;
			}
			if (p.getX() == core.getMaxX()) {
				touchMaxX = true;
			}
			if (p.getY() == core.getMinY()) {
				touchMinY = true;
			}
			if (p.getY() == core.getMaxY()) {
				touchMaxY = true;
			}
		}
		if (touchMinX == false) {
			core = core.withMinX(cluster.getMinX());
		}
		if (touchMaxX == false) {
			core = core.withMaxX(cluster.getMaxX());
		}
		if (touchMinY == false) {
			core = core.withMinY(cluster.getMinY());
		}
		if (touchMaxY == false) {
			core = core.withMaxY(cluster.getMaxY());
		}
	}

	public ClusterPosition getSuggestedPosition() {
		return core;
	}

}
