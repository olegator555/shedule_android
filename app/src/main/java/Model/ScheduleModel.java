package Model;


public class ScheduleModel {
    private final String departure;
    private final String departure_platform;
    private final String arrival;
    private final String arrival_platform;
    private final int duration;
    private final String stops;
    private final String number;
    private final String title;
    private final String type_code;

    public ScheduleModel(String departure, String departure_platform, String arrival, String arrival_platform,
                         int duration, String stops, String number, String title, String type_code) {
        this.departure = departure;
        this.departure_platform = departure_platform;
        this.arrival = arrival;
        this.arrival_platform = arrival_platform;
        this.duration = duration;
        this.stops = stops;
        this.number = number;
        this.title = title;
        this.type_code = type_code;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDeparture_platform() {
        return departure_platform;
    }

    public String getArrival() {
        return arrival;
    }

    public String getArrival_platform() {
        return arrival_platform;
    }

    public int getDuration() {
        return duration;
    }

    public String getStops() {
        return stops;
    }

    public String getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getType_code() {
        return type_code;
    }
}
