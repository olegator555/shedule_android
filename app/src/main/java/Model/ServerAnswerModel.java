package Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ServerAnswerModel implements Parcelable {
    private String country;
    private String region;
    private String direction;
    private String station_type;
    private String yandex_code;
    private String transport_type;

    public ServerAnswerModel(Parcel in) {
        country = in.readString();
        region = in.readString();
        direction = in.readString();
        station_type = in.readString();
        yandex_code = in.readString();
        transport_type = in.readString();
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getStation_type() {
        return station_type;
    }

    public void setStation_type(String station_type) {
        this.station_type = station_type;
    }

    public String getYandex_code() {
        return yandex_code;
    }

    public void setYandex_code(String yandex_code) {
        this.yandex_code = yandex_code;
    }

    public String getTransport_type() {
        return transport_type;
    }

    public void setTransport_type(String transport_type) {
        this.transport_type = transport_type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(country);
        parcel.writeString(region);
        parcel.writeString(direction);
        parcel.writeString(station_type);
        parcel.writeString(yandex_code);
        parcel.writeString(transport_type);
    }
}
