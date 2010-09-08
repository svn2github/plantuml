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
 * Revision $Revision: 5207 $
 * 
 */
package net.sourceforge.plantuml;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.plantuml.activitydiagram.ActivityDiagramFactory;
import net.sourceforge.plantuml.classdiagram.ClassDiagramFactory;
import net.sourceforge.plantuml.componentdiagram.ComponentDiagramFactory;
import net.sourceforge.plantuml.compositediagram.CompositeDiagramFactory;
import net.sourceforge.plantuml.eggs.PSystemEggFactory;
import net.sourceforge.plantuml.eggs.PSystemLostFactory;
import net.sourceforge.plantuml.eggs.PSystemRIPFactory;
import net.sourceforge.plantuml.objectdiagram.ObjectDiagramFactory;
import net.sourceforge.plantuml.oregon.PSystemOregonFactory;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.preproc.Preprocessor;
import net.sourceforge.plantuml.preproc.ReadLineReader;
import net.sourceforge.plantuml.preproc.UncommentReadLine;
import net.sourceforge.plantuml.printskin.PrintSkinFactory;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagramFactory;
import net.sourceforge.plantuml.statediagram.StateDiagramFactory;
import net.sourceforge.plantuml.sudoku.PSystemSudokuFactory;
import net.sourceforge.plantuml.usecasediagram.UsecaseDiagramFactory;
import net.sourceforge.plantuml.version.PSystemVersionFactory;

public abstract class AbstractSourceReader {

	private final Defines defines;

	public AbstractSourceReader(Defines defines) {
		this.defines = defines;
	}

	private SortedMap<Integer, StartUml> tryThisFactory(List<String> strings, PSystemFactory systemFactory)
			throws IOException, InterruptedException {
		return new SystemFactoryTry(strings, systemFactory).getAllStartUml();

	}

	private StartUml getStartUmlAtLine(List<SortedMap<Integer, StartUml>> rzz, int i) {
		for (SortedMap<Integer, StartUml> map : rzz) {
			final StartUml s1 = map.get(i);
			if (isOk(s1.getSystem())) {
				return s1;
			}
		}
		return null;
	}

	final protected List<StartUml> getAllStartUml(List<String> config) throws IOException, InterruptedException {
		final List<String> strings = new ArrayList<String>(getStrings(getReader()));
		insertConfig(strings, config);

		final List<PSystemFactory> factories = new ArrayList<PSystemFactory>();
		factories.add(new SequenceDiagramFactory());
		factories.add(new ClassDiagramFactory());
		factories.add(new ActivityDiagramFactory());
		factories.add(new UsecaseDiagramFactory());
		factories.add(new ComponentDiagramFactory());
		factories.add(new StateDiagramFactory());
		factories.add(new CompositeDiagramFactory());
		factories.add(new ObjectDiagramFactory());
		factories.add(new PrintSkinFactory());
		factories.add(new PSystemVersionFactory());
		factories.add(new PSystemSudokuFactory());
		factories.add(new PSystemEggFactory());
		factories.add(new PSystemRIPFactory());
		factories.add(new PSystemLostFactory());
		factories.add(new PSystemOregonFactory());

		final List<SortedMap<Integer, StartUml>> allResults = new ArrayList<SortedMap<Integer, StartUml>>();
		for (PSystemFactory systemFactory : factories) {
			allResults.add(tryThisFactory(strings, systemFactory));
		}

		final SortedSet<Integer> lines = new TreeSet<Integer>();

		for (SortedMap<Integer, StartUml> map : allResults) {
			lines.addAll(map.keySet());
		}

		final List<StartUml> result = new ArrayList<StartUml>();

		for (Integer i : lines) {
			StartUml system = getStartUmlAtLine(allResults, i);
			if (system == null) {
				system = getError(allResults, i);
				if (OptionFlags.getInstance().isQuiet()==false && system.getSystem() instanceof PSystemError) {
					((PSystemError) system.getSystem()).print(System.err);
				}
			}
			result.add(system);
		}

		return result;
	}

	private StartUml getError(final List<SortedMap<Integer, StartUml>> allResults, Integer i) {
		int cpt = 0;
		final StartUml s1 = allResults.get(cpt++).get(i);
		final StartUml s2 = allResults.get(cpt++).get(i);
		final StartUml s3 = allResults.get(cpt++).get(i);
		final StartUml s4 = allResults.get(cpt++).get(i);
		final StartUml s5 = allResults.get(cpt++).get(i);
		final StartUml s6 = allResults.get(cpt++).get(i);
		final StartUml s7 = allResults.get(cpt++).get(i);
		final StartUml s8 = allResults.get(cpt++).get(i);
		final PSystemError merge = merge((PSystemError) s1.getSystem(), (PSystemError) s2.getSystem(),
				(PSystemError) s3.getSystem(), (PSystemError) s4.getSystem(), (PSystemError) s5.getSystem(),
				(PSystemError) s6.getSystem(), (PSystemError) s7.getSystem(), (PSystemError) s8.getSystem());
		return new StartUml(merge, s1.getStartuml());
	}

	static public PSystemError merge(PSystemError... ps) {
		UmlSource source = null;
		final List<ErrorUml> errors = new ArrayList<ErrorUml>();
		for (PSystemError system : ps) {
			// if (system == null) {
			// throw new IllegalStateException();
			// // continue;
			// }
			if (system.getSource() != null && source == null) {
				source = system.getSource();
			}
			errors.addAll(system.getErrorsUml());
		}
		if (source == null) {
			throw new IllegalStateException();
		}
		return new PSystemError(source, errors);
	}

	private void insertConfig(List<String> strings, List<String> config) {
		if (config.size() == 0) {
			return;
		}
		for (int i = 0; i < strings.size(); i++) {
			if (SystemFactoryTry.isArobaseStartuml(strings.get(i))) {
				strings.addAll(i + 1, config);
				i += config.size();
			}
		}

	}

	private boolean isOk(PSystem ps) {
		if (ps == null || ps instanceof PSystemError) {
			return false;
		}
		return true;
	}

	private List<String> getStrings(Reader reader) throws IOException {
		final List<String> result = new ArrayList<String>();
		Preprocessor includer = null;
		try {
			includer = new Preprocessor(new UncommentReadLine(new ReadLineReader(reader)), defines);
			String s = null;
			while ((s = includer.readLine()) != null) {
				result.add(s);
			}
		} finally {
			if (includer != null) {
				includer.close();
			}
		}
		return Collections.unmodifiableList(result);
	}

	protected abstract Reader getReader() throws IOException;
}
