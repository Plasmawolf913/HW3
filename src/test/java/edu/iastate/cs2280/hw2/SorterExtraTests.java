package edu.iastate.cs2280.hw2;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;
import java.util.function.Function;

//Add these imports at top if not present
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.DisplayName;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SorterExtraTests {

 private static Student s(double g, int c) { return new Student(g, c); }

 // Reuse  isSorted helper from SorterTest 
 private static boolean isSorted(AbstractSorter sorter, int order) {
     try {
         var f = AbstractSorter.class.getDeclaredField("students");
         f.setAccessible(true);
         Student[] a = (Student[]) f.get(sorter);

         for (int i = 1; i < a.length; i++) {
             int cmp;
             if (order == 0) {
                 cmp = Comparator
                     .comparing(Student::getGpa).reversed()
                     .thenComparing(Comparator.comparing(Student::getCreditsTaken).reversed())
                     .compare(a[i - 1], a[i]);
             } else {
                 cmp = Comparator
                     .comparing(Student::getCreditsTaken)
                     .thenComparing(Comparator.comparing(Student::getGpa).reversed())
                     .compare(a[i - 1], a[i]);
             }
             if (cmp > 0) return false;
         }
         return true;
     } catch (Exception e) {
         return false;
     }
 }

 // elper to run sorters
 private void runAndAssertSorted(AbstractSorter sorter, int order) {
     sorter.setComparator(order);
     sorter.sort();
     assertTrue(isSorted(sorter, order));
 }

 // checks for a single sorter ----------
 private void checkSorterBothOrdersForAllConditions(Function<Student[], AbstractSorter> ctor, String sorterName) {
     // Test single-element
     Student[] one = { s(3.5, 40) };
     for (int order = 0; order <= 1; order++) {
         AbstractSorter st = ctor.apply(one.clone());
         runAndAssertSorted(st, order);
     }

     // already sorted (order 0 sorted descending by gpa then credits)
     Student[] sortedByGpaDesc = {
         s(4.0, 90),
         s(3.8, 70),
         s(3.6, 50),
         s(3.6, 30)
     };
     Student[] sortedByCreditsAsc = {
         s(3.6, 10),
         s(3.8, 30),
         s(3.9, 50),
         s(4.0, 90)
     };
     for (int order = 0; order <= 1; order++) {
         AbstractSorter st = ctor.apply((order == 0) ? sortedByGpaDesc.clone() : sortedByCreditsAsc.clone());
         runAndAssertSorted(st, order);
     }

     // test reverse-sorted
     Student[] revGpa = {
         s(2.0, 10),
         s(2.5, 20),
         s(3.0, 30),
         s(4.0, 90)
     }; // unsorted for order 0
     Student[] revCredits = {
         s(4.0, 90),
         s(3.0, 70),
         s(2.0, 50),
         s(1.0, 10)
     }; // unsorted for order 1
     for (int order = 0; order <= 1; order++) {
         AbstractSorter st = ctor.apply((order == 0) ? revGpa.clone() : revCredits.clone());
         runAndAssertSorted(st, order);
     }

     // Test duplicates (tie-breaker behaviour)
     Student[] duplicates = {
         s(3.5, 50),
         s(3.5, 50),
         s(3.5, 40),
         s(3.5, 60)
     };
     for (int order = 0; order <= 1; order++) {
         AbstractSorter st = ctor.apply(duplicates.clone());
         runAndAssertSorted(st, order);
     }
 }

 // tests for each sort 
 @Test

 void selectionSorter_allConditions() {
     checkSorterBothOrdersForAllConditions(arr -> new SelectionSorter(arr), "SelectionSorter");
 }

 @Test

 void insertionSorter_allConditions() {
     checkSorterBothOrdersForAllConditions(arr -> new InsertionSorter(arr), "InsertionSorter");
 }

 @Test

 void mergeSorter_allConditions() {
     checkSorterBothOrdersForAllConditions(arr -> new MergeSorter(arr), "MergeSorter");
 }

 @Test

 void quickSorter_allConditions() {
     checkSorterBothOrdersForAllConditions(arr -> new QuickSorter(arr), "QuickSorter");
 }
}
