package project.android.softuni.bg.androiddetective.fragment.menu;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.adapter.CameraGridViewAdapter;
import project.android.softuni.bg.androiddetective.listener.IOnImageClickListener;
import project.android.softuni.bg.androiddetective.util.Constants;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

/**
 * Created by Milko on 6.10.2016 г..
 */

public class CameraGridFragment extends Fragment {
  private GridView mGridView;
  private CameraGridViewAdapter mGridAdapter;
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
    mGridView = (GridView) rootView.findViewById(R.id.gridViewCamera);
    mGridAdapter = new CameraGridViewAdapter(getContext(), R.layout.layout_grid_camera, mAdapterData);
    mGridView.setAdapter(mGridAdapter);

    mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        callback.onClick(parent, position);
      }
    });

    mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        showAlertDialog(getContext(), getString(R.string.delete_picture), getString(R.string.do_you_confirm_deleting_selected_picture), mGridAdapter, position);
        return false;
      }
    });

    return rootView;
  }

  private void showAlertDialog(Context context, String title, String message, final CameraGridViewAdapter adapterView, final int position) {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
            context);

    // set title
    alertDialogBuilder.setTitle(title);

    // set dialog message
    alertDialogBuilder
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                callback.onLongClick(adapterView, mAdapterData, position);
              }
            })
            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
              }
            });

    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
  }
}
