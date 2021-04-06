package com.lonewolf.recko.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "admins")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Admin {

    @Id
    @Column(name = "username")
    @Setter(AccessLevel.NONE)
    @JsonProperty("username")
    private String adminName;

    @Column(nullable = false)
    private String password;
}
