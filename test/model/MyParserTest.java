/**
 * 
 */
package model;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Collection;

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
public class MyParserTest {
	static final String pathToExamples = "/Users/dev/Documents/projects/propra-ws21/ProPra-WS21-Basis/Beispiele/";
	
	private MyParser parser;
	private Petrinet petrinet;
	
	@BeforeEach
	void setup() {
		petrinet = new Petrinet();
	}
	
	@Test
	@DisplayName("Eine Stelle zwei Marken")
	void testOnePlaceTwoTokens() {
		File pnmlFile = new File(pathToExamples + "110-B1-N01-A00-EineStelleZweiMarken.pnml");
		parser = new MyParser(pnmlFile, petrinet);
		parser.initParser();
		parser.parse();
		Collection<Place> places = petrinet.getPlaces();;
		assertEquals(places.size(), 1);
		Place p1 = petrinet.getPlaceById("p1");
		assertNotNull(p1);
		assertEquals(p1.getName(), "p1");
		assertEquals(p1.getPosition().getX(), 250);
		assertEquals(((Place) p1).getNumberOfTokens(), 2);
		assertEquals(petrinet.toString(), "(2)");
	}
	
	@Test
	@DisplayName("Eine Stelle eine Transition")
	void testOnePlaceOneTransition() {
		File pnmlFile = new File(pathToExamples + "111-B1-N01-A00-EineStelleEineTransition.pnml");
		parser = new MyParser(pnmlFile, petrinet);
		parser.initParser();
		parser.parse();
		Collection<Place> places = petrinet.getPlaces();
		assertEquals(places.size(), 1);
		Collection<Transition> transitions = petrinet.getTransitions();
		assertEquals(transitions.size(), 1);
		Place p1 = petrinet.getPlaceById("p1");
		assertNotNull(p1);
		Transition t1 = petrinet.getTransitionById("t1");
		assertNotNull(t1);
		Collection<TransitionRelation> sources = t1.getSources();
		assertEquals(sources.size(), 1);
		Collection<TransitionRelation> targets = t1.getTargets();
		assertEquals(targets.size(), 0);
		assertEquals(petrinet.toString(), "(0)");
	}
}
