package maggie.drawings;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.io.File;

import maggie.network.gui.GuiUtils;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.generators.random.KleinbergSmallWorldGenerator;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;

public class SmallWorldNetwork {

	class GraphFactory implements Factory {

		@Override
		public UndirectedSparseGraph<Integer, Integer> create() {
			return new UndirectedSparseGraph<Integer, Integer>();
		}

	}

	class VertexFactory implements Factory {
		int id = 0;

		@Override
		public Integer create() {
			return id++;
		}

	}

	class EdgeFactory implements Factory {
		int id = 0;

		@Override
		public Integer create() {
			return id++;
		}

	}

	@SuppressWarnings("rawtypes")
	class GraphViewer extends VisualizationViewer {

		/**
		 * 
		 */
		private static final long serialVersionUID = -9111226419973329984L;

		/**
		 * @param layout
		 */
		@SuppressWarnings("unchecked")
		public GraphViewer(Layout<Integer, Integer> layout) {
			super(layout);
			initialize();
		}

		@SuppressWarnings("unchecked")
		private void initialize() {
			// background color
			setBackground(Color.WHITE);

			// node size
			getRenderContext().setVertexShapeTransformer(
					new ConstantTransformer(new Ellipse2D.Float(-12, -12, 24, 24)));

			// node color
			getRenderContext().setVertexFillPaintTransformer(new Transformer<Integer, Color>() {
				@Override
				public Color transform(Integer i) {
					return Color.black;
				}
			});

			// edge shape
			getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Integer, Number>());

			// edge stroke
			getRenderContext().setEdgeStrokeTransformer(new Transformer<Integer, Stroke>() {
				@Override
				public Stroke transform(Integer input) {
					return new BasicStroke(3);
				};

			});
			// edge color
			// vv.getRenderContext().setEdgeDrawPaintTransformer(
			// new Transformer<MyEdge, Color>() {
			// @Override
			// public Color transform(MyEdge edge) {
			// if (edge.getWeight() < year) {
			// return new Color(1, 1, 1, 1);
			// }
			// int color = 255 - (edge.getWeight() + 1) * 50;
			// if (color < 0) {
			// color = 0;
			// }
			// return new Color(color, color, color);
			//
			// }
			// });
		}
	}

	public static void main(String[] args) {
		SmallWorldNetwork rn = new SmallWorldNetwork();
		rn.draw();
	}

	private void draw() {

		KleinbergSmallWorldGenerator gen =
				new KleinbergSmallWorldGenerator(new GraphFactory(), new VertexFactory(),
						new EdgeFactory(), 20, 0.1f);
		Graph randomNetwork = gen.create();
		// create graph viewer
		Layout<Integer, Integer> layout = new FRLayout<Integer, Integer>(randomNetwork);
		GraphViewer gv = new GraphViewer(layout);
		gv.setSize(600, 600);
		gv.revalidate();
		gv.setBackground(Color.WHITE);

		// scale graph
		// final ScalingControl scaler = new CrossoverScalingControl();
		// gv.scaleToLayout(scaler);
		// scaler.scale(gv, 2.0f, gv.getCenter());

		GuiUtils.drawComponentToFile(gv, new File("./image/SWNetwork.png"), "png");
	}

}
