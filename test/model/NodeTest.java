/**
 * 
 */
package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Alexander Steinberg
 *
 */
public class NodeTest {
	private Petrinet petrinet;
	private Place p1;
	private Place p2;
	private Place p3;
	private Place a;

	@BeforeEach
	void setUp() {
		petrinet = new Petrinet();
		p1 = new Place("p1");
		p2 = new Place("p2");
		p3 = new Place("p3");
		a = new Place("a");
		petrinet.addPlaces(new Place[] { p1, p2, p3, a });
	}

	@Test
	@DisplayName("Konstruktor(Petrinet)")
	public void testConstructor() {
		p1.setNumberOfTokens(1);
		p2.setNumberOfTokens(2);
		p3.setNumberOfTokens(3);

		PetrinetState petrinetState = new PetrinetState(petrinet);

		assertEquals(petrinetState.toString(), "<a: 0, p1: 1, p2: 2, p3: 3>");
		assertEquals(petrinetState, petrinetState);

		Petrinet petrinet2 = new Petrinet();
		Place q1 = new Place("q1");
		q1.setNumberOfTokens(3);
		petrinet2.addPlaces(new Place[] { q1, p1, p2, a });
		PetrinetState node2 = new PetrinetState(petrinet2);

		assertEquals(node2.toString(), "<a: 0, p1: 1, p2: 2, q1: 3>");
		assertNotEquals(petrinetState, node2);
	}

	@Test
	@DisplayName("Konstruktor(Petrinet)")
	public void testSecondConstructor() {
		p1.setNumberOfTokens(3);
		p2.setNumberOfTokens(2);
		p3.setNumberOfTokens(1);
		a.setNumberOfTokens(4);
		PetrinetState node1 = new PetrinetState(petrinet);

		List<PetrinetState> predecessorNodes = new ArrayList<PetrinetState>();
		predecessorNodes.add(node1);

		p1.setNumberOfTokens(2);
		p2.setNumberOfTokens(2);
		p3.setNumberOfTokens(2);
		a.setNumberOfTokens(4);
		PetrinetState node2 = new PetrinetState(petrinet);

		PetrinetState node3 = new PetrinetState(petrinet);

		assertNotEquals(node1, node2);
		assertEquals(node2, node3);
	}

	@Test
	@DisplayName("getName")
	public void testGetName() {
		p1.setNumberOfTokens(1);
		p2.setNumberOfTokens(2);
		p3.setNumberOfTokens(3);
		PetrinetState petrinetState = new PetrinetState(petrinet);

		assertEquals(petrinetState.getName(), "(0|1|2|3)");
		
		p1.setNumberOfTokens(4);
		
		assertEquals(petrinetState.getName(), "(0|1|2|3)");
		assertEquals(new PetrinetState(petrinet).getName(), "(0|4|2|3)");
	}
}
