package com.glofox.app.studio.service;

import com.glofox.app.studio.entity.SportClass;
import com.glofox.app.studio.repository.SportClassRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SportClassService {

    private final SportClassRepository sportClassRepository;

    public List<SportClass> findAllSportClasses() {
        return sportClassRepository.findAll();
    }

    public Optional<SportClass> findSportClassById(Integer id) {
        return sportClassRepository.findById(id);
    }

    public SportClass saveSportClass(SportClass sportClass) {
        return sportClassRepository.save(sportClass);
    }

    public SportClass updateSportClass(SportClass sportClass) {
        if(sportClassRepository.existsById(sportClass.getId())) {
            return  saveSportClass(sportClass);
        }
        return null;
    }

    public boolean deleteSportClass(Integer id) {
        try {
            sportClassRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException erdae) {
            return false;
        }
    }
}
