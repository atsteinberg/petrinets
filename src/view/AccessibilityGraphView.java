/**
 * 
 */
package view;

import java.util.Collection;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.ui.spriteManager.Sprite;

import model.AccessibilityGraph;
import model.PetrinetState;
import model.Transition;

/**
 * Class to visually represent an accessibility graph. Extends PetriGraph, and,
 * thus, is a subtype of graph streams multigraph class.
 * 
 * @author Alexander Steinberg
 *
 */
public class AccessibilityGraphView extends PetriGraph {

	private AccessibilityGraph accessibilityGraph;

	private AccessibilityGraphViewListener listener;

	private boolean shouldHighlightUnbounded;

	private boolean shouldHighlightUnboundedFromSettings;

	/**
	 * Constructor for a class to visually represent an accessibility graph.
	 * 
	 * @param id                 the id of the graph view
	 * @param accessibilityGraph the accessibility graph represented (the view's
	 * @param listener           the accessibility graph listener to register model)
	 */
	public AccessibilityGraphView(String id, AccessibilityGraph accessibilityGraph,
			AccessibilityGraphViewListener listener) {
		super(id);
		this.accessibilityGraph = accessibilityGraph;
		this.listener = listener;
		shouldHighlightUnboundedFromSettings = false;

		drawGraph();
	}

	@Override
	void updateGraph() {
		PetrinetState currentState = accessibilityGraph.getCurrentState();
		Node currentNode = drawNode(currentState);
		drawEdges(currentState);
		highlightCurrents(currentNode);
		if (shouldHighlightUnbounded()) {
			System.out.println("should highlight");
			accessibilityGraph.runAnalysis();
			highlightAnalysis();
		}
	}

	/**
	 * @param currentNode
	 */
	private void highlightCurrents(Node currentNode) {
		if (currentNode.getId().equals(accessibilityGraph.getInitialStateId())) {
			PetriGraphUtils.addAttribute(currentNode, "ui.class", "initialState");
		}
		for (Node node : this.nodeArray) {
			if (node != null && !node.equals(currentNode)) {
				PetriGraphUtils.removeAttribute(node, "ui.class", "currentState");
			}
		}
		PetriGraphUtils.addAttribute(currentNode, "ui.class", "currentState");
		Edge currentEdge = this.getEdge(accessibilityGraph.getCurrentEdgeInfo());
		for (Edge edge : this.edgeArray) {
			if (edge != null && !edge.equals(currentEdge)) {
				PetriGraphUtils.removeAttribute(edge, "ui.class", "currentEdge");
			}
		}
		if (currentEdge != null) {
			PetriGraphUtils.addAttribute(currentEdge, "ui.class", "currentEdge");
		}
	}

	/**
	 * @param currentState
	 */
	private void drawEdges(PetrinetState state) {
		if (state != null) {
			Transition[] transitions = state.getTransitions();
			for (Transition transition : transitions) {
				String transitionId = transition.getId();
				String edgeId = state.getEdgeInfo(transitionId);
				if (edgeId != null) {
					Edge currentEdge = this.getEdge(edgeId);
					String targetId = state.getId();
					String sourceId = state.getParentIdByTransitionId(transitionId);
					if (currentEdge == null) {
						currentEdge = this.addEdge(edgeId, sourceId, targetId, true);
						Sprite labelSprite = spriteManager.addSprite(edgeId);
						labelSprite.setAttribute("ui.label", transition.getDisplayName());
						labelSprite.setAttribute("ui.class", "edgeLabel");
						labelSprite.attachToEdge(edgeId);
						labelSprite.setPosition(0.5);
					} else {
						System.out.println("edge already exists");
					}
				}
			}
		}
	}

	/**
	 *
	 */
	private boolean highlightAnalysis() {
		if (accessibilityGraph.isUnbounded()) {
			removeAnalysisHighlighting();
			Node mStateNode = this.getNode(accessibilityGraph.getmStateId());
			PetriGraphUtils.addAttribute(mStateNode, "ui.class", "mState");
			Node mPrimeStateNode = this.getNode(accessibilityGraph.getmPrimeStateId());
			PetriGraphUtils.addAttribute(mPrimeStateNode, "ui.class", "mPrimeState");
			for (Sprite sprite : spriteManager.sprites()) {
				if (sprite != null) {
					if (accessibilityGraph.getPathIds().contains(sprite.getId())) {
						sprite.setAttribute("ui.class", "belongsToPath");
					}
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 
	 */
	private void removeAnalysisHighlighting() {
		for (Node node : this.nodeArray) {
			PetriGraphUtils.removeAttribute(node, "ui.class", "mState");
			PetriGraphUtils.removeAttribute(node, "ui.class", "mPrimeState");
		}
		for (Sprite sprite : spriteManager.sprites()) {
			if (sprite != null) {
				sprite.setAttribute("ui.class", "edgeLabel");
			}
		}
	}

	/**
	 * @param state
	 * @return
	 */
	private Node drawNode(PetrinetState state) {
		Node currentNode = null;
		if (state != null) {
			currentNode = this.getNode(state.getId());
			if (currentNode == null) {
				currentNode = this.addNode(state.getId());
				currentNode.setAttribute("ui.label", state.getName());
				currentNode.setAttribute("ui.class", "petriState");
				if (listener != null) {
					listener.accessibilityGraphViewChanged(
							"Knoten zum Erreichbarkeitsgraphen hinzugefügt: " + state.getName());
				}
			}
		}
		return currentNode;
	}

	@Override
	void drawGraph() {
		initializeGraph();
		updateGraph();
	}

	/**
	 * @return the shouldHighlightUnbounded
	 */
	private boolean shouldHighlightUnbounded() {
		return shouldHighlightUnbounded;
	}

	/**
	 * @param shouldHighlightUnbounded true if the view should highlight
	 *                                 unboundedness analysis results, false
	 *                                 otherwise
	 */
	private void setShouldHighlightUnbounded(boolean shouldHighlightUnbounded) {
		this.shouldHighlightUnbounded = shouldHighlightUnbounded;
	}

	@Override
	void initializeGraph() {
		// TODO Auto-generated method stub
		super.initializeGraph();
		setShouldHighlightUnbounded(shouldHighlightUnboundedFromSettings);
	}

	/**
	 * @param shouldHighlightUnboundedFromSettings true if the view should highlight
	 *                                             unboundedness analysis results,
	 *                                             false otherwise
	 */
	private void setShouldHighlightUnboundedFromSettings(boolean shouldHighlightUnboundedFromSettings) {
		this.shouldHighlightUnboundedFromSettings = shouldHighlightUnboundedFromSettings;
	}

	/**
	 * set whether view runs and displays unboundedness analysis continuously
	 * 
	 * @param continuousAnalysisActive true if continuousAnalysis should be active,
	 *                                 false otherwise
	 */
	public void setContinuousAnalysisActive(boolean continuousAnalysisActive) {
		setShouldHighlightUnboundedFromSettings(continuousAnalysisActive);
		setShouldHighlightUnbounded(continuousAnalysisActive);
		if (!continuousAnalysisActive) {
			removeAnalysisHighlighting();
		}
	}

	/**
	 * remove current state css highlighting from graph view
	 */
	void clearCurrentStateHighlights() {
		String currentStateId = accessibilityGraph.getCurrentStateId();
		if (currentStateId != null) {

			PetriGraphUtils.removeAttribute(this.getNode(currentStateId), "ui.class", "currentState");
		}
		String currentTransitionId = accessibilityGraph.getCurrentTransitionId();
		if (currentTransitionId != null) {

			PetriGraphUtils.removeAttribute(this.getEdge(currentTransitionId), "ui.class", "currentEdge");
		}
	}

	/**
	 * draws the view's accessibility graph from scratch. Useful if the graph was
	 * autogenerated rather than constructed incrementally.
	 */
	public void drawGraphFromScratch() {
		initializeGraph();
		Collection<PetrinetState> states = accessibilityGraph.getPetrinetStates();
		for (PetrinetState state : states) {
			drawNode(state);
		}
		for (PetrinetState state : states) {
			drawEdges(state);
		}
		Node currentNode = this.getNode(accessibilityGraph.getCurrentStateId());
		highlightCurrents(currentNode);
		highlightAnalysis();
	}

}
