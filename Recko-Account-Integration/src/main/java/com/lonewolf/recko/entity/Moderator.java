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
@Table(name = "moderators")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Moderator {

    @Id
    @Column(name = "username")
    @Setter(AccessLevel.NONE)
    @JsonProperty("username")
    private String moderatorName;

    @Column(nullable = false)
    private String password;
}