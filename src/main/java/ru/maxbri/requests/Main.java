package ru.maxbri.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.knowm.xchart.*;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import ru.maxbri.RestApp.models.Sensor;

import java.text.DecimalFormat;
import java.util.*;


//этот класс использует созданное RestApp
public class Main {
    static String url;
    static String response;
    static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) throws JsonProcessingException {

        /* расскомментить, если надо отправить 1000 POST запросов
        post();
         */
        System.out.println(get());
        drawChart(get());
    }

    public static void drawChart(String response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode obj = mapper.readTree(response);
        List<Double> list = new ArrayList<>();
        double x;
        for (int i = 0; i < 200; i++) {
            x = obj.get(i).get("value").doubleValue();
            list.add(x);
        }
        //todo как-то надо увеличить шаг X, а то график сжатый-скомканный-некрасивый
        XYChart chart = new XYChartBuilder().xAxisTitle("X").yAxisTitle("Y").width(1600).height(700).build();
        XYSeries series = chart.addSeries("temperature", null, list);
        new SwingWrapper(chart).displayChart();
    }

    public static void post() {
        Random random = new Random();
        boolean isRaining;
        double temperature;
        Map<String, Object> jsonToSend = new HashMap<>();

        Sensor sensor = new Sensor();
        sensor.setId(3);
        sensor.setName("test3");

        String formattedTemperature;
        for (int i = 0; i <= 1000; i++) { //отправка 1000 POST запросов
            isRaining = random.nextBoolean();
            temperature = random.nextDouble(-100.0, 100.0);
            formattedTemperature = new DecimalFormat("#0.0").format(temperature).replace(',', '.');
            temperature = Double.parseDouble(formattedTemperature);

            jsonToSend.put("value", temperature);
            jsonToSend.put("sensor", sensor);
            jsonToSend.put("raining", isRaining);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(jsonToSend);
            url = "http://localhost:8080/measurements/add";
            response = restTemplate.postForObject(url, request, String.class);
        }
    }

    public static String get() {
        url = "http://localhost:8080/measurements";
        response = restTemplate.getForObject(url, String.class);
        return response;
    }
}
