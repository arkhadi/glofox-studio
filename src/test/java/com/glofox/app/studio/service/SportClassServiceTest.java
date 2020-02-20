package com.glofox.app.studio.service;


import com.glofox.app.studio.entity.SportClass;
import com.glofox.app.studio.repository.SportClassRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SportClassServiceTest {

    @Mock
    private SportClassRepository sportClassRepository;

    private SportClassService classToTest;

    @BeforeEach
    void setUp() {
        classToTest = new SportClassService(sportClassRepository);
    }

    @Test
    void shouldFindAllSportClasses() {
        //Given
        SportClass sportClass = new SportClass();
        List<SportClass> sportClasses = Collections.singletonList(sportClass);
        given(sportClassRepository.findAll()).willReturn(sportClasses);

        //When
        List<SportClass> result = classToTest.findAllSportClasses();

        //Then
        assertThat(result).isNotNull();
        assertThat(result.get(0)).isEqualTo(sportClass);

    }

    @Test
    void shouldFindSportClassByName() {
        //Given
        SportClass sportClass = new SportClass();
        sportClass.setId(1);
        sportClass.setName("name");
        given(sportClassRepository.findById(eq(1))).willReturn(Optional.of(sportClass));

        //When
        Optional<SportClass> result = classToTest.findSportClassById(1);

        //Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
        assertThat(result.get().getName()).isEqualTo("name");
    }

    @Test
    void shouldNotFindSportClassByName() {
        //Given
        SportClass sportClass = new SportClass();
        sportClass.setName("name");

        given(sportClassRepository.findById(eq(1))).willReturn(Optional.empty());

        //When
        Optional<SportClass> result = classToTest.findSportClassById(1);

        //Then
        assertThat(result).isNotPresent();
    }

    @Test
    void shouldSaveSportClass() {
        //Given
        SportClass sportClass = new SportClass();

        ArgumentCaptor<SportClass> captor = ArgumentCaptor.forClass(SportClass.class);
        given(sportClassRepository.save(captor.capture())).willReturn(sportClass);

        //When
        SportClass result = classToTest.saveSportClass(sportClass);

        //Then
        verify(sportClassRepository).save(sportClass);
        assertThat(result).isEqualTo(captor.getValue());
    }

    @Test
    void shouldUpdateSportClass() {
        //Given
        SportClass sportClass = new SportClass();
        sportClass.setId(1);

        SportClass updatedSportClass = new SportClass();
        updatedSportClass.setId(1);

        ArgumentCaptor<SportClass> captor = ArgumentCaptor.forClass(SportClass.class);
        given(sportClassRepository.existsById(1)).willReturn(true);
        given(sportClassRepository.save(captor.capture())).willReturn(updatedSportClass);

        //When
        SportClass result = classToTest.updateSportClass(sportClass);

        //Then
        verify(sportClassRepository).save(sportClass);
        assertThat(captor.getValue()).isEqualTo(sportClass);
        assertThat(result).isEqualTo(updatedSportClass);
    }

    @Test
    void shouldNotUpdateSportClass() {
        //Given
        SportClass sportClass = new SportClass();
        sportClass.setId(1);

        given(sportClassRepository.existsById(1)).willReturn(false);

        //When
        SportClass result = classToTest.updateSportClass(sportClass);

        //Then
        verify(sportClassRepository).existsById(1);
        verify(sportClassRepository, times(0)).save(any());
    }

    @Test
    void shouldDeleteSportClass() {
        //Given
        SportClass sportClass = new SportClass();

        Mockito.doNothing().when(sportClassRepository).deleteById(1);

        //When
        boolean result = classToTest.deleteSportClass(1);

        //Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldNotDeleteSportClass() {
        //Given
        SportClass sportClass = new SportClass();

        Mockito.doThrow(EmptyResultDataAccessException.class).when(sportClassRepository).deleteById(1);

        //When
        boolean result = classToTest.deleteSportClass(1);

        //Then
        assertThat(result).isFalse();
    }


}