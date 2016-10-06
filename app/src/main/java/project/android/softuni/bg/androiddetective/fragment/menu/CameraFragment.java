package project.android.softuni.bg.androiddetective.fragment.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.adapter.RecycleViewImageCustomAdapter;
import project.android.softuni.bg.androiddetective.util.Constants;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

/**
 * Created by Milko on 6.10.2016 г..
 */

public class CameraFragment extends Fragment{
  private List<ResponseObject> mAdapterData;
  private RecyclerView.Adapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;
  private RecyclerView mRecyclerView;


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
    //  Bundle bundle = new Bundle();
//    bundle.putString(Constants.BROADCAST_NAME, Constants.RECEIVER_SMS_RECEIVED);
//    bundle.putInt(Constants.LAYOUT_ID, R.layout.fragment_camera);

    mAdapterData = ResponseObject.find(ResponseObject.class, Constants.BROADCAST_NAME + "=?", Constants.RECEIVER_CAMERA);
    mAdapter = new RecycleViewImageCustomAdapter(mAdapterData, getContext());
    mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewImageData);

    mLayoutManager = new LinearLayoutManager(getContext());
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setHasFixedSize(false);

    return rootView;
  }
}
