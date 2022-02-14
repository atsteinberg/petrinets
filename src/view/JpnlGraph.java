/**
 * 
 */
package view;

import java.util.Collection;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.ui.spriteManager.Sprite;

import model.PetriElement;
import model.Petrinet;
import model.Place;
import model.Transition;
import model.TransitionRelation;

/**
 * class for displaying a petrinet.
 * 
 * @author Alexander Steinberg
 *
 */
class JpnlGraph extends PetriGraph {

	private Petrinet petrinet;

	/**
	 * constructor for the JpnlGraph class to display a petrinet.
	 * 
	 * @param id       the jpnl graph's id
	 * @param petrinet the petrinet that is displayed by the jpnl graph
	 */
	JpnlGraph(String id, Petrinet petrinet) {
		super(id);
		this.petrinet = petrinet;

		drawGraph();
	}

	/**
	 * 
	 */
	@Override
	void drawGraph() {
		initializeGraph();
		Collection<Place> places = petrinet.getPlaces();
		Collection<Transition> transitions = petrinet.getTransitions();
		for (Place place : places) {
			Node node = prepareNode(place);
			node.setAttribute("ui.class", "place");
			addTokens(node, place);
		}
		for (Transition transition : transitions) {
			Node node = prepareNode(transition);
			node.setAttribute("ui.class", "transition");
			highlightLiveTransition(node, transition);
			Collection<TransitionRelation> sources = transition.getSources();
			for (TransitionRelation source : sources) {
				Edge edge = this.addEdge(source.getId(), this.getNode(source.getBearer().getId()), node, true);
				prepareEdge(edge, source);
			}
			Collection<TransitionRelation> targets = transition.getTargets();
			for (TransitionRelation target : targets) {
				Edge edge = this.addEdge(target.getId(), node, this.getNode(target.getBearer().getId()), true);
				prepareEdge(edge, target);
			}
		}
	}

	/**
	 * @param node
	 * @param transition
	 */
	private void highlightLiveTransition(Node node, Transition transition) {
		if (transition.isEnabled()) {
			PetriGraphUtils.addAttribute(node, "ui.class", "live");
		} else {
			PetriGraphUtils.removeAttribute(node, "ui.class", "live");
		}
	}

	private Node prepareNode(PetriElement element) {
		Node node = this.addNode(element.getId());
		node.setAttribute("ui.label", element.getDisplayName());
		node.setAttribute("xy", element.getPosition().getX(), -1 * element.getPosition().getY());
		return node;
	}

	private void prepareEdge(Edge edge, TransitionRelation relation) {
		edge.setAttribute("ui.label", relation.getDisplayName());
	}

	private void addTokens(Node node, Place place) {
		int numberOfTokens = place.getNumberOfTokens();
		if (numberOfTokens > 0) {
			String label = numberOfTokens > 9 ? ">9" : String.valueOf(numberOfTokens);
			Sprite sprite = spriteManager.addSprite(place.getId());
			sprite.attachToNode(node.getId());
			sprite.setAttribute("ui.label", label);
			sprite.setAttribute("ui.class", "tokenMarker");
		} else {
			spriteManager.removeSprite(place.getId());
		}

	}

	/**
	 * update jpnl graph to reflect the current state of the petrinet.
	 */
	void updateGraph() {
		Collection<Place> places = petrinet.getPlaces();
		Collection<Transition> transitions = petrinet.getTransitions();
		for (Place place : places) {
			Node node = this.getNode(place.getId());
			addTokens(node, place);
			node.setAttribute("ui.label", place.getDisplayName());
		}
		for (Transition transition : transitions) {
			highlightLiveTransition(this.getNode(transition.getId()), transition);
		}
	}

	/**
	 * update css class of the graph's nodes to highlight the currently selected
	 * place. Also removes selection highlighting from all place nodes that are not
	 * currently selected.
	 */
	void updateSelection() {
		Place selectedPlace = petrinet.getSelectedPlace();
		for (Node node : this.nodeArray) {
			if (node != null) {
				if (selectedPlace != null && node.getId().equals(selectedPlace.getId())) {
					PetriGraphUtils.addAttribute(node, "ui.class", "selected");
				} else {
					PetriGraphUtils.removeAttribute(node, "ui.class", "selected");
				}
			}
		}
	}

}
