package com.technical.test.gli.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "DOG")
public class Dog {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "DOG_NAME")
    private String dogName;
    @Column(name = "`VALUE`")
    private String value;

}
