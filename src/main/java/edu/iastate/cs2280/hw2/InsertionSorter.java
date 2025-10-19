/**
 * @author tfolkers
 */
package edu.iastate.cs2280.hw2;

public class InsertionSorter extends AbstractSorter {

/**
 * Insertion sort that extends abstract sorter to provide an implementation of 
 * insertion sort()
 * @param students array
 * @throws IllegalArgumentException
 */
	protected InsertionSorter(Student[] students) throws IllegalArgumentException {
		super(students);
		
	}

	/**
	 * sorts the array given using insertion sort implementation, uses
	 * .compare according to the comparator type outlined in abstract sorter
	 * instead of < or >
	 */
	@Override
	public void sort() {
		for (int i = 1; i < students.length; i++) {
            Student key = students[i];
            int j = i - 1;

            // move items that should come AFTER 'key' to the right
            while (j >= 0 && studentComparator.compare(students[j], key) > 0) {
                students[j + 1] = students[j];
                j--;
            }
            students[j + 1] = key;
        }
    }

}
