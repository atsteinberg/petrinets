/**
 * 
 */
package view;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 * class that extends JDesktopPane to hold all internal frames to either display
 * a single petrinet or a batch analysis.
 * 
 * @author Alexander Steinberg
 *
 */
class PetriDesktopPane extends JDesktopPane {

	private static final long serialVersionUID = 7504993279385249187L;

	/**
	 * constructor for the PetriDesktopPane class. Sets outline drag mode for
	 * performance reasons.
	 */
	PetriDesktopPane() {
		setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		setVisible(true);
	}

	/**
	 * Get the internal frame with the passed id from the desktop pane.
	 * 
	 * @param id the id of the internal frame to get
	 * @return the internal frame managed by the petri desktop pane with that id.
	 */
	InternalPetriFrame getInternalFrameById(int id) {
		JInternalFrame[] frames = getAllFrames();
		InternalPetriFrame frameWithId = null;
		for (JInternalFrame frame : frames) {
			InternalPetriFrame petriFrame = (InternalPetriFrame) frame;
			if (petriFrame.getId() == id) {
				frameWithId = petriFrame;
				break;
			}
		}
		return frameWithId;
	}

	/**
	 * Remove the internal frame with the passed id from the desktop pane.
	 * 
	 * @param id the id of the internal frame to remove
	 * @return true if a frame was removed, false otherwise
	 */
	boolean removeInternalFrameById(int id) {
		JInternalFrame[] frames = getAllFrames();
		for (JInternalFrame frame : frames) {
			InternalPetriFrame petriFrame = (InternalPetriFrame) frame;
			if (petriFrame.getId() == id) {
				petriFrame.dispose();
				return true;
			}
		}
		return false;
	}

	/**
	 * remove all frames from the desktop pane
	 */
	void clearAllFrames() {
		JInternalFrame[] openInternalFrames = getAllFrames();
		for (JInternalFrame internalFrame : openInternalFrames) {
			internalFrame.dispose();
		}
	}

	/**
	 *
	 * @return the number of frames currently held by the internal desktop pane
	 */
	int getFrameCount() {
		return getAllFrames().length;
	}

}
