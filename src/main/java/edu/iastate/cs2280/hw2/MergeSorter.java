/**
 * @author tfolkers
 */
package edu.iastate.cs2280.hw2;

public class MergeSorter extends AbstractSorter {


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
	    mergeSortRec(0, students.length - 1);
	}

	/**
	 * merges the arrays using .compare from studentcomparator instead of < or >
	 * @param lo
	 * @param mid
	 * @param hi
	 */
	  private void merge(int first, int mid, int last) {
	    Student[] temp = new Student[last - first + 1];
	    int left = first, right = mid + 1, k = 0;

	    // merge while both halves have elements
	    while (left <= mid && right <= last) {
	      if (studentComparator.compare(students[left], students[right]) <= 0) {
	        temp[k++] = students[left++];
	      } else {
	        temp[k++] = students[right++];
	      }
	    }

	    // copy remaining from left
	    while (left <= mid) temp[k++] = students[left++];

	    // copy remaining from right
	    while (right <= last) temp[k++] = students[right++];

	    // copy merged result back into students
	    System.arraycopy(temp, 0, students, first, temp.length);
	  }

	
	/**
	   * Recursively divides and sorts the subarray students[first..last].
	   */
	  private void mergeSortRec(int first, int last) {
	    if (first >= last) return;

	    int mid = (first + last) / 2;

	    // recursively sort both halves
	    mergeSortRec(first, mid);
	    mergeSortRec(mid + 1, last);

	    // merge the sorted halves
	    merge(first, mid, last);
	  }

}


