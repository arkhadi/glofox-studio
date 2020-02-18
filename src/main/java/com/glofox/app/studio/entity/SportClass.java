package com.glofox.app.studio.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "CLASS")
public class SportClass {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CAPACITY")
    private Integer capacity;

    @Column(name = "START_DATE", columnDefinition="DATETIME")
    private LocalDate startDate;

    @Column(name = "END_DATE", columnDefinition="DATETIME")
    private LocalDate endDate;
}
