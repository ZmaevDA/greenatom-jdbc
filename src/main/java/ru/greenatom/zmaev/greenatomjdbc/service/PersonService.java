package ru.greenatom.zmaev.greenatomjdbc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.greenatom.zmaev.greenatomjdbc.domain.dto.PersonDTO;
import ru.greenatom.zmaev.greenatomjdbc.domain.entity.Person;
import ru.greenatom.zmaev.greenatomjdbc.domain.mapper.PersonMapper;
import ru.greenatom.zmaev.greenatomjdbc.repository.PersonRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public List<PersonDTO> findAll() {
        return personRepository.findAll()
                .stream().map(personMapper::toDto)
                .collect(Collectors.toList());
    }

    public PersonDTO findPersonById(Long id) {
        return personMapper.toDto(personRepository.findById(id));
    }

    public PersonDTO save(PersonDTO personDTO) {
        Person person = personMapper.toEntity(personDTO);
        personRepository.save(person);
        return personMapper.toDto(person);
    }

    public int update(Long id, PersonDTO personDTO) {
        Person oldPerson = personMapper.toEntity(findPersonById(id));
        oldPerson.setAge(personDTO.getAge());
        oldPerson.setFirstname(personDTO.getFirstname());
        oldPerson.setLastname(personDTO.getLastname());
        oldPerson.setIsAdmin(personDTO.getIsAdmin());
        return personRepository.update(oldPerson);
    }

    public void delete(Long id) {
        personRepository.deleteById(id);
    }
}
