package Utils;

import Model.Date;

public class UrlCreator {
    private static final String API_KEY = "3dc30c98-5fa3-4660-9ecd-16a161ee38f7";
    private String departure_station;
    private String destination_station;
    private Date date;

    public UrlCreator(String departure_station, String destination_station, Date date) {
        this.departure_station = departure_station;
        this.destination_station = destination_station;
        this.date = date;
    }


    public UrlCreator() {}
    public String getScheduleUrl() {
        return "https://api.rasp.yandex.net/v3.0/search/?apikey=" + API_KEY + "&format=json&from=" + departure_station +
                "&to=" + destination_station + "&lang=ru_RU&page=1&date=" + date.toString();
    }
    public String getStationsListUrl() {
        return "https://api.rasp.yandex.net/v3.0/stations_list/?apikey=" + API_KEY + "&lang=ru_RU&format=json";
    }
}
