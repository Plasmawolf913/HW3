package edu.iastate.cs2280.hw2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class SorterTest {
	
	private static Student s(double g, int c) { return new Student(g, c); }

    // ------------------ SELECTION SORTER ------------------

    @Test
    void selectionSort_emptyArray_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SelectionSorter(new Student[0]);
        });
    }

    @Test
    void selectionSort_order0_and_order1() {
        Student[] data = {
            s(4.0, 90),
            s(3.8, 50),
            s(3.2, 70),
            s(2.9, 30)
        };

        // order 0 = GPA  then credits 
        SelectionSorter sel0 = new SelectionSorter(data.clone());
        sel0.setComparator(0);
        sel0.sort();
        assertTrue(isSorted(sel0, 0));

        // order 1 = credits  then GPA 
        SelectionSorter sel1 = new SelectionSorter(data.clone());
        sel1.setComparator(1);
        sel1.sort();
        assertTrue(isSorted(sel1, 1));
    }

    // ------------------ INSERTION SORTER ------------------

    @Test
    void insertionSort_emptyArray_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new InsertionSorter(new Student[0]);
        });
    }

    @Test
    void insertionSort_sorted_and_reverse() {
        Student[] sorted = {
            s(4.0, 90),
            s(3.8, 70),
            s(3.6, 50)
        };
        Student[] reversed = {
            s(3.6, 50),
            s(3.8, 70),
            s(4.0, 90)
        };

        // sorted stays sorted
        InsertionSorter ins0 = new InsertionSorter(sorted.clone());
        ins0.setComparator(0);
        ins0.sort();
        assertTrue(isSorted(ins0, 0));

        // reversed sorts properly
        InsertionSorter ins1 = new InsertionSorter(reversed.clone());
        ins1.setComparator(0);
        ins1.sort();
        assertTrue(isSorted(ins1, 0));
    }

    // ------------------ MERGE SORTER ------------------

    @Test
    void mergeSort_emptyArray_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new MergeSorter(new Student[0]);
        });
    }

    @Test
    void mergeSort_singleElement_ok() {
        Student[] one = { s(3.5, 40) };
        MergeSorter m = new MergeSorter(one.clone());
        m.setComparator(0);
        m.sort();
        assertEquals(3.5, m.getMedian().getGpa(), 1e-9);
    }

    // ------------------ QUICK SORTER ------------------

    @Test
    void quickSort_emptyArray_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new QuickSorter(new Student[0]);
        });
    }

    @Test
    void quickSort_reverseSorted_ok() {
        Student[] rev = {
            s(2.0, 10),
            s(3.0, 20),
            s(3.5, 40),
            s(4.0, 90)
        };
        QuickSorter q = new QuickSorter(rev.clone());
        q.setComparator(0);
        q.sort();
        assertTrue(isSorted(q, 0));
    }

    // ------------------ HELPERS ------------------

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
    
}
