package ru.greenatom.zmaev.greenatomjdbc.repository;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.greenatom.zmaev.greenatomjdbc.domain.entity.Person;

import java.util.List;

@Repository
public class PersonRepository implements MyCrudRepository<Person> {

    private final JdbcTemplate jdbcTemplate;

    public PersonRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(Person entity) {
        return jdbcTemplate.update("INSERT INTO person(age, firstname, lastname, is_admin) VALUES(?, ?, ?, ?)",
                entity.getAge(), entity.getFirstname(), entity.getLastname(), entity.getIsAdmin());
    }

    @Override
    public int update(Person entity) {
        return jdbcTemplate.update("UPDATE person SET age=?, firstname=?, lastname=?, is_admin=? WHERE id=?",
                entity.getAge(), entity.getFirstname(), entity.getLastname(), entity.getIsAdmin(), entity.getId());
    }

    @Override
    public Person findById(Long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM person WHERE id=?",
                    BeanPropertyRowMapper.newInstance(Person.class), id);
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
    }

    @Override
    public List<Person> findAll() {
        return jdbcTemplate.query("SELECT * FROM person",
                BeanPropertyRowMapper.newInstance(Person.class));
    }
}
