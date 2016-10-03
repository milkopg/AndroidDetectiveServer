package project.android.softuni.bg.androiddetective.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.util.DateUtil;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseBase;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

/**
 * Created by Milko on 30.9.2016 г..
 */

public class RecycleViewCustomAdapter extends RecyclerView.Adapter<RecycleViewCustomAdapter.ViewHolder>{
  List<ResponseObject> mAdapterData;

  public RecycleViewCustomAdapter(List<ResponseObject> mAdapterData) {
    this.mAdapterData = mAdapterData;
  }

  public static class ViewHolder extends  RecyclerView.ViewHolder {
    private TextView mTextViewDate;
    private TextView mTextViewSendTo;
    private TextView mTextViewSendText;
    private TextView mTextViewNotes;

    public ViewHolder(View itemView) {
      super(itemView);

      mTextViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
      mTextViewSendTo = (TextView) itemView.findViewById(R.id.textViewSendTo);
      mTextViewSendText = (TextView) itemView.findViewById(R.id.textViewSendText);
    }
  }


  @Override
  public RecycleViewCustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_template, parent, false);
    ViewHolder vh = new ViewHolder(view);
    return vh;
  }

  @Override
  public void onBindViewHolder(RecycleViewCustomAdapter.ViewHolder holder, int position) {
    if ((holder != null) && (mAdapterData != null) && (mAdapterData.size() > 0)) {
      holder.mTextViewDate.setText(DateUtil.convertDateToShortString(mAdapterData.get(position).date));
      holder.mTextViewSendTo.setText(mAdapterData.get(position).sendTo);
      holder.mTextViewSendText.setText(mAdapterData.get(position).sendText);
    }
  }

  @Override
  public int getItemCount() {
    return mAdapterData.size();
  }


}
