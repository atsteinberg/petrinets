/**
 * 
 */
package view;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * 
 * filter that filters out pnml files. Also allows to show folders.
 * 
 * @author Alexander Steinberg
 *
 */
public class PnmlFileFilter extends FileFilter {

	@Override
	public String getDescription() {
		return "pnml File";
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		String fileName = f.getName();
		int extensionStartIndex = fileName.lastIndexOf(".") + 1;
		if (extensionStartIndex != 0) {
			String extension = fileName.substring(extensionStartIndex);
			return extension.equals("pnml");
		}
		return false;
	}

}
