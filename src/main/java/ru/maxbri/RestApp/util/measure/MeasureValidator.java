package ru.maxbri.RestApp.util.measure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.maxbri.RestApp.models.Measurement;
import ru.maxbri.RestApp.services.MeasurementService;
import ru.maxbri.RestApp.services.SensorService;

@Component
public class MeasureValidator implements Validator {

    private final MeasurementService measurementService;
    private final SensorService sensorService;

    @Autowired
    public MeasureValidator(MeasurementService measurementService, SensorService sensorService) {
        this.measurementService = measurementService;
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return MeasurementService.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Measurement measurement = (Measurement) target;

        if (measurement.getSensor() == null){
            return;
        }

        if (sensorService.findByName(measurement.getSensor().getName()) == null){
            errors.rejectValue("sensor","", "Нет сенсора с таким именем");
        }
    }
}
