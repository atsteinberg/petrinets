/**
 * 
 */
package model;

import java.awt.Point;

/**
 * Abstract class whose subtypes are Place and Transition, the elements of a
 * petrinet.
 * 
 * @author Alexander Steinberg
 *
 */
public abstract class PetriElement {
	private String id;
	private String name;
	private Point position;

	/**
	 * Constructor for the PetriElement class. Creates a new PetriElement with an id
	 * passed to the function.
	 * 
	 * @param id the id of the element
	 */
	PetriElement(String id) {
		super();
		this.id = id;
	}

	/**
	 * @return the id of the element
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @return the name of the element
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the position of the element
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	void setPosition(Point position) {
		this.position = position;
	}

	/**
	 * Gets the display name of the element. An elements display name is constructed
	 * from its id and name according to the following schema: [id] name
	 * 
	 * @return the display name of the element
	 */
	public String getDisplayName() {
		return "[" + getId() + "] " + getName();
	}

}
