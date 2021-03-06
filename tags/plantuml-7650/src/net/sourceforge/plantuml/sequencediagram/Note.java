/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 7111 $
 *
 */
package net.sourceforge.plantuml.sequencediagram;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.SpecificBackcolorable;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.HtmlColor;

public class Note implements Event, SpecificBackcolorable {

	private final Participant p;
	private final Participant p2;

	private final List<String> strings;

	private final NotePosition position;
	private NoteStyle style = NoteStyle.NORMAL;

	private final Url url;

	public Note(Participant p, NotePosition position, List<String> strings) {
		this(p, null, position, strings);
	}

	public Note(Participant p, Participant p2, List<String> strings) {
		this(p, p2, NotePosition.OVER_SEVERAL, strings);
	}

	private Note(Participant p, Participant p2, NotePosition position, List<String> strings) {
		this.p = p;
		this.p2 = p2;
		this.position = position;
		if (strings != null && strings.size() > 0) {
			this.url = extractUrl(strings.get(0));
		} else {
			this.url = null;
		}

		if (this.url == null) {
			this.strings = strings;
		} else {
			this.strings = strings.subList(1, strings.size());
		}
	}

	public static Url extractUrl(String s) {
		final Pattern p = Pattern.compile("(?i)^\\[\\[([^|]*)(?:\\|([^|]*))?\\]\\]$");
		final Matcher m = p.matcher(s.trim());
		if (m.matches() == false) {
			return null;
		}
		return new Url(m.group(1), m.group(2));
	}

	public Participant getParticipant() {
		return p;
	}

	public Participant getParticipant2() {
		return p2;
	}

	public List<String> getStrings() {
		return strings;
	}

	public NotePosition getPosition() {
		return position;
	}

	private HtmlColor specificBackcolor;

	public HtmlColor getSpecificBackColor() {
		return specificBackcolor;
	}

	public void setSpecificBackcolor(HtmlColor color) {
		this.specificBackcolor = color;
	}

	public boolean dealWith(Participant someone) {
		return p == someone || p2 == someone;
	}

	public Url getUrl() {
		return url;
	}

	public final NoteStyle getStyle() {
		return style;
	}

	public final void setStyle(NoteStyle style) {
		this.style = style;
	}

}
