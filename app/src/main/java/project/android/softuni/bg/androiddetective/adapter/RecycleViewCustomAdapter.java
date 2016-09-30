package project.android.softuni.bg.androiddetective.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import project.android.softuni.bg.androiddetective.webapi.model.ResponseBase;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

/**
 * Created by Milko on 30.9.2016 г..
 */

public class RecycleViewCustomAdapter extends BaseAdapter{
  List<ResponseObject> objectList = new ArrayList<ResponseObject>(ResponseBase.getDataMap().values());
  @Override
  public int getCount() {
    return objectList.size();
  }

  @Override
  public Object getItem(int i) {
    return objectList.get(i);
  }

  @Override
  public long getItemId(int i) {
    return 0;
  }

  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    return null;
  }
}
