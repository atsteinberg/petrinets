/**
 * 
 */
package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.graphstream.ui.view.ViewerListener;

import model.AccessibilityGraph;
import model.MyParser;
import model.Petrinet;
import model.PetrinetState;
import model.Place;
import model.Transition;
import view.AccessibilityGraphViewListener;
import view.InternalPetriFrame;
import view.InternalPetriFrameListener;
import view.MainFrame;
import view.PetrinetStatus;
import view.ToolBarAction;
import view.ToolBarListener;

/**
 * @author Alexander Steinberg
 * 
 *         A controller for managing an individual petrinet and the internal
 *         frame that serves as its. Controls mouse events from petrinet and
 *         accessibility graph, the internal frame and its toolbar.
 *
 */
public class PetrinetController
		implements ViewerListener, ToolBarListener, AccessibilityGraphViewListener, InternalPetriFrameListener {

	private Petrinet petrinet;

	private AccessibilityGraph accessibilityGraph;

	private MainFrame mainFrame;

	private InternalPetriFrame internalFrame;

	private File currentFile;

	private String name;

	private boolean continuousAnalysisActive;

	/**
	 * Constructor for the PetrinetController class. Sets the app's main frame and
	 * an initially selected file. Creates a petrinet model and an internal frame
	 * that acts as a view for that model.
	 * 
	 * @param selectedFile the file selected by the user
	 * @param mainFrame the app's main frame
	 */
	public PetrinetController(File selectedFile, MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		petrinet = new Petrinet();

		continuousAnalysisActive = false;

		currentFile = selectedFile;

		name = selectedFile.getName().replaceFirst(".pnml$", "");

		parseFile(selectedFile);

		internalFrame = mainFrame.addInternalFrame(name, this);

		printStartupInfo();

		internalFrame.appendToTextArea("Petrinetz geladen: " + name);

		accessibilityGraph = new AccessibilityGraph(petrinet);

		internalFrame.appendToTextArea("Erreichbarkeitsgraph wird initialisiert.");
		mainFrame.initializeInternalPetriFrame(internalFrame, petrinet, accessibilityGraph, name, this);
		internalFrame.setToolBarListener(this);
		internalFrame.setContinuousAnalysisActive(isContinuousAnalysisActive());
		enableNavigationButtons();
	}

	/**
	 * @param file
	 */
	private void parseFile(File file) {
		petrinet.clear();
		MyParser pnmlParser = new MyParser(file, petrinet);
		pnmlParser.initParser();
		pnmlParser.parse();
	}

	@Override
	public void viewClosed(String viewName) {
		// left unimplemented
	}

	@Override
	public void buttonPushed(String id) {
		Transition transition = petrinet.getTransitionById(id);
		if (transition != null) {
			fireTransition(transition);
		} else {
			Place place = petrinet.getPlaceById(id);
			if (place != null) {
				setSelectedPlace(place);
			} else {
				PetrinetState petrinetState = accessibilityGraph.getPetrinetStateById(id);
				if (petrinetState != null) {
					setPetrinetState(petrinetState);
					internalFrame.updateGraphs();
				}
			}
		}

	}

	/**
	 * @param transition to fire
	 */
	private void fireTransition(Transition transition) {
		if (transition.isEnabled()) {
			internalFrame.appendToTextArea("PN: Transition " + transition.getDisplayName() + " geschaltet.");
			transition.fire();
			accessibilityGraph.addState(transition);
			internalFrame.updateGraphs();
		}
	}

	/**
	 * @param place to select
	 */
	private void setSelectedPlace(Place place) {
		internalFrame.appendToTextArea("Stelle " + place.getDisplayName() + " ausgewählt.");
		Place selectedPlace = petrinet.togglePlaceSelected(place.getId());
		internalFrame.updateSelection();
		if (selectedPlace == null) {
			internalFrame.enableTokenButtons(false);
		} else {
			if (selectedPlace.getNumberOfTokens() == 0) {
				internalFrame.enableTokenButtons(true, false);
			} else {
				internalFrame.enableTokenButtons(true);
			}
		}
	}

	@Override
	public void buttonReleased(String id) {
		// left unimplemented

	}

	@Override
	public void mouseOver(String id) {
		// left unimplemented
	}

	@Override
	public void mouseLeft(String id) {
		// left unimplemented
	}

	@Override
	public void toolBarIconClicked(ToolBarAction action) {
		Place selectedPlace = petrinet.getSelectedPlace();
		switch (action) {
		case REINITIALISE_PETRINET:
			internalFrame.appendToTextArea("Petrinetz auf Ausgangsmarkierung zurückgesetzt.");
			reinitialisePetrinet(false);
			break;
		case CLEAR_TEXTAREA:
			internalFrame.clearTextArea();
			internalFrame.appendToTextArea("Textfeld wurde zurückgesetzt.");
			break;
		case ADD_TOKEN:
			selectedPlace.addToken();
			if (selectedPlace.getNumberOfTokens() > 0) {
				internalFrame.enableTokenButtons(true);
			}
			internalFrame.appendToTextArea(
					"Marke zu " + selectedPlace.getName() + " hinzugefügt: " + selectedPlace.getNumberOfTokens() + ".");
			internalFrame.setStatus(PetrinetStatus.MODIFIED);
			reInitializeGraphs();
			break;
		case SUBTRACT_TOKEN:
			selectedPlace.subtractToken();
			if (selectedPlace.getNumberOfTokens() == 0) {
				internalFrame.enableTokenButtons(true, false);
			}
			internalFrame.appendToTextArea(
					"Marke von " + selectedPlace.getName() + " entfernt: " + selectedPlace.getNumberOfTokens() + ".");
			reInitializeGraphs();
			internalFrame.setStatus(PetrinetStatus.MODIFIED);
			break;
		case CLEAR_ACCESSIBILITY_GRAPH:
			internalFrame.appendToTextArea(
					"Petrinetz auf Ausgangsmarkierung zurückgesetzt und Erreichbarkeitsgraph reinitialisiert.");
			reinitialisePetrinet(true);
			break;
		case ANALYZE_PETRINET:
			accessibilityGraph.autoGenerate();
			internalFrame.drawGeneratedGraphs();
			setContinuousAnalysisActive(accessibilityGraph.isUnbounded());
			internalFrame.appendToTextArea("(Partieller) Erreichbarkeitsgraph wird automatisch generiert.");
			if (accessibilityGraph.isUnbounded()) {
				internalFrame.appendToTextArea("EG: Das Petrinetz ist unbeschränkt.");
				internalFrame.showInfoPanel("Das Petrinetz ist unbeschränkt");
				String message = accessibilityGraph.getCurrentMAndMPrimeMessage();
				if (message != null) {
					internalFrame.appendToTextArea(message);
				}
			} else {
				internalFrame.appendToTextArea(
						"EG: Das Petrinetz ist beschränkt. Erreichbarkeitsgraph wurde vollständig generiert.");
				internalFrame.showInfoPanel("Das Petrinetz ist beschränkt.");
			}
			break;
		case CONTINUOUS_ANALYSIS:
			setContinuousAnalysisActive(!isContinuousAnalysisActive());
			internalFrame.appendToTextArea("Kontinuierliche Analyse wird "
					+ (isContinuousAnalysisActive() ? "aktiv" : "inaktiv") + " gesetzt.");
			break;
		case PREVIOUS_FILE:
			File previousFile = getAdjacentFiles()[0];
			if (previousFile != null) {
				currentFile = previousFile;
				internalFrame.appendToTextArea("Vorherige Datei wird geladen: " + previousFile.getName() + ".");
				parseCurrentFile();
			}
			break;
		case NEXT_FILE:
			File nextFile = getAdjacentFiles()[1];
			if (nextFile != null) {
				currentFile = nextFile;
				internalFrame.appendToTextArea("Nächste Datei wird geladen: " + nextFile.getName() + ".");
				parseCurrentFile();
			}
			break;
		default:
			System.out.println("toolbar action unimplemented in petrinet controller: " + action);
		}

	}

	/**
	 * 
	 */
	private void parseCurrentFile() {
		parseFile(currentFile);
		accessibilityGraph.clear();
		internalFrame.drawGraphs();
		internalFrame.setStatus(PetrinetStatus.UNMODIFIED);
		mainFrame.setFrameTitle(currentFile.getName().replaceFirst(".pnml$", ""), internalFrame);
		enableNavigationButtons();
	}

	/**
	 * 
	 */
	private void enableNavigationButtons() {
		File[] adjacentFiles = getAdjacentFiles();
		Boolean hasPrevious = adjacentFiles[0] != null;
		Boolean hasNext = adjacentFiles[1] != null;
		internalFrame.enableNavigationButtons(hasPrevious, hasNext);
	}

	/**
	 * 
	 */
	private File[] getAdjacentFiles() {
		File folder = currentFile.getParentFile();
		File previousFile = null;
		File nextFile = null;
		if (folder != null) {
			List<File> pnmlFiles = new ArrayList<File>(
					Arrays.asList(folder.listFiles((dir, name) -> name.endsWith(".pnml"))));
			pnmlFiles.sort((File a, File b) -> a.getName().compareTo(b.getName()));
			int i = 0;
			while (i < pnmlFiles.size() && previousFile == null && nextFile == null) {
				if (pnmlFiles.get(i).equals(currentFile)) {
					if (i > 0) {
						previousFile = pnmlFiles.get(i - 1);
					}
					if (i < pnmlFiles.size() - 1) {
						nextFile = pnmlFiles.get(i + 1);
					}
				}
				i += 1;
			}
		}
		return new File[] { previousFile, nextFile };
	}

	/**
	 * @param redraw if true graphs are redrawn; update otherwise
	 */
	private void reinitialisePetrinet(boolean redraw) {
		PetrinetState initialState = accessibilityGraph.getInitialState();
		setPetrinetState(initialState);
		if (redraw) {
			accessibilityGraph.clear();
			internalFrame.drawGraphs();
		} else {
			internalFrame.updateGraphs();
		}
	}

	/**
	 * @param state to set the petrinet to
	 */
	private void setPetrinetState(PetrinetState state) {
		internalFrame.appendToTextArea("Petrinetz in den Zustand " + state.getName() + " gesetzt.");
		petrinet.setState(state);
		internalFrame.clearAccessibilityGraphHighlights();
		accessibilityGraph.setCurrentState(state);
	}

	/**
	 * 
	 */
	private void reInitializeGraphs() {
		internalFrame.appendToTextArea("Erreichbarkeitsgraph wird neu initialisiert.");
		accessibilityGraph.clear();
		internalFrame.updateJpnlGraph();
		internalFrame.drawAccessibilityGraph();
	}

	@Override
	public void accessibilityGraphViewChanged(String description) {
		internalFrame.appendToTextArea("EG: " + description);
	}

	@Override
	public void onReloadFrame(int id) {
		if (id == petrinet.getId()) {
			internalFrame.appendToTextArea("Datei " + name + " neu geladen.");
			parseCurrentFile();
		}
	}

	/**
	 * @return the continuousAnalysisActive
	 */
	private boolean isContinuousAnalysisActive() {
		return continuousAnalysisActive;
	}

	/**
	 * @param continuousAnalysisActive the continuousAnalysisActive to set
	 */
	private void setContinuousAnalysisActive(boolean continuousAnalysisActive) {
		this.continuousAnalysisActive = continuousAnalysisActive;
		internalFrame.setContinuousAnalysisActive(continuousAnalysisActive);
	}

	/**
	 * prints the current directory and the system's java version to the internal
	 * frame's text box.
	 * 
	 */
	private void printStartupInfo() {
		internalFrame.appendToTextArea("Aktuelles Verzeichnis: " + System.getProperty("user.dir"));
		internalFrame.appendToTextArea("Java Version: " + System.getProperty("java.version"));
	}
}
