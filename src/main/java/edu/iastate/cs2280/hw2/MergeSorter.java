/**
 * @author tfolkers
 */
package edu.iastate.cs2280.hw2;

public class MergeSorter extends AbstractSorter {

	private Student[] aux;
	protected MergeSorter(Student[] students) throws IllegalArgumentException {
		super(students);
		
	}

	@Override
	public void sort() {
		if (students == null || students.length < 2) return;

        aux = new Student[students.length];

        // Bottom-up (iterative) mergesort: no recursive helpers/params needed
        for (int width = 1; width < students.length; width <<= 1) {
            for (int lo = 0; lo < students.length - width; lo += (width << 1)) {
                int mid = lo + width - 1;
                int hi  = Math.min(lo + (width << 1) - 1, students.length - 1);

                // Optional skip if already in order
                if (students[mid].compareTo(students[mid + 1]) <= 0) continue;

                merge(lo, mid, hi);
            }
        }

        aux = null; // let GC reclaim
		
	}
	
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
            } else if (aux[j].compareTo(aux[i]) < 0) {
                // aux[j] should come before aux[i]
                students[k] = aux[j++];
            } else {
                students[k] = aux[i++];
            }
        }
    }
}


