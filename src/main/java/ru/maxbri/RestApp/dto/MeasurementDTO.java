package ru.maxbri.RestApp.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Range;
import ru.maxbri.RestApp.models.Sensor;

public class MeasurementDTO {
    @NotNull
    private Boolean isRaining;

    @NotNull
    @Min(value = -100, message = "Значение должно быть от -100 до 100")
    @Max(value = 100, message = "Значение должно быть от -100 до 100")
    private Double value;

    @NotNull
    private SensorDTO sensor;

    public Boolean getRaining() {
        return isRaining;
    }

    public void setRaining(Boolean raining) {
        isRaining = raining;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }
}

