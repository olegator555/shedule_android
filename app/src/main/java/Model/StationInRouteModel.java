package Model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StationInRouteModel {
    private Date arrival;
    private Date departure;
    private final int duration;
    private final String platform;
    private final String station_code;
    private final String station_title;

    public StationInRouteModel(String arrival, String departure, int duration, String platform, String station_code,
                               String station_title) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.d("arrival: ", arrival);
        Log.d("departure: ", departure);
        try {
            if(arrival!=null)
                this.arrival = format.parse(arrival);
            if(departure!=null)
                this.departure = format.parse(departure);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.duration = duration;
        this.platform = platform;
        this.station_code = station_code;
        this.station_title = station_title;
    }

    public Date getArrival() {
        return arrival;
    }

    public Date getDeparture() {
        return departure;
    }

    public int getDuration() {
        return duration;
    }

    public String getPlatform() {
        return platform;
    }

    public String getStation_code() {
        return station_code;
    }

    public String getStation_title() {
        return station_title;
    }
}
