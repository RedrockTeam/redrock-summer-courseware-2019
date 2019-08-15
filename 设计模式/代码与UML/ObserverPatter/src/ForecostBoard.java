public class ForecostBoard implements Observer {
    private WeatherData weatherData;

    private double temp;
    private double humidity;
    private double pressure;

    ForecostBoard(Subject weatherData){
        if (weatherData.getClass() == WeatherData.class){
            this.weatherData = (WeatherData)weatherData;
        }
    }

    @Override
    public void update() {
        this.temp = weatherData.getTemperature();
        this.humidity = weatherData.getHumidity();
        this.pressure = weatherData.getPressure();

        //计算天气预报
        System.out.println("New pressure is " + pressure + " might rains!");
    }
}
