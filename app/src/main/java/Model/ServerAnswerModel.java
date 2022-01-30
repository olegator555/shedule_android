package Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ServerAnswerModel implements Parcelable {
    private String country;
    private String region;
    private String settlement;
    private String direction;
    private String station_name;
    private String yandex_code;

    public ServerAnswerModel(Parcel in) {
        country = in.readString();
        region = in.readString();
        settlement = in.readString();
        direction = in.readString();
        station_name = in.readString();
        yandex_code = in.readString();
    }

    public static final Creator<ServerAnswerModel> CREATOR = new Creator<ServerAnswerModel>() {
        @Override
        public ServerAnswerModel createFromParcel(Parcel in) {
            return new ServerAnswerModel(in);
        }

        @Override
        public ServerAnswerModel[] newArray(int size) {
            return new ServerAnswerModel[size];
        }
    };

    public ServerAnswerModel() {}

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSettlement() { return settlement; }

    public void setSettlement(String settlement) { this.settlement = settlement; }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public String getYandex_code() {
        return yandex_code;
    }

    public void setYandex_code(String yandex_code) {
        this.yandex_code = yandex_code;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public ServerAnswerModel(String country, String region, String settlement, String direction, String station_name, String yandex_code) {
        this.country = country;
        this.region = region;
        this.settlement = settlement;
        this.direction = direction;
        this.station_name = station_name;
        this.yandex_code = yandex_code;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(country);
        parcel.writeString(region);
        parcel.writeString(settlement);
        parcel.writeString(direction);
        parcel.writeString(station_name);
        parcel.writeString(yandex_code);
    }
}
