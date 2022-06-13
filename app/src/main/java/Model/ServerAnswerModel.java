package Model;

import android.os.Parcel;
import android.os.Parcelable;
import com.olegator555.rasp.DB.DatabaseTransferable;

public class ServerAnswerModel implements Parcelable, Cloneable {
    @DatabaseTransferable(sqlType = "TEXT")
    private String country;
    @DatabaseTransferable(sqlType = "TEXT")
    private String region;
    @DatabaseTransferable(sqlType = "TEXT")
    private String settlement;
    @DatabaseTransferable(sqlType = "TEXT")
    private String direction;
    @DatabaseTransferable(sqlType = "TEXT")
    private String station_name;
    @DatabaseTransferable(sqlType = "TEXT", primaryKey = true)
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
        formatDirection();
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
        this.yandex_code = "s" + yandex_code;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    public ServerAnswerModel(String country, String region, String settlement, String direction, String station_name,
                             String yandex_code) {
        this.country = country;
        this.region = region;
        this.settlement = settlement;
        this.direction = direction;
        this.station_name = station_name;
        this.yandex_code = yandex_code;
        formatDirection();
    }

    private void formatDirection() {
        if(!(direction.contains(":") || direction.contains("-")))
            direction = direction + " направление";
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

    @Override
    public ServerAnswerModel clone() {
        try {
            ServerAnswerModel clone = (ServerAnswerModel) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
