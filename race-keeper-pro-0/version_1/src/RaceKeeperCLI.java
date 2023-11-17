package com.techelevator.racekeeper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Scanner;

import com.techelevator.racekeeper.dao.JdbcRaceDao;
import com.techelevator.racekeeper.dao.JdbcRunnerDao;
import com.techelevator.racekeeper.dao.RaceDao;
import com.techelevator.racekeeper.dao.RunnerDao;
import com.techelevator.racekeeper.exception.DaoException;
import com.techelevator.racekeeper.model.*;
import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.racekeeper.view.Menu;
import org.springframework.cglib.core.Local;

import javax.sql.DataSource;

public class RaceKeeperCLI {

    private final Scanner sc = new Scanner(System.in);
    private static final String MAIN_MENU_OPTION_RUNNERS = "Runners";
    private static final String MAIN_MENU_OPTION_RACES = "Races";
    private static final String MAIN_MENU_OPTION_EXIT = "Exit";
    private static final String[] MAIN_MENU_OPTIONS = new String[]{
            MAIN_MENU_OPTION_RUNNERS, MAIN_MENU_OPTION_RACES, MAIN_MENU_OPTION_EXIT};

    private static final String MENU_OPTION_RETURN_TO_MAIN = "Return to main menu";

    private static final String RUNNERS_MENU_OPTION_ALL_RUNNERS = "Get all Runners";
    private static final String RUNNERS_MENU_OPTION_RUNNER_BY_ID = "Get Runner by ID";
    private static final String RUNNERS_MENU_OPTION_RUNNERS_BY_NAME_WILDCARD = "Get Runners by name with wildcard";
    private static final String RUNNERS_MENU_OPTION_RUNNERS_BY_NAME_EXACT = "Get Runners by exact name";
    private static final String RUNNERS_MENU_OPTION_RUNNER_BY_CITY = "Get Runners by city";
    private static final String RUNNERS_MENU_OPTION_CREATE_RUNNER = "Create Runner";
    private static final String RUNNERS_MENU_OPTION_UPDATE_RUNNER = "Update Runner information";
    private static final String[] RUNNERS_MENU_OPTIONS = new String[]{RUNNERS_MENU_OPTION_ALL_RUNNERS,
            RUNNERS_MENU_OPTION_RUNNER_BY_ID,
            RUNNERS_MENU_OPTION_RUNNERS_BY_NAME_WILDCARD,
            RUNNERS_MENU_OPTION_RUNNERS_BY_NAME_EXACT,
            RUNNERS_MENU_OPTION_RUNNER_BY_CITY,
            RUNNERS_MENU_OPTION_CREATE_RUNNER,
            RUNNERS_MENU_OPTION_UPDATE_RUNNER,
            MENU_OPTION_RETURN_TO_MAIN};

    private static final String RACE_MENU_OPTION_ALL_RACES = "Get all Races";
    private static final String RACE_MENU_OPTION_RACE_BY_ID = "Get Race by ID";
    private static final String RACE_MENU_OPTION_RACES_BY_NAME_WILDCARD = "Get Race by name with wildcard";
    private static final String RACE_MENU_OPTION_RACES_BY_NAME_EXACT = "Get Race by exact name";
    private static final String[] RACE_MENU_OPTIONS = new String[]{RACE_MENU_OPTION_ALL_RACES,
            RACE_MENU_OPTION_RACE_BY_ID,
            RACE_MENU_OPTION_RACES_BY_NAME_WILDCARD,
            RACE_MENU_OPTION_RACES_BY_NAME_EXACT,
            MENU_OPTION_RETURN_TO_MAIN};


    private final Menu menu;
    private final RunnerDao runnerDao;
    private final RaceDao raceDao;


    public static void main(String[] args) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/RunningRaces");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");

        RaceKeeperCLI application = new RaceKeeperCLI(dataSource);
        application.run();
    }

    public RaceKeeperCLI(DataSource dataSource) {
        this.menu = new Menu(System.in, System.out);

        runnerDao = new JdbcRunnerDao(dataSource);
        raceDao = new JdbcRaceDao(dataSource);

    }

    private void run() {
        boolean running = true;
        while (running) {
            printHeading("Main Menu");
            String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
            switch (choice) {
                case MAIN_MENU_OPTION_RUNNERS:
                    handleRunners();
                    break;
                case MAIN_MENU_OPTION_RACES:
                    handleRaces();
                    break;
                case MAIN_MENU_OPTION_EXIT:
                    running = false;
                    break;
            }
        }
    }

    private void handleRunners() {
        while (true) {
            printHeading("Runners");
            String choice = (String) menu.getChoiceFromOptions(RUNNERS_MENU_OPTIONS);
            if (choice.equals(RUNNERS_MENU_OPTION_ALL_RUNNERS)) {
                handleGetRunners();
            } else if (choice.equals(RUNNERS_MENU_OPTION_RUNNER_BY_ID)) {
                handleGetRunnerByID();
            } else if (choice.equals(RUNNERS_MENU_OPTION_RUNNERS_BY_NAME_WILDCARD)) {
                handleGetRunnersByNameWildcard();
            } else if (choice.equals(RUNNERS_MENU_OPTION_RUNNERS_BY_NAME_EXACT)) {
                handleGetRunnersByNameExact();
            } else if (choice.equals(RUNNERS_MENU_OPTION_RUNNER_BY_CITY)) {
                handleGetRunnersByCity();
            } else if (choice.equals(RUNNERS_MENU_OPTION_CREATE_RUNNER)) {
                handleAddRunner();
            } else if (choice.equals(RUNNERS_MENU_OPTION_UPDATE_RUNNER)) {
                handleUpdateRunner();
            }

            else if (choice.equals(MENU_OPTION_RETURN_TO_MAIN)) {
                break;
            }
        }
    }

    private void handleGetRunners() {
        printHeading("Get Runners");
        List<Runner> runners = runnerDao.getRunners();
        listRunners(runners);
    }

    private void handleGetRunnerByID() {
        printHeading("Get Runner by ID");
        int runnerID = Integer.parseInt(getUserInput("Enter Runner ID"));
        Runner runner = runnerDao.getRunnerById(runnerID);
        if (runner != null) {
            System.out.println(runner);
        } else {
            System.out.println("\n*** No results ***");
        }
    }

    private void handleGetRunnersByNameWildcard() {
        printHeading("Get Runner(s) by Name (wildcard)");
        String runnerName = getUserInput("Enter Runner Name");
        List<Runner> runners = runnerDao.getRunnersByName(runnerName, true);
        listRunners(runners);
    }

    private void handleGetRunnersByNameExact() {
        printHeading("Get Runner(s) by Name (exact)");
        String runnerName = getUserInput("Enter Runner Name");
        List<Runner> runners = runnerDao.getRunnersByName(runnerName, false);
        listRunners(runners);
    }

    private void handleGetRunnersByCity() {
        printHeading("Get Runner(s) by City");
        String city = getUserInput("Enter the Runner's City");
        List<Runner> runners = runnerDao.getRunnersByCity(city);
        listRunners(runners);
    }

    private void handleAddRunner() {

        printHeading("Add Runner");
        // Prompt the user for runner information
        Runner newRunner = promptForRunnerInformation(null);

        // If runner is null, user cancelled
        if (newRunner == null) {
            return;
        }
		// Call the DAO to add the new runner
		newRunner = runnerDao.createRunner(newRunner);
		// Inform the user
        System.out.println("Runner " + newRunner.getId() + " has been created.");
    }

	private void handleUpdateRunner() {

		// Get the list of customers so the user can choose one
		List<Runner> runners = runnerDao.getRunners();

		// Display the list of runners and ask for selection
		Runner runner = selectRunner(runners);
		if (runner == null) {
			// User cancelled
			return;
		}
		// Prompt the user for customer information
		runner = promptForRunnerInformation(runner);

		// Call the DAO to update the customer
		runnerDao.updateRunner(runner);
		// Inform the user
        System.out.println("Customer has been updated.");
	}


    private void listRunners(List<Runner> runners) {
        System.out.println();
        System.out.println("Runner count: " + runners.size());
        int limit = Math.min(runners.size(), 10);
        if (limit > 0) {
            for (int i = 0; i < limit; i++) {
                Runner runner = runners.get(i);
                System.out.println(runner);
            }
        } else {
            System.out.println("\n*** No results ***");
        }
    }

    private void handleRaces() {
        while (true) {
            printHeading("Races");
            String choice = (String) menu.getChoiceFromOptions(RACE_MENU_OPTIONS);
            if (choice.equals(RACE_MENU_OPTION_ALL_RACES)) {
                handleGetRaces();
            } else if (choice.equals(RACE_MENU_OPTION_RACE_BY_ID)) {
                handleGetRaceByID();
            } else if (choice.equals(RACE_MENU_OPTION_RACES_BY_NAME_WILDCARD)) {
                handleGetRaceByNameWildcard();
            } else if (choice.equals(RACE_MENU_OPTION_RACES_BY_NAME_EXACT)) {
                handleGetRaceByNameExact();
            } else if (choice.equals(MENU_OPTION_RETURN_TO_MAIN)) {
                break;
            }
        }
    }

    private void handleGetRaces() {
        printHeading("Get Races");
        List<Race> races = raceDao.getRaces();
        listRaces(races);
    }

    private void handleGetRaceByID() {
        printHeading("Get Race by ID");
        int raceID = Integer.parseInt(getUserInput("Enter Race ID"));
        Race race = raceDao.getRaceById(raceID);
        if (race != null) {
            System.out.println(race);
        } else {
            System.out.println("\n*** No results ***");
        }
    }

    private void handleGetRaceByNameWildcard() {
        printHeading("Get Race by Name (wildcard)");
        String raceName = getUserInput("Enter Race Name");
        List<Race> races = raceDao.getRacesByName(raceName, true);
        listRaces(races);
    }

    private void handleGetRaceByNameExact() {
        printHeading("Get Race by Name (exact)");
        String raceName = getUserInput("Enter Race Name");
        List<Race> races = raceDao.getRacesByName(raceName, false);
        listRaces(races);
    }

    private void listRaces(List<Race> races) {
        System.out.println();
        System.out.println("Race count: " + races.size());
        int limit = Math.min(races.size(), 10);
        if (limit > 0) {
            for (int i = 0; i < limit; i++) {
                Race race = races.get(i);
                System.out.println(race);
            }
        } else {
            System.out.println("\n*** No results ***");
        }
    }


    private void printHeading(String headingText) {
        System.out.println("\n" + headingText);
        for (int i = 0; i < headingText.length(); i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    private String getUserInput(String prompt) {
        System.out.print(prompt + " >>> ");
        return new Scanner(System.in).nextLine();
    }

    // **************************************************************
    // region Prompt the user for object property information
    // **************************************************************

    public Runner promptForRunnerInformation(Runner existingRunner) {
        Runner newRunner = new Runner();
        if (existingRunner == null) {
            // No default values
            newRunner.setFirst_name(promptForRunnerFirstName(null));
            newRunner.setLast_name(promptForRunnerLastName(null));
            newRunner.setStreet(promptForRunnerStreet(null));
            newRunner.setCity(promptForRunnerCity(null));
            newRunner.setState_code(promptForRunnerStateCode(null));

            newRunner.setGender_code('M');

            //newRunner.setGender_code(promptForRunnerGenderCode(null));

            newRunner.setBirthday(LocalDate.parse("2000-11-10"));
           // newRunner.setBirthday(promptForRunnerBirthday(null));
        } else {
            // This is an update -- make all prompts default to current values
            // Set the id
            newRunner.setId(existingRunner.getId());
            newRunner.setFirst_name(promptForRunnerFirstName(null));
            newRunner.setLast_name(promptForRunnerLastName(null));
            newRunner.setStreet(promptForRunnerStreet(null));
            newRunner.setCity(promptForRunnerCity(null));
            newRunner.setState_code(promptForRunnerStateCode(null));
            newRunner.setGender_code(promptForRunnerGenderCode(null));
            newRunner.setBirthday(promptForRunnerBirthday(null));
        }
        return newRunner;
    }

    private String promptForRunnerFirstName(String defaultValue) {
        return promptForString("Runner first name", true, defaultValue);
    }

    private String promptForRunnerLastName(String defaultValue) {
        return promptForString("Runner last name", true, defaultValue);
    }

    private String promptForRunnerStreet(String defaultValue) {
        return promptForString("Street Address", true, defaultValue);
    }

    private String promptForRunnerCity(String defaultValue) {
        return promptForString("City", true, defaultValue);
    }

    private String promptForRunnerStateCode(String defaultValue) {
        return promptForString("State (2-letter)", true, defaultValue);
    }

    private char promptForRunnerGenderCode(String defaultValue) {
        char genderCode;
        do {
           String entry = promptForString("Gender (M or F)", true, defaultValue);
           genderCode = entry.charAt(0);
           if (genderCode != 'M' && genderCode != 'F') {
               System.out.println("Error: Value must be either M or F");
           }

        } while (genderCode != 'M' && genderCode != 'F');
        return genderCode;
    }

    private LocalDate promptForRunnerBirthday (String defaultValue){
        LocalDate date = null;
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

        do {
            String entry = promptForString("Birthday (in format \"28/04/2023\"", true, defaultValue);
           try {
               date = LocalDate.parse (entry, formater);
           } catch (Exception e){
               System.err.println("Illegal date information");
           }
        } while (date == null);
        return date;
    }


    private String promptForString(String prompt, boolean required, String defaultValue) {
        prompt = promptWithDefault(prompt, defaultValue);
        while (true) {
            //String entry = console.promptForString(prompt);
            String entry = prompt;
            if (!entry.isEmpty()) {
                return entry;
            }
            // Entry is empty: see if we have a default, or if empty is OK (!required)
            if (defaultValue != null && !defaultValue.isEmpty()) {
                return defaultValue;
            }
            if (!required) {
                return entry;
            }
            System.out.println("A value is required, please try again.");
        }
    }




    private String promptWithDefault(String prompt, Object defaultValue) {
        if (defaultValue != null) {
            return prompt + "[" + defaultValue.toString() + "]: ";
        }
        return prompt + ": ";
    }

    public Runner selectRunner(List<Runner> runners) {
        while (true) {
            printRunnerList(runners);
            System.out.println("Enter customer id [0 to cancel]: ");
            int runnerId = sc.nextInt();
            if (runnerId == 0) {
                return null;
            }
            Runner selectedRunner= runnerDao.getRunnerById(runnerId);
            if (selectedRunner != null) {
                return selectedRunner;
            }
            System.err.println("That's not a valid id, please try again.");
        }
    }

    public void printRunnerList (List <Runner> runners){
        for (Runner runner : runners){
            System.out.println(runner.getId() + " " + runner.getLast_name() + ", " + runner.getLast_name());
        }
    }




}
