package com.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Author(@JsonAlias("birth_year") int birthYear,
                     @JsonAlias("death_year") int deathYear,
                     @JsonAlias("name") String name) {}
