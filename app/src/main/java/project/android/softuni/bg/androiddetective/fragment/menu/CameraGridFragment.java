package project.android.softuni.bg.androiddetective.fragment.menu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.adapter.CameraGridViewAdapter;
import project.android.softuni.bg.androiddetective.listener.IOnImageClickListener;
import project.android.softuni.bg.androiddetective.util.Constants;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

/**
 * Created by Milko on 6.10.2016 г..
 */

public class CameraGridFragment extends Fragment{
  private GridView gridView;
  private CameraGridViewAdapter gridAdapter;
  private List<ResponseObject> mAdapterData;
  private IOnImageClickListener callback;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    callback = (IOnImageClickListener) context;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_camera_grid, container, false);

    mAdapterData = ResponseObject.find(ResponseObject.class, Constants.BROADCAST_NAME + "=?", Constants.RECEIVER_CAMERA);
    gridView = (GridView) rootView.findViewById(R.id.gridViewCamera);
    gridAdapter = new CameraGridViewAdapter(getContext(), R.layout.layout_grid_camera, mAdapterData);
    gridView.setAdapter(gridAdapter);

    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        callback.onClick(parent, position);
      }
    });

    gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> adapterView,  View view, int i, long l) {
        return callback.onLongClick(gridAdapter, view, mAdapterData, i, l);
      }
    });

    return rootView;
  }
}
