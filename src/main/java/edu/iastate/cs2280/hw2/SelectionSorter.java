/**
 * @author tfolkers
 */
package edu.iastate.cs2280.hw2;
/**
 * Selection sorter class that extends abstract sorter to implement
 * the sort method for students.
 */
public class SelectionSorter extends AbstractSorter{
/**
 * Selection sorter class
 * @param students array
 * @throws IllegalArgumentException if argument is wrong
 */
	public SelectionSorter(Student[] students) throws IllegalArgumentException {
		super(students);
		
	}

	/**
	 * sorts the array given using selection sort implementation, uses
	 * .compare according to the comparator type outlined in abstract sorter
	 * instead of < or >
	 */
	@Override
	public void sort() {
		int n = students.length;

	    for (int i = 0; i < n - 1; i++) {
	        int minIndex = i;

	        // find the smallest element (based on compareTo)
	        for (int j = i + 1; j < n; j++) {
	            if (studentComparator.compare(students[j], students[minIndex]) < 0) {
	                minIndex = j;
	            }
	        }

	        // swap the found minimum element with the first unsorted element
	        if (minIndex != i) {
	            Student temp = students[i];
	            students[i] = students[minIndex];
	            students[minIndex] = temp;
	        }
	    }
		
	}

}
