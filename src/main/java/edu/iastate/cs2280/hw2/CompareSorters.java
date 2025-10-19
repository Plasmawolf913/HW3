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
    
    Scanner scan = new Scanner(System.in);

    int choice = 0;
    int trialNum = 1;
    boolean quit = false;
    String choiceTwo = null;
    
    while(!quit) {
    	System.out.println();
    	System.out.println("-----------------------------------------------------------");
    	System.out.println("keys:  1 (random student data)  2 (file input)  3 (exit)");
    
	    try {
	    	choice = scan.nextInt();
	    }catch(Exception e) {
	    	System.out.println("Please enter a value 1-3");
	    }
	    
    	System.out.println("Trial " + trialNum + ": " + choice);

    	
	    //Based on the user's choice, generate random students and sort, or pull from a file, or quit
	    switch(choice) {
	    	case 1:
	    		
	    		System.out.println("Enter the number of students to generate:");
	    		int numStudents = scan.nextInt();
	    		Random rand = new Random();
	    		Student[] randomStudents = generateRandomStudents(numStudents, rand);
	    		
	    		//making scanners
	    		StudentScanner[] randomScanners = {
	    		        new StudentScanner(randomStudents, Algorithm.SelectionSort),
	    		        new StudentScanner(randomStudents, Algorithm.InsertionSort),
	    		        new StudentScanner(randomStudents, Algorithm.QuickSort),
	    		        new StudentScanner(randomStudents, Algorithm.MergeSort)
	    		    };
	    		
	    		//scan them
	    		for (StudentScanner s : randomScanners) {
	    			s.scan();
	    		}

    		    // find median student
    		    Student randomMedian = randomScanners[0].getMedianStudent();
    		    
    		    //use helper method to display results
    		    displayResults(randomScanners, randomMedian);

    		
    		    System.out.print("Export results to CSV? (y/n): ");
    		    String randomCsvChoice = scan.next();

    		    if (randomCsvChoice.equals("y")) {
    		    	handleExportOption(scan, randomScanners); 
    		    }
	    		break;
	    		
	    		
	    		
	    		
	    		
	    	case 2:
	    		System.out.print("File name: ");
	    		String fileName = scan.next().trim();   // single token is safest for filenames
	    		scan.nextLine();   
	    		
				Student[] fileStudents;
				
				//catch errors when reading files
				try {
					fileStudents = readStudentsFromFile(fileName);
				} catch (FileNotFoundException e) {
					System.out.println("File not found: " + fileName);
				    return; // stop here; don't proceed with null
				} catch (InputMismatchException e) {
					System.out.println("File format error: " + e.getMessage());
				    return;
				} 
				
	    		//set up scanners
	    		StudentScanner[] fileScanners = {
	    		        new StudentScanner(fileStudents, Algorithm.SelectionSort),
	    		        new StudentScanner(fileStudents, Algorithm.InsertionSort),
	    		        new StudentScanner(fileStudents, Algorithm.QuickSort),
	    		        new StudentScanner(fileStudents, Algorithm.MergeSort)
	    		    };
	    		
	    		//call scan method for all scanners
	    		for (StudentScanner s : fileScanners) {
	    			s.scan();
	    		}
	    		
	    		Student fileMedian = fileScanners[0].getMedianStudent();
    		    
	    		//use helper method to display the table
    		    displayResults(fileScanners, fileMedian);

    		
    		    System.out.print("Export results to CSV? (y/n): ");
    		    String fileCsvChoice = scan.next();

    		    if (fileCsvChoice.equalsIgnoreCase("y")) {
    		    	handleExportOption(scan, fileScanners); 
    		    }
	    		break;
	    	case 3:
	    		quit = true;
	    		break;
	    	default:
	    		System.out.println("Please input a number 1-3");
	    
	    }
	    
	    //re run the program catching exceptions
	    System.out.println("Would you like to re run the program or quit? (r or q)");
	    try {
	    	choiceTwo = scan.next();
	    }catch(Exception e) {
	    	System.out.println("Please enter a valid value");
	    }
	    switch(choiceTwo) {
	    	case "r":
	    		trialNum++;
	    		continue;
	    		
	    	case "q":
	    		quit = true;
	    		break;
	    	default:
	    		quit = true;
	    		break;
	    }
    }
    
    

    System.out.println("Exiting program.");
    
    scan.close();
    System.exit(0);
   
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
	  

	    System.out.print("Enter filename for export (e.g., results.csv): ");
	    String filename = scan.next().trim();

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
		  students[i] = new Student(rand.nextDouble()*4, rand.nextInt(131));
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
	  Student[] students = null;
	  
	  File f = new File(filename);
	  if (!f.exists()) {
	        throw new FileNotFoundException("File not found: " + filename);
	   }
	  
	  ArrayList<Student> list = new ArrayList<>();

	    
	  try (Scanner sc = new Scanner(f)) {
	        int lineNo = 0;
	        while (sc.hasNextLine()) {
	            lineNo++;
	            String line = sc.nextLine().trim();
	            if (line.isEmpty() || line.startsWith("#")) continue; // allow blanks/comments

	            String[] parts = line.split("\\s+");
	            if (parts.length < 2) {
	                throw new InputMismatchException("Line " + lineNo + " must contain GPA and credits");
	            }

	            double gpa;
	            int credits;
	            try {
	                gpa = Double.parseDouble(parts[0]);
	                credits = Integer.parseInt(parts[1]);
	            } catch (NumberFormatException nfe) {
	                throw new InputMismatchException("Invalid number on line " + lineNo + ": " + nfe.getMessage());
	            }

	            list.add(new Student(gpa, credits));
	        }
	    }

	    if (list.isEmpty()) {
	        throw new IllegalArgumentException("student array must have at least 1 value");
	    }
	    return list.toArray(new Student[0]);
  }
  
  
  
  
  /**
   * Displays the sorting performance table for all algorithms.
   * Assumes each StudentScanner object stores algorithm name, input size, and runtime.
   */
  private static void displayResults(StudentScanner[] scanners, Student medianStudent) {
      System.out.println();
      System.out.println("algorithm\t\tsize\ttime (ns)");
      System.out.println("--------------------------------------------");

      // Loop through scanners and print each algorithm’s results
      for (StudentScanner s : scanners) {
          System.out.printf("%-15s %8d %12d%n",
                  s.getAlgorithm(),
                  s.getSize(),
                  s.getScanTime());
      }

      System.out.println("--------------------------------------------");
      System.out.println();

      // Median student info
      System.out.printf("Median Student Profile: (GPA: %.2f, Credits: %d)%n",
              medianStudent.getGpa(),
              medianStudent.getCreditsTaken());
      System.out.println();

  }

}
