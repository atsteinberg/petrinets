/**
 * 
 */
package view;

/**
 * Enum that lists possible types of menu bar clicks
 * 
 * @author Alexander Steinberg
 *
 */
public enum MenuBarClick {
	/**
	 * open a single file
	 */
	OpenFile, 
	/**
	 * quit the app
	 */
	Quit,
	/**
	 * close all windows
	 */
	CloseAllWindows,
	/**
	 * reload current petrinet from file
	 */
	ReloadCurrent,
	/**
	 * analyse multiple files
	 */
	AnalyseFiles, 
	/**
	 * show app info
	 */
	Info;
}
