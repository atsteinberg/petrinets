/**
 * 
 */
package view;

/**
 * interface to listen to changes in the accessibility graph view. (moderately)
 * used for logging in textarea.
 * 
 * @author Alexander Steinberg
 *
 */
public interface AccessibilityGraphViewListener {

	/**
	 * describes a change in the accessibility graph view. Useful e.g. for logging
	 * such changes.
	 * 
	 * @param description a description of the change
	 */
	void accessibilityGraphViewChanged(String description);

}
