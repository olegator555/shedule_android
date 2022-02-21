package Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Date implements Parcelable {
    public int year;
    public int month;
    public int day;

    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    protected Date(Parcel in) {
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
    }

    public static final Creator<Date> CREATOR = new Creator<Date>() {
        @Override
        public Date createFromParcel(Parcel in) {
            return new Date(in);
        }

        @Override
        public Date[] newArray(int size) {
            return new Date[size];
        }
    };

    @Override
    public String toString() {
        return year + "-" + month + "-" + day;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(year);
        parcel.writeInt(month);
        parcel.writeInt(day);
    }
}
