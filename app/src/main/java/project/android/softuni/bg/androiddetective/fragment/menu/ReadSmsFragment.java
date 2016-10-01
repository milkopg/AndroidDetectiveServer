package project.android.softuni.bg.androiddetective.fragment.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.adapter.RecycleViewCustomAdapter;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseBase;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

/**
 * Created by Milko G on 22/09/2016.
 */
public class ReadSmsFragment extends Fragment {
  private static final String TAG = ReadSmsFragment.class.getSimpleName();
  private ConcurrentHashMap<String, ResponseObject> dataMap;
  private List<ResponseObject> mAdapterData;
  private RecycleViewCustomAdapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;
  private RecyclerView mRecyclerView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_data, container, false);
    mAdapterData  = new ArrayList<ResponseObject>(ResponseBase.getDataMap().values());
    mAdapter  = new RecycleViewCustomAdapter(mAdapterData);
    mLayoutManager = new LinearLayoutManager(getContext());

    mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setHasFixedSize(true);

    //setTextViewText();
    return rootView;
  }

//  private void setTextViewText() {
//   if (dataMap == null) return;
//    if (mTextViewReadSms == null) return;
//    StringBuilder builder = new StringBuilder();
//
//    for (ConcurrentHashMap.Entry<String, ResponseObject> entry : dataMap.entrySet()) {
//      if (entry.getValue().broadcastName.equals(Constants.RECEIVER_SMS_RECEIVED) || entry.getValue().broadcastName.equals(Constants.RECEIVER_SMS_SENT)) {
//        builder.append("Id:").append(entry.getValue().id).append("\n");
//        builder.append("Broadcast name: ").append(entry.getValue().broadcastName).append("\n");
//        builder.append("Date: ").append(entry.getValue().date).append("\n");
//        builder.append("Send to: ").append(entry.getValue().sendTo).append("\n");
//        builder.append("Send text: ").append(entry.getValue().sendText).append("\n");
//        builder.append("Notes ").append(entry.getValue().notes).append("\n");
//      }
//    }
//    mTextViewReadSms.setText(builder.toString());
//  }
}