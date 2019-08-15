public class TemperatureBoard implements Observer {
    private WeatherData weatherData;
    private double temp;

    TemperatureBoard(Subject weatherData){
        if (weatherData.getClass() == WeatherData.class){
            this.weatherData = (WeatherData)weatherData;
        }
    }

    @Override
    public void update() {
        this.temp = weatherData.getTemperature();
        System.out.println("Now Temperature is " + this.temp);
    }
}
