package com.lonewolf.recko.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "moderators")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Moderator {

    @Id
    @Column(name = "username")
    @JsonProperty("username")
    private String moderatorName;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    private Company company;
}