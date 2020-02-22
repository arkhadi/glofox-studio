package com.glofox.app.studio.validator;

import com.glofox.app.studio.entity.SportClass;
import com.glofox.app.studio.service.SportClassService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BookingValidator implements ConstraintValidator<BookingDate, LocalDate> {

    private final SportClassService sportClassService;

    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        List<SportClass> sportClasses = sportClassService.findAllSportClasses();
        boolean existsClass = false;
        for(SportClass sportClass : sportClasses) {
            if(isDateInRange(date, sportClass.getStartDate(), sportClass.getEndDate())) {
                existsClass = true;
                break;
            }
        }
        return existsClass;
    }

    private boolean isDateInRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}


