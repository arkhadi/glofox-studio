package com.glofox.app.studio.validator;

import com.glofox.app.studio.entity.Booking;
import com.glofox.app.studio.entity.SportClass;
import com.glofox.app.studio.service.SportClassService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;

import java.time.LocalDate;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BookingValidatorTest {

    @Mock
    private SportClassService sportClassService;

    private BookingValidator classToTest;

    @BeforeEach
    public void setUp() {
        classToTest = new BookingValidator(sportClassService);
    }

    @Test
    public void shouldSupportBookingClass() {
        //Given
        //When
        boolean result = classToTest.supports(Booking.class);

        //Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldNotSupportOtherClass() {
        //Given
        //When
        boolean result = classToTest.supports(SportClass.class);

        //Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldValidateReturnEmptyErrorsWhenMatchingDate() {
        //Given
        LocalDate startDateMatching = LocalDate.of(2020, 2, 10);
        LocalDate endDateMatching = LocalDate.of(2020, 2, 24);
        SportClass sportClassMatching = new SportClass();
        sportClassMatching.setStartDate(startDateMatching);
        sportClassMatching.setEndDate(endDateMatching);

        LocalDate startDateNotMatching = LocalDate.of(2020, 4, 10);
        LocalDate endDateNotMatching = LocalDate.of(2020, 4, 24);
        SportClass sportClassNotMatching = new SportClass();
        sportClassNotMatching.setStartDate(startDateNotMatching);
        sportClassNotMatching.setEndDate(endDateNotMatching);

        LocalDate bookingDate = LocalDate.of(2020, 2, 15);
        Booking booking = new Booking();
        booking.setName("name");
        booking.setDate(bookingDate);

        given(sportClassService.findAllSportClasses()).willReturn(Arrays.asList(sportClassMatching, sportClassNotMatching));
        BeanPropertyBindingResult beanPropertyBindingResult = new BeanPropertyBindingResult(booking, "booking");
        BindException errors = new BindException(beanPropertyBindingResult);

        //When
        classToTest.validate(booking, errors);

        //Then
        assertThat(errors.getAllErrors()).isEmpty();
    }

    @Test
    void shouldValidateReturnErrorsWhenNotMatchingDate() {
        //Given
        LocalDate startDateNotMatching = LocalDate.of(2020, 3, 10);
        LocalDate endDateNotMatching = LocalDate.of(2020, 3, 24);
        SportClass sportClassNotMatching = new SportClass();
        sportClassNotMatching.setStartDate(startDateNotMatching);
        sportClassNotMatching.setEndDate(endDateNotMatching);

        LocalDate startDateNotMatching2 = LocalDate.of(2020, 4, 10);
        LocalDate endDateNotMatching2 = LocalDate.of(2020, 4, 24);
        SportClass sportClassNotMatching2 = new SportClass();
        sportClassNotMatching2.setStartDate(startDateNotMatching2);
        sportClassNotMatching2.setEndDate(endDateNotMatching2);

        LocalDate bookingDate = LocalDate.of(2020, 2, 15);
        Booking booking = new Booking();
        booking.setName("name");
        booking.setDate(bookingDate);

        given(sportClassService.findAllSportClasses()).willReturn(Arrays.asList(sportClassNotMatching, sportClassNotMatching2));
        BeanPropertyBindingResult beanPropertyBindingResult = new BeanPropertyBindingResult(booking, "booking");
        BindException errors = new BindException(beanPropertyBindingResult);

        //When
        classToTest.validate(booking, errors);

        //Then
        assertThat(errors.getAllErrors()).isNotEmpty();
        assertThat(errors.getFieldErrorCount()).isEqualTo(1);
        assertThat(errors.getAllErrors().get(0).getCodes()).contains("No class available the requested day");

    }

}