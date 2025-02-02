package com.arthur.webflux.framework.trainning;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "users")
public record User(@Id Long id, String username, String password, String email) {
}
