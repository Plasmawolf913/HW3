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
	
	    // Order the first, middle, and last elements
	    if (students[middle].compareTo(students[first]) < 0) {
	        swap(students, first, middle);
	    }
	    if (students[last].compareTo(students[first]) < 0) {
	        swap(students, first, last);
	    }
	    if (students[last].compareTo(students[middle]) < 0) {
	        swap(students, middle, last);
	    }
	
	    // Move the median element (currently at middle) to position last - 1
	    swap(students, middle, last - 1);
	}

	private static void swap(Student[] students, int i, int j) {
	    Student temp = students[i];
	    students[i] = students[j];
	    students[j] = temp;
	}

  

  @Override
  public void sort() {
	  
	  if (first >= last) return;

      int localFirst = first;
      int localLast = last;

      if (localLast - localFirst + 1 >= 3) {
          medianOfThree(localFirst, localLast);
          Student pivot = students[localLast - 1];

          int i = localFirst;
          int j = localLast - 2;

          while (true) {
              while (++i <= localLast - 2 && students[i].compareTo(pivot) < 0) {}
              while (--j >= localFirst && students[j].compareTo(pivot) > 0) {}
              if (i >= j) break;
              swap(i, j);
          }

          swap(i, localLast - 1);

          // recurse on left side
          first = localFirst;
          last = i - 1;
          sort();

          // recurse on right side
          first = i + 1;
          last = localLast;
          sort();

          // restore
          first = localFirst;
          last = localLast;
      } else {
          if (localFirst < localLast && students[localLast].compareTo(students[localFirst]) < 0) {
              swap(localFirst, localLast);
          }
      }
  }
}

