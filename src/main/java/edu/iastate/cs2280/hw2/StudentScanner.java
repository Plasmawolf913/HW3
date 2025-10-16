/**
 * @author tfolkers
 */
package edu.iastate.cs2280.hw2;

/**
 * This class orchestrates the process of finding a "median student" from an array
 * of Student objects. It does this by sorting the array twice: once by GPA to find
 * the median GPA, and once by credits taken to find the median number of credits.
 * It then constructs a new Student object with these median values. The class
 * also measures the total time taken for both sorting operations for a specific
 * sorting algorithm.
 */
public class StudentScanner {
  /**
   * A copy of the student data to be processed.
   */
  private final Student[] students;
  /**
   * The sorting algorithm to be used for finding the medians.
   */
  private final Algorithm sortingAlgorithm;
  /**
   * The total time in nanoseconds taken by the two sorting operations.
   */
  protected long scanTime;
  /**
   * The resulting student object with median GPA and median credits.
   */
  private Student medianStudent;
  

  /**
   * Constructs a StudentScanner. It takes an array of students and the sorting
   * algorithm to use. A deep copy of the students array is made to avoid
   * modifying the original array.
   *
   * @param students The array of students to scan.
   * @param algo  The sorting algorithm to use.
   * @throws IllegalArgumentException if the input students array is null or empty.
   */
  public StudentScanner(Student[] students, Algorithm algo) {
	  if(students == null || students.length < 1) {
		  throw new IllegalArgumentException("Student array must have at least 1 value");
	  }
	  
	  this.students = new Student[students.length];
	  this.sortingAlgorithm = algo;
	  
	  for(int i = 0; i < students.length; i++) {
		  this.students[i] = students[i];
	  }
	  
	  

  }

  /**
   * Executes the scanning process. It creates an appropriate sorter based on the
   * specified algorithm, sorts the students array by GPA, finds the median GPA,
   * then sorts by credits, and finds the median credits. It calculates the total
   * time and creates the medianStudent.
   */
  public void scan() {
	  long start = System.nanoTime();

      // 1) Make a sorter instance with a fresh deep copy (handled by AbstractSorter)
      AbstractSorter sorter = switch (sortingAlgorithm) {
          case SelectionSort -> new SelectionSorter(students);
          case InsertionSort -> new InsertionSorter(students);
          case MergeSort     -> new MergeSorter(students);
          case QuickSort     -> new QuickSorter(students);
      };

      // Pass A: Order 0 (by GPA desc, tie credits desc)
      sorter.setComparator(0);
      sorter.sort();
      double medianGpa = sorter.getMedian().getGpa();

      // Pass B: Order 1 (by credits asc, tie GPA desc)
      // Make a new sorter to avoid in-place side-effects OR reuse but re-copy students yourself.
      sorter = switch (sortingAlgorithm) {
          case SelectionSort -> new SelectionSorter(students);
          case InsertionSort -> new InsertionSorter(students);
          case MergeSort     -> new MergeSorter(students);
          case QuickSort     -> new QuickSorter(students);
      };
      sorter.setComparator(1);
      sorter.sort();
      int medianCredits = sorter.getMedian().getCreditsTaken();

      medianStudent = new Student(medianGpa, medianCredits);
      scanTime = System.nanoTime() - start;
  }

  /**
   * Returns a formatted string containing the performance statistics of the scan.
   *
   * @return A string with the algorithm name, data size, and total scan time.
   */
  public String stats() {
    return String.format("%-15s %-5d %-10d", sortingAlgorithm, students.length, scanTime);
  }

  /**
   * Gets the calculated median student after a scan has been performed.
   *
   * @return The median student.
   */
  public Student getMedianStudent() {
	  return medianStudent;
  }

  /**
   * Provides a string representation of the StudentScanner's result.
   *
   * @return A string indicating the median student profile.
   */
  @Override
  public String toString() {
    return "Median Student: " + medianStudent.toString();
  }
}
