/**
 * @author tfolkers
 */
package edu.iastate.cs2280.hw2;

/**
 * This class implements the quicksort algorithm for an array of Student objects.
 * It uses a median-of-three pivot selection strategy to improve performance and
 * avoid worst-case scenarios with already sorted or reverse-sorted data.
 */
public class QuickSorter extends AbstractSorter {

      private int first;
      private int last;
	  protected QuickSorter(Student[] students) throws IllegalArgumentException {
			super(students);
			first = 0;
			last = students.length-1;
		}

	  /**
	   * Selects a pivot using the median-of-three strategy. It considers the first,
	   * middle, and last elements of the subarray, sorts them, and uses the median
	   * as the pivot. The pivot is swapped to the second-to-last position (last - 1)
	   * to simplify the partition step.
	   *
	   * @param first The starting index of the subarray.
	   * @param last  The ending index of the subarray.
	   */
	  private void medianOfThree(int first, int last) {
		    int middle = (first + last) / 2;

		    // order first, middle, last under the current comparator
		    if (studentComparator.compare(students[middle], students[first]) < 0) {
		        swap(first, middle);
		    }
		    if (studentComparator.compare(students[last], students[first]) < 0) {
		        swap(first, last);
		    }
		    if (studentComparator.compare(students[last], students[middle]) < 0) {
		        swap(middle, last);
		    }

		    // move median (currently at middle) to last - 1 for partition
		    swap(middle, last - 1);
		}
	  


  

	@Override
	public void sort() {
	    if (first >= last) return;

	    int localFirst = first;
	    int localLast  = last;

	    if (localLast - localFirst + 1 >= 3) {
	        medianOfThree(localFirst, localLast);
	        Student pivot = students[localLast - 1];     // median moved here

	        int i = localFirst;
	        int j = localLast - 2;

	        while (true) {
	            while (++i <= localLast - 2 &&
	                   studentComparator.compare(students[i], pivot) < 0) { /* advance i */ }

	            while (--j >= localFirst &&
	                   studentComparator.compare(students[j], pivot) > 0) { /* advance j */ }

	            if (i >= j) break;
	            swap(i, j);
	        }

	        swap(i, localLast - 1); // restore pivot to its final place

	        // recurse left
	        first = localFirst;
	        last  = i - 1;
	        sort();

	        // recurse right
	        first = i + 1;
	        last  = localLast;
	        sort();

	        // restore fields
	        first = localFirst;
	        last  = localLast;
	    } else {
	        // small array of size 2: order under the current comparator
	        if (localFirst < localLast &&
	            studentComparator.compare(students[localLast], students[localFirst]) < 0) {
	            swap(localFirst, localLast);
	        }
	    }
	}

	
}