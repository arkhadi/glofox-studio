package com.glofox.app.studio.controller;

import com.glofox.app.studio.entity.SportClass;
import com.glofox.app.studio.service.SportClassService;
import com.sun.javafx.binding.StringFormatter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("classes")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SportClassController {

    private final SportClassService sportClassService;

    @PostMapping
    public ResponseEntity<SportClass>  createSportClass(@RequestBody SportClass sportClass) {
        return ResponseEntity.ok(sportClassService.saveSportClass(sportClass));
    }

    @GetMapping
    public ResponseEntity<List<SportClass>> getSportClasses() {
        return ResponseEntity.ok(sportClassService.findAllSportClasses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SportClass> getSportClassByName(@PathVariable Integer id) {
        Optional<SportClass> sportClassOptional = sportClassService.findSportClassById(id);
        return sportClassOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<SportClass> updateSportClass(@RequestBody SportClass sportClass) {
        return ResponseEntity.ok(sportClassService.updateSportClass(sportClass));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSportClass(@PathVariable Integer id) {
        if(sportClassService.deleteSportClass(id)) {
            return ResponseEntity.ok(StringFormatter.format("Sport Class with id: %s successfully deleted", id).getValue());
        }
        return ResponseEntity.notFound().build();
    }
}
