/**
 * 
 */
package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * class that extends JMenuBar to create a menu bar for the main app window.
 * 
 * @author Alexander Steinberg
 *
 */
class MainMenuBar extends JMenuBar {

	private static final long serialVersionUID = -7189289031089104154L;
	private JMenu filesMenu;
	private JMenu windowsMenu;
	private JMenuItem openFilesMenuItem;
	private JMenuItem closeAllMenuItem;
	private JMenuItem reloadCurrentMenuItem;
	private JMenuItem analyseFilesMenuItem;
	private JMenu helpMenu;
	private JMenuItem infoMenuItem;
	private List<MenuBarListener> menuBarListeners;
	private ActionListener menuItemListener;
	private JMenuItem quitMenuItem;
	private Map<Integer, WindowMenuItem> openWindowMenuItems;

	/**
	 * constructor for the MainMenuBar class. MainMenuBar extends JMenuBar to create
	 * a menu bar for the main app window.
	 */
	MainMenuBar() {
		super();
		menuBarListeners = new ArrayList<MenuBarListener>();
		menuItemListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (MenuBarListener menuBarListener : menuBarListeners) {
					if (e.getSource() == openFilesMenuItem) {
						menuBarListener.onMenuItemClicked(MenuBarClick.OpenFile);
					} else if (e.getSource() == quitMenuItem) {
						menuBarListener.onMenuItemClicked(MenuBarClick.Quit);
					} else if (e.getSource() instanceof WindowMenuItem) {
						WindowMenuItem windowMenuItem = (WindowMenuItem) e.getSource();
						menuBarListener.onWindowMenuItemClicked(windowMenuItem.getId());
					} else if (e.getSource() == closeAllMenuItem) {
						menuBarListener.onMenuItemClicked(MenuBarClick.CloseAllWindows);
					} else if (e.getSource() == reloadCurrentMenuItem) {
						menuBarListener.onMenuItemClicked(MenuBarClick.ReloadCurrent);
					} else if (e.getSource() == analyseFilesMenuItem) {
						menuBarListener.onMenuItemClicked(MenuBarClick.AnalyseFiles);
					} else if (e.getSource() == infoMenuItem) {
						menuBarListener.onMenuItemClicked(MenuBarClick.Info);
					}
				}
			}
		};

		setupFilesMenu();
		setupWindowsMenu();
		setupHelpMenu();

	}

	/**
	 * 
	 */
	private void setupHelpMenu() {
		helpMenu = new JMenu("Hilfe");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		infoMenuItem = new JMenuItem("Info", KeyEvent.VK_I);
		infoMenuItem.addActionListener(menuItemListener);

		helpMenu.add(infoMenuItem);
		add(helpMenu);
	}

	/**
	 * 
	 */
	private void setupWindowsMenu() {
		windowsMenu = new JMenu("Fenster");
		windowsMenu.setMnemonic(KeyEvent.VK_F);
		closeAllMenuItem = new JMenuItem("Alle schließen", KeyEvent.VK_S);
		closeAllMenuItem.addActionListener(menuItemListener);
		add(windowsMenu);

		openWindowMenuItems = new HashMap<Integer, WindowMenuItem>();
		generateWindowsMenu();
	}

	/**
	 * 
	 */
	private void setupFilesMenu() {
		filesMenu = new JMenu("Dateien");
		filesMenu.setMnemonic(KeyEvent.VK_D);
		openFilesMenuItem = new JMenuItem("Datei öffnen...");
		KeyStroke keyStrokeToOpen = KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.META_DOWN_MASK);
		openFilesMenuItem.setAccelerator(keyStrokeToOpen);
		openFilesMenuItem.addActionListener(menuItemListener);
		filesMenu.add(openFilesMenuItem);

		reloadCurrentMenuItem = new JMenuItem("Datei neu laden", KeyEvent.VK_N);
		reloadCurrentMenuItem.addActionListener(menuItemListener);
		filesMenu.add(reloadCurrentMenuItem);

		filesMenu.addSeparator();

		analyseFilesMenuItem = new JMenuItem("Dateien analysieren...", KeyEvent.VK_A);
		analyseFilesMenuItem.setDisplayedMnemonicIndex(8);
		analyseFilesMenuItem.addActionListener(menuItemListener);
		filesMenu.add(analyseFilesMenuItem);

		filesMenu.addSeparator();

		quitMenuItem = new JMenuItem("Beenden");
		KeyStroke keyStrokeToQuit = KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.META_DOWN_MASK);
		quitMenuItem.setAccelerator(keyStrokeToQuit);
		quitMenuItem.addActionListener(menuItemListener);
		filesMenu.add(quitMenuItem);

		add(filesMenu);
	}

	/**
	 * adds a new listener for menu bar clicks. If the listener is already
	 * registered, no changes are made.
	 * 
	 * @param menuBarListener the MenuBarListener to register
	 */
	void addMenuBarListener(MenuBarListener menuBarListener) {
		if (!menuBarListeners.contains(menuBarListener)) {
			menuBarListeners.add(menuBarListener);
		}
	}

	/**
	 * add a new WindowMenuItem to the windows menu. The menu item has the id and
	 * label passed. It is added to the end of the list of open windows.
	 * 
	 * @param id the if of the corresponding internal frame
	 * @param title the label to be displayeds
	 */
	void addWindow(int id, String title) {
		WindowMenuItem newWindow = new WindowMenuItem(id, title);
		openWindowMenuItems.put(id, newWindow);
		newWindow.addActionListener(menuItemListener);
		generateWindowsMenu();
	}

	/**
	 * remove the window menu item that has the passed id.
	 * 
	 * @param id the id of the window the menu item for which should be removed
	 */
	public void removeWindow(int id) {
		WindowMenuItem removedWindowMenuItem = openWindowMenuItems.remove(id);
		if (removedWindowMenuItem != null) {
			generateWindowsMenu();
		}
	}

	/**
	 * 
	 */
	private void generateWindowsMenu() {
		windowsMenu.removeAll();
		for (WindowMenuItem windowMenuItem : openWindowMenuItems.values()) {
			windowsMenu.add(windowMenuItem);
		}
		if (windowsMenu.getMenuComponentCount() != 0) {
			windowsMenu.addSeparator();
		}
		windowsMenu.add(closeAllMenuItem);
		if (windowsMenu.getMenuComponentCount() == 1) {
			closeAllMenuItem.setEnabled(false);
		} else {
			closeAllMenuItem.setEnabled(true);
		}
	}

	/**
	 * remove all window menu items from the window menu.
	 */
	public void clearOpenWindows() {
		openWindowMenuItems.clear();
		generateWindowsMenu();
	}

	/**
	 * Update the label of the window menu item for the window with the passed id.
	 * 
	 * @param title the new title to set for the window menu item
	 * @param id    the id of the window menu item to update
	 */
	public void updateTitle(String title, int id) {
		JMenuItem currentWindowMenu = openWindowMenuItems.get(id);
		currentWindowMenu.setText(title);
	}
}
