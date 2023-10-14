package ru.greenatom.zmaev.greenatomjdbc.domain.mapper;

import org.mapstruct.Mapper;
import ru.greenatom.zmaev.greenatomjdbc.domain.dto.PersonDTO;
import ru.greenatom.zmaev.greenatomjdbc.domain.entity.Person;

@Mapper(componentModel = "spring")
public interface PersonMapper extends EntityMapper<PersonDTO, Person> {
    PersonDTO toDto(Person p);
    Person toEntity(PersonDTO p);

}
