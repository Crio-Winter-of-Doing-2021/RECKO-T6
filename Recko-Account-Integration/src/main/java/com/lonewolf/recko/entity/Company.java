package com.lonewolf.recko.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Company {

    @Id
    @Column
    @JsonProperty("id")
    private String id;

    @Column
    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column
    @JsonProperty(value = "name", access = JsonProperty.Access.WRITE_ONLY)
    private String name;

    @OneToMany(mappedBy = "company")
    @Setter(AccessLevel.NONE)
    @JsonIgnore
    private List<Admin> admins = new ArrayList<>();

    @OneToMany(mappedBy = "company")
    @Setter(AccessLevel.NONE)
    @JsonIgnore
    private List<Moderator> moderators = new ArrayList<>();

    @OneToMany(mappedBy = "company")
    @Setter(AccessLevel.NONE)
    @JsonIgnore
    private List<CompanyCredential> credentials = new ArrayList<>();
}
