package project.android.softuni.bg.androiddetective.webapi.model;

import com.orm.SugarRecord;

/**
 * Created by Milko on 5.10.2016 г..
 */

public class Counters extends SugarRecord{

  private String receiverName;
  private long counter;

  public String getReceiverName() {
    return receiverName;
  }

  public void setReceiverName(String receiverName) {
    this.receiverName = receiverName;
  }

  public long getCounter() {
    return counter;
  }

  public void setCounter(long counter) {
    this.counter = counter;
  }


  public Counters() {
  }

  public Counters(String receiverName, long counter) {
    this.receiverName = receiverName;
    this.counter = counter;
  }


}
