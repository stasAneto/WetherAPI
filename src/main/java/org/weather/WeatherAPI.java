package org.weather;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class WeatherAPI {
    private final String apiEndpoint  = "http://api.openweathermap.org/";
    private final String apiCommand  = "data/2.5/onecall";
    private final String apiKey  = "3dd91be37658cc49f8b2560f9494d062";
    public String getWeather(){
        try{
            //https://api.openweathermap.org/data/2.5/weather?lat=47.917&lon=33.350&lang=ua&APPID=3dd91be37658cc49f8b2560f9494d062
            String queryStr = "?lat=47.917&lon=33.350&exclude=minutely,hourly&lang=ru&units=metric&APPID="+apiKey;
            URI apiURI = new URI(apiEndpoint + apiCommand + queryStr);

            //Создаем и заполняем построитель запроса
            HttpRequest.Builder ifRequestBuilder = HttpRequest.newBuilder();
            ifRequestBuilder = ifRequestBuilder.version(HttpClient.Version.HTTP_2); //адрес
            ifRequestBuilder = ifRequestBuilder.uri(apiURI); //адрес
            ifRequestBuilder = ifRequestBuilder.GET();//тип запрсоа
            ifRequestBuilder = ifRequestBuilder.timeout(Duration.ofMinutes(1));//таймаут

            //Создаем запрос
            HttpRequest request = ifRequestBuilder.build();

            //Создаем клиент
            HttpClient client = HttpClient.newHttpClient();

            //Отправляем запрос
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode()==200){
                ObjectMapper objectMapper = new ObjectMapper();
                String JSON_result = response.body();
                JsonNode jsonNode = objectMapper.readTree(JSON_result);
                JsonNode curDay = jsonNode.get("daily").get(0);

                JsonNode curDayTemp = curDay.get("temp");
                String tempMon = curDayTemp.get("morn").asText();
                String tempDay = curDayTemp.get("day").asText();
                String tempEve = curDayTemp.get("eve").asText();
                String tempNight = curDayTemp.get("night").asText();

                JsonNode curDayWheather = curDay.get("weather");
                String weatherDescr = curDayWheather.get(0).get("description").asText();

                String windSpeed = curDay.get("wind_speed").asText();

                String result = "Погода:" + System.lineSeparator() +
                        "Сегодня " + weatherDescr + System.lineSeparator() +
                        "Температура:" + System.lineSeparator() +
                        "утро " + tempMon + System.lineSeparator() +
                        "день " + tempDay + System.lineSeparator() +
                        "вечер " + tempEve + System.lineSeparator() +
                        "ночь " + tempNight + System.lineSeparator() +
                        "скорость ветра " + windSpeed + System.lineSeparator() +
                        "Хорошего дня :)";
                return result;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
