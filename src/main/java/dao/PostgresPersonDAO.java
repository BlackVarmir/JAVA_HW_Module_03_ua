package dao;

import static java.lang.System.out;

import model.Fine;
import model.Person;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresPersonDAO implements PersonDAO {
    private final Connection connection;

    public PostgresPersonDAO(Connection connection) {
        this.connection = connection;
        createTablesIfNotExists();
    }

    private void createTablesIfNotExists() {
        try (Statement stmt = connection.createStatement()) {

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS persons (" +
                    "id VARCHAR(255) PRIMARY KEY," +
                    "name VARCHAR(255)," +
                    "city VARCHAR(255)" +
                    ")");


            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS fines (" +
                    "person_id VARCHAR(255) REFERENCES persons(id)," +
                    "type VARCHAR(255)," +
                    "amount DOUBLE PRECISION" +
                    ")");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create tables", e);
        }
    }

    @Override
    public void addPerson(String id, String name, String city) {
        String query = "INSERT INTO persons (id, name, city) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, city);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addFineToPerson(String personId, String type, double amount) {
        String query = "INSERT INTO fines (person_id, type, amount) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, personId);
            stmt.setString(2, type);
            stmt.setDouble(3, amount);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Person> getAllPersons() {
        List<Person> persons = new ArrayList<>();
        String query = "SELECT * FROM persons";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                persons.add(resultSetToPerson(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persons;
    }

    @Override
    public Person getPersonById(String id) {
        String query = "SELECT * FROM persons WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return resultSetToPerson(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Person> getPersonsByCity(String city) {
        List<Person> persons = new ArrayList<>();
        String query = "SELECT * FROM persons WHERE city = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, city);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                persons.add(resultSetToPerson(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persons;
    }

    @Override
    public void removeFineFromPerson(String personId, String fineType) {
        String query = "DELETE FROM fines WHERE person_id = ? AND type = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, personId);
            stmt.setString(2, fineType);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePerson(String id, String name, String city, List<Fine> fines) {
        String updatePersonQuery = "UPDATE persons SET name = ?, city = ? WHERE id = ?";
        String deleteFinesQuery = "DELETE FROM fines WHERE person_id = ?";
        String addFineQuery = "INSERT INTO fines (person_id, type, amount) VALUES (?, ?, ?)";

        try (PreparedStatement updatePersonStmt = connection.prepareStatement(updatePersonQuery);
             PreparedStatement deleteFinesStmt = connection.prepareStatement(deleteFinesQuery)) {
            connection.setAutoCommit(false);

            updatePersonStmt.setString(1, name);
            updatePersonStmt.setString(2, city);
            updatePersonStmt.setString(3, id);
            updatePersonStmt.executeUpdate();

            deleteFinesStmt.setString(1, id);
            deleteFinesStmt.executeUpdate();

            try (PreparedStatement addFineStmt = connection.prepareStatement(addFineQuery)) {
                for (Fine fine : fines) {
                    addFineStmt.setString(1, id);
                    addFineStmt.setString(2, fine.getType());
                    addFineStmt.setDouble(3, fine.getAmount());
                    addFineStmt.executeUpdate();
                }
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean personExists(String id) {
        String query = "SELECT COUNT(*) FROM persons WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Person resultSetToPerson(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String name = rs.getString("name");
        String city = rs.getString("city");

        List<Fine> fines = new ArrayList<>();
        String fineQuery = "SELECT * FROM fines WHERE person_id = ?";
        try (PreparedStatement fineStmt = connection.prepareStatement(fineQuery)) {
            fineStmt.setString(1, id);
            ResultSet fineRs = fineStmt.executeQuery();
            while (fineRs.next()) {
                fines.add(new Fine(fineRs.getString("type"), fineRs.getDouble("amount")));
            }
        }

        return new Person(id, name, city, fines);
    }
}
