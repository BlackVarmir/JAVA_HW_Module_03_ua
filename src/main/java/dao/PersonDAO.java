package dao;

import static java.lang.System.out;

import model.Fine;
import model.Person;

import java.util.List;

public interface PersonDAO {
    void addPerson (String id, String name, String city);
    void addFineToPerson (String personId, String type, double amount);
    List<Person> getAllPersons();
    Person getPersonById(String id);
    List<Person> getPersonsByCity(String city);
    void removeFineFromPerson(String personId, String fineType);
    void updatePerson(String id, String name, String city, List<Fine> fines);
    boolean personExists(String id);
}
