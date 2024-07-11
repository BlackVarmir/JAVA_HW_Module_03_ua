package service;

import dao.PersonDAO;
import model.Fine;
import model.Person;

import java.util.List;

import static java.lang.System.out;

public class TaxDatabase {
    private final PersonDAO personDAO;

    public TaxDatabase(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    public void addPerson(String id, String name, String city) {
        if (!personDAO.personExists(id)) {
            personDAO.addPerson(id, name, city);
        } else {
            out.println("Людина з ідентифікатором " + id + " вже існує.");
        }
    }

    public void addFineToPerson(String personId, String type, double amount) {
        personDAO.addFineToPerson(personId, type, amount);
    }

    public void printDatabase() {
        List<Person> persons = personDAO.getAllPersons();
        for (Person person : persons) {
            out.println(person);
        }
    }

    public void printPersonById(String id) {
        Person person = personDAO.getPersonById(id);
        if (person != null) {
            out.println(person);
        } else {
            out.println("Людину не знайдена з ідентифікатором: " + id);
        }
    }

    public void printPersonsByCity(String city) {
        List<Person> persons = personDAO.getPersonsByCity(city);
        for (Person person : persons) {
            out.println(person);
        }
    }

    public void removeFineFromPerson(String personId, String fineType) {
        personDAO.removeFineFromPerson(personId, fineType);
    }

    public void updatePerson(String id, String name, String city, List<Fine> fines) {
        personDAO.updatePerson(id, name, city, fines);
    }
}
