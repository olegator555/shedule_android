package Model;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScheduleModel {
    private Date departure;
    private final String departure_platform;
    private Date arrival;
    private final String arrival_platform;
    private String duration;

    public String getUid() {
        return uid;
    }

    private final String stops;
    private final String number;
    private final String title;
    private final String type_title;
    private final String uid;

    public ScheduleModel(String departure, String departure_platform, String arrival, String arrival_platform,
                         int duration, String stops, String number, String title, String type_title, String uid) {
        this.departure_platform = departure_platform;
        this.arrival_platform = arrival_platform;
        this.duration = String.valueOf(duration);
        this.stops = stops;
        this.number = number;
        this.title = title;
        this.type_title = type_title;
        this.uid = uid;
        this.formatDuration();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            if(arrival!=null)
                this.arrival = format.parse(arrival);
            if(departure!=null)
                this.departure = format.parse(departure);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    public Date getDeparture() {
        return departure;
    }

    public String getDeparture_platform() {
        return departure_platform;
    }

    public Date getArrival() {
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
