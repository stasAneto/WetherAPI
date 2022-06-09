package org.weather;

public class Run {
    public static void main(String[] args){
        WeatherAPI obWeather = new WeatherAPI();
        String curWeather = obWeather.getWeather();
        if(curWeather != null){
            System.out.println(curWeather);
        }
    }
}
