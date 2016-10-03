package project.android.softuni.bg.androiddetective.webapi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Milko on 23.9.2016 г..
 */

public class ResponseObject /*extends SugarRecord */implements Parcelable {
  @SerializedName("uuid")
  public String uuid;

  @SerializedName("broacast_name")
  public String broadcastName;

  @SerializedName("date")
  public String date;

  @SerializedName("send_to")
  public String sendTo;

  @SerializedName("send_text")
  public String sendText;

  @SerializedName("direction")
  public int direction;

  public ResponseObject() {}

  public ResponseObject(String uuid, String broadcastName, String date, String sendTo, String sendText, int direction) {
    this.uuid = uuid;
    this.broadcastName = broadcastName;
    this.date = date;
    this.sendTo = sendTo;
    this.sendText = sendText;
    this.direction = direction;
  }

  protected ResponseObject(Parcel in) {
    uuid = in.readString();
    broadcastName = in.readString();
    date = in.readString();
    sendTo = in.readString();
    sendText = in.readString();
    direction = in.readInt();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(uuid);
    dest.writeString(broadcastName);
    dest.writeString(date);
    dest.writeString(sendTo);
    dest.writeString(sendText);
    dest.writeInt(direction);
  }

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<ResponseObject> CREATOR = new Parcelable.Creator<ResponseObject>() {
    @Override
    public ResponseObject createFromParcel(Parcel in) {
      return new ResponseObject(in);
    }

    @Override
    public ResponseObject[] newArray(int size) {
      return new ResponseObject[size];
    }
  };
}