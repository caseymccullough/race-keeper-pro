package org.mccullough.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class SystemInOutConsole implements BasicConsole{

    private final Scanner sc = new Scanner (System.in);
    @Override
    public void pauseOutput() {
        System.out.println("Press return to continue . . . ");
        sc.nextLine();
    }
    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void printErrorMessage(String message) {
        System.out.println("***" + message + "***");
        pauseOutput();
    }

    @Override
    public void printDivider() {
        System.out.println("-----------------------------");
    }

    @Override
    public void printBanner(String message) {
        String dashes = "-".repeat(message.length());
        System.out.println(dashes);
        System.out.println(message);
        System.out.println(dashes);
    }

    @Override
    public void printBulletedItems(String[] items) {
        for(String item : items){
            System.out.println("* " + item );
        }
    }

    @Override
    public String getMenuSelection(String[] options) {
        return getMenuSelection(options, false);
    }


    @Override
    public String getMenuSelection(String[] options, boolean allowNullResponse) {
        Integer index = getMenuSelectionIndex(options, allowNullResponse);
        return index == null ? null : options[index];
    }

    @Override
    public Integer getMenuSelectionIndex(String[] options, boolean allowNullResponse) {
        Integer result = null;
        boolean validInput = false;
        while (!validInput) {
            for (int i = 0; i < options.length; i++){
                System.out.format("%d: %s\n", i + 1, options[i]);
            }
            Integer selection = promptForInteger("Please select: ");
            if (selection == null){
                if (allowNullResponse) {
                    validInput = true;
                } else {
                    printErrorMessage("Please make a selection");
                }
            } else if (selection > 0 && selection <= options.length) {
                result = selection - 1; // adjust to array numbering
                validInput = true;
            }
            else {
                printErrorMessage("Invalid selection"); // loops back to top
            }
        }
        return result;
    }

    @Override
    public String promptForString(String prompt) {
        System.out.println(prompt);
        String entry = sc.nextLine();
        return entry;
    }

    @Override
    public boolean promptForYesNo(String prompt) {
        while (true){
            String reply = promptForString(prompt);
            String upperCaseReply = reply.toUpperCase();

            if (upperCaseReply.startsWith("Y")){
                return true;
            } else if (upperCaseReply.startsWith("N")){
                return false;
            } else {
                printErrorMessage("Please entery Y or N");
            }
        }
    }

    @Override
    public Integer promptForInteger(String prompt) {
        Integer result = null;
        String entry = promptForString(prompt);
        while (!entry.isBlank() && result == null) {
            try {
                result = Integer.parseInt(entry);
            } catch (NumberFormatException e){
                printErrorMessage("Enter a number please");
                entry = promptForString(prompt);
            }
        }

        return result;
    }

    @Override
    public Double promptForDouble(String prompt) {
        Double result = null;
        String entry = promptForString(prompt);
        while (!entry.isBlank() && result == null){
            try {
                result = Double.parseDouble(entry);
            } catch (NumberFormatException e) {
                printErrorMessage("Enter a number please");
                entry = promptForString(prompt);
            }
        }
        return result;
    }

    @Override
    public BigDecimal promptForBigDecimal(String prompt) {
        BigDecimal result = null;
        String entry = promptForString(prompt);
        while (!entry.isBlank() && result == null){
            try {
                result = new BigDecimal(entry);
            } catch (NumberFormatException e) {
                printErrorMessage("Enter a number please");
                entry = promptForString(prompt);
            }
        }
        return result;
    }

    @Override
    public LocalDate promptForLocalDate(String prompt) {
        LocalDate result = null;
        String entry = promptForString(prompt + "(YYYY-MM-DD) ");
        while (!entry.isBlank() && result == null) {
            try {
                result = LocalDate.parse(entry);
            } catch (DateTimeParseException e) {
                printErrorMessage("Enter a date in YYYY-MM-DD format, please");
                entry = promptForString(prompt);
            }
        }
        return result;
    }
}
