package com.jenifly.timeboard.info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by florentchampigny on 21/06/2016.
 */
public class BGInfo implements Parcelable {
    String name;
    int image;

    public BGInfo(String name, int image) {
        this.name = name;
        this.image = image;
    }

    protected BGInfo(Parcel in) {
        name = in.readString();
        image = in.readInt();
    }

    public static final Creator<BGInfo> CREATOR = new Creator<BGInfo>() {
        @Override
        public BGInfo createFromParcel(Parcel in) {
            return new BGInfo(in);
        }

        @Override
        public BGInfo[] newArray(int size) {
            return new BGInfo[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(image);
    }
}
