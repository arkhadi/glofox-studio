package com.glofox.app.studio.validator;

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
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class SportClassValidatorTest {

    @Mock
    private SportClassService sportClassService;

    private SportClassValidator classToTest;

    @BeforeEach
    void setUp() {
        SportClass sportClass = new SportClass();
        sportClass.setName("name");
        sportClass.setCapacity(12);
        sportClass.setStartDate(LocalDate.of(2020, 3, 10));
        sportClass.setEndDate(LocalDate.of(2020, 3, 25));
        lenient().when(sportClassService.findAllSportClasses()).thenReturn(Collections.singletonList(sportClass));

        classToTest = new SportClassValidator(sportClassService);
    }

    @Test
    void shouldValidateWhenNewSportClassEndsBeforeExistingClassStart() {
        //Given
        SportClass newSportClass = new SportClass();
        newSportClass.setStartDate(LocalDate.of(2020, 2,25));
        newSportClass.setEndDate(LocalDate.of(2020, 3,9));

        //When
        String result = classToTest.validate(newSportClass);

        //Then
        assertThat(result).isNull();
    }

    @Test
    void shouldValidateWhenNewSportClassStartsAfterExistingClassEnd() {
        //Given
        SportClass newSportClass = new SportClass();
        newSportClass.setStartDate(LocalDate.of(2020, 3,26));
        newSportClass.setEndDate(LocalDate.of(2020, 4,9));

        //When
        String result = classToTest.validate(newSportClass);

        //Then
        assertThat(result).isNull();
    }

    @Test
    void shouldNotValidateWhenEndDateBeforeStartDate() {
        //Given
        SportClass newSportClass = new SportClass();
        newSportClass.setStartDate(LocalDate.of(2020, 3,26));
        newSportClass.setEndDate(LocalDate.of(2020, 2,9));

        //When
        String result = classToTest.validate(newSportClass);

        //Then
        assertThat(result).isNotBlank();
        assertThat(result).isEqualTo("Date Error - End Date must be after Start Date");
    }

    @Test
    void shouldNotValidateWhenNewEndDateAfterExistingStartDate() {
        //Given
        SportClass newSportClass = new SportClass();
        newSportClass.setStartDate(LocalDate.of(2020, 3,4));
        newSportClass.setEndDate(LocalDate.of(2020, 4,9));

        //When
        String result = classToTest.validate(newSportClass);

        //Then
        assertThat(result).isNotBlank();
        assertThat(result).isEqualTo("Date Error - The new class is overlapping with Class: [name] for dates: [2020-03-10 - 2020-03-25].");
    }

    @Test
    void shouldNotValidateWhenNewStartDateBeforeExistingEndDate() {
        //Given
        SportClass newSportClass = new SportClass();
        newSportClass.setStartDate(LocalDate.of(2020, 3,22));
        newSportClass.setEndDate(LocalDate.of(2020, 4,9));

        //When
        String result = classToTest.validate(newSportClass);

        //Then
        assertThat(result).isNotBlank();
        assertThat(result).isEqualTo("Date Error - The new class is overlapping with Class: [name] for dates: [2020-03-10 - 2020-03-25].");
    }

    @Test
    void shouldNotValidateWhenNewSportClassDatesWithinExistingSportClassDates() {
        //Given
        SportClass newSportClass = new SportClass();
        newSportClass.setStartDate(LocalDate.of(2020, 3,17));
        newSportClass.setEndDate(LocalDate.of(2020, 3,22));

        //When
        String result = classToTest.validate(newSportClass);

        //Then
        assertThat(result).isNotBlank();
        assertThat(result).isEqualTo("Date Error - The new class is overlapping with Class: [name] for dates: [2020-03-10 - 2020-03-25].");
    }

    @Test
    void shouldNotValidateWhenNewSportClassDatesContainExistingSportClass() {
        //Given
        SportClass newSportClass = new SportClass();
        newSportClass.setStartDate(LocalDate.of(2020, 2,17));
        newSportClass.setEndDate(LocalDate.of(2020, 5,22));

        //When
        String result = classToTest.validate(newSportClass);

        //Then
        assertThat(result).isNotBlank();
        assertThat(result).isEqualTo("Date Error - The new class is overlapping with Class: [name] for dates: [2020-03-10 - 2020-03-25].");
    }

}