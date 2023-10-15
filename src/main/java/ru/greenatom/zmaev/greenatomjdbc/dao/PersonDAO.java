package ru.greenatom.zmaev.greenatomjdbc.dao;

import ru.greenatom.zmaev.greenatomjdbc.domain.entity.Person;
import ru.greenatom.zmaev.greenatomjdbc.domain.entity.PersonRequest;

import java.util.List;
import java.util.Map;

public interface PersonDAO {

    void create(Person person);

    void update(Long id, Person person);

    void delete(Long id);

    Person findById(Long id);

    List<Person> getAll(PersonRequest personRequest);
}
