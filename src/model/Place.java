/**
 * 
 */
package model;

/**
 * Class that represents a place element in a petrinet. Places
 * characteristically have 0 or more tokens.
 * 
 * @author Alexander Steinberg
 *
 */
public class Place extends PetriElement implements Comparable<Place> {
	private int numberOfTokens;

	/**
	 * @param id the id of the place
	 */
	Place(String id) {
		super(id);
	}

	/**
	 * @return the number of tokens this place has (0 >= number of tokens)
	 */
	public int getNumberOfTokens() {
		return numberOfTokens;
	}

	/**
	 * @param numberOfTokens the numberOfTokens to set
	 */
	void setNumberOfTokens(int numberOfTokens) {
		this.numberOfTokens = numberOfTokens;
	}

	@Override
	public int compareTo(Place p) {
		return getId().compareTo(p.getId());
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return super.getDisplayName() + " <" + getNumberOfTokens() + ">";
	}

	/**
	 * add one to the number of tokens
	 */
	public void addToken() {
		numberOfTokens += 1;
	}

	/**
	 * subtract one from the number of tokens. If the number of tokens is 0, the
	 * number of tokens will not change.
	 */
	public void subtractToken() {
		if (numberOfTokens > 0) {
			numberOfTokens -= 1;
		}
	}

}
