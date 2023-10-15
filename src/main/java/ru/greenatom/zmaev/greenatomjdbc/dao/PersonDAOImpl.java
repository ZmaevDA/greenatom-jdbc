package ru.greenatom.zmaev.greenatomjdbc.dao;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.greenatom.zmaev.greenatomjdbc.domain.entity.Person;
import ru.greenatom.zmaev.greenatomjdbc.domain.entity.PersonRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PersonDAOImpl implements PersonDAO {

    private static final String JDBC_URL = "jdbc:postgresql://localhost/user_db";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    private Connection connection;

    public PersonDAOImpl() throws SQLException {
        this.connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    @Override
    public void create(Person person) {
        String query = "INSERT INTO person (age, firstname, lastname, is_admin) VALUES (?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, person.getAge());
            preparedStatement.setString(2, person.getFirstname());
            preparedStatement.setString(3, person.getLastname());
            preparedStatement.setBoolean(4, person.getIsAdmin());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Long id, Person person) {
        String query = "UPDATE person SET age=?, firstname=?, lastname=?, is_admin=? WHERE id=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, person.getAge());
            preparedStatement.setString(2, person.getFirstname());
            preparedStatement.setString(3, person.getLastname());
            preparedStatement.setBoolean(4, person.getIsAdmin());
            preparedStatement.setLong(5, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM person where id=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Person findById(Long id) {
        String query = "SELECT * FROM person WHERE id=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                return toPerson(res);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @SneakyThrows
    @Override
    public List<Person> getAll(PersonRequest personRequest) {
        List<Person> filteredPersons = new ArrayList<>();
        String query = getFindAllFilteredPaginated(personRequest, personRequest.getCriteria());
        List<Object> values = personRequest.getParams();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int i = 1;
            for (Object value : values) {
                statement.setObject(i++, value);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Person person = Person.builder()
                            .age(resultSet.getInt("age"))
                            .firstname(resultSet.getString("firstname"))
                            .lastname(resultSet.getString("lastname"))
                            .isAdmin(resultSet.getBoolean("is_admin"))
                            .build();
                    person.setId(resultSet.getLong("id"));
                    person.setCreationTime(resultSet.getTimestamp("creation_time").toLocalDateTime());
                    filteredPersons.add(person);
                }
            }
        }
        return filteredPersons;
    }

    private String getFindAllFilteredPaginated(PersonRequest personRequest, Map<String, Object> criteria) {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        if (personRequest.isPageable()) {
            sb.append("(SELECT *, ROW_NUMBER() OVER (ORDER BY id) FROM person) x");
        }
        if (personRequest.isFilter() || personRequest.isPageable()) {
            sb.append(" WHERE ");
        }
        if (personRequest.isFilter()) {
            for (Map.Entry<String, Object> entry : criteria.entrySet()) {
                if (sb.length() > 30) {
                    sb.append(" AND ");
                }
                sb.append(entry.getKey()).append(" = ?");
            }
        }
        if (personRequest.isPageable()) {
            sb.append("ROW_NUMBER BETWEEN ? AND ?");
        }
        return sb.toString();
    }

    private Person toPerson(ResultSet res) throws SQLException {
        return new Person(
                res.getLong("id"),
                res.getInt("age"),
                res.getString("firstname"),
                res.getString("lastname"),
                res.getBoolean("is_admin"),
                res.getTimestamp("creation_time").toLocalDateTime()
        );
    }
}
