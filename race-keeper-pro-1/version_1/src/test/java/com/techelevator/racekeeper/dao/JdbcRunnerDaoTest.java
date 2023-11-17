package com.techelevator.racekeeper.dao;

import com.techelevator.racekeeper.model.Person;
import com.techelevator.racekeeper.model.Runner;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JdbcRunnerDaoTest extends BaseDaoTest {

    private static final Runner RUNNER_1 =
            new Runner (1,
                    "Male1",
                    "Runner",
                    "40 Main Street",
                    "Boulder",
                    "CO",
                    'M',
                    LocalDate.parse("1971-03-09")
            );

    private JdbcRunnerDao sut; // for system under test

    @Before
    public void setup() {
        sut = new JdbcRunnerDao(dataSource);
    }

    @Test
    public void getRunners_returns_correct_number_of_runners() {

        List<Runner> runners = sut.getRunners();
        assertNotNull("getRunners() returned a null list of persons.", runners);
        assertEquals("getRunners() returned the wrong number of persons in the list.",
                6, runners.size());
    }

    @Test
    public void getRunnerById_with_valid_id_returns_correct_runner() {

        Runner runner = sut.getRunnerById(1);
        assertNotNull("getRunnerById with valid id returned a null person.", runner);
        assertPersonsMatch("getRunnerById with valid id returned the incorrect/incomplete person.", RUNNER_1, runner);
    }

    @Test
    public void getRunnerById_with_invalid_id_returns_null_person() {

        Runner runner = sut.getRunnerById(0); // IDs begin with 1, cannot be 0
        assertNull("getRunnerById with invalid id returned a person rather than null.", runner);
    }

    @Test
    public void getRunnerByName_with_full_name_exact_match_returns_correct_number_of_persons() {

        List<Runner> runners = sut.getRunnersByName("Male1 Runner", false);
        assertNotNull("getRunnerByName with full name exact match returned a null list of runners.", runners);
        assertEquals("getRunnersByName with full name exact match returned the wrong number of runners in the list.",
                1, runners.size());
    }

    @Test
    public void getPersonsByName_with_partial_name_exact_match_returns_correct_number_of_persons() {

        List<Runner> runners = sut.getRunnersByName("Runner", false);
        assertNotNull("getRunnersByName with partial name exact match returned a null list of runners.", runners);
        assertEquals("getRunnersByName with partial name exact match returned the wrong number of runners in the list.",
                0, runners.size());
    }

    @Test
    public void getRunnerByName_with_empty_name_exact_match_returns_correct_number_of_runners() {

        List<Runner> runners = sut.getRunnersByName("", false);
        assertNotNull("getRunnersByName with empty name exact match returned a null list of persons.", runners);
        assertEquals("getRunnersByName with empty name exact match returned the wrong number of runners in the list.",
                0, runners.size());
    }

    @Test
    public void getRunnersByName_with_partial_name_wildcard_match_returns_correct_number_of_persons() {

        List<Runner> runners = sut.getRunnersByName("runner", true);
        assertNotNull("getPersonsByName with partial name wildcard match returned a null list of persons.", runners);
        assertEquals("getPersonsByName with partial name wildcard match returned the wrong number of persons in the list.",
                6, runners.size());

        List<Runner> femaleRunners = sut.getRunnersByName("female", true);
        assertNotNull("getPersonsByName with partial name wildcard match returned a null list of persons.", femaleRunners);
        assertEquals("getPersonsByName with partial name wildcard match returned the wrong number of persons in the list.",
                3, femaleRunners.size());

    }

    @Test
    public void getRunnersByName_with_empty_name_wildcard_match_returns_correct_number_of_runners() {

        List<Runner> runners = sut.getRunnersByName("", true);
        assertNotNull("getRunnersByName with empty name wildcard match returned a null list of runners.", runners);
        assertEquals("getPersonsByName with empty name wildcard match returned the wrong number of persons in the list.",
                6, runners.size());
    }

//    @Test
//    public void getPersonsByCollectionName_with_full_collection_name_exact_match_returns_correct_number_of_persons() {
//
//        List<Person> persons = sut.getPersonsByCollectionName("Star Wars Collection", false);
//        assertNotNull("getPersonsByCollectionName with full collection name exact match returned a null list of persons.", persons);
//        assertEquals("getPersonsByCollectionName with full collection name exact match returned the wrong number of persons in the list.",
//                25, persons.size());
//    }
//
//    @Test
//    public void getPersonsByCollectionName_with_partial_collection_name_exact_match_returns_correct_number_of_persons() {
//
//        List<Person> persons = sut.getPersonsByCollectionName("Star", false);
//        assertNotNull("getPersonsByCollectionName with partial name exact match returned a null list of persons.", persons);
//        assertEquals("getPersonsByCollectionName with partial name exact match returned the wrong number of persons in the list.",
//                0, persons.size());
//    }
//
//    @Test
//    public void getPersonsByCollectionName_with_partial_collection_name_wildcard_match_returns_correct_number_of_persons() {
//
//        List<Person> persons = sut.getPersonsByCollectionName("F", true);
//        assertNotNull("getPersonsByCollectionName with partial name wildcard match returned a null list of persons.", persons);
//        assertEquals("getPersonsByCollectionName with partial name wildcard match returned the wrong number of persons in the list.",
//                29, persons.size());
//    }

    private void assertPersonsMatch(String message, Runner expected, Runner actual) {

        assertEquals(message, expected.getId(), actual.getId());
        assertEquals(message, expected.getFirst_name(), actual.getFirst_name());
        assertEquals(message, expected.getLast_name(), actual.getLast_name());
        assertEquals(message, expected.getStreet(), actual.getStreet());
        assertEquals(message, expected.getCity(), actual.getCity());
        assertEquals(message, expected.getState_code(), actual.getState_code());
        assertEquals(message, expected.getGender_code(), actual.getGender_code());
        assertEquals(message, expected.getBirthday(), actual.getBirthday());
    }
}
