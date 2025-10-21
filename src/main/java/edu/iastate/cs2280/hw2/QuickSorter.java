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

      /**
       * 
       * @param students array
       * @throws IllegalArgumentException
       */
	  public QuickSorter(Student[] students) throws IllegalArgumentException {
			super(students);

		}

	  /**
	   * Selects a pivot using the median-of-three strategy. It considers the first,
	   * middle, and last elements of the subarray, sorts them, and uses the median
	   * as the pivot. The pivot is swapped to the last position (last) to simplify
	   * the partition step.
	   *
	   * @param first The starting index of the subarray.
	   * @param last  The ending index of the subarray.
	   */
	  private void medianOfThree(int first, int last) {
	      int middle = (first + last) / 2;

	      // order the first, middle, and last elements under the current comparator
	      if (studentComparator.compare(students[middle], students[first]) < 0) {
	          swap(first, middle);
	      }
	      if (studentComparator.compare(students[last], students[first]) < 0) {
	          swap(first, last);
	      }
	      if (studentComparator.compare(students[last], students[middle]) < 0) {
	          swap(middle, last);
	      }

	      // move the median (currently at middle) to the last position for partitioning
	      swap(middle, last);
	  }
	  


  
	  /**
	   * Overrides abstractsorter's sort method to implement quick sort
	   */
	  @Override
	  public void sort() {
	      if (students.length <= 1) return;
	      quickSortRec(0, students.length - 1);
	  }

	  // ---- private helpers ----

	  /**
	   * recursive quick sort implementation
	   * @param low
	   * @param high
	   */
	  private void quickSortRec(int low, int high) {
	      if (low >= high) return;

	      // Place a good pivot at the end using median-of-three
	      medianOfThree(low, high);

	      int p = partition(low, high); // pivot placed at index p
	      quickSortRec(low, p - 1);
	      quickSortRec(p + 1, high);
	  }

	  /**
	   * Partition the array into low and high sections
	   */
	  private int partition(int low, int high) {
	      Student pivot = students[high];
	      int i = low - 1;
	      for (int j = low; j < high; j++) {
	          // Put items "less than or equal to" pivot (under current comparator) on the left
	          if (studentComparator.compare(students[j], pivot) <= 0) {
	              i++;
	              swap(i, j);
	          }
	      }
	      swap(i + 1, high); // final pivot spot
	      return i + 1;
	  }

	
}