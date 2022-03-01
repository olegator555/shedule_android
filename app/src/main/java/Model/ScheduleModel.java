package Model;


public class ScheduleModel {
    private String departure;
    private final String departure_platform;
    private String arrival;
    private final String arrival_platform;
    private String duration;
    private final String stops;
    private final String number;
    private final String title;
    private final String type_title;
    private int departure_hour;
    private int departure_minute;

    public ScheduleModel(String departure, String departure_platform, String arrival, String arrival_platform,
                         int duration, String stops, String number, String title, String type_title) {
        this.departure = departure;
        this.departure_platform = departure_platform;
        this.arrival = arrival;
        this.arrival_platform = arrival_platform;
        this.duration = String.valueOf(duration);
        this.stops = stops;
        this.number = number;
        this.title = title;
        this.type_title = type_title;
        this.splitTime();
        this.formatDuration();
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

    public String getDuration() {
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

    public String getType_title() {
        return type_title;
    }

    public int getDeparture_hour() {
        return departure_hour;
    }

    public int getDeparture_minute() {
        return departure_minute;
    }

    private void splitTime() {
        int splitTokenIndex = departure.indexOf(":");
        departure = departure.substring(splitTokenIndex-2, splitTokenIndex+3);
        splitTokenIndex = arrival.indexOf(":");
        arrival = arrival.substring(splitTokenIndex-2, splitTokenIndex+3);
        departure_hour = Integer.parseInt(departure.substring(0,2));
        departure_minute = Integer.parseInt(departure.substring(3));
    }
    private void formatDuration() {
        StringBuilder durationBuilder = new StringBuilder();
        int seconds = Integer.parseInt(duration);
        int hh = seconds/3600;
        if(hh!=0)
            durationBuilder.append(hh).append(" ").append("ч.");
        int mm = seconds/60;
        durationBuilder.append(mm).append(" ").append("мин");
        duration = durationBuilder.toString();

    }
}
