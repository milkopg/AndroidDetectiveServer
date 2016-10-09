package project.android.softuni.bg.androiddetective.webapi.model;

import com.orm.SugarRecord;

/**
 * Created by Milko on 9.10.2016 г..
 */

public class Setting extends SugarRecord{
  private String name;
  private String value;
  private String resourceName;

  public Setting() {
  }

  public Setting(String name, String value, String resourceName) {
    this.name = name;
    this.value = value;
    this.resourceName = resourceName;
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

  public String getResourceName() {
    return resourceName;
  }

  public void setResourceName(String resourceName) {
    this.resourceName = resourceName;
  }
}
