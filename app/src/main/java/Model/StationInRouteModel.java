package Model;

public class StationInRouteModel {
    private final String arrival;
    private final String departure;
    private final int duration;
    private final String platform;
    private final String station_code;
    private final String station_title;

    public StationInRouteModel(String arrival, String departure, int duration, String platform, String station_code,
                               String station_title) {
        this.arrival = arrival;
        this.departure = departure;
        this.duration = duration;
        this.platform = platform;
        this.station_code = station_code;
        this.station_title = station_title;
    }

    public String getArrival() {
        return arrival;
    }

    public String getDeparture() {
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
