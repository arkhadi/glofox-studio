package com.glofox.app.studio.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Column(name = "CAPACITY")
    @NotNull(message = "Capacity is mandatory")
    private Integer capacity;

    @Column(name = "START_DATE", columnDefinition="DATETIME")
    @NotNull(message = "Start Date is mandatory")
    private LocalDate startDate;

    @Column(name = "END_DATE", columnDefinition="DATETIME")
    @NotNull(message = "End Date is mandatory")
    private LocalDate endDate;
}
