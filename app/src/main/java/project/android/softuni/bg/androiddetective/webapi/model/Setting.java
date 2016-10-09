package project.android.softuni.bg.androiddetective.webapi.model;

import com.orm.SugarRecord;

/**
 * Created by Milko on 9.10.2016 г..
 */

public class Setting extends SugarRecord{
  private String name;
  private String value;
  private int resourceId;

  public Setting() {
  }

  public Setting(String name, String value, int resourceId) {
    this.name = name;
    this.value = value;
    this.resourceId = resourceId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public int getResourceId() {
    return resourceId;
  }

  public void setResourceId(int resourceId) {
    this.resourceId = resourceId;
  }
}
