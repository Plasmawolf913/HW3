/**
 * @author tfolkers
 */
package edu.iastate.cs2280.hw2;

public class InsertionSorter extends AbstractSorter {


	protected InsertionSorter(Student[] students) throws IllegalArgumentException {
		super(students);
		
	}

	@Override
	public void sort() {
		for (int i = 1; i < students.length; i++) {
	        Student key = students[i];
	        int j = i - 1;

	        // Move elements that are "less" than key (based on compareTo)
	        // so that we maintain the correct order
	        while (j >= 0 && studentComparator.compare(key, students[j]) > 0) {
	            students[j + 1] = students[j];
	            j--;
	        }
	        students[j + 1] = key;
	    }
		
	}

}
