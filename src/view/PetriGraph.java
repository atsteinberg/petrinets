/**
 * 
 */
package view;

import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.spriteManager.SpriteManager;

/**
 * abstract class that extends graphstream's MultiGraph class. Super class of
 * both PnmlGraph and AccessibilityGraphView.
 * 
 * @author Alexander Steinberg
 *
 */
abstract class PetriGraph extends MultiGraph {

	
	private static String CSS_FILE = "url(" + JpnlGraph.class.getResource("/graph.css") + ")";
	/**
	 * the graph's sprite manager
	 */
	SpriteManager spriteManager;

	/**
	 * constructor for the PetriGraph class.
	 * 
	 * @param id the id of the new PetriGraph
	 */
	public PetriGraph(String id) {
		super(id);
	}

	/**
	 * draws a view of the current state of the represented model.
	 */
	abstract void drawGraph();

	/**
	 * initializes the graph.
	 */
	void initializeGraph() {
		this.clear();
		spriteManager = new SpriteManager(this);
		this.setAttribute("ui.stylesheet", CSS_FILE);
	}

	/**
	 * updates the view to the current state of the represented model.
	 */
	abstract void updateGraph();

}