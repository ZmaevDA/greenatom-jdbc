package ru.greenatom.zmaev.greenatomjdbc.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {

    private Long id;

    private Integer age;

    private String firstname;

    private String lastname;

    private Boolean isAdmin;

    private LocalDateTime creationTime;

}
