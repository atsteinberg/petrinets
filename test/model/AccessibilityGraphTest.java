/**
 * 
 */
package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author Alexander Steinberg
 *
 */
@TestMethodOrder(MethodOrderer.DisplayName.class)
@SuppressWarnings("javadoc")
public class AccessibilityGraphTest {
	private AccessibilityGraph graph;
	private Petrinet petrinet;
	private PetrinetState initialNode;
	private Place p1;
	private Place p2;
	private Place p3;

	@BeforeEach
	private void setup() {
		p1 = new Place("p1");
		p1.setNumberOfTokens(0);
		p2 = new Place("p2");
		p2.setNumberOfTokens(3);
		p3 = new Place("p3");
		p3.setNumberOfTokens(1);

		petrinet = new Petrinet();
		petrinet.addPlaces(new Place[] { p1, p2, p3 });

		initialNode = new PetrinetState(petrinet);

		graph = new AccessibilityGraph(petrinet);
	}

	@Test
	@DisplayName("Initialisiere den Graphen")
	void testConstructor() {
		PetrinetState graphInitialNode = graph.getInitialState();
		assertEquals(graphInitialNode, initialNode);
		assertEquals(graph.getCurrentState(), initialNode);
	}

	@Test
	@DisplayName("Einen Knoten hinzufügen")
	void testAddNode() {
		p1.setNumberOfTokens(1);
		Transition t1 = new Transition("t1");
		graph.addState(t1);

		PetrinetState currentNode = graph.getCurrentState();
		assertEquals(currentNode.getName(), "(1|3|1)");
		assertNotEquals(currentNode, initialNode);
	}
}
