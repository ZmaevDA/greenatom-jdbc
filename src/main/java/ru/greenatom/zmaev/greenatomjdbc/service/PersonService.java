package ru.greenatom.zmaev.greenatomjdbc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.greenatom.zmaev.greenatomjdbc.dao.PersonDAO;
import ru.greenatom.zmaev.greenatomjdbc.domain.dto.PersonDTO;
import ru.greenatom.zmaev.greenatomjdbc.domain.entity.Person;
import ru.greenatom.zmaev.greenatomjdbc.domain.entity.PersonRequest;
import ru.greenatom.zmaev.greenatomjdbc.domain.mapper.PersonMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {
    private PersonDAO personDAO;
    private final PersonMapper personMapper;

    @Autowired
    public PersonService(PersonDAO personDAO, PersonMapper personMapper) {
        this.personDAO = personDAO;
        this.personMapper = personMapper;
    }

    public PersonDTO findPersonById(Long id) {
        return personMapper.toDto(personDAO.findById(id));
    }

    public PersonDTO save(PersonDTO personDTO) {
        Person person = personMapper.toEntity(personDTO);
        personDAO.create(person);
        return personMapper.toDto(person);
    }

    public void update(Long id, PersonDTO personDTO) {
        personDAO.update(id, personMapper.toEntity(personDTO));
    }

    public void delete(Long id) {
        personDAO.delete(id);
    }

    public List<Person> findAll(PersonRequest personRequest) {
        return personDAO.getAll(personRequest);
    }
}
