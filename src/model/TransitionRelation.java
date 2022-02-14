/**
 * 
 */
package model;

/**
 * class that represents a transition relation: a place that acts as the source
 * or target of the relation to a transition, and an id.
 * 
 * @author Alexander Steinberg
 *
 */
public class TransitionRelation implements Comparable<TransitionRelation> {
	private Place bearer;
	private String id;

	/**
	 * constructor for the class TransitionRelation.
	 * 
	 * @param id     the id of the transition relation
	 * @param bearer the place that stands in the relation with a transition
	 */
	public TransitionRelation(String id, Place bearer) {
		super();
		this.bearer = bearer;
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @return the name to display in the petrinet graph. The display name has the
	 *         form: [id].
	 */
	public String getDisplayName() {
		return "[" + getId() + "]";
	}

	/**
	 * Each transition relation consists of a relation of a place to a transition.
	 * The bearer of a transition relation is the place that enters in the relation.
	 * 
	 * @return the bearer of the transition relation
	 */
	public Place getBearer() {
		return bearer;
	}

	@Override
	public int compareTo(TransitionRelation o) {
		return this.id.compareTo(((TransitionRelation) o).id);
	}

}
