package com.lonewolf.recko.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "consumers")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Consumer {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @JsonIgnore
    private long id;

    @Column(name = "consumer_id")
    @JsonProperty(value = "id")
    private String consumerId;

    @Column(nullable = false)
    @JsonProperty(required = true)
    private String name;

    @Column(length = 30, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Setter(AccessLevel.NONE)
    private double amount;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Setter(AccessLevel.NONE)
    private LocalDate date;

    @Column(nullable = false)
    @JsonProperty(required = true)
    private String type;

    @Column(name = "update_count")
    @JsonIgnore
    private String updateCount;

    @Column(name = "active", nullable = false)
    @JsonIgnore
    private boolean isActive = true;

    @ManyToOne
    @Setter(AccessLevel.NONE)
    private CompanyCredential credential;

    public Consumer() {
    }

    public Consumer(String consumerId, String name, double amount, LocalDate date, String type,
                    CompanyCredential credential) {
        this.consumerId = consumerId;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.credential = credential;
    }
}
