package com.cognizant.mortgagefeasibilityvalidator.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @OneToMany(mappedBy = "request", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Mortgage> mortgages;
    private String company;

}
