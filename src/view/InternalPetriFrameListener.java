/**
 * 
 */
package view;

/**
 * interface that internal petri frame listeners need to implement.
 * 
 * @author Alexander Steinberg
 *
 */
public interface InternalPetriFrameListener {
	/**
	 * method to call when internal petri frame is reloaded. Accepts the id of the internal frame.
	 * 
	 * @param id the id of the internal frame
	 *
	 */
	void onReloadFrame(int id);
}
