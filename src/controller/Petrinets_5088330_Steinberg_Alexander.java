package controller;

import view.MainFrame;

/**
 * 
 */

/**
 * App for interacting with and analysing petrinets. Petrinets can be loaded
 * from pnml files. (Partial) accessibility graphs can be built manually. And
 * accessibility graphs can be auto generated, including an analysis for
 * unboundedness.
 * 
 * @author Alexander Steinberg
 *
 */

public class Petrinets_5088330_Steinberg_Alexander {

	/**
	 * 
	 * Main method for the Petrinets app. Petrinets can be loaded from pnml files.
	 * (Partial) accessibility graphs can be built manually. And accessibility
	 * graphs can be auto generated, including an analysis for unboundedness. Loads
	 * the app's main view and main controller.
	 * 
	 * @param args command line arguments (not used)
	 */
	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame mainFrame = new MainFrame("Petrinets_5088330_Steinberg_Alexander");
				new MainController(mainFrame);
			}
		});
	}

}
