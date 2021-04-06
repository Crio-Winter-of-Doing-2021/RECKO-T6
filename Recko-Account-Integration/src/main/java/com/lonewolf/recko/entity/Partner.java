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
@Table(name = "partners")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Partner {

    @Id
    @Column(name = "name", length = 20, nullable = false, unique = true)
    @Setter(AccessLevel.NONE)
    private String name;

    @Column(name = "description", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Setter(AccessLevel.NONE)
    private String description;

    @Column(name = "active", nullable = false)
    @JsonIgnore
    private boolean isActive = true;

    @OneToMany(mappedBy = "partner")
    @Setter(AccessLevel.NONE)
    @JsonIgnore
    private final List<PartnerCredential> credentials = new ArrayList<>();
}
