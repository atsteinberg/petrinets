/**
 * 
 */
package model;

import java.awt.Point;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Class that represents a petrinet. A petrinet is a kind of state model whose
 * elements are places and transitions. With the firing of a transition a state
 * change is (typically) triggered in the petrinet. Such a state change consists
 * in a different distribution of tokens across the petrinet's places.
 * 
 * @author Alexander Steinberg
 *
 */
public class Petrinet implements Comparable<Petrinet> {
	private AbstractMap<String, Place> places;
	private AbstractMap<String, Transition> transitions;
	private static int count;
	private int id;
	private Place selectedPlace;

	/**
	 * constructor for the Petrinet class. A petrinet is a kind of state model whose
	 * elements are places and transitions. With the firing of a transition a state
	 * change is (typically) triggered in the petrinet. Such a state change consists
	 * in a different distribution of tokens across the petrinet's places.
	 */
	public Petrinet() {
		super();
		places = new HashMap<String, Place>();
		transitions = new HashMap<String, Transition>();
		id = count++;
	}

	/**
	 * @return the petrinet's id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Add the passed place to the petrinet. If the petrinet already contains a
	 * place with the same id, there will be no change in the petrinet.
	 * 
	 * @param place to add
	 */
	void addPlace(Place place) {
		if (!places.containsKey(place.getId())) {
			places.put(place.getId(), place);
		}
	}

	/**
	 * Create a place with the id and add it to the petrinet. If the petrinet
	 * already contains a place with the id, there will be no change in the
	 * petrinet.
	 * 
	 * @param id the id of the place to add
	 */
	void addPlace(String id) {
		Place place = new Place(id);
		addPlace(place);
	}

	/**
	 * Convenience function that adds several places to the petrinet via
	 * [{@link #addPlace(Place) addPlace}
	 * 
	 * @param places the places to add
	 */

	void addPlaces(Place[] places) {
		for (Place place : places) {
			addPlace(place);
		}
	}

	/**
	 * Convenience function that adds several places to the petrinet via
	 * [{@link #addPlace(String) addPlace}
	 * 
	 * @param ids the ids of the places to add
	 */
	void addPlaces(String[] ids) {
		for (String id : ids) {
			addPlace(id);
		}
	}

	/**
	 * Add the passed transition to the petrinet. If the petrinet already contains a
	 * transition with the same id, the petrinet is not changed.
	 * 
	 * @param transition the transition to add
	 */
	void addTransition(Transition transition) {
		if (!transitions.containsKey(transition.getId())) {
			transitions.put(transition.getId(), transition);
		}
	}

	/**
	 * Create a transition with the passed id and add ir to the petrinet. If the
	 * petrinet already contains a transition with the id, the petrinet is not
	 * changed.
	 * 
	 * @param id the id of the transition to add
	 */
	void addTransition(String id) {
		Transition transition = new Transition(id);
		addTransition(transition);
	}

	/**
	 * Convenience function that adds several transitions to the petrinet via
	 * [{@link #addTransition(Transition) addTransition}
	 * 
	 * @param transitions the transitions to add
	 */
	void addTransitions(Transition[] transitions) {
		for (Transition transition : transitions) {
			addTransition(transition);
		}
	}

	/**
	 * Convenience function that adds several transitions to the petrinet via
	 * [{@link #addTransition(String) addTransition}
	 * 
	 * @param ids the ids of the transitions to add
	 */
	void addTransitions(String[] ids) {
		for (String id : ids) {
			addTransition(id);
		}
	}

	/**
	 * Set the number of tokens for the place with the passed id. If the petrinet
	 * does not contain a place with that id, the petrinet is not changed.
	 * 
	 * @param id             the id of the place
	 * @param numberOfTokens the number of tokens to set
	 */
	void setTokens(String id, int numberOfTokens) {
		Place placeToUpdate = places.get(id);
		if (placeToUpdate != null) {
			(placeToUpdate).setNumberOfTokens(numberOfTokens);
		}
	}

	/**
	 * 
	 * @param id the id of the element to get
	 * @return the petrinet element (place or transition) with the id, if it exists;
	 *         null otherwise
	 */

	PetriElement getPetriElement(String id) {
		if (places.containsKey(id)) {
			return places.get(id);
		}
		if (transitions.containsKey(id)) {
			return transitions.get(id);
		}
		return null;
	}

	/**
	 * Sets the name of the element with the id. If the petrinet does not contain an
	 * element with that id, the petrinet is not changed.
	 * 
	 * @param id   of the element
	 * @param name to set
	 */
	void setName(String id, String name) {
		PetriElement elementToUpdate = getPetriElement(id);
		if (elementToUpdate != null) {
			elementToUpdate.setName(name);
		}
	}

	/**
	 * Sets the position of the element with the id. If the petrinet does not
	 * contain an element with that id, the petrinet is not changed.
	 *
	 * @param id the id of the element
	 * @param x  the horizontal position
	 * @param y  the vertical position
	 */
	void setPosition(String id, int x, int y) {
		PetriElement elementToUpdate = getPetriElement(id);
		if (elementToUpdate != null) {
			Point position = new Point(x, y);
			elementToUpdate.setPosition(position);
		}
	}

	/**
	 * 
	 * @return the collection of places in the petrinet
	 */
	public Collection<Place> getPlaces() {
		return Collections.unmodifiableCollection(places.values());
	}

	/**
	 * 
	 * @return the collection of transitions in the petrinet
	 */

	public Collection<Transition> getTransitions() {
		return Collections.unmodifiableCollection(transitions.values());
	}

	@Override
	public String toString() {
		List<Place> placesList = new ArrayList<Place>(places.values());
		placesList.sort((n1, n2) -> n1.getName().compareTo(n2.getName()));
		String outputString = "(";
		for (int i = 0; i < placesList.size(); i++) {
			Place currentPlace = placesList.get(i);
			outputString += currentPlace.getNumberOfTokens();
			if (i < placesList.size() - 1) {
				outputString += "|";
			}
		}
		outputString += ")";
		return outputString;
	}

	/**
	 * removes all places, transitions and arcs from the petrinet.
	 */
	public void clear() {
		places.clear();
		transitions.clear();
		setSelectedPlace(null);
	}

	/**
	 * @param id the id of the transition to get
	 * @return the transition with the passed id
	 */
	public Transition getTransitionById(String id) {
		return transitions.get(id);
	}

	@Override
	public int compareTo(Petrinet o) {
		return getId() - ((Petrinet) o).getId();
	}

	/**
	 * Sets the petrinet to the passed state. In particular, the configuration of
	 * tokens is taken from the passed petrinet state. Assumes that the petrinet
	 * state passed is a state of this petrinet.
	 * 
	 * @param petrinetState the state to set the petrinet to
	 */
	public void setState(PetrinetState petrinetState) {
		Map<Place, Integer> tokenConfig = petrinetState.getTokenConfig();
		tokenConfig.forEach((place, numberOfTokens) -> {
			Place currentPlace = places.get(place.getId());
			currentPlace.setNumberOfTokens(numberOfTokens);
		});
	}

	/**
	 * @param id the id of the place to get
	 * @return the place with the id, if it exists; null otherwise
	 */
	public Place getPlaceById(String id) {
		return places.get(id);
	}

	/**
	 * Sets the selected place of the petrinet to the place with the passed id. If
	 * there is no place with the id or if the place with the id is already
	 * selected, it sets the selected place to null.
	 * 
	 * @param id the id of the place to toggle
	 * @return the new selected place if one is set, null otherwise
	 */
	public Place togglePlaceSelected(String id) {
		if (selectedPlace == null || !selectedPlace.getId().equals(id)) {
			selectedPlace = places.get(id);
		} else {
			selectedPlace = null;
		}
		return selectedPlace;
	}

	/**
	 * @return the selected place
	 */
	public Place getSelectedPlace() {
		return selectedPlace;
	}

	/**
	 * @param place
	 */
	private void setSelectedPlace(Place place) {
		selectedPlace = place;
	}

	/**
	 * A transition is enabled when all its input places have at least one token.
	 * Gets a collection of all enabled transitions in the petrinet.
	 * 
	 * @return the collection of currently enabled transitions
	 */
	public Collection<Transition> getEnabledTransitions() {
		Collection<Transition> enabledTransitions = new HashSet<Transition>();
		for (Transition transition : getTransitions()) {
			if (transition.isEnabled()) {
				enabledTransitions.add(transition);
			}
		}
		return enabledTransitions;
	}

}
