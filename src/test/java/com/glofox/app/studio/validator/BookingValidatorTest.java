package com.glofox.app.studio.validator;

import com.glofox.app.studio.entity.Booking;
import com.glofox.app.studio.entity.SportClass;
import com.glofox.app.studio.service.SportClassService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BookingValidatorTest {

    @Mock
    private SportClassService sportClassService;

    private BookingValidator classToTest;

    @BeforeEach
    void setUp() {
        classToTest = new BookingValidator(sportClassService);
    }

    @Test
    void shouldValidateIfClassExistsForBookingDate() {
        //Given
        SportClass sportClass = new SportClass();
        sportClass.setName("name");
        sportClass.setCapacity(12);
        sportClass.setStartDate(LocalDate.of(2020, 3, 10));
        sportClass.setEndDate(LocalDate.of(2020, 3, 25));

        given(sportClassService.findAllSportClasses()).willReturn(Collections.singletonList(sportClass));

        Booking booking = new Booking();
        booking.setName("name");
        booking.setDate(LocalDate.of(2020, 3, 15));

        //When
        String result = classToTest.validate(booking);

        //Then
        assertThat(result).isNull();
    }

    @Test
    void shouldNotValidateIfClassDoesNotExistsForBookingDate() {
        //Given
        SportClass sportClass = new SportClass();
        sportClass.setName("name");
        sportClass.setCapacity(12);
        sportClass.setStartDate(LocalDate.of(2020, 3, 10));
        sportClass.setEndDate(LocalDate.of(2020, 3, 25));

        given(sportClassService.findAllSportClasses()).willReturn(Collections.singletonList(sportClass));

        Booking booking = new Booking();
        booking.setName("name");
        booking.setDate(LocalDate.of(2020, 4, 15));

        //When
        String result = classToTest.validate(booking);

        //Then
        assertThat(result).isNotBlank();
        assertThat(result).isEqualTo("No class available that day");
    }

}