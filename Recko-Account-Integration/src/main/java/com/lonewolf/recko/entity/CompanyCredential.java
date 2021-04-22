package com.lonewolf.recko.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "credentials")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CompanyCredential {

    @Id
    @Column
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "client_id")
    @JsonIgnore
    private String clientId;

    @Column(name = "client_secret")
    @JsonIgnore
    private String clientSecret;

    @Column(name = "application_id")
    @JsonIgnore
    private String applicationId;

    @Column(name = "scope")
    @JsonIgnore
    private String scope;

    @Column(name = "access_token", length = 2048)
    @JsonIgnore
    private String accessToken;

    @Column(name = "refresh_token")
    @JsonIgnore
    private String refreshToken;

    @Column(name = "last_access")
    @JsonIgnore
    private LocalDateTime lastAccess;

    @ManyToOne
    private Partner partner;

    @ManyToOne
    private Company company;

    @OneToMany(mappedBy = "credential")
    @Setter(AccessLevel.NONE)
    @JsonIgnore
    private final List<Consumer> consumers = new ArrayList<>();

    @OneToMany(mappedBy = "credential")
    @Setter(AccessLevel.NONE)
    @JsonIgnore
    private final List<Transaction> transactions = new ArrayList<>();
}