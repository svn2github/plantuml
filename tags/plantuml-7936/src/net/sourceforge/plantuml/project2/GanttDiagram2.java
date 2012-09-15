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
 * Revision $Revision: 6104 $
 *
 */
package net.sourceforge.plantuml.project2;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class GanttDiagram2 {

	private final Project2 project;
	private final double dayWith = 20;

	public GanttDiagram2(Project2 project) {
		this.project = project;
	}

	private final UFont font = new UFont("Serif", Font.PLAIN, 9);
	private final FontConfiguration fontConfig = new FontConfiguration(font, HtmlColorUtils.BLACK);

	public void draw(UGraphic ug, double x, double y) {

		final TextBlock timeHeader = project.getTimeHeader(dayWith);
		final Row row = getMainRow();
		final TextBlock headers = row.header();

		final double deltaX = headers.calculateDimension(ug.getStringBounder()).getWidth();
		final double deltaY = timeHeader.calculateDimension(ug.getStringBounder()).getHeight();

		headers.drawU(ug, x, y + deltaY);
		final TextBlock tbRow = row.asTextBloc(project.getTimeConverter(dayWith));
		tbRow.drawU(ug, x + deltaX, y + deltaY);

		timeHeader.drawU(ug, x + deltaX, y);
	}

	private Row getMainRow() {
		final List<Task> tasks = project.getTasks();
		final List<Row> rows = new ArrayList<Row>();
		for (Task t : tasks) {
			final String text = t.getCode();
			final TextBlock label = TextBlockUtils.create(Arrays.asList(text), fontConfig, HorizontalAlignement.LEFT,
					new SpriteContainerEmpty());
			rows.add(new RowSimple((Day) t.getStart(), (Day) t.getEnd(), HtmlColorUtils.BLACK, TextBlockUtils
					.withMargin(label, 3, 3)));
		}
		final Row row = RowUtils.merge(rows);
		return row;
	}

	public double getWidth(StringBounder stringBounder) {
		final TextBlock timeHeader = project.getTimeHeader(dayWith);
		final Row row = getMainRow();
		final TextBlock headers = row.header();
		return headers.calculateDimension(stringBounder).getWidth()
				+ timeHeader.calculateDimension(stringBounder).getWidth() + 1;
	}

	public double getHeight(StringBounder stringBounder) {
		final TextBlock timeHeader = project.getTimeHeader(dayWith);
		final Row row = getMainRow();
		final TextBlock headers = row.header();
		return headers.calculateDimension(stringBounder).getHeight()
				+ timeHeader.calculateDimension(stringBounder).getHeight();
	}

}