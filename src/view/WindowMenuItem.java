/**
 * 
 */
package view;

import javax.swing.JMenuItem;

/**
 * class that extends JMenuItem. WindowMenuItems are JMenuItems that represent
 * internal frames of the application in the menu bar.
 * 
 * @author Alexander Steinberg
 *
 */
public class WindowMenuItem extends JMenuItem {

	private static final long serialVersionUID = 2465253361739569229L;
	private int id;

	/**
	 * constructor for the WindowMenuItem class. Create a new JMenuItem that has the
	 * passed title and is connected to the internal frame with the passed id.
	 * 
	 * @param id the id of the connected internal frame
	 * @title the label for the menu item
	 */
	WindowMenuItem(int id, String title) {
		super(title);
		this.id = id;
	}

	/**
	 * get the id of the corresponding internal frame
	 * 
	 * @return id the id of the corresponding internal frame
	 */
	int getId() {
		return id;
	}

}
