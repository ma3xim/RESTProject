package ru.maxbri.RestApp.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import ru.maxbri.RestApp.models.Measurement;

import java.util.List;


public class SensorDTO {
    @NotEmpty(message = "Название не должно быть пустым")
    @Size(min = 3, max = 30, message = "Имя сенсора должно быть от 3 до 30 символов")
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
