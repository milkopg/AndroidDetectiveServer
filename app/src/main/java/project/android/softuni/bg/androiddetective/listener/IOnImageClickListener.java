package project.android.softuni.bg.androiddetective.listener;

import android.view.View;
import android.widget.AdapterView;

import java.util.List;

import project.android.softuni.bg.androiddetective.adapter.CameraGridViewAdapter;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

/**
 * Created by Milko on 6.10.2016 г..
 */

public interface IOnImageClickListener {
  void onClick(AdapterView<?> adapter, int position);
  boolean onLongClick(CameraGridViewAdapter adapterView, List<ResponseObject> mAdapterData, int position);
}
