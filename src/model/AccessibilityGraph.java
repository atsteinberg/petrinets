/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Class that represents the (partial) accessibility graph of a petrinet.
 * Provides functionality to build the graph both manually and automatically,
 * and to analyse the graph's petrinet for unboundedness.
 * 
 * @author Alexander Steinberg
 *
 */
public class AccessibilityGraph {

	private Petrinet petrinet;
	private PetrinetState initialState;
	private PetrinetState currentState;
	private Transition currentTransition;
	private PetrinetState mState;
	private PetrinetState mPrimeState;
	private List<PetrinetState> pathTomPrime;

	/**
	 * constructor for the accessibility graph. Sets the graph's petrinet, and
	 * initialises the graph with the current state of the petrinet.
	 * 
	 * @param petrinet the graph's petrinet in its initial state
	 */
	public AccessibilityGraph(Petrinet petrinet) {
		this.petrinet = petrinet;
		pathTomPrime = new ArrayList<PetrinetState>();
		initializeGraph();
	}

	/**
	 * 
	 */
	private void initializeGraph() {
		initialState = new PetrinetState(petrinet);
		currentState = initialState;
		clearCurrentmAndmPrime();
	}

	/**
	 * Adds the current state of the petrinet to the accessibility graph. The
	 * transition provided must be the one that triggered the petrinet's update from
	 * the previous state.
	 * 
	 * @param transition the transition that triggered the update from previous to
	 *                   current state of the graph's petrinet
	 */
	public void addState(Transition transition) {
		currentTransition = transition;
		PetrinetState stateToAdd;
		final PetrinetState newState = new PetrinetState(petrinet);
		PetrinetState existingState = findState((state) -> state.equals(newState));
		if (existingState != null) {
			stateToAdd = existingState;
		} else {
			stateToAdd = newState;
		}
		stateToAdd.addParent(currentState, transition);
		currentState.addChild(stateToAdd, transition);
		currentState = stateToAdd;
	}

	/**
	 * @param criterion
	 * @return the first state found that satisfies the criterion
	 */
	private PetrinetState findState(SearchCriterion criterion) {
		return findState(criterion, initialState, new HashSet<PetrinetState>());
	}

	private PetrinetState findParent(SearchCriterion criterion, PetrinetState state) {
		return findParent(criterion, state, new HashSet<PetrinetState>());
	}

	/**
	 * 
	 * @param criterion
	 * @return the states that satisfy the criterion
	 */
	private Collection<PetrinetState> findStates(SearchCriterion criterion) {
		return findStates(criterion, initialState, new HashSet<PetrinetState>(), new HashSet<PetrinetState>());
	}

	/**
	 * @param criterion
	 * @param root
	 * @param alreadyFound
	 * @return the collection of states that satisfy the criterion starting from
	 *         root
	 * 
	 */
	private Collection<PetrinetState> findStates(SearchCriterion criterion, PetrinetState root,
			Collection<PetrinetState> alreadyFound, Collection<PetrinetState> alreadyVisited) {
		if (!alreadyVisited.contains(root)) {
			if (criterion == null || criterion.isSatisfiedBy(root)) {
				// add a state to the list of found states if it satisfies the criterion. Null
				// criteria are vacuously satisfied by every state.
				alreadyFound.add(root);
			}

			for (PetrinetState child : root.getChildren()) {
				// check all the state's children
				alreadyVisited.add(root);
				alreadyFound = findStates(criterion, child, alreadyFound, alreadyVisited);
				alreadyVisited.remove(root);
			}
		}
		return alreadyFound;
	}

	/**
	 * @param criterion
	 * @param root
	 * @return the first state that satisfies the criterion starting from root
	 */
	private PetrinetState findState(SearchCriterion criterion, PetrinetState root,
			Collection<PetrinetState> alreadyVisited) {
		PetrinetState foundState = null;
		if (!alreadyVisited.contains(root)) {
			if (criterion == null || criterion.isSatisfiedBy(root)) {
				// if the state satisfies the criterion return it
				return root;
			}
			for (PetrinetState child : root.getChildren()) {
				alreadyVisited.add(root);
				foundState = findState(criterion, child, alreadyVisited);
				alreadyVisited.remove(root);
				if (foundState != null) {
					break;
				}
			}
		}
		return foundState;
	}

	/**
	 * @param criterion
	 * @param root
	 * @return the first parent that satisfies the criterion starting from state
	 */
	private PetrinetState findParent(SearchCriterion criterion, PetrinetState state,
			Collection<PetrinetState> alreadyVisited) {
		PetrinetState foundState = null;
		if (!alreadyVisited.contains(state)) {

			if (criterion == null || criterion.isSatisfiedBy(state)) {
				return state;
			}
			for (PetrinetState parent : state.getParentArray()) {
				alreadyVisited.add(state);
				foundState = findParent(criterion, parent, alreadyVisited);
				alreadyVisited.remove(state);
				if (foundState != null) {
					break;
				}
			}
		}
		return foundState;
	}

	/**
	 * get the state the petrinet is currently in.
	 * 
	 * @return the most recent petrinet state
	 */
	public PetrinetState getCurrentState() {
		return currentState;
	}

	/**
	 * set the state the petrinet is currently in.
	 * 
	 * @param currentState the currentState to set
	 */
	public void setCurrentState(PetrinetState currentState) {
		this.currentState = currentState;
		currentTransition = null;
	}

	/**
	 * clear the accessibility graph and set petrinet to initial state
	 */
	public void clear() {
		initializeGraph();
	}

	/**
	 * returns the petrinet state within the current accessibility graph that has
	 * the id, if it exists. Returns null otherwise.
	 * 
	 * @param id the id of the petrinet state to get
	 * @return the petrinet state with the id
	 */
	public PetrinetState getPetrinetStateById(String id) {
		return findState(state -> state.getId().equals(id));
	}

	/**
	 * gets a message that informs about the last discovered m and m' states.
	 * Returns null if none have been discovered.
	 * 
	 * @return the string message that informs about the last discovered m and m'
	 *         states, null if there are none
	 */
	public String getCurrentMAndMPrimeMessage() {
		if (isUnbounded()) {
			return "M und m' wurden bei der Analyse entdeckt:\n\tm: " + getmStateName() + "; m': "
					+ getmPrimeStateName();

		}
		return null;
	}

	/**
	 * checks whether there are m and m' in the current (partial) accessibility
	 * graph. If so, it sets the graph's m and m' state to them. Method is meant for
	 * on the fly analysis.
	 */
	public void runAnalysis() {
		PetrinetState currentState = getCurrentState();
		PetrinetState mState = findParent((PetrinetState ancestor) -> ancestor.isStrictlySmallerThan(currentState),
				currentState);
		if (mState != null) {
			this.mState = mState;
			mPrimeState = currentState;
			pathTomPrime.clear();
		}
	}
//

	/**
	 * Auto generates a (potentially partial) accessibility graph If the petrinet is
	 * bounded: generates the complete graph If the petrinet is unbounded: generates
	 * a partial accessibility graph with m and m'.
	 */

	public void autoGenerate() {
		reInitializePetrinet();
		clear();
		autoGenerate(new ArrayList<PetrinetState>());
		reInitializePetrinet();
	}

	private void reInitializePetrinet() {
		PetrinetState initialState = getInitialState();
		if (initialState != null) {
			petrinet.setState(initialState);
			setCurrentState(initialState);
		}
	}

	private void autoGenerate(List<PetrinetState> alreadyVisited) {
		if (mState != null) {
			// nothing left to do: unboundedness has been discovered
			return;
		}
		PetrinetState startingState = getCurrentState();
		if (alreadyVisited.contains(startingState)) {
			// we're on a loop; break out
			return;
		}

		for (PetrinetState ancestor : alreadyVisited) {
			// check if some ancestor is strictly smaller than current state. If so: graph
			// is unbounded
			if (ancestor.isStrictlySmallerThan(startingState)) {
				pathTomPrime = new ArrayList<PetrinetState>(alreadyVisited);
				pathTomPrime.add(startingState);
				mPrimeState = currentState;
				mState = ancestor;
				return;
			}
		}
		for (Transition transition : petrinet.getEnabledTransitions()) {
			if (isUnbounded()) {
				// leave loop when unboundedness has already been detected
				return;
			}
			// fire the next available transition, record that current state has been
			// visited, and autogenerate from that state of the petrinet
			transition.fire();
			addState(transition);
			alreadyVisited.add(startingState);
			autoGenerate(alreadyVisited);
			// roll back to before transition fire
			petrinet.setState(startingState);
			setCurrentState(startingState);
			alreadyVisited.remove(startingState);
		}
	}

	/**
	 * If an analysis has revealed that the graph's petrinet is unbounded, this
	 * function returns true. Returns false otherwise.
	 * 
	 * @return true if unboundedness has been detected for the petrinet, false
	 *         otherwise
	 */
	public boolean isUnbounded() {
		return mPrimeState != null;
	}

	/**
	 * The function returns the initial state of the petrinet. This is either the
	 * state as it was loaded from file or the state set by the user when manually
	 * modifying the loaded petrinet.
	 * 
	 * @return the accessibility graph's initial state
	 */
	public PetrinetState getInitialState() {
		return initialState;
	}

	/**
	 * The function returns the id of the initial state of the petrinet. The intial
	 * state is either the state as it was loaded from file or the state set by the
	 * user when manually modifying the loaded petrinet.
	 * 
	 * @return the id of the accessibility graph's initial state
	 */
	public String getInitialStateId() {
		PetrinetState initialState = getInitialState();
		if (initialState == null) {
			return null;
		}
		return initialState.getId();
	}

	/**
	 * The functino returns the id of the currently active petrinet state. The
	 * currently active petrinet state is either a) the initial state, b) the last
	 * state transitioned to or c) the state manually set to active by the user.
	 * 
	 * @return the id of the currently active petrinet state
	 */
	public String getCurrentStateId() {
		PetrinetState currentState = getCurrentState();
		if (currentState == null) {
			return null;
		}
		return currentState.getId();
	}

	/**
	 * @return the collection of petrinet states in the graph
	 */
	public Collection<PetrinetState> getPetrinetStates() {
		return Collections.unmodifiableCollection(findStates(null));
	}

	/**
	 * @return the id of the currently found m state (a strictly smaller ancestor to
	 *         some state in the graph)
	 */
	public String getmStateId() {
		PetrinetState mState = getmState();
		if (mState == null) {
			return null;
		}
		return mState.getId();
	}

	/**
	 * @return the currently found m state (a strictly smaller ancestor to some
	 *         state in the graph)
	 */
	private PetrinetState getmState() {
		return mState;
	}

	/**
	 * @return the id of the currently found m' state (strictly greater than one of
	 *         its ancestors)
	 */
	public String getmPrimeStateId() {
		PetrinetState mPrimeState = getmPrimeState();
		if (mPrimeState == null) {
			return null;
		}
		return mPrimeState.getId();
	}

	/**
	 * @return the currently found m' state (strictly greater than one of its
	 *         ancestors in the graph)
	 */
	private PetrinetState getmPrimeState() {
		return mPrimeState;
	}

	/**
	 * Constructs ids for all edges on the path from the initial state to the found
	 * mPrime state, if any. The ids are constructed via the getEdgeInfo function.
	 * 
	 * @return the list of ids of edges on the path from the initial state to the
	 *         currently found m' state
	 */
	public Collection<String> getPathIds() {
		Collection<String> pathIds = new HashSet<String>();
		if (isUnbounded()) {
			List<PetrinetState> path = pathTomPrime;
			for (int i = 0; i < path.size() - 1; i += 1) {
				PetrinetState source = path.get(i);
				PetrinetState target = path.get(i + 1);
				Transition transition = target.getParentTransition(source);
				if (transition != null) {
					String pathId = target.getEdgeInfo(transition.getId());
					pathIds.add(pathId);
				}
			}
		}
		return pathIds;
	}

	/**
	 * @return the number of petrinet states in the graph
	 */
	public int getPetrinetStateCount() {
		Collection<PetrinetState> petrinetStates = getPetrinetStates();
		if (petrinetStates == null) {
			return 0;
		}
		return petrinetStates.size();
	}

	/**
	 * @return the number of edges in the graph
	 */
	public int getEdgeCount() {
		int edgeCount = 0;
		for (PetrinetState state : getPetrinetStates()) {
			edgeCount += state.getTransitions().length;
		}
		return edgeCount;
	}

	/**
	 * @return the length of the path from the initial state to the currently found
	 *         m' state
	 */
	public int getPathLength() {
		List<PetrinetState> path = pathTomPrime;
		if (path == null) {
			return 0;
		}
		return path.size() - 1;
	}

	/**
	 * If an unboundedness has been detected, this function returns a string
	 * representation of the path from the initial state to the found m' state. The
	 * representation has the form '([t1], ...[tn])'.
	 * 
	 * @return a string that represents the path from the initial petrinet state to
	 *         the currently found m' state
	 */
	public String getPathString() {
		if (isUnbounded()) {
			String result = "(";
			List<PetrinetState> path = pathTomPrime;
			for (int i = 0; i < path.size() - 1; i += 1) {
				PetrinetState source = path.get(i);
				PetrinetState target = path.get(i + 1);
				Transition transition = target.getParentTransition(source);
				if (transition != null) {
					result += transition.getId() + ",";
				}
			}
			result = result.substring(0, result.length() - 1);
			result += ")";
			return result;
		}
		return "";
	}

	/**
	 * @return the name of the currently found m state
	 */
	public String getmStateName() {
		PetrinetState mState = getmState();
		if (mState == null) {
			return "";
		}
		return mState.getName();
	}

	/**
	 * @return the name of the currently found m' state
	 */
	public String getmPrimeStateName() {
		PetrinetState mPrimeState = getmPrimeState();
		if (mPrimeState == null) {
			return "";
		}
		return mPrimeState.getName();
	}

	/**
	 * clears previous unboundedness analysis results. Allows a new analysis to
	 * start from scratch.
	 */
	private void clearCurrentmAndmPrime() {
		mState = null;
		mPrimeState = null;
		pathTomPrime.clear();
	}

	/**
	 * The current transition is the transition that triggered the current state of
	 * the petrinet. If a user has manually set the petrinet to a certain state, the
	 * current transition is `null`.
	 * 
	 * @return the current transition
	 */
	private Transition getCurrentTransition() {
		return currentTransition;
	}

	/**
	 * The id is constructed via the getEdgeInfo function.
	 * 
	 * @return a unique id for the current edge
	 */
	public String getCurrentEdgeInfo() {
		PetrinetState currentState = getCurrentState();
		if (currentState != null && currentTransition != null) {
			return currentState.getEdgeInfo(currentTransition.getId());
		}
		return null;
	}

	/**
	 * get the id of the current transition, if any. The current transition is the
	 * transition that triggered the current state of the petrinet. If a user has
	 * manually set the petrinet to a certain state, the current transition is
	 * `null`.
	 * 
	 * @return the id of the current transition, null if there is none.
	 */
	public String getCurrentTransitionId() {
		Transition currentTransition = getCurrentTransition();
		if (currentTransition != null) {
			return currentTransition.getId();
		}
		return null;
	}

}
