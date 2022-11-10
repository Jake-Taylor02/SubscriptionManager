package subscriptionmanager;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * @author b1086175 | Jake Taylor
 *  10/01/2022
 */
public class SubscriptionManager {

    // Declaring ArrayLists that will hold file data
    static ArrayList<Subscription> currentArr = new ArrayList<>();
    static ArrayList<Subscription> sampleArr = new ArrayList<>();
    
    /** Prints menu to console and prompts user to select one of the options.
     */
    static void displayOptions() {
        System.out.println("\n1. Enter new Subscription\n"
                + "2. Display Summary of subscriptions\n"
                + "3. Display Summary of subscription for Selected Month\n"
                + "4. Find and display subscription\n"
                + "0. Exit\n");

        System.out.print("Please select an option from above: ");
    }
    
    /** Returns the File, after insuring that it exists.
     * If myFile is already correct, the user will not be prompted to enter a
     * file path at all.
     * The fName argument is just what the file is supposed to be called and is 
     * used for prompting the user to enter the file path.
     * 
     * @param myFile The file to be validated
     * @param fName  The name of the file
     * @return       The file that can now be used to hold subscription records
     */
    static File validateFile(File myFile, String fName) { // Do i need to check the file is current or sample?
        Scanner myInput = new Scanner(System.in);

        // If current.txt doesn't exist, user is prompted to enter the file path
        while (!myFile.isFile()) {// If file is not there, prompt user to enter path:
            System.out.print(fName + " file path invalid.\nPlease enter correct file path: ");
            myFile = new File(myInput.nextLine());// Could this throw an exception?
        }
        return myFile;
    }

    /** Reads subscription records from file, converting them into Customer objects
     * and adding objects to ArrayList.
     * Writable represents whether the file is current.txt or sample.txt, determining
     * which ListArray the records are added to.
     * 
     * @param myFile   The file containing the subscription records
     * @param writable Determines which ArrayList the data is added to
     */
    public static void readFile(File myFile, boolean writable) {
        
        if (writable) {
            currentArr.clear();
        } else {
            sampleArr.clear();
        }
        
        BufferedReader bRead;
        while (true){// assign bRead and catch the exception:
            try {
                bRead = new BufferedReader(new FileReader(myFile));
                break;// Break from loop as no exception occured
            } catch (FileNotFoundException ex) {
                System.out.println("Unable to read from subscription file.");
                // Make the user enter and validate the file again 
                String iFile = writable ? "current.txt" : "sample.txt";
                myFile = validateFile(new File(iFile), iFile);
            }
        }
        
        String newLine;
        String[] tempArray;
        while (true) {
            try {
                newLine = bRead.readLine();// Read in next line
                
                if (newLine == null) {//End of file
                    break;
                } else {
                    // Split the line, using tab as the delimiter
                    tempArray = newLine.split("\t");
                    
                    Subscription nSub;
                    // Create the correct Subscription Subclass based on the package
                    if (tempArray[1].charAt(0) == 'G') {
                        nSub = new GoldSubscription(
                                DateHelper.strToDate(tempArray[0]), // Date
                                Integer.parseInt(tempArray[2]),     // Duration
                                tempArray[3],                       // Discount code
                                tempArray[4].charAt(0),             // Terms
                                Integer.parseInt(tempArray[5]),     // Amount
                                tempArray[6]                        // Name
                        );
                    } else if (tempArray[1].charAt(0) == 'S') {
                        nSub = new SilverSubscription(
                                DateHelper.strToDate(tempArray[0]), // Date
                                Integer.parseInt(tempArray[2]),     // Duration
                                tempArray[3],                       // Discount code
                                tempArray[4].charAt(0),             // Terms
                                Integer.parseInt(tempArray[5]),     // Amount
                                tempArray[6]                        // Name
                        );
                    } else {
                        nSub = new BronzeSubscription(
                                DateHelper.strToDate(tempArray[0]), // Date
                                Integer.parseInt(tempArray[2]),     // Duration
                                tempArray[3],                       // Discount code
                                tempArray[4].charAt(0),             // Terms
                                Integer.parseInt(tempArray[5]),     // Amount
                                tempArray[6]                        // Name
                        );
                    }
                    
                    // Add the new subscription to the right ArrayList:
                    if (writable) {// If we are using current.txt, add to current array
                        currentArr.add(nSub);
                    } else {// If we are using sample.txt, add to sample array
                        sampleArr.add(nSub);
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error reading from file.");
                newLine = null;
            }
        }
    }
    
    /** Creates new Subscription object using input from the user.
     * Prompts the user to enter each subscription attribute and validates the
     * input. The new subscription is then appended to the ArrayList and
     * subscription file.
     * 
     * @param outFile File that the new subscription record will be appended to
     */
     public static void enterNewSubscription(File outFile) {
        Scanner myScan = new Scanner(System.in);
        
        // Get which array the user wants to use:
        boolean arrayChoice = chooseFile();
        
        // Declare variables for subscription attributes
        String userDate = DateHelper.getDate();
        
        char userPackage;
        int userDuration;
        String userDiscount = " ";
        char userTerms;
        String userName;

        
        System.out.println("\nNEW SUBSCRIPTION:");
        
        // Get Customer name from user:
        System.out.print("Please enter Name: ");
        userName = myScan.nextLine();
        // Call method to validate name
        while (!FieldValidation.validName(userName)) {
            System.out.print("Name invalid.\nPlease enter a valid Name: ");
            userName = myScan.nextLine();
        }
        
        // Get Package from user
        System.out.print("Please enter a valid Package ('B', 'S' or 'G'): ");
        String tempPack = myScan.nextLine();// Using tempory string to avoid IndexOutOfBounds
        while (!FieldValidation.validPackage(tempPack)){
            System.out.print("Invalid Package.\nPlease enter a valid Package ('B', 'S' or 'G'): ");
            tempPack = myScan.nextLine();
        }
        userPackage = tempPack.toUpperCase().charAt(0);
        
        
        do{// Get subscription duration from user:
            System.out.print("Please enter Duration (1, 3, 6 or 12): ");
            try{
                userDuration = myScan.nextInt();
            }catch(InputMismatchException ex){
                myScan.next();
                userDuration = 0;
            }
        } while (!FieldValidation.validDuration(userDuration));
      
      
        do {// Get Discount code from user, validating it:
            System.out.print("Please enter Discount code (Type '-' for no discount code): ");
            userDiscount = myScan.nextLine().toUpperCase();
            
            if (userDiscount.equals("-")) {
                break;// There is no discount code, so skip the validation
            }
        } while (!FieldValidation.validDiscountCode(userDiscount));
        
        // Get Payment terms from user
        System.out.print("Please enter Payment terms ('O' or 'M'): ");
        String tempTerms = myScan.nextLine();
        while (!FieldValidation.validTerms(tempTerms)) {
            System.out.print("Invalid payment terms.\nPlease enter Payment terms ('O' or 'M'): ");
            tempTerms = myScan.nextLine();
        }
        userTerms = tempTerms.toUpperCase().charAt(0);
        
        
        Subscription nSub;
        // Create a Subscription object of the right subclass:
        if (userPackage == 'G') {
            nSub = new GoldSubscription(
                    DateHelper.strToDate(userDate), // Date
                    userDuration, // Duration
                    userDiscount, // Discount code
                    userTerms, // Terms
                    0, // Amount - placeholder
                    userName // Name
            );
        } else if (userPackage == 'S') {
            nSub = new SilverSubscription(
                    DateHelper.strToDate(userDate), // Date
                    userDuration, // Duration
                    userDiscount, // Discount code
                    userTerms, // Terms
                    0, // Amount - placeholder
                    userName // Name
            );
        } else {
            nSub = new BronzeSubscription(
                    DateHelper.strToDate(userDate), // Date
                    userDuration, // Duration
                    userDiscount, // Discount code
                    userTerms, // Terms
                    0, // Amount - placeholder
                    userName // Name
            );
        }
        
        nSub.calcAmount(); // overwrite placeholder amount with actual amount
        
        // Add new record to Arraylist:
        if (arrayChoice){
            currentArr.add(nSub);
            
            // Print summary of most recently added Subscription object
            printSummary(currentArr.get(currentArr.size() - 1));
        }else{
            sampleArr.add(nSub);
            
            // Print summary of most recently added Subscription object
            printSummary(sampleArr.get(sampleArr.size() - 1));
        }
        
        while (true){// Append new Subscription to file
            try {try (BufferedWriter bWrite = new BufferedWriter(new FileWriter(outFile, true))) {
                    bWrite.write(nSub.toRecord() + "\n");
                    System.out.println("File Appended.");
                }
                break;
            } catch (IOException ex) {
                // Unable to write to file, try to recover by validating summary file again:
                System.out.println("Error writing new sub to subscription.txt");
                System.out.println("Press enter to try again or type 'QUIT' to retun to menu");
                String answer = myScan.next();
                
                if (answer.equals("QUIT")){
                    /* Allowing user to give up trying to write to file if
                       the exception repatedly happens */
                    break;
                }

                outFile = validateFile(new File(outFile.getName()), "subscription.txt");
            }
        }
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner myInput = new Scanner(System.in);
        
        // Validate that current.txt is a file, then read file into array:
        File currentFile = validateFile(new File("current.txt"), "current.txt");
        readFile(currentFile, true);
        
        // Validate that sample.txt is a file, then read file into array:
        File sampleFile = validateFile(new File("sample.txt"), "sample.txt");
        readFile(sampleFile, false);

        String userSelection;
        do {// Display menu for user to select from:
            displayOptions();
            userSelection = myInput.nextLine();

            switch (userSelection) {
                case "1":// Enter new Subscription
                    System.out.println("New Subcription:");
                    enterNewSubscription(new File("subscription.txt"));
                    break;
                case "2":// Display Summary of subscriptions
                    System.out.println("Summary of subscriptions:");
                    subSummary();
                    break;
                case "3":// Display Summary of subscription for Selected Month
                    System.out.println("Summary of subscription for Selected Month");
                    subSummary(getUserMonth());
                    break;
                case "4":// Find and display subscription
                    System.out.println("Find and Display Subscription");
                    findSubs();
                    break;
                case "0":// Exit
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("\nInvalid input, Please select a valid option.");
            }

        } while (!userSelection.equals("0"));
    }

    /** Returns month input by user, after validating it.
     * 
     * @return Selected Month (1 - 12) 
     */
    static int getUserMonth() {
        Scanner scan = new Scanner(System.in);
        int month;
        
        System.out.print("Enter month (1 - 12): ");
        try {
            month = scan.nextInt();
        } catch (InputMismatchException e){
            scan.next();
            month = 0;
        }
        while (month < 1 || month > 12) {
            System.out.print("Invalid month.\nEnter month (1 - 12): ");
            try {
                month = scan.nextInt();
            } catch (InputMismatchException e){
                scan.next();
                month = 0;
            }
        }
        return month;
    }

    /** Creates Summary for all of the Subscriptions in a given month and prints
     * it to console.
     * 
     * @param selectedMonth The month (1 -12) to be summarised
     */
    static void subSummary(int selectedMonth) {
        // create target array so that it only stores objects from a particular month
        
        ArrayList<Subscription> targetArr;
        
        // Get which ArrayList the user wants to use:
        boolean fileChoice = chooseFile();
        targetArr = new ArrayList<>(fileChoice ? currentArr : sampleArr);

        int numberOfSubs = 0; // Total number of Subscriptions
        double totalOfSubs = 0;// Sum of all Subscription Amounts
        double[] Packages = {0, 0, 0};// Totals for each Package type, Gold, Silver, Bronze

        int monthSub;// Will hold the month of a Subscription
        
        // For each subscription in ArrayList
        for (int count = 0; count < targetArr.size(); count++) {
            // Get month part of date of record
            monthSub = DateHelper.getMonthInt(targetArr.get(count).getDate());

            // If subscription is from the selected month, add it to the various totals:
            if (monthSub == selectedMonth) {
                
                numberOfSubs++;// Increment Total number of Subscriptions
                totalOfSubs += targetArr.get(count).getAmount();// Add amount to Total
                
                // Get Package from Subscription and increment corresponding totals:
                char tPackage = targetArr.get(count).getPackageChar();
                if (tPackage == 'G') {
                    Packages[0]++;
                } else if (tPackage== 'S') {
                    Packages[1]++;
                } else {// tPackage must be Bronze
                    Packages[2]++;
                }
            }
            
            
        }
        
        String[] monthArr = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                               "Aug", "Sep", "Oct", "Nov", "Dec"};
        
        // Convert Packages from totals to percentages:
        Packages[2] = (Packages[2] / numberOfSubs) * 100;
        Packages[1] = (Packages[1] / numberOfSubs) * 100;
        Packages[0] = (Packages[0] / numberOfSubs) * 100;
        
        // Print Summary to console:,
        System.out.printf("\nTotal number of subscriptions for %s: %d%n", monthArr[selectedMonth - 1], numberOfSubs);
        System.out.printf("Average subscription fee: £%.2f%n", (totalOfSubs / numberOfSubs) /100);

        System.out.println("\nPercentage of Subscriptions:");
        System.out.printf("Bronze: %3.1f%n", Packages[2]);
        System.out.printf("Silver: %3.1f%n", Packages[1]);
        System.out.printf("Gold: %3.1f%n", Packages[0]);
    }

    /** Creates summary of all of the subscriptions and prints it to console.
     */
    static void subSummary() {

        ArrayList<Subscription> targetArr;
        
        // Get which ArrayList the user wants to use
        boolean fileChoice = chooseFile();
        targetArr = new ArrayList<>(fileChoice ? currentArr : sampleArr);
        
        int numberOfSubs = targetArr.size();// Total number of Subscriptions
        double totalOfSubs = 0;// Sum of all Subscription Amounts
        double[] Packages = {0, 0, 0}; // Packages in order G, S, B

        // Hashmap containing the months and their totals
        HashMap<String, Integer> months = new HashMap<>();
        months.put("Jan", 0);
        months.put("Feb", 0);
        months.put("Mar", 0);
        months.put("Apr", 0);
        months.put("May", 0);
        months.put("Jun", 0);
        months.put("Jul", 0);
        months.put("Aug", 0);
        months.put("Sep", 0);
        months.put("Oct", 0);
        months.put("Nov", 0);
        months.put("Dec", 0);
        
        // Loop through each object in ArrayList
        for (int count = 0; count < targetArr.size(); count++) {
            totalOfSubs += targetArr.get(count).getAmount(); //Adds the subscription ammount to the total
            
            // Get Package from Subscription and increment corresponding totals:
            char tPackage = targetArr.get(count).getPackageChar();
            if (tPackage== 'G') {
                Packages[0]++;
            } else if (tPackage == 'S') {
                Packages[1]++;
            } else {// tPackage must be BRONZE
                Packages[2]++;
            }

            //Get date from object
            Date tDate = targetArr.get(count).getDate();
            //Extract month part
            String monthDate = DateHelper.getMonthWord(tDate);
            //Increment total for corresponding month using HashMap
            months.put(monthDate, months.get(monthDate) + 1);
        }
        // Convert packages from totals to percentages:
        Packages[2] = (Packages[2] / numberOfSubs) * 100;
        Packages[1] = (Packages[1] / numberOfSubs) * 100;
        Packages[0] = (Packages[0] / numberOfSubs) * 100;
        
        //Display the totals:
        double averageSub = (totalOfSubs / numberOfSubs) / 100;
        System.out.printf("\nTotal number of subscriptions: %d%n", numberOfSubs);
        
        System.out.printf("Average monthly subscription: %d%n", numberOfSubs / 12);
        System.out.printf("Average monthly subscription fee: £%.2f%n", averageSub);

        System.out.println("\nPercentage of subscriptions:");
        System.out.printf("Bronze: %3.1f%n", Packages[2]);
        System.out.printf("Silver: %3.1f%n", Packages[1]);
        System.out.printf("Gold: %3.1f%n", Packages[0]);

        // Calculating averages for each month:
        String headers = "\n";// Will hold month headers
        String mValues = "";// Will hold subscriptions for each month

        for (String m : months.keySet()) {// For each month in HashMap
            headers += (m + " ");// add month to header string and one space

            mValues += ((String.format("%3d", months.get(m))) + " ");// Add No. of subscriptions for that month
            // This will break if the number of subscriptions for a month exceeds 3 digits
        }

        // Print out month headers and subscription values
        System.out.println(headers);
        System.out.println(mValues);
    }

    /** Returns which of the two data files the user selects.
     * <p>
     * (true: current.txt | false: sample.txt)
     * 
     * @return 
     */
    static boolean chooseFile() {
        Scanner scan = new Scanner(System.in);
        String option;
        do {
            System.out.println("\n1 - current.txt");
            System.out.println("2 - sample.txt");
            System.out.print("Please select which file you wish to use (1 or 2): ");

            option = scan.nextLine();
        } while (!option.equals("1") && !option.equals("2"));

        // Returns true for current.txt, false for sample.txt
        return option.equals("1");
    }
    
    /** Finds all Subscription records matching criteria provided by the user and 
     * calls printSummary() to print them to console.
     */
    static void findSubs() {
        Scanner scan = new Scanner(System.in);
        
        ArrayList<Subscription> targetArr;

        //Determine which file the user wants to use
        boolean fileChoice = chooseFile();
        targetArr = new ArrayList<>(fileChoice ? currentArr : sampleArr);

        String criteria = "";
        System.out.print("Please enter search criteria: ");
        criteria = scan.nextLine().toLowerCase();
        while (criteria.length() < 1) {
            System.out.print("Invalid search criteria.\nPlease enter search criteria: ");
            criteria = scan.nextLine().toLowerCase();
        }
        
        System.out.printf("\nDisplaying subscriptions matching '%s':%n", criteria);

        String name;
        boolean noMatches = true;
        /* Loop through each Customer object in Arraylist, checking if
           customer name contains search criteria */
        for (int count = 0; count < targetArr.size(); count++) {
            name = targetArr.get(count).getName().toLowerCase();

            if (name.contains(criteria)) {
                // Call method to print summary
                noMatches = false;
                printSummary(targetArr.get(count));
            }
        }
        
        if (noMatches) System.out.println("Entered criteria has no matches.");
    }
    
    /** Prints to console, the summary of a given Subscription object.
     * 
     * @param record The Subscription object to be printed.
     */
    
    static void printSummary(Subscription record) {

        // Print the top of the box
        System.out.println("\n+===============================================+");
        System.out.printf("|%48s", "|");// Empty line, Placing 2 '|', 47 spaces apart

        // Print Customer name with appropriate right padding
        System.out.printf("\n| %-46s|", ("Customer: " + record.getName()));

        System.out.printf("\n|%48s", "|");// Empty line with padding

        // Print line containing Date and Discount code
        System.out.printf("\n|%11s%-11s", "Date: ", DateHelper.dateToStr(record.getDate()));
        System.out.printf("%18s%-7s|", "Discount Code: ", record.getDiscount());

        // Print line containing Package and Duration
        System.out.printf("\n|%11s%-11s", "Package: ", record.getPackageString());
        System.out.printf("%18s%-7s|", "Duration: ", record.getDuration().toString());

        // Print line containing payment terms
        System.out.printf("\n|%11s%-36s|", "Terms: ", record.getTerms().toString());

        System.out.printf("\n|%48s", "|");// Empty line with padding

        // String containing Subcription amount header and value)
        String subCost = String.format("%s Subscription: £%.2f", record.getTerms().toString(), new Double(record.getAmount()) / 100);

        int spaceNum = (subCost.length() + 47) / 2;// The amount of left spacing required

        String summary = String.format("\n|%" + spaceNum + "s", subCost);// Left spacing
        summary += String.format("%" + (50 - summary.length()) + "s", "|");// Right spacing
        System.out.println(summary);

        System.out.printf("|%48s", "|");// Empty line with padding
        System.out.println("\n+===============================================+");
    }
}
