/**
 * 
 */
package view;

/**
 * enum that represents the status of a petrinet: substantially modified or unmodified.
 * 
 * @author Alexander Steinberg
 *
 */
public enum PetrinetStatus {
	/**
	 * petrinet is unmodified
	 */
	UNMODIFIED {
		public String toString() {
			return "unverändert";
		}
	},
	/**
	 * petri
	 */
	MODIFIED {
		public String toString() {
			return "modifiziert";
		}
	}
	
}
