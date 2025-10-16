/**
 * @author tfolkers
 */
package edu.iastate.cs2280.hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/**
 * This class serves as the main driver for the sorting algorithm comparison application.
 * It provides a prompt interface for users to generate or load student data,
 * run four different sorting algorithms (Selection, Insertion, Merge, Quick),
 * and compare their performance based on execution time. The results can be
 * displayed in the console and optionally exported to a CSV file.
 */
public class CompareSorters {

  /**
   * The main entry point of the application. It presents a menu to the user to
   * choose between generating random student data, reading data from a file, or exiting.
   * For each set of data, it runs the sorting algorithms and prints their performance statistics.
   * Should handle InputMismatchExceptions and FileNotFoundExceptions gracefully.
   *
   * @param args Command-line arguments (not used).
   */
  public static void main(String[] args) {
    System.out.println("Sorting Algorithms Performance Analysis using Student Data\n");
    System.out.println("keys:  1 (random student data)  2 (file input)  3 (exit)");
    Scanner scan = new Scanner(System.in);

    int choice = 0;
    
    try {
    	choice = scan.nextInt();
    }catch(Exception e) {
    	System.out.println("Please enter a value 1-3");
    }
    
    switch(choice) {
    	case 1:
    		
    		System.out.println("Enter the number of students to generate:");
    		int numStudents = scan.nextInt();
    		Random rand = new Random();
    		generateRandomStudents(numStudents, rand);
    		
    		break;
    	case 2:
    		break;
    	case 3:
    		break;
    	default:
    		System.out.println("Please input a number 1-3");
    
    }
    
    //while loop!!! prob

    System.out.println("Exiting program.");
    scan.close();
  }

  /**
   * Handles the user prompt for exporting sorting performance results to a CSV file.
   * This method catches and handles a {@link FileNotFoundException} if the specified
   * output file cannot be created or written to, printing an error message to the console.
   *
   * @param scan     The Scanner object to read user input.
   * @param scanners An array of StudentScanner objects containing the performance stats.
   */
  private static void handleExportOption(Scanner scan, StudentScanner[] scanners) {
	  System.out.print("Export results to CSV? (y/n): ");
	    String choice = scan.nextLine().trim().toLowerCase();

	    if (!choice.equals("y")) {
	        return; 
	    }

	    System.out.print("Enter filename for export (e.g., results.csv): ");
	    String filename = scan.nextLine().trim();

	    try (PrintWriter writer = new PrintWriter(filename)) {
	        // CSV header
	        writer.println("algorithm,size,time(ns)");

	        // Write each sorter’s performance row
	        for (StudentScanner s : scanners) {
	            writer.printf("%s,%d,%d%n",
	                    s.getAlgorithm(),
	                    s.getSize(),
	                    s.getScanTime());
	        }

	        // Median student profile
	        Student median = scanners[0].getMedianStudent();
	        writer.printf("Median Student,%.2f,%d%n",
	                median.getGpa(),
	                median.getCreditsTaken());

	        System.out.printf("Data exported successfully to %s%n", filename);

	    } catch(Exception e) {
	    	System.out.printf("Error: Could not create or write to file %s%n", filename);
	    }
	  
  }

  /**
   * Generates an array of Student objects with random GPA and credit values.
   *
   * @param numStudents The number of random students to generate.
   * @param rand        The Random object to use for generating values.
   * @return An array of randomly generated Student objects.
   * @throws IllegalArgumentException if numStudents is less than 1.
   */
  public static Student[] generateRandomStudents(int numStudents, Random rand) {
	  if(numStudents < 1) {
		  throw new IllegalArgumentException("NumStudents cannot be less than 1");
	  }
	  
	  Student[] students = new Student[numStudents];
	  for(int i = 0; i < numStudents; i++) {
		  students[i] = new Student(rand.nextDouble(4.1), rand.nextInt(131));
	  }
	  return students;
  }

  /**
   * Reads student data from a text file. Each line of the file should contain a
   * GPA (double) followed by credits taken (int), separated by whitespace.
   *
   * @param filename The name of the file to read from.
   * @return An array of Student objects created from the file data.
   * @throws FileNotFoundException    if the specified file does not exist.
   * @throws InputMismatchException   if the file content is not in the expected format
   * (e.g., non-numeric data, empty file).
   */
  private static Student[] readStudentsFromFile(String filename) throws FileNotFoundException, InputMismatchException {
	  
	  int size = 0;
	  Scanner counter = new Scanner(filename);
	  while(counter.hasNext()) {
		  String[] parts = counter.nextLine().split(" ");
		  size += parts.length;
	  }
	  Scanner scnr = new Scanner(filename);
	  int i = 0;
	  Student[] students = new Student[size/2];
	  
	  while(scnr.hasNextLine()) {
		  String[] parts = scnr.nextLine().split(" ");
		  double gpa = Double.parseDouble(parts[0]);
		  int creditsTaken = Integer.parseInt(parts[1]);
		  students[i] = new Student(gpa, creditsTaken);
	  }
	  
	  return students;
  }
}
