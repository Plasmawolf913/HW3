/**
 * @author tfolkers
 */
package edu.iastate.cs2280.hw2;

public class SelectionSorter extends AbstractSorter{

	protected SelectionSorter(Student[] students) throws IllegalArgumentException {
		super(students);
		
	}

	@Override
	public void sort() {
		int n = students.length;

	    for (int i = 0; i < n - 1; i++) {
	        int minIndex = i;

	        // Find the smallest element (based on compareTo)
	        for (int j = i + 1; j < n; j++) {
	            if (students[j].compareTo(students[minIndex]) < 0) {
	                minIndex = j;
	            }
	        }

	        // Swap the found minimum element with the first unsorted element
	        if (minIndex != i) {
	            Student temp = students[i];
	            students[i] = students[minIndex];
	            students[minIndex] = temp;
	        }
	    }
		
	}

}
