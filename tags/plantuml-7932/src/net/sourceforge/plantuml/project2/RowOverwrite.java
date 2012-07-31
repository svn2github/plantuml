package net.sourceforge.plantuml.project2;

import java.awt.geom.Dimension2D;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;

class RowOverwrite implements Row {

	private final Row r1;
	private final Row r2;

	public RowOverwrite(Row r1, Row r2) {
		this.r1 = r1;
		this.r2 = r2;
	}

	public TextBlock asTextBloc(final TimeConverter timeConverter) {
		return new TextBlock() {

			public void drawU(UGraphic ug, double x, double y) {
				final double minX = getMinXwithoutHeader(timeConverter);
				final double minXr1 = r1.getMinXwithoutHeader(timeConverter);
				final double minXr2 = r2.getMinXwithoutHeader(timeConverter);
				r1.asTextBloc(timeConverter).drawU(ug, x + (minXr1 - minX), y);
				r2.asTextBloc(timeConverter).drawU(ug, x + (minXr2 - minX), y);
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final double width = getMaxXwithoutHeader(timeConverter) - getMinXwithoutHeader(timeConverter);
				final double height = getHeight();
				return new Dimension2DDouble(width, height);
			}

			public List<Url> getUrls() {
				return Collections.emptyList();
			}
		};
	}

	public double getMinXwithoutHeader(TimeConverter timeConverter) {
		return Math.min(r1.getMinXwithoutHeader(timeConverter), r2.getMinXwithoutHeader(timeConverter));
	}

	public double getMaxXwithoutHeader(TimeConverter timeConverter) {
		return Math.max(r1.getMaxXwithoutHeader(timeConverter), r2.getMaxXwithoutHeader(timeConverter));
	}

	public double getHeight() {
		return Math.max(r1.getHeight(), r2.getHeight());
	}

	public TextBlock header() {
		return r1.header();
	}

}
