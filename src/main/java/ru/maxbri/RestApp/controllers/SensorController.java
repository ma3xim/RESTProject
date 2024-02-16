package ru.maxbri.RestApp.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.maxbri.RestApp.dto.SensorDTO;
import ru.maxbri.RestApp.models.Sensor;
import ru.maxbri.RestApp.services.SensorService;
import ru.maxbri.RestApp.util.sensor.SensorErrorResponse;
import ru.maxbri.RestApp.util.sensor.SensorNotCreatedException;
import ru.maxbri.RestApp.util.sensor.SensorNotFoundException;
import ru.maxbri.RestApp.util.sensor.SensorValidator;

import java.util.List;
import java.util.stream.Collectors;

import static ru.maxbri.RestApp.util.ErrorUtil.returnErrors;

@RestController // @Controller + @ResponseBody над каждым методом
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService sensorService;
    private final ModelMapper mapper;
    private final SensorValidator sensorValidator;

    @Autowired
    public SensorController(SensorService sensorService, ModelMapper mapper, SensorValidator sensorValidator) {
        this.sensorService = sensorService;
        this.mapper = mapper;
        this.sensorValidator = sensorValidator;
    }

    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return mapper.map(sensorDTO, Sensor.class);
    }

    private SensorDTO convertToSensorDTO(Sensor sensor){
        return mapper.map(sensor, SensorDTO.class);
    }

    @GetMapping()
    public List<SensorDTO> getSensors() {
        return sensorService.findAll().stream().map(this::convertToSensorDTO).collect(Collectors.toList()); // Jackson конвертирует эти объекты в JSON
    }

    @GetMapping("/{id}")
    public SensorDTO getSensor(@PathVariable("id") int id) {
        return convertToSensorDTO(sensorService.findOne(id)); // Jackson конвертирует в JSON
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid SensorDTO sensorDTO,
                                             BindingResult bindingResult){ //в этот метод придет JSON и RequestBody сконвертирует его в Sensor
        Sensor sensor = convertToSensor(sensorDTO);
        sensorValidator.validate(sensor, bindingResult);

        if (bindingResult.hasErrors()){
            returnErrors(bindingResult);
        }

        sensorService.save(convertToSensor(sensorDTO));
        //отправляем хттп ответ с пустым телом и статусом 200
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorNotFoundException e){
        SensorErrorResponse response = new SensorErrorResponse(
                e.getMessage(), System.currentTimeMillis()
        );

        // в HTTP ответе будет тело ответа(response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorNotCreatedException e){
        SensorErrorResponse response = new SensorErrorResponse(
                e.getMessage(), System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}