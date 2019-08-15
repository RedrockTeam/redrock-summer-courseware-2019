import java.util.ArrayList;

public class WeatherData implements Subject {
    private double temperature;
    private double humidity;
    private double pressure;

    private boolean changed;

    WeatherData(){

    }

    public void dataChanged(){
        setChanged();
    }

    public void setNewData(double temperature, double humidity, double pressure){
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        dataChanged();
    }

    void setChanged(){
        changed = true;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public boolean isChanged(){
        return changed;
    }
}
