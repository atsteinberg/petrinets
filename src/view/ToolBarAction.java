/**
 * 
 */
package view;

/**
 * enum that lists possible tool bar actions
 * 
 * @author Alexander Steinberg
 *
 */
public enum ToolBarAction {
	/**
	 * reinitialise petrinet
	 */
	REINITIALISE_PETRINET, 
	/**
	 * clear text area
	 */
	CLEAR_TEXTAREA, 
	/** 
	 * add token to selected place
	 */
	ADD_TOKEN, 
	/**
	 * subtract token from selected place
	 */
	SUBTRACT_TOKEN, 
	/**
	 * clear accessibility graph
	 */
	CLEAR_ACCESSIBILITY_GRAPH, 
	/**
	 * continuously analyse petrinet
	 */
	CONTINUOUS_ANALYSIS,
	/**
	 * auto generate accessibility graph and analyse
	 */
	ANALYZE_PETRINET, 
	/**
	 * open previous file
	 */
	PREVIOUS_FILE, 
	/**
	 * open next file
	 */
	NEXT_FILE
}
