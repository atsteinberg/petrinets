/**
 * 
 */
package view;

import java.util.Arrays;

import org.graphstream.graph.Element;

/**
 * utility class to make the missing functions addAttribute and removeAttribute
 * availabe for MultiGraph Elements.
 * 
 * @author Alexander Steinberg
 *
 */
abstract class PetriGraphUtils {

	/**
	 * Add a new value to the attribute with the passed key of the passed element.
	 * If the attribute is currently unset it gets sets. If it is already set, the
	 * new value is added to the currently existing ones.
	 * 
	 * @param element  the element to modify
	 * @param key      the key of the attribute
	 * @param newValue the value to be added
	 */
	static void addAttribute(Element element, String key, String newValue) {
		if (element == null) {
			return;
		}
		String oldValue = element.getAttribute(key, String.class);
		if (oldValue != null) {
			// one formerly set value
			Object[] oldValues = { oldValue };
			Object[] newValues = addTo(oldValues, newValue);
			element.setAttribute(key, newValues);
		} else {
			Object[] oldValues = element.getAttribute(key, Object[].class);
			if (oldValues != null) {
				// several formerly set values
				Object[] newValues = addTo(oldValues, newValue);
				element.setAttribute(key, newValues);
			} else {
				// no formerly set value
				element.setAttribute(key, newValue);
			}
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param array
	 * @param value
	 * @return an expansion of array with value
	 */
	private static <T> Object[] addTo(T[] array, T value) {
		if (value == null) {
			return array;
		}
		for (T item : array) {
			if (value.equals(item)) {
				return array;
			}
		}
		Object[] valuesToAdd = Arrays.copyOf(array, array.length + 1);
		valuesToAdd[array.length] = value;
		return valuesToAdd;
	}

	/**
	 * Remove a value from the attribute with the passed key from the passed
	 * element. If the value to remove is the only value for the attribute, the
	 * attribute is removed completely. Otherwise the value is removed from the
	 * values array.
	 * 
	 * @param element the element to modify
	 * @param key the attribute's key
	 * @param valueToRemove the value to remove
	 */
	static void removeAttribute(Element element, String key, String valueToRemove) {
		if (element == null) {
			return;
		}
		String oldValue = element.getAttribute(key, String.class);
		if (oldValue != null) {
			if (oldValue.equals(valueToRemove)) {
				element.removeAttribute(key);
			}
		}
		Object[] oldValues = element.getAttribute(key, Object[].class);
		if (oldValues != null) {
			Object[] newValues = removeFrom(oldValues, valueToRemove);
			element.setAttribute(key, newValues);
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param array
	 * @param item
	 * @return array without item
	 */
	private static <T> Object[] removeFrom(T[] array, T item) {
		Object[] newArray = new Object[array.length];
		int j = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] != item) {
				newArray[j++] = array[i];
			}
		}
		return newArray;
	}
}
