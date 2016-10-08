package project.android.softuni.bg.androiddetective.webapi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Milko on 23.9.2016 г..
 */

public class Response implements Parcelable {
  @SerializedName("location")
  public String location;

  public ResponseObject body;

  protected Response(Parcel in) {
    location = in.readString();
    body = (ResponseObject) in.readValue(ResponseObject.class.getClassLoader());
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(location);
    dest.writeValue(body);
  }

  @SuppressWarnings("unused")
  public static final Creator<Response> CREATOR = new Creator<Response>() {
    @Override
    public Response createFromParcel(Parcel in) {
      return new Response(in);
    }

    @Override
    public Response[] newArray(int size) {
      return new Response[size];
    }
  };
}