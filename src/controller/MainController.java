/**
 * 
 */
package controller;

import java.io.File;

import view.MainFrame;
import view.MenuBarClick;
import view.MenuBarListener;

/**
 * @author Alexander Steinberg
 * 
 *         The app's main controller. Responsible for managing functionality
 *         that concerns the whole app: opening a new file, quitting the
 *         application, running a batch analysis.
 */
public class MainController implements MenuBarListener {
	/**
	 * the default folder to start a new instance of JFileChooser with
	 */
	static final String defaultFolder = "../ProPra-WS21-Basis/Beispiele/";

	/**
	 * the example file to start a new instance of the app with
	 */
	static final String exampleFile = "110-B1-N01-A00-EineStelleZweiMarken.pnml";

	private MainFrame mainFrame;

	/**
	 * Constructor for MainController class. Registers itself as a listener for menu
	 * bar events and loads a default file on startup.
	 * 
	 * @param mainFrame the app's main frame
	 */
	public MainController(MainFrame mainFrame) {
		this.mainFrame = mainFrame;

		File defaultFile = new File(defaultFolder + exampleFile);

		// open default file on startup
		loadFile(defaultFile);

		mainFrame.addMenuBarListener(this);
	}

	@Override
	public void onMenuItemClicked(MenuBarClick click) {
		switch (click) {
		case OpenFile:
			openFile();
			break;
		case Quit:
			quitApplication();
			break;
		case AnalyseFiles:
			openFiles();
			break;
		default:
			System.out.println(click + " not implemented in main controller");
		}
	}

	/**
	 * 
	 */
	private void openFiles() {
		File[] files = mainFrame.openFiles(defaultFolder);
		if (files != null) {
			analyseFiles(files);
		}

	}

	/**
	 * @param files
	 */
	private void analyseFiles(File[] files) {
		if (files.length > 0) {
			new AnalysisController(files, mainFrame);
		}
	}

	/**
	 * 
	 */
	private void openFile() {
		File selectedFile = mainFrame.openFile(defaultFolder);
		if (selectedFile != null) {
			loadFile(selectedFile);
		}
	}

	/**
	 * @param selectedFile
	 */
	private void loadFile(File selectedFile) {
		if (selectedFile.exists()) {
			new PetrinetController(selectedFile, mainFrame);
		}
	}

	/**
	 * 
	 */
	private void quitApplication() {
		System.exit(0);
	}

	@Override
	public void onWindowMenuItemClicked(int id) {
		// left unimplemented
	}

}
