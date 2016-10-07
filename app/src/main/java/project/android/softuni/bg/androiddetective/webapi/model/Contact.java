package project.android.softuni.bg.androiddetective.webapi.model;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by Milko on 7.10.2016 г..
 */

public class Contact extends SugarRecord{
  @SerializedName("contactId")
  private String contactId;

  @SerializedName("name")
  private String name;

  @SerializedName("phoneNumber")
  private String phoneNumber;

  @SerializedName("email")
  private String email;

  @SerializedName("request")
  private ResponseObject response;

  public Contact() {
  }

  public Contact(String email, String contactId, String name, String phoneNumber, ResponseObject response) {
    this.email = email;
    this.contactId = contactId;
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.response = response;
  }

  public String getContactId() {
    return contactId;
  }

  public void setContactId(String contactId) {
    this.contactId = contactId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public ResponseObject getRequest() {
    return response;
  }

  public void setRequest(ResponseObject response) {
    this.response = response;
  }
}
