import static java.lang.System.out;

import dao.PersonDAO;
import dao.PostgresPersonDAO;
import model.Fine;
import service.TaxDatabase;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            PersonDAO personDAO = new PostgresPersonDAO(connection);
            TaxDatabase taxDatabase = new TaxDatabase(personDAO);

            taxDatabase.addPerson("1", "John Doe", "Kyiv");
            taxDatabase.addFineToPerson("1", "Speeding", 100.0);

            taxDatabase.printDatabase();
            taxDatabase.printPersonById("1");
            taxDatabase.printPersonsByCity("Kyiv");

            taxDatabase.removeFineFromPerson("1", "Speeding");
            taxDatabase.printPersonById("1");

            taxDatabase.updatePerson("1", "John Smith", "Kyiv", List.of(new Fine("Parking", 50.0)));
            taxDatabase.printPersonById("1");

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to database", e);
        }
    }
}