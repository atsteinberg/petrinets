/**
 * 
 */
package model;

/**
 * class that represents a search criterion regarding a petrinet state. I.e. a
 * criterion that must be satisfied by a state for it to count as a valid search
 * result.
 * 
 * @author Alexander Steinberg
 *
 */
public interface SearchCriterion {
	/**
	 * 
	 * @param state the state to check
	 * @return true if the passed state satisfies the search criterion, false
	 *         otherwise.
	 */
	boolean isSatisfiedBy(PetrinetState state);
}
