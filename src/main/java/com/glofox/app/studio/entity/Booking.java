package com.glofox.app.studio.entity;

import com.glofox.app.studio.validator.BookingDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

interface Cheap {}
interface Expensive {}


@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "BOOKING")
@GroupSequence(value={Cheap.class, Expensive.class, Booking.class})
public class Booking {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    @NotBlank(message = "Name is mandatory", groups = Cheap.class)
    private String name;

    @Column(name = "DATE", columnDefinition="DATETIME")
    @NotNull(message = "Date is mandatory", groups = Cheap.class)
    @BookingDate(groups = Expensive.class)
    private LocalDate date;
}
