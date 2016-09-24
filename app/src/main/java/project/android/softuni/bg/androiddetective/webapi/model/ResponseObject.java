package project.android.softuni.bg.androiddetective.webapi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Milko on 23.9.2016 г..
 */

public class ResponseObject extends ResponseBase implements Parcelable {
  @SerializedName("id")
  public String id;

  @SerializedName("broacast_name")
  public String broadcastName;

  @SerializedName("date")
  public Date date;

  @SerializedName("send_to")
  public String sendTo;

  @SerializedName("send_text")
  public String sendText;

  @SerializedName("notes")
  public String notes;

  public ResponseObject(String id, String broadcastName, Date date, String sendTo, String sendText, String notes) {
    this.id = id;
    this.broadcastName = broadcastName;
    this.date = date;
    this.sendTo = sendTo;
    this.sendText = sendText;
    this.notes = notes;
  }



  protected ResponseObject(Parcel in) {
    id = in.readString();
    broadcastName = in.readString();
    long tmpDate = in.readLong();
    date = tmpDate != -1 ? new Date(tmpDate) : null;
    sendTo = in.readString();
    sendText = in.readString();
    notes = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(broadcastName);
    dest.writeLong(date != null ? date.getTime() : -1L);
    dest.writeString(sendTo);
    dest.writeString(sendText);
    dest.writeString(notes);
  }

  @SuppressWarnings("unused")
  public static final Creator<ResponseObject> CREATOR = new Creator<ResponseObject>() {
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
