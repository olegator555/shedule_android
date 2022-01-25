package Utils;
public class UrlCreator {
    private static final String api_key = "3dc30c98-5fa3-4660-9ecd-16a161ee38f7";
    private String from_station = null;
    private String to_station = null;
    private String date = null;

    public UrlCreator(String api_key, String from_station, String to_station, String date) {
        this.from_station = from_station;
        this.to_station = to_station;
        this.date = date;
    }


    public UrlCreator() {}
    public String getFinalUrl() {
        return "https://api.rasp.yandex.net/v3.0/search/?apikey=" + api_key + "format=json&from=" + from_station +
                "&to=" + to_station + "&lang=ru_RU&page=1&date=" + date;
    }
    public String getStationsListUrl() {
        return "https://api.rasp.yandex.net/v3.0/stations_list/?apikey=" + api_key + "&lang=ru_RU&format=json";
    }
}
