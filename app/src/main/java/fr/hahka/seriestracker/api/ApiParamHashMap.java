package fr.hahka.seriestracker.api;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thibautvirolle on 24/06/2016.
 */
public class ApiParamHashMap implements Parcelable{

    public static final Creator<ApiParamHashMap> CREATOR = new Creator<ApiParamHashMap>() {
        @Override
        public ApiParamHashMap createFromParcel(Parcel in) {
            return new ApiParamHashMap(in);
        }

        @Override
        public ApiParamHashMap[] newArray(int size) {
            return new ApiParamHashMap[size];
        }
    };
    private HashMap<String, String> map = new HashMap<String, String>();

public ApiParamHashMap() {}

    protected ApiParamHashMap(Parcel in) {

        int size = in.readInt();
        for(int i = 0; i < size; i++) {
            String key = in.readString();
            String value = in.readString();
            map.put(key,value);
        }

    }

    public String toString() {

        String output = "";

        for (Map.Entry<String, String> e : map.entrySet()) {
            output = output.concat(e.getKey() + "=" + e.getValue() + "&");
        }

        if(output.length() > 1)
            return output.substring(0, output.length() - 2);
        else
            return "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(map.size());

        for(Map.Entry<String, String> entry : map.entrySet()) {
            parcel.writeString(entry.getKey());
            parcel.writeString(entry.getValue());
        }

    }


    public String put(String key, String value) {
        return map.put(key, value);
    }

    public String get(String key) {
        return map.get(key);
    }
}
