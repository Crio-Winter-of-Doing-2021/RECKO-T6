package com.lonewolf.recko.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "admins")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Admin {

    @Id
    @Column(name = "username")
    @JsonProperty("username")
    private String adminName;

    @Column
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    private Company company;
}
