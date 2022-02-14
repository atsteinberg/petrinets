/**
 * 
 */
package model;

import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

/**
 * class that represents a transition element of a petrinet. Transitions have
 * places as inputs and outputs. They are enabled if all input places have at
 * least one token. When they are fired, each input place loses exactly one
 * token whereas each output place gains one. This implementation tracks inputs
 * and outputs via TransitionRelations. TransitionRelations are places-cum-ids.
 * This is one (retrospectively: maybe not the most straightforward) way of
 * giving unique names to edges between places and transitions.
 * 
 * @author Alexander Steinberg
 *
 */
public class Transition extends PetriElement {

	private Collection<TransitionRelation> sources;
	private Collection<TransitionRelation> targets;

	/**
	 * constructor for the transition class. Takes an id and initializes the
	 * transition to receive sources and targets.
	 * 
	 * @param id the id of the transition
	 */
	Transition(String id) {
		super(id);
		sources = new TreeSet<TransitionRelation>();
		targets = new TreeSet<TransitionRelation>();
	}

	/**
	 * Create a source and add it to the transition.
	 * 
	 * @param id    the id of the source
	 * @param place the input place
	 */
	public void addSource(String id, Place place) {
		TransitionRelation newSource = new TransitionRelation(id, place);
		sources.add(newSource);
	}

	/**
	 * Create a target and add it to the transition
	 * 
	 * @param id    the id of the target
	 * @param place the output place
	 */
	public void addTarget(String id, Place place) {
		TransitionRelation newTarget = new TransitionRelation(id, place);
		targets.add(newTarget);
	}

	/**
	 * @return the sources of the transition
	 */
	public Collection<TransitionRelation> getSources() {
		return Collections.unmodifiableCollection(sources);
	}

	/**
	 * @return the targets of the transition
	 */
	public Collection<TransitionRelation> getTargets() {
		return Collections.unmodifiableCollection(targets);
	}

	/**
	 * A transition is enabled just in case all input places have at least one
	 * token.
	 * 
	 * @return true if enabled, false otherwise
	 */
	public boolean isEnabled() {
		for (TransitionRelation source : sources) {
			if (source.getBearer().getNumberOfTokens() <= 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * fire the transition to modify the petrinet. If the transition is not enabled,
	 * calling fire does not have any effect.
	 */
	public void fire() {
		if (isEnabled()) {
			for (TransitionRelation source : sources) {
				Place bearer = source.getBearer();
				bearer.setNumberOfTokens(bearer.getNumberOfTokens() - 1);
			}
			for (TransitionRelation target : targets) {
				Place bearer = target.getBearer();
				bearer.setNumberOfTokens(bearer.getNumberOfTokens() + 1);
			}
		}
	}

}
