package ru.maxbri.RestApp.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.maxbri.RestApp.dto.MeasurementDTO;
import ru.maxbri.RestApp.models.Measurement;
import ru.maxbri.RestApp.services.MeasurementService;
import ru.maxbri.RestApp.util.measure.MeasureValidator;
import ru.maxbri.RestApp.util.measure.MeasurementErrorResponse;
import ru.maxbri.RestApp.util.measure.MeasurementNotCreatedException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.maxbri.RestApp.util.ErrorUtil.returnErrors;

@RestController // @Controller, а также @ResponseBody над каждым методом
@RequestMapping("/measurements")
public class MeasurementController {
    private final MeasurementService measurementService;
    private final ModelMapper mapper;
    private final MeasureValidator measureValidator;

    @Autowired
    public MeasurementController(MeasurementService measurementService, ModelMapper mapper, MeasureValidator measureValidator) {
        this.measurementService = measurementService;
        this.mapper = mapper;
        this.measureValidator = measureValidator;
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return mapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement){
        return mapper.map(measurement, MeasurementDTO.class);
    }

    @GetMapping
    public List<MeasurementDTO> getMeasurements(){
        return measurementService.findAll().stream().map(this::convertToMeasurementDTO).collect(Collectors.toList());
    }

    @GetMapping("/raining")
    public Long countRainyDays(){
        return measurementService.findAll().stream().filter(measurement -> measurement.getRaining()).count();
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid MeasurementDTO measurementDTO,
                                             BindingResult bindingResult){ //в этот метод придет JSON и RequestBody сконвертирует его в Sensor
        Measurement measurement = convertToMeasurement(measurementDTO);

        measureValidator.validate(measurement, bindingResult);
        if (bindingResult.hasErrors()){
            returnErrors(bindingResult);
        }

        measurementService.save(measurement);
        //отправляем хттп ответ с пустым телом и статусом 200
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementNotCreatedException e){
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(), System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
