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
    boolean choiceException = false;
    
    System.out.println();
	System.out.println("-----------------------------------------------------------");
	System.out.println("keys:  1 (random student data)  2 (file input)  3 (exit)");
	
	//main simulation loop
	
    while(!quit) {
    	
    
    	System.out.println("Trial " + trialNum + ": ");
    	
    	
	        
	        //catch inputmismatches like strings
    		try{
    			choice = scan.nextInt();
    		}catch(InputMismatchException e) {
    			System.out.println("Invalid number. Please enter an integer");
    			choiceException = true;
    			scan.nextLine();
    		}

	
	    //Based on the user's choice, generate random students and sort, or pull from a file, or quit
	    switch(choice) {
	    //random generation
	    	case 1:
	    		try {
		    		System.out.println("Enter number of random students: ");
		    		
		    		//make sure numstudents is not an illegal number
		    		int numStudents = scan.nextInt();
		    		if(numStudents < 1) {
		    			System.out.println("Number of students must be at least 1.");
		    			trialNum++;
		    			continue;
		    		}
		    		
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
	    		    
	    		    //end trial and re run
	    		    trialNum++;
		    		break;
		    		
		    		//make sure to catch strings
	    		}catch(InputMismatchException e) {
	    			System.out.println("Invalid number. Please enter an integer");
	    			scan.nextLine();
	    		}
	    		//if the try fails, still increase trial number
	    		trialNum++;
	    		break;
	    
	    		
	    		
	    	//file reading
	    	case 2:
	    		System.out.print("File name: ");
	    		String fileName = scan.next().trim();   // single token is safest for filenames
	    		scan.nextLine();   
	    		
				Student[] fileStudents;
				
				//catch errors when reading files
				try {
					fileStudents = readStudentsFromFile(fileName);
				} catch (FileNotFoundException e) {
					System.out.println("Error: File not found: " + fileName);
					trialNum++;
				    continue; // stop here; don't proceed with null
				} catch (InputMismatchException e) {
					System.out.println("Error: Input file format is incorrect. " + e.getMessage());
				    trialNum++;
					continue;
				} catch(IllegalArgumentException e) {
					System.out.println("Error: Input file format is incorrect. File is empty or contains no valid student data.");
					trialNum++;
					continue;
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
	    		
	    		//find median student from one of the scanners
	    		Student fileMedian = fileScanners[0].getMedianStudent();
    		    
	    		//use helper method to display the table
    		    displayResults(fileScanners, fileMedian);

    		
    		    System.out.print("Export results to CSV? (y/n): ");
    		    String fileCsvChoice = scan.next();

    		    //possibly export to a file
    		    if (fileCsvChoice.equalsIgnoreCase("y")) {
    		    	handleExportOption(scan, fileScanners); 
    		    }
    		    
    		    trialNum++;
	    		break;
	    	case 3:
	    		quit = true;
	    		break;
	    	//default case - re run and increment trial number
	    	default:
	    		//if it was a string input do not print error message twice
	    		if(choiceException) {
	    			trialNum++;
		    		break;
	    		}
	    		else if((choice > 3 || choice < 1)) {
		        	System.out.println("Invalid choice. Please enter 1, 2, or 3.");
		        	trialNum++;
		        	break;
		        }
	    		trialNum++;
	    		break;
	    
	    }
	    
    }
    
    //program exit
    System.out.println("Exiting program.");   
    scan.close();
    return;
   
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
		  throw new IllegalArgumentException();
	  }
	  
	  //create an array of new students with the correct parameters for GPA and creditsTaken
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
	  
	  File f = new File(filename);
	  if (!f.exists()) {
	        throw new FileNotFoundException("File not found: " + filename);
	   }
	  
	  ArrayList<Student> list = new ArrayList<>();

	  //add students to the arraylist, catching all possible errors in the file
	  //(input mismatch, numberformat, wrong file type
	  try (Scanner sc = new Scanner(f)) {
	        int lineNo = 0;
	        while (sc.hasNextLine()) {
	            lineNo++;
	            String line = sc.nextLine().trim();
	            if (line.isEmpty() || line.startsWith("#")) continue; // allow blanks/comments

	            String[] parts = line.split("\\s+");
//	            if (parts.length < 2) {
//	                throw new InputMismatchException("Line " + lineNo + " must contain GPA and credits");
//	            }

	            double gpa;
	            int credits;
	            try {
	                gpa = Double.parseDouble(parts[0]);
	                
	            } catch (NumberFormatException nfe) {
	                throw new InputMismatchException("File format error: Invalid GPA format. Expected a double.");
	            }
	            
	            try {
	            	credits = Integer.parseInt(parts[1]);
	            }catch(NumberFormatException nfe) {
	            	throw new InputMismatchException("File format error: Invalid credits format. Expected an integer.");
	            }

	            list.add(new Student(gpa, credits));
	        }
	    }

	    if (list.isEmpty()) {
	        throw new IllegalArgumentException("Student array must have at least 1 value.");
	    }
	    //return the list as an array of students
	    return list.toArray(new Student[0]);
  }
  
  
  
  
  /**
   * helper method for displaying the sorting performance table for all algorithms.
   * Assumes each StudentScanner object stores algorithm name, input size, and runtime.
   */
  private static void displayResults(StudentScanner[] scanners, Student medianStudent) {
      System.out.println();
      System.out.println("algorithm\t     size\ttime (ns)");
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
