package com.glofox.app.studio.validator;

import com.glofox.app.studio.entity.SportClass;
import com.glofox.app.studio.service.SportClassService;
import com.sun.javafx.binding.StringFormatter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SportClassValidator {

    private final SportClassService sportClassService;

    public String validate(SportClass sportClass) {
        if(!sportClass.getEndDate().isAfter(sportClass.getStartDate())){
            return "Date Error - End Date must be after Start Date";
        }

        List<SportClass> existingSportClasses = sportClassService.findAllSportClasses();

        for (SportClass existingSportClass : existingSportClasses) {
            if(isOverlapping(sportClass, existingSportClass)){
                return StringFormatter.format("Date Error - The new class is overlapping with Class: [%s] for dates: [%s - %s].", existingSportClass.getName(), existingSportClass.getStartDate(), existingSportClass.getEndDate()).getValue();
            }
        }
        return null;
    }

    private boolean isOverlapping(SportClass newSportClass, SportClass existingSportClass) {
        return !(newSportClass.getEndDate().isBefore(existingSportClass.getStartDate()) || newSportClass.getStartDate().isAfter(existingSportClass.getEndDate()));
    }

}
