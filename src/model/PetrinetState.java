/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

//
/**
 * Class that represents the state of a petrinet. In particular, a petrinet
 * state consists of a configuration of tokens, determining how many tokens each
 * place in the petrinet has. Additionally, each petrinet state has a collection
 * of parents: the states that are such that firing a transition will result in
 * this state. And a petrinet state has a collection of children: those states
 * that result from firing a certain (enabled) transition.
 * 
 * @author Alexander Steinberg
 *
 */
public class PetrinetState {

	private static int count = 0;

	private SortedMap<Place, Integer> tokenConfig;

	private Map<PetrinetState, Collection<Transition>> parents;

	private Map<PetrinetState, Collection<Transition>> children;

	private int id;

	/**
	 * constructor for the petrinet state class. Encapsulates the current state of
	 * the petrinet passed.
	 * 
	 * @param petrinet the petrinet this is a state of
	 */
	public PetrinetState(Petrinet petrinet) {
		tokenConfig = new TreeMap<Place, Integer>();
		id = count++;

		for (Place place : petrinet.getPlaces()) {
			tokenConfig.put(place, place.getNumberOfTokens());
		}

		parents = new HashMap<PetrinetState, Collection<Transition>>();
		children = new HashMap<PetrinetState, Collection<Transition>>();
	}

	/**
	 * each state of a given petrinet is defined by the distribution of the tokens
	 * over its places. This function returns that distribution as an (unmodifiable)
	 * map from places to tokens.
	 * 
	 * @return the distribution of the tokens over the places of the petrinet
	 */
	Map<Place, Integer> getTokenConfig() {
		return Collections.unmodifiableMap(tokenConfig);
	}

	/**
	 * Get the name of the petrinet state consisting of a list of tokens for the
	 * places in alphabetical order of their ids. Example: (1|2|0) may represent the
	 * following distribution of tokens: p1: 1; p2: 2; p3:0.;
	 * 
	 * @return the name of the petrinet state
	 */
	public String getName() {
		String result = "(";
		Object[] tokens = tokenConfig.values().toArray();
		for (int i = 0; i < tokens.length; i++) {
			result += tokens[i].toString();
			if (i < tokens.length - 1) {
				result += "|";
			}
		}
		result += ")";
		return result;
	}

	/**
	 * @return the string version of the id
	 */
	public String getId() {
		return "ps" + id;
	}

	@Override
	public String toString() {
		String result = "<";
		for (Entry<Place, Integer> entry : tokenConfig.entrySet()) {
			result += entry.getKey().getId() + ": " + entry.getValue() + ", ";
		}
		result = (result.length() > 1 ? result.substring(0, result.length() - 2) : result) + ">";
		return result;
	}

	/**
	 * @param tokenConfig
	 * @return true if each place has a number of tokens that is smaller than or
	 *         equal to the corresponding one in tokenConfig and at least one is
	 *         strictly smaller; false otherwise
	 */
	private boolean isStrictlySmallerThan(Map<Place, Integer> tokenConfig) {
		boolean isSmallerThanOrEqual = true;
		boolean hasSmallerEntry = false;

		for (Entry<Place, Integer> entry : tokenConfig.entrySet()) {
			Place place = entry.getKey();
			Integer theirNumberOfTokens = entry.getValue();
			Integer ourNumberOfTokens = this.tokenConfig.get(place);
			if (ourNumberOfTokens == null) {
				return false;
			}
			if (ourNumberOfTokens > theirNumberOfTokens) {
				isSmallerThanOrEqual = false;
			} else if (ourNumberOfTokens < theirNumberOfTokens) {
				hasSmallerEntry = true;
			}
		}

		return isSmallerThanOrEqual && hasSmallerEntry;
	}

	/**
	 * determines whether this petrinet state is strictly smaller than the one
	 * passed, in the sense that each place in this state has at most as many tokens
	 * as the same place in the passed state, and at least one has fewer. Assumes
	 * that the passed state is a state of the same petrinet (i.e. in particular:
	 * has the same places).
	 * 
	 * @param state the state to compare
	 * @return true if each place has a number of tokens that is smaller than or
	 *         equal to the corresponding one of the state and at least one is
	 *         strictly smaller; false otherwise
	 */
	public boolean isStrictlySmallerThan(PetrinetState state) {
		return isStrictlySmallerThan(state.getTokenConfig());
	}

	/**
	 * Add a petrinet state as a parent of this state (i.e. as a state of a petrinet
	 * the firing of a transition results in it being in this state)
	 * 
	 * @param parent the parent to add
	 * @param transition the transition that yielded this state from the parent
	 */
	public void addParent(PetrinetState parent, Transition transition) {
		if (parent != null && transition != null) {

			Collection<Transition> transitions = parents.get(parent);
			if (transitions == null) {
				transitions = new HashSet<Transition>();
			}
			transitions.add(transition);
			parents.put(parent, transitions);
		}
	}

	/**
	 * Get the children of this state, i.e. the states that derive from this state
	 * by the firing of an enabled transition.
	 * 
	 * @return the children
	 */
	public Collection<PetrinetState> getChildren() {
		return Collections.unmodifiableCollection(children.keySet());
	}

	/**
	 * Add a state as a child of this state (i.e. as a state that results from
	 * firing a transition of the petrinet that is in this state).
	 * 
	 * @param child the child to add
	 * @param transition the transition that yields the child from this state
	 */
	public void addChild(PetrinetState child, Transition transition) {
		if (child != null && transition != null) {

			Collection<Transition> transitions = children.get(child);
			if (transitions == null) {
				transitions = new HashSet<Transition>();
			}
			transitions.add(transition);
			children.put(child, transitions);
		}
	}

	/**
	 * @return the parents
	 */
	private Collection<PetrinetState> getParents() {
		return parents.keySet();
	}

	/**
	 * Get an array of parent states of this state, i.e. an array of states such
	 * that: if the petrinet is in them, firing some (enabled) transition results in
	 * the petrinet being in this state.
	 * 
	 * @return an array of parent states of this state
	 */
	PetrinetState[] getParentArray() {
		Collection<PetrinetState> parents = getParents();
		parents.removeIf(Objects::isNull);
		return parents.toArray(new PetrinetState[parents.size()]);
	}

	@Override
	public int hashCode() {
		// individuate a state by the distribution of tokens over places
		return Objects.hash(tokenConfig);
	}

	@Override
	public boolean equals(Object obj) {
		// individuate a state by the distribution of tokens over places
		if (this == obj)
			return true;
		if (!(obj instanceof PetrinetState))
			return false;
		PetrinetState other = (PetrinetState) obj;
		return Objects.equals(tokenConfig, other.tokenConfig);
	}

	/**
	 * get all transitions firing of which resulted in this state
	 * 
	 * @return the transitions that resulted in the state
	 */
	public Transition[] getTransitions() {
		List<Transition> transitions = new ArrayList<Transition>();
		for (Collection<Transition> currentTransitions : parents.values()) {
			transitions.addAll(currentTransitions);
		}
		return transitions.toArray(new Transition[transitions.size()]);
	}

	private Transition[] getTransitions(PetrinetState parent) {
		Collection<Transition> transitions = parents.get(parent);
		if (transitions != null) {
			return transitions.toArray(new Transition[transitions.size()]);
		}
		return null;
	}

	/**
	 * @param transitionId
	 * @return
	 */
	private PetrinetState getParentByTransitionId(String transitionId) {
		PetrinetState parent = null;
		for (Entry<PetrinetState, Collection<Transition>> entry : parents.entrySet()) {
			Collection<Transition> transitions = entry.getValue();
			for (Transition transition : transitions) {
				if (transition.getId().equals(transitionId)) {
					parent = entry.getKey();
				}
			}
		}
		return parent;
	}

	/**
	 * construct an id that uniquely determines an edge between some parent of this
	 * state and this state, such that this state resulted from the parent by the
	 * transition whose id was passed. The edge id has the form:
	 * [t1]:[sourceId]->[targetId].
	 * 
	 * @param transitionId the id of a transition that resulted in this state
	 * @return the edge info string
	 */
	public String getEdgeInfo(String transitionId) {
		PetrinetState parent = getParentByTransitionId(transitionId);
		if (parent != null) {
			String sourceId = parent.getId();
			return transitionId + ":" + sourceId + "->" + getId();
		}
		return null;
	}

	/**
	 * get the id of a parent state of this state that is such that: firing the
	 * transition with the passed id results in this state.
	 * 
	 * @param transitionId the id of a transition that resulted in this state
	 * @return the id of a parent state that resulted in this by the transition
	 */
	public String getParentIdByTransitionId(String transitionId) {
		// TODO Auto-generated method stub
		PetrinetState parent = getParentByTransitionId(transitionId);
		if (parent != null) {
			return parent.getId();
		}
		return null;
	}

	/**
	 * get one of the transitions that are such that: the passed state results in
	 * this state via the transition passed.
	 * 
	 * @param parent a parent of this state
	 * @return a transition that results in this state
	 */
	Transition getParentTransition(PetrinetState parent) {
		Transition[] transitions = getTransitions(parent);
		if (transitions != null && transitions.length > 0) {
			return transitions[0];
		}
		return null;
	}
}
