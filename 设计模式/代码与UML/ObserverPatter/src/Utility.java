public class Utility {
    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();
        TemperatureBoard temperatureBoard = new TemperatureBoard(weatherData);
        ForecostBoard forecostBoard = new ForecostBoard(weatherData);

        weatherData.setNewData(27.9, 34.9, 56.2);
        System.out.println();
        weatherData.setNewData(37.9, 82, 23);
    }
}
