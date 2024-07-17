package com.arthur.webflux.framework.trainning.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Table(name = "Anime")
public class Anime {

    @Id
    private Integer id;

    @NotNull
    @NotEmpty(message = "The name of this anime cannot be empty")
    private String nome;
}
