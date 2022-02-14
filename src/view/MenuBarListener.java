/**
 * 
 */
package view;

/**
 * interface that menu bar listeners need to implement.
 * 
 * @author Alexander Steinberg
 *
 */
public interface MenuBarListener {
	/**
	 * perform some action when the user clicked on a menu bar item of the passed
	 * click (type)
	 * 
	 * @param click the type of menu bar item clicked
	 */
	void onMenuItemClicked(MenuBarClick click);

	/**
	 * perform some action when the user clicked on a window menu bar item that has
	 * the passed id associated with it (i.e. that is linked to am internal frame
	 * with that id).
	 * 
	 * @param id the id associated with the internal frame
	 */
	void onWindowMenuItemClicked(int id);

}
