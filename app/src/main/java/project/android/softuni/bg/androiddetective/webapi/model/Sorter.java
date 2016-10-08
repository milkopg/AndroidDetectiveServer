package project.android.softuni.bg.androiddetective.webapi.model;

/**
 * Created by Milko on 8.10.2016 г..
 */

public class Sorter {
  private String columnName;
  private boolean asc;

  public Sorter(String columnName, boolean asc) {
    this.columnName = columnName;
    this.asc = asc;
  }

  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  public boolean isAsc() {
    return asc;
  }

  public void setAsc(boolean asc) {
    this.asc = asc;
  }
}
