/**
 * 
 */
package model;

import java.io.File;

import propra.pnml.PNMLWopedParser;

/**
 * pnml parser for the Petrinet class. Extends PNMLWopedParser. Populates a
 * petrinet with the nodes and edges described in a pnml file.
 * 
 * @author Alexander Steinberg
 *
 */
public class MyParser extends PNMLWopedParser {
	private Petrinet petrinet;

	/**
	 * constructor for the MyParser class. Accepts a file and a petrinet and
	 * populates the petrinet with the nodes and edges described in the file.
	 * 
	 * @param pnml     the file that describes the petrinet
	 * @param petrinet the petrinet to which elements should be added
	 */
	public MyParser(File pnml, Petrinet petrinet) {
		super(pnml);
		this.petrinet = petrinet;
	}

	@Override
	public void newTransition(String id) {
		super.newTransition(id);
		petrinet.addTransition(id);
	}

	@Override
	public void newPlace(String id) {
		super.newPlace(id);
		petrinet.addPlace(id);
	}

	@Override
	public void newArc(String id, String sourceId, String targetId) {
		super.newArc(id, sourceId, targetId);
		PetriElement source = petrinet.getPetriElement(sourceId);
		PetriElement target = petrinet.getPetriElement(targetId);
		if (source != null && target != null) {
			if (source instanceof Transition && target instanceof Place) {
				((Transition) source).addTarget(id, (Place) target);
			} else if (source instanceof Place && target instanceof Transition) {
				((Transition) target).addSource(id, (Place) source);
			}
		}
	}

	@Override
	public void setPosition(String id, String x, String y) {
		super.setPosition(id, x, y);

		try {
			int intX = Integer.parseInt(x);
			int intY = Integer.parseInt(y);

			petrinet.setPosition(id, intX, intY);
		} catch (NumberFormatException ex) {
			System.err.println("Invalid position for id " + id + ": " + x + ", " + y);
		}
	}

	@Override
	public void setName(String id, String name) {
		super.setName(id, name);
		petrinet.setName(id, name);
	}

	@Override
	public void setTokens(String id, String tokens) {
		super.setTokens(id, tokens);
		try {
			int numberOfTokens = Integer.parseInt(tokens);
			petrinet.setTokens(id, numberOfTokens);
		} catch (NumberFormatException ex) {
			System.err.println("Invalid number of tokens for id " + id + ": " + tokens);
		}

	}

}
