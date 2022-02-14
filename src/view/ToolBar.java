/**
 * 
 */
package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

/**
 * class that extends JToolBar to display an internal petri frame's toolbar.
 * 
 * @author Alexander Steinberg
 *
 */
class ToolBar extends JToolBar implements ActionListener {

	private static final long serialVersionUID = 6549436577168120416L;
	private JButton resetButton;
	private JButton clearTextAreaButton;
	private JButton plusButton;
	private JButton minusButton;
	private JButton clearAccessibilityGraphButton;
	private JButton analyzeButton;
	private JButton continuousAnalysisButton;
	private JButton nextFileButton;
	private JButton previousFileButton;
	private ImageIcon continuousAnalysisNotActiveIcon;
	private ImageIcon continuousAnalysisActiveIcon;
	private ToolBarListener toolBarListener;

	/**
	 * constructor for the ToolBar class. Sets the title of the toolBar and adds its
	 * buttons: 2 navigation buttons, 2 token adjust buttons, 2 reset buttons for
	 * petrinet and accessibility graph, 2 analysis buttons and a button to clear
	 * the text area.
	 * 
	 * @param title the title to set
	 */
	ToolBar(String title) {
		super(title);

		previousFileButton = buttonFactory("Back24.png", "Vorherige Datei laden");
		nextFileButton = buttonFactory("Next24.png", "Nächste Datei laden");
		addSeparator();
		minusButton = buttonFactory("Minus24.png", "Marke entfernen");
		plusButton = buttonFactory("Plus24.png", "Marke hinzufügen");
		addSeparator();
		resetButton = buttonFactory("Refresh24.png", "Petrinetz auf Ausgangsmarkierung zurücksetzen");
		clearAccessibilityGraphButton = buttonFactory("Delete24.png",
				"Petrinetz zurücksetzen und Erreichbarkeitsgraph verwerfen");
		addSeparator();
		analyzeButton = buttonFactory("Analyze24.png", "Petrinetz analysieren");
		setupContinuousAnalysisIcons();
		continuousAnalysisButton = new JButton(continuousAnalysisNotActiveIcon);
		continuousAnalysisButton.setToolTipText("Petrinetz fortlaufend analysieren");
		continuousAnalysisButton.addActionListener(this);
		add(continuousAnalysisButton);
		addSeparator();
		clearTextAreaButton = buttonFactory("DeleteText24.png", "AusgabeFeld löschen");
		enableTokenButtons(false);
	}

	private void setupContinuousAnalysisIcons() {
		String path = "/icons/ContinuousAnalysisNotActive24.png";
		URL imageURL = ToolBar.class.getResource(path);
		continuousAnalysisNotActiveIcon = new ImageIcon(imageURL);
		path = "/icons/ContinuousAnalysisActive24.png";
		imageURL = ToolBar.class.getResource(path);
		continuousAnalysisActiveIcon = new ImageIcon(imageURL);
	}

	private JButton buttonFactory(String imageLocation, String toolTip) {
		String path = "/icons/" + imageLocation;
		URL imageURL = ToolBar.class.getResource(path);
		JButton button = new JButton(new ImageIcon(imageURL));
		button.setToolTipText(toolTip);
		button.addActionListener(ToolBar.this);
		add(button);
		return button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (toolBarListener != null) {
			if (e.getSource().equals(resetButton)) {
				toolBarListener.toolBarIconClicked(ToolBarAction.REINITIALISE_PETRINET);
			} else if (e.getSource().equals(clearTextAreaButton)) {
				toolBarListener.toolBarIconClicked(ToolBarAction.CLEAR_TEXTAREA);
			} else if (e.getSource().equals(plusButton)) {
				toolBarListener.toolBarIconClicked(ToolBarAction.ADD_TOKEN);
			} else if (e.getSource().equals(minusButton)) {
				toolBarListener.toolBarIconClicked(ToolBarAction.SUBTRACT_TOKEN);
			} else if (e.getSource().equals(clearAccessibilityGraphButton)) {
				toolBarListener.toolBarIconClicked(ToolBarAction.CLEAR_ACCESSIBILITY_GRAPH);
			} else if (e.getSource().equals(continuousAnalysisButton)) {
				toolBarListener.toolBarIconClicked(ToolBarAction.CONTINUOUS_ANALYSIS);
			} else if (e.getSource().equals(analyzeButton)) {
				toolBarListener.toolBarIconClicked(ToolBarAction.ANALYZE_PETRINET);
			} else if (e.getSource().equals(previousFileButton)) {
				toolBarListener.toolBarIconClicked(ToolBarAction.PREVIOUS_FILE);
			} else if (e.getSource().equals(nextFileButton)) {
				toolBarListener.toolBarIconClicked(ToolBarAction.NEXT_FILE);
			}
		}
	}

	/**
	 * register a ToolBarListener
	 * 
	 * @param listener the ToolBarListener to register
	 */
	void setToolBarListener(ToolBarListener listener) {
		toolBarListener = listener;
	}

	/**
	 * enable/disable the token buttons. Convenience method for
	 * {@code enableTokenButtons(enable, enable)}
	 * 
	 * @param enable true if token buttons are enabled, false if disabled
	 */
	public void enableTokenButtons(boolean enable) {
		enableTokenButtons(enable, enable);
	}

	/**
	 * enable/disable the add and subtract token buttons.
	 * 
	 * @param enablePlus  true if add token button is enabled, false if disabled
	 * @param enableMinus true if subtract token button is enabled, false if
	 *                    disabled
	 */
	public void enableTokenButtons(boolean enablePlus, boolean enableMinus) {
		minusButton.setEnabled(enableMinus);
		plusButton.setEnabled(enablePlus);
	}

	/**
	 * Enable/disable the navigation buttons
	 * 
	 * @param enabledBack true if previous file button is enabled, false if disabled
	 * @param enableNext  true of next file button is enabled, false if disabled
	 */
	public void enableNavigationButtons(boolean enabledBack, boolean enableNext) {
		previousFileButton.setEnabled(enabledBack);
		nextFileButton.setEnabled(enableNext);
	}

	/**
	 * Set whether the continuous analysis button is activated
	 * 
	 * @param continusousAnalysisActive true if continuous analysis is active, false
	 *                                  if inactive
	 */
	public void setContinuousAnalysisActive(boolean continusousAnalysisActive) {
		if (continusousAnalysisActive) {
			continuousAnalysisButton.setIcon(continuousAnalysisActiveIcon);
		} else {
			continuousAnalysisButton.setIcon(continuousAnalysisNotActiveIcon);
		}

	}

}
