package com.glofox.app.studio.repository;

import com.glofox.app.studio.entity.SportClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SportClassRepository extends JpaRepository<SportClass, Integer> {

    Optional<SportClass> findByName(String name);

    void deleteByName(String name);
}
