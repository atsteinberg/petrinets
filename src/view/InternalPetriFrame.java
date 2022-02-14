/**
 * 
 */
package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.graphstream.ui.view.ViewerListener;

import model.AccessibilityGraph;
import model.Petrinet;

/**
 * class that represents an internal frame for displaying petrinets and batch
 * analysis results.
 * 
 * @author Alexander Steinberg
 *
 */
public class InternalPetriFrame extends JInternalFrame implements AccessibilityGraphViewListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6989396654227520971L;

	private Petrinet petrinet;

	private JpnlGraph jpnlGraph;

	private AccessibilityGraphView accessibilityGraphView;

	private PetriGraphPanel jpnlGraphPanel;

	private PetriGraphPanel accessibilityGraphPanel;

	private JTextArea textArea;

	private ToolBar toolBar;

	private InternalPetriFrameListener listener;

	private JScrollPane textScrollPane;

	private JPanel statusPanel;

	private JLabel statusLabel;

	/**
	 * Constructor for InternalPetriFrame class, a kind of JInternalFrame for
	 * displaying petrinets and batch analyses.
	 * 
	 * @param title    the title of the internal frame
	 * @param listener the AccessibilityGraphViewListener to set
	 */
	InternalPetriFrame(String title, InternalPetriFrameListener listener) {
		super(title, true, true, true, true);
		this.title = title;
		this.listener = listener;

		setMinimumSize(new Dimension(400, 300));

		textArea = new JTextArea();
		textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		textArea.setEditable(false);
		textScrollPane = new JScrollPane(textArea);
		Border emptyBorder = BorderFactory.createEmptyBorder(0, 10, 0, 10);
		textScrollPane.setBorder(emptyBorder);

		setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);

	}

	/**
	 * initializes the internal petri frame as a frame to display a petrinet and its
	 * corresponding accessibility graph. Sets the petrinet and accessibilitygraph
	 * to display by its views, as well as a listener that listens for mouse events
	 * on these views.
	 * 
	 * @param petrinet the displayed petrinet
	 * @param accessibilityGraph the displayed accessibilityGraph
	 * @param viewerListener the ViewerListener to be registered
	 */
	void initializeInternalPetriFrame(Petrinet petrinet, AccessibilityGraph accessibilityGraph,
			ViewerListener viewerListener) {
		this.petrinet = petrinet;

		jpnlGraph = new JpnlGraph("jpnlGraph: " + title, petrinet);
		jpnlGraphPanel = new PetriGraphPanel(jpnlGraph);
		jpnlGraphPanel.disableAutoLayout();
		jpnlGraphPanel.addViewerListener(viewerListener);

		accessibilityGraphView = new AccessibilityGraphView("accessibilityGraph: " + title, accessibilityGraph, this);
		accessibilityGraphPanel = new PetriGraphPanel(accessibilityGraphView);
		accessibilityGraphPanel.enableAutoLayout();
		accessibilityGraphPanel.addViewerListener(viewerListener);

		JSplitPane graphSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jpnlGraphPanel,
				accessibilityGraphPanel);
		graphSplitPane.setResizeWeight(0.6);
		JSplitPane textBoxSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, graphSplitPane, textScrollPane);
		textBoxSplitPane.setResizeWeight(0.85);

		add(textBoxSplitPane);

		toolBar = new ToolBar("Petrinet Tools");

		add(toolBar, BorderLayout.NORTH);

		statusPanel = new JPanel();
		Border bevelBorder = new BevelBorder(BevelBorder.RAISED);
		Border emptyBorder = new EmptyBorder(2, 10, 2, 10);
		statusPanel.setBorder(BorderFactory.createCompoundBorder(bevelBorder, emptyBorder));
		add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel = new JLabel(PetrinetStatus.UNMODIFIED.toString());
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);

		setVisible(true);
	}

	/**
	 * initializes the internal petri frame as a frame to display a batch analysis.
	 * Sets the petrinet to use for batch analysis.
	 * 
	 * @param petrinet the petrinet to use for batch analysis
	 */
	void initializeInternalAnalysisFrame(Petrinet petrinet) {
		this.petrinet = petrinet;

		textArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		add(textScrollPane);

		setVisible(true);

	}

	/**
	 * set the initial size of the frame to fill the remaining horizontal and
	 * vertical space.
	 * 
	 */
	void setInitialSize() {
		JDesktopPane parent = this.getDesktopPane();
		setSize(parent.getWidth() - this.getX(), parent.getHeight());
	}

	/**
	 * 
	 * @return the id of the internal petri frame.
	 */
	int getId() {
		return this.petrinet.getId();
	}

	/**
	 * draw both petrinet and accessibility graph views from scratch.
	 */
	public void drawGraphs() {
		drawJpnlGraph();
		drawAccessibilityGraph();
	}

	/**
	 * update both petrinet and accessibility graph views to reflect current state
	 * of the petrinet.
	 */
	public void updateGraphs() {
		updateJpnlGraph();
		updateAccessibilityGraph();
	}

	/**
	 * Appends text to the internal frame's bottom text area in a new line.
	 * 
	 * @param string the text to append
	 */
	public void appendToTextArea(String string) {
		if (textArea.getText().length() > 0) {
			textArea.append("\n");
		}
		textArea.append(string);
	}

	@Override
	public void accessibilityGraphViewChanged(String description) {
		appendToTextArea("EG: " + description);
	}

	/**
	 * clear the contents of the text area.
	 */
	public void clearTextArea() {
		textArea.setText(null);
	}

	/**
	 * set the internal frame's toolbar listener
	 * 
	 * @param listener the ToolBarListener to pass to the ToolBar
	 */
	public void setToolBarListener(ToolBarListener listener) {
		toolBar.setToolBarListener(listener);
	}

	/**
	 * reload current petrinet from file.
	 */
	void reloadPetrinet() {
		listener.onReloadFrame(getId());
	}

	/**
	 * get currently selected place from petrinet and update the view accordingly.
	 */
	public void updateSelection() {
		jpnlGraph.updateSelection();
	}

	/**
	 * Convenice method equivalent to {@code enableTokenButtons(enable, enable)}
	 * 
	 * @param enable true if token buttons should both be enabled, false if both
	 *               should be disabled.
	 */
	public void enableTokenButtons(boolean enable) {
		toolBar.enableTokenButtons(enable);
	}

	/**
	 * Enable token buttons in toolbar.
	 * 
	 * @param enablePlus  true if add token button should be enabled, false
	 *                    otherwise
	 * @param enableMinus true of subtract token button should be enabled, false
	 *                    otherwise
	 */
	public void enableTokenButtons(boolean enablePlus, boolean enableMinus) {
		toolBar.enableTokenButtons(enablePlus, enableMinus);
	}

	/**
	 * update petrinet view to reflect the current state of its petrinet.
	 */
	public void updateJpnlGraph() {
		jpnlGraph.updateGraph();
	}

	private void updateAccessibilityGraph() {
		accessibilityGraphView.updateGraph();
	}

	private void drawJpnlGraph() {
		jpnlGraph.drawGraph();
	}

	/**
	 * draw a newly initialized accessibility graph for the petrinet
	 */
	public void drawAccessibilityGraph() {
		accessibilityGraphView.drawGraph();
	}

	/**
	 * enable/disable continuous analysis of the petrinet.
	 * 
	 * @param continuousAnalysisActive true of continuousAnalysis is turned on,
	 *                                 false otherwise
	 */
	public void setContinuousAnalysisActive(boolean continuousAnalysisActive) {
		if (toolBar != null) {
			toolBar.setContinuousAnalysisActive(continuousAnalysisActive);
		}
		if (accessibilityGraphView != null) {
			accessibilityGraphView.setContinuousAnalysisActive(continuousAnalysisActive);
		}
	}

	/**
	 * draw the complete accessibilityGraph from scratch.
	 */
	public void drawGeneratedGraphs() {
		updateJpnlGraph();
		accessibilityGraphView.drawGraphFromScratch();
	}

	/**
	 * clear the current state css highlighting of the accessibility graph view
	 */
	public void clearAccessibilityGraphHighlights() {
		accessibilityGraphView.clearCurrentStateHighlights();
	}

	/**
	 * set status of the petrinet used in the status bar of the internal frame
	 * 
	 * @param status the PetrinetStatus to set
	 */
	public void setStatus(PetrinetStatus status) {
		statusLabel.setText(status.toString());
	}

	/**
	 * enable/disable the navigation button in the toolbar.
	 * 
	 * @param enablePrevious true if previous file button is enabled, false if disabled
	 * @param enableNext true if next file button is enabled, false if disabled
	 */
	public void enableNavigationButtons(boolean enablePrevious, boolean enableNext) {
		if (toolBar != null) {
			toolBar.enableNavigationButtons(enablePrevious, enableNext);
		}
	}

	/**
	 * Show an info dialog that displays the message.
	 * 
	 * @param message the message to display
	 */
	public void showInfoPanel(String message) {
		JOptionPane.showMessageDialog(this, message, "Analyse erfolgreich abgeschlossen",
				JOptionPane.INFORMATION_MESSAGE, MainFrame.getInfoIcon());
	}

	/**
	 * 
	 */

}
