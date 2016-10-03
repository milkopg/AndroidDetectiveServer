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
import project.android.softuni.bg.androiddetective.util.Constants;
import project.android.softuni.bg.androiddetective.webapi.model.Response;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseBase;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

/**
 * Created by Milko G on 22/09/2016.
 */
public class CallerFragment extends Fragment {
  private static final String TAG = CallerFragment.class.getSimpleName();
  private ConcurrentHashMap<String, ResponseObject> dataMap;
  private List<ResponseObject> mAdapterData;
  private List<ResponseObject> mAdapterDateFiltered;
  private RecycleViewCustomAdapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;
  private RecyclerView mRecyclerView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_data, container, false);

    mAdapterData  = new ArrayList<ResponseObject>(ResponseBase.getDataMap().values());
    mAdapterDateFiltered = new ArrayList<>();
    for (ResponseObject object : mAdapterData) {
      if (object.broadcastName.equals(Constants.RECEIVER_CALL)) {
        mAdapterDateFiltered.add(object);
      }
    }

    mAdapter  = new RecycleViewCustomAdapter(mAdapterDateFiltered);
    mLayoutManager = new LinearLayoutManager(getContext());

    mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setHasFixedSize(true);

    return rootView;
  }
}
