package ru.maxbri.RestApp.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.maxbri.RestApp.util.measure.MeasurementNotCreatedException;

import java.util.List;

public class ErrorUtil {
    public static void returnErrors(BindingResult bindingResult) {
        StringBuilder stringBuilder = new StringBuilder();

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError error : fieldErrors) {
            stringBuilder.append(error.getField())
                    .append(" - ").append(error.getDefaultMessage() == null ? error.getCode() : error.getDefaultMessage())
                    .append(";");
        }

        throw new MeasurementNotCreatedException(stringBuilder.toString());
    }
}
