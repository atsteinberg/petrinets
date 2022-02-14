/**
 * 
 */
package view;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.beans.PropertyVetoException;
import java.io.File;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.graphstream.ui.view.ViewerListener;

import model.AccessibilityGraph;
import model.Petrinet;

/**
 * class that represents the main window of the application. Sets up the desktop
 * pane in which all internal frames are displayed, as well as the application's
 * menu bar.
 * 
 * @author Alexander Steinberg
 *
 */
public class MainFrame extends JFrame implements MenuBarListener, InternalFrameListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 570188278732863684L;

	private PetriGraphPanel petriGraphPanel;

	private MainMenuBar mainMenuBar;

	private JFileChooser fileChooser;

	private PetriDesktopPane desktopPane;

	/**
	 * constructor for the MainFrame class. Initializes the desktop pane in which
	 * all internal frames are displayed as well as the app's menu bar. Sets the
	 * initial dimensions of the main app window, minimal dimensions as well as to
	 * exit on close.
	 * 
	 * @param title the title of the main frame
	 * @throws HeadlessException
	 */
	public MainFrame(String title) throws HeadlessException {
		super(title);

		initializeDesktopPane();

		initializeMenuBar();

		initializeFrame();
	}

	/**
	 * 
	 */
	private void initializeMenuBar() {
		mainMenuBar = new MainMenuBar();
		mainMenuBar.addMenuBarListener(this);
		this.setJMenuBar(mainMenuBar);
	}

	/**
	 * 
	 */
	private void initializeDesktopPane() {
		desktopPane = new PetriDesktopPane();
		add(desktopPane);
	}

	private void setFrameSizeRelativeToScreen() {
		// from Demo
		double heightPercentage = 0.8;
		double aspectRatio = 16.0 / 10.0;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int h = (int) (screenSize.height * heightPercentage);
		int w = (int) (h * aspectRatio);
		setBounds((screenSize.width - w) / 2, (screenSize.height - h) / 2, w, h);
	}

	private void initializeFrame() {
		setFrameSizeRelativeToScreen();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(400, 300));

		setVisible(true);
	}

	@Override
	public void onMenuItemClicked(MenuBarClick click) {
		switch (click) {
		case CloseAllWindows:
			closeAllWindows();
			break;
		case ReloadCurrent:
			InternalPetriFrame activeFrame = (InternalPetriFrame) desktopPane.getSelectedFrame();
			if (activeFrame != null) {
				activeFrame.reloadPetrinet();
			}
			break;
		case Info:
			JOptionPane.showMessageDialog(this,
					"Aktuelles Verzeichnis: " + System.getProperty("user.dir") + "\nJava Version: "
							+ System.getProperty("java.version"),
					"Info", JOptionPane.INFORMATION_MESSAGE, MainFrame.getInfoIcon());
			break;
		default:
			System.out.println("Case " + click + " not implemented in mainFrame");
		}
	}

	/**
	 * 
	 */
	private void closeAllWindows() {
		desktopPane.clearAllFrames();
		mainMenuBar.clearOpenWindows();
	}

	/**
	 * Registers the passed listener as a menu bar listener with the menu bar.
	 * 
	 * @param menuBarListener the menuBarListener to add to the menu bar
	 */
	public void addMenuBarListener(MenuBarListener menuBarListener) {
		if (mainMenuBar != null) {
			mainMenuBar.addMenuBarListener(menuBarListener);
		}
	}

	/**
	 * Opens a file chooser for the user to pick a single pnml file to open in a new
	 * internal frame. If the file chooser instance needs to be newly created it
	 * opens in the passed default folder. (Otherwise it opens in whatever folder
	 * was last used.).
	 * 
	 * @param defaultfolder the folder to open a newly created JFileChooser in
	 * @return the selected file
	 */
	public File openFile(String defaultfolder) {
		initializeFileChooser(defaultfolder, false);
		int userResponse = fileChooser.showOpenDialog(this);
		if (userResponse == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}

	/**
	 * @param defaultfolder
	 * @param multiSelect
	 */
	private void initializeFileChooser(String defaultfolder, boolean multiSelect) {
		if (fileChooser == null) {
			fileChooser = new JFileChooser(defaultfolder);
			fileChooser.setFileFilter(new PnmlFileFilter());
			fileChooser.setAcceptAllFileFilterUsed(false);
		}
		fileChooser.setMultiSelectionEnabled(multiSelect);
	}

	/**
	 * Opens a file chooser for the user to pick a single pnml file to open in a new
	 * internal frame. If the file chooser instance needs to be newly created it
	 * opens in the passed default folder. (Otherwise it opens in whatever folder
	 * was last used.)
	 * 
	 * @param defaultfolder the folder the file chooser is set to by default
	 * @return the selected files
	 */
	public File[] openFiles(String defaultfolder) {
		initializeFileChooser(defaultfolder, true);
		int userResponse = fileChooser.showOpenDialog(this);
		if (userResponse == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFiles();
		}
		return null;
	}

	/**
	 * adds a ViewerListener to the graph panel's viewer pipe
	 * 
	 * @param viewerListener the ViewerListener to register
	 */
	public void addJpnlGraphPanelViewerListener(ViewerListener viewerListener) {
		if (petriGraphPanel != null) {
			petriGraphPanel.addViewerListener(viewerListener);
		}
	}

	/**
	 * Initialize an internal frame newly added to the main frame's desktop pane.
	 * The internal frame will display a single petrinet and corresponding
	 * accessibility graph.
	 * 
	 * @param internalFrame      the internal frame to initialize
	 * @param petrinet           the petrinet displayed by the internal frame
	 * @param accessibilityGraph the accessibilityGraph displayed by the internal
	 *                           frame
	 * @param name               the name of the internal frame
	 * @param viewerListener     the viewer listener to set to the internal frame's
	 *                           viewer pipes
	 */
	public void initializeInternalPetriFrame(InternalPetriFrame internalFrame, Petrinet petrinet,
			AccessibilityGraph accessibilityGraph, String name, ViewerListener viewerListener) {
		internalFrame.initializeInternalPetriFrame(petrinet, accessibilityGraph, viewerListener);
		initializeInternalFrame(internalFrame, petrinet, name);
	}

	private void initializeInternalFrame(InternalPetriFrame internalFrame, Petrinet petrinet, String name) {
		internalFrame.addInternalFrameListener(this);
		internalFrame.setLocation(20 * ((desktopPane.getFrameCount()) % 10), 0);
		desktopPane.add(internalFrame);
		internalFrame.setInitialSize();
		try {
			internalFrame.setSelected(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		mainMenuBar.addWindow(petrinet.getId(), name);
	}

	/**
	 * Initialize an internal frame newly added to the main frame's desktop pane.
	 * The internal frame will display a batch analysis run in the passed petrinet.
	 * 
	 * @param internalFrame the internal frame to initialize
	 * @param petrinet      the petrinet to use for batch analysis
	 * @param title         the title of the internal frame
	 */
	public void initializeInternalAnalysisFrame(InternalPetriFrame internalFrame, Petrinet petrinet, String title) {
		internalFrame.initializeInternalAnalysisFrame(petrinet);
		initializeInternalFrame(internalFrame, petrinet, title);
	}

	/**
	 * Add a new internal frame to the main frame's desktop pane. The frame may
	 * either display a single petrinet and corresponding accessibility graph or a
	 * batch analysis. Sets the name as well as the passed
	 * InternalPetriFrameListener.
	 * 
	 * @param name     the name of the InternalPetriFrame
	 * @param listener the InternalPetriFrameListener to register with the internal
	 *                 frame
	 * @return the internal frame added
	 */
	public InternalPetriFrame addInternalFrame(String name, InternalPetriFrameListener listener) {
		return new InternalPetriFrame(name, listener);
	}

	@Override
	public void onWindowMenuItemClicked(int id) {
		InternalPetriFrame currentFrame = desktopPane.getInternalFrameById(id);
		if (currentFrame != null) {
			try {
				currentFrame.setSelected(true);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void internalFrameClosing(InternalFrameEvent e) {
		InternalPetriFrame closingFrame = (InternalPetriFrame) e.getInternalFrame();
		mainMenuBar.removeWindow(closingFrame.getId());
		closingFrame.dispose();
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void internalFrameIconified(InternalFrameEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void internalFrameActivated(InternalFrameEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Set the frame title of the passed internal petri frame. Also updates the menu
	 * bar's window menu.
	 * 
	 * @param title the title to set
	 * @param internalFrame the internal frame to modify
	 */
	public void setFrameTitle(String title, InternalPetriFrame internalFrame) {
		internalFrame.setTitle(title);
		mainMenuBar.updateTitle(title, internalFrame.getId());
	}

	/**
	 * Convenience method to get the icon of JOptionPanes that show info messages.
	 * 
	 * @return the app's designated info icon
	 */
	static Icon getInfoIcon() {
		String path = "/icons/InfoIcon64.png";
		URL imageURL = MainFrame.class.getResource(path);
		return new ImageIcon(imageURL);
	}

}
