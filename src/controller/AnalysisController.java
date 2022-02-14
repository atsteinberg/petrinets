/**
 * 
 */
package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.AccessibilityGraph;
import model.MyParser;
import model.Petrinet;
import view.InternalPetriFrame;
import view.InternalPetriFrameListener;
import view.MainFrame;

/**
 * @author Alexander Steinberg
 * 
 *         The controller for batch analyses Runs a batch analysis of some files
 *         and displays it in an internal frame for batch analyses
 * 
 */
public class AnalysisController implements InternalPetriFrameListener {

	private static int id = 1;

	private InternalPetriFrame internalFrame;

	private Petrinet petrinet;

	private AccessibilityGraph accessibilityGraph;

	private List<String> resultTableRows;

	private boolean[] boundedAnalysisResults;

	private String[] fileNames;

	private int[] nodeCounts;

	private int[] edgeCounts;

	private int[] pathLengths;

	private String[] pathStrings;

	private String[] mStateNames;

	private String[] mPrimeStateNames;

	/**
	 * constructor for the analysis controller. Adds an internal frame that displays
	 * the analysis results for the chosen files.
	 * 
	 * @param files     the files to analyse
	 * @param mainFrame the mainFrame in which an internal frame with the analysis
	 *                  is displayed
	 */
	public AnalysisController(File[] files, MainFrame mainFrame) {
		String title = "Stapelanalyse #" + id++;

		petrinet = new Petrinet();
		accessibilityGraph = new AccessibilityGraph(petrinet);

		internalFrame = mainFrame.addInternalFrame(title, this);
		mainFrame.initializeInternalAnalysisFrame(internalFrame, petrinet, title);

		resultTableRows = new ArrayList<String>();

		List<File> sortedFiles = sortAlphabetically(files);

		internalFrame.appendToTextArea(title + " gestartet.");

		analyseFiles(sortedFiles);

		printResultTable();

		for (String row : resultTableRows) {
			internalFrame.appendToTextArea(row);
		}

	}

	/**
	 * @param sortedFiles
	 */
	private void analyseFiles(List<File> sortedFiles) {
		boundedAnalysisResults = new boolean[sortedFiles.size()];
		fileNames = new String[sortedFiles.size()];
		nodeCounts = new int[sortedFiles.size()];
		edgeCounts = new int[sortedFiles.size()];
		pathLengths = new int[sortedFiles.size()];
		pathStrings = new String[sortedFiles.size()];
		mStateNames = new String[sortedFiles.size()];
		mPrimeStateNames = new String[sortedFiles.size()];

		for (int i = 0; i < sortedFiles.size(); i += 1) {
			File file = sortedFiles.get(i);
			if (file.exists()) {
				parseFile(file);
				accessibilityGraph.clear();
				accessibilityGraph.autoGenerate();
				if (accessibilityGraph.isUnbounded()) {
					internalFrame.appendToTextArea("EG: Das Petrinetz ist unbeschränkt.");
					String message = accessibilityGraph.getCurrentMAndMPrimeMessage();
					if (message != null) {
						internalFrame.appendToTextArea(message);
					}
				} else {
					internalFrame.appendToTextArea(
							"EG: Das Petrinetz ist beschränkt. Erreichbarkeitsgraph wurde vollständig generiert.");
				}
				boundedAnalysisResults[i] = accessibilityGraph.isUnbounded() ? false : true;
				fileNames[i] = file.getName();
				nodeCounts[i] = accessibilityGraph.getPetrinetStateCount();
				edgeCounts[i] = accessibilityGraph.getEdgeCount();
				pathLengths[i] = accessibilityGraph.getPathLength();
				pathStrings[i] = accessibilityGraph.getPathString();
				mStateNames[i] = accessibilityGraph.getmStateName();
				mPrimeStateNames[i] = accessibilityGraph.getmPrimeStateName();
			}
		}

	}

	/**
	 * append results table to internal frame text box
	 */
	private void printResultTable() {
		int fileNameColumnWidth = getMaxWidth(fileNames);
		int nodeCountColumnWidth = getMaxWidth(nodeCounts);
		int edgeCountColumnWidth = getMaxWidth(edgeCounts);
		int pathLengthColumnWidth = getMaxWidth(pathLengths) + 1;
		int pathStringColumnWidth = getMaxWidth(pathStrings) + 1;
		int mStateNameColumnWidth = getMaxWidth(mStateNames) + 1;
		int mPrimeStateNameColumnWidth = getMaxWidth(mPrimeStateNames);
		int firstColumnWidth = Math.max(fileNameColumnWidth + 1, "Dateiname".length());
		int secondColumnWidth = "beschränkt".length() + 2;
		int thirdColumnWidth = Math.max(
				pathLengthColumnWidth + pathStringColumnWidth + mStateNameColumnWidth + mPrimeStateNameColumnWidth + 3,
				" Pfadlänge:Pfad; m, m'".length());
		int totalWidth = firstColumnWidth + secondColumnWidth + thirdColumnWidth + 2;
		String fileNameFormat = getStringFormat(firstColumnWidth);
		String boundedStringFormat = getStringFormat(secondColumnWidth);
		String nodeCountFormat = getIntFormat(nodeCountColumnWidth);
		String edgeCountFormat = getIntFormat(edgeCountColumnWidth);
		String pathLengthFormat = getIntFormat(pathLengthColumnWidth);
		String pathStringFormat = getStringFormat(pathStringColumnWidth);
		String mStateNameFormat = getStringFormat(mStateNameColumnWidth);
		String mPrimeStateNameFormat = getStringFormat(mPrimeStateNameColumnWidth);

		// Table header
		internalFrame.appendToTextArea(getRepeatingString("-", totalWidth));
		internalFrame.appendToTextArea(getRepeatingString(" ", firstColumnWidth) + "|"
				+ getRepeatingString(" ", secondColumnWidth) + "| Knoten / Kanten bzw.");
		internalFrame
				.appendToTextArea(String.format(fileNameFormat, "Dateiname") + "| beschränkt | Pfadlänge:Pfad; m, m'");
		internalFrame.appendToTextArea(getRepeatingString("-", totalWidth));

		// Table body
		for (int i = 0; i < fileNames.length; i += 1) {
			String firstColumn = String.format(fileNameFormat, fileNames[i]);
			String secondCoumn = String.format(boundedStringFormat, boundedAnalysisResults[i] ? " ja" : " nein");
			String thirdColumn;
			if (boundedAnalysisResults[i]) {
				thirdColumn = " " + String.format(nodeCountFormat, nodeCounts[i]) + " / "
						+ String.format(edgeCountFormat, edgeCounts[i]);
			} else {
				thirdColumn = " " + String.format(pathLengthFormat, pathLengths[i] + ":")
						+ String.format(pathStringFormat, pathStrings[i] + ";") + " "
						+ String.format(mStateNameFormat, mStateNames[i] + ",") + " "
						+ String.format(mPrimeStateNameFormat, mPrimeStateNames[i]);
			}
			internalFrame.appendToTextArea(firstColumn + "|" + secondCoumn + "|" + thirdColumn);
		}
	}

	/**
	 * @param width
	 * @return
	 */
	private String getIntFormat(int width) {
		return "%" + width + "s";
	}

	/**
	 * @param width
	 * @return
	 */
	private String getStringFormat(int width) {
		return "%-" + width + "s";
	}

	/**
	 * @param string
	 * @param repeatCount
	 * @return repeatCount many repeats of string
	 */
	private String getRepeatingString(String string, int repeatCount) {
		String result = "";
		for (int i = 0; i < repeatCount; i += 1) {
			result += string;
		}
		return result;
	}

	/**
	 * @param integers
	 * @return the maximum width of a decimal representation of the integers
	 */
	private int getMaxWidth(int[] integers) {
		int maxWidth = 1;
		for (int integer : integers) {
			int currentWidth = (int) Math.floor(Math.log10(integer)) + 1;
			if (currentWidth > maxWidth) {
				maxWidth = currentWidth;
			}
		}
		return maxWidth;
	}

	/**
	 * @param strings
	 * @return the max width of the strings
	 */
	private int getMaxWidth(String[] strings) {
		int maxWidth = 0;
		for (String string : strings) {
			if (string.length() > maxWidth) {
				maxWidth = string.length();
			}
		}
		return maxWidth;
	}

	/**
	 * @param files
	 * @return the alphabetically sorted list of files
	 */
	private List<File> sortAlphabetically(File[] files) {
		List<File> sortedFiles = Arrays.asList(files);
		sortedFiles.sort((file1, file2) -> file1.getName().compareTo(file2.getName()));
		return sortedFiles;
	}

	@Override
	public void onReloadFrame(int id) {
		// unimplemented for analysis frames
	}

	/**
	 * @param file
	 */
	private void parseFile(File file) {
		petrinet.clear();
		MyParser pnmlParser = new MyParser(file, petrinet);
		pnmlParser.initParser();
		pnmlParser.parse();
		internalFrame.appendToTextArea("Datei " + file.getName() + " geladen.");
	}

}
