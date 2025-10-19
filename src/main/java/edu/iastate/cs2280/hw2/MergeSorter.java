/**
 * @author tfolkers
 */
package edu.iastate.cs2280.hw2;

public class MergeSorter extends AbstractSorter {

	/**
	 * secondary array to make sorting easier
	 */
	private Student[] aux;
	/**
	 * Implements mergesort by extending abstractsorter
	 * @param students array
	 * @throws IllegalArgumentException
	 */
	protected MergeSorter(Student[] students) throws IllegalArgumentException {
		super(students);
		
	}

	/**
	 * breaks the array into small pieces until it gets down to 1 piece (mergesort) and then
	 * merges the arrays
	 */
	@Override
	public void sort() {
	    if (students == null || students.length < 2) return;

	    aux = new Student[students.length];

	    
	    for (int width = 1; width < students.length; width <<= 1) {
	        for (int lo = 0; lo < students.length - width; lo += (width << 1)) {
	            int mid = lo + width - 1;
	            int hi  = Math.min(lo + (width << 1) - 1, students.length - 1);

	            // skip if boundary already in order under the CURRENT comparator
	            if (studentComparator.compare(students[mid], students[mid + 1]) <= 0) continue;

	            merge(lo, mid, hi);
	        }
	    }

	    aux = null; //free it like c
	}

	/**
	 * merges the arrays using .compare from studentcomparator instead of < or >
	 * @param lo
	 * @param mid
	 * @param hi
	 */
	private void merge(int lo, int mid, int hi) {
	    // copy to aux
	    for (int k = lo; k <= hi; k++) aux[k] = students[k];

	    int i = lo;       // pointer in left half
	    int j = mid + 1;  // pointer in right half

	    for (int k = lo; k <= hi; k++) {
	        if (i > mid) {
	            students[k] = aux[j++];
	        } else if (j > hi) {
	            students[k] = aux[i++];
	        } else if (studentComparator.compare(aux[j], aux[i]) < 0) {
	            // Right item strictly "smaller" by comparator so take from right
	            students[k] = aux[j++];
	        } else {
	            // Left item <= right so take from left
	            students[k] = aux[i++];
	        }
	    }
	}

}


