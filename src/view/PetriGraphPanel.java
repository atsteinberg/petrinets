/**
 * 
 */
package view;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

/**
 * class that extends JPanel to hold MultiGraph's. In particular: instances of
 * JpnlGraph and AccessibilityGraphView.
 * 
 * @author Alexander Steinberg
 *
 */
public class PetriGraphPanel extends JPanel {

	private static final long serialVersionUID = -4273263194376844011L;

	private ViewPanel viewPanel;

	private ViewerPipe viewerPipe;

	private SwingViewer viewer;

	/**
	 * constructor for the PetriGraphPanel class. Initializes a new viewer and sets
	 * up its viewer pipe.
	 * 
	 * @param multiGraph the graph to add to the panel
	 */
	public PetriGraphPanel(MultiGraph multiGraph) {
		super();

		viewer = new SwingViewer(multiGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);

		multiGraph.setAttribute("ui.quality");
		multiGraph.setAttribute("ui.antialias");

		this.setLayout(new BorderLayout());

		initializeViewPanel();

	}

	private void initializeViewPanel() {
		viewPanel = (ViewPanel) viewer.addDefaultView(false);

		viewerPipe = viewer.newViewerPipe();

		add(viewPanel);

		viewPanel.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent me) {
				System.out.println("MyFrame - mousePressed: " + me);
				viewerPipe.pump();
			}

			@Override
			public void mouseReleased(MouseEvent me) {
				System.out.println("MyFrame - mouseReleased: " + me);
				viewerPipe.pump();
			}
		});

		viewPanel.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				double zoomLevel = viewPanel.getCamera().getViewPercent();
				if (e.getWheelRotation() == -1) {
					zoomLevel -= 0.1;
					if (zoomLevel < 0.1) {
						zoomLevel = 0.1;
					}
				}
				if (e.getWheelRotation() == 1) {
					zoomLevel += 0.1;
				}
				viewPanel.getCamera().setViewPercent(zoomLevel);
			}
		});
	}

	/**
	 * add new viewer listener to the panel's viewer pipe
	 * 
	 * @param viewerListener the ViewerListener to add to the viewer pipe
	 */
	void addViewerListener(ViewerListener viewerListener) {
		viewerPipe.addViewerListener(viewerListener);
	}

	/**
	 * enable the viewer's auto layout.
	 */
	void enableAutoLayout() {
		viewer.enableAutoLayout();
	}

	/**
	 * disable the viewer's auto layout.
	 */
	void disableAutoLayout() {
		viewer.disableAutoLayout();
	}

}
