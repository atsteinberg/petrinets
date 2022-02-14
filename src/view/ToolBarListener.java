/**
 * 
 */
package view;

/**
 * interface that ToolBarListeners need to implement
 * 
 * @author Alexander Steinberg
 *
 */
public interface ToolBarListener {
	/**
	 * perform some action in response to a click on the toolbar icon corresponding
	 * to the passed action type
	 * 
	 * @param action the type of action associated with the tool bar icon clicked
	 */
	void toolBarIconClicked(ToolBarAction action);
}
