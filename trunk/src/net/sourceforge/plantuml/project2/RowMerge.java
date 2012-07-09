package net.sourceforge.plantuml.project2;

import java.awt.geom.Dimension2D;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UGraphic;

class RowMerge implements Row {

	private final Row r1;
	private final Row r2;

	public RowMerge(Row r1, Row r2) {
		this.r1 = r1;
		this.r2 = r2;
	}

	public TextBlock asTextBloc(final TimeConverter timeConverter) {
		return new TextBlock() {

			public void drawU(UGraphic ug, double x, double y) {
				r1.asTextBloc(timeConverter).drawU(ug, x, y);
				r2.asTextBloc(timeConverter).drawU(ug, x, y + r1.getHeight());
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
		return r1.getHeight() + r2.getHeight();
	}

	public TextBlock header() {
		return new TextBlock() {

			public void drawU(UGraphic ug, double x, double y) {
				r1.header().drawU(ug, x, y);
				r2.header().drawU(ug, x, y + r1.getHeight());
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final double width = Math.max(r1.header().calculateDimension(stringBounder).getWidth(), r2.header()
						.calculateDimension(stringBounder).getWidth());
				final double height = getHeight();
				return new Dimension2DDouble(width, height);
			}

			public List<Url> getUrls() {
				return Collections.emptyList();
			}
		};
	}

}
