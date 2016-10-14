package project.android.softuni.bg.androiddetective.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.util.DateUtil;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

/**
 * Created by Milko on 30.9.2016 г..
 */

public class RecycleViewTableCustomAdapter extends RecyclerView.Adapter<RecycleViewTableCustomAdapter.ViewHolder>{
  List<ResponseObject> mAdapterData;

  /**
   * Custom RecycleViewTableCustomAdapter. It's a base Adapter used for displaying data from SMSReceiver and CallReceiver
   * @param mAdapterData - List of ResponseObject data
   */
  public RecycleViewTableCustomAdapter(List<ResponseObject> mAdapterData) {
    this.mAdapterData = mAdapterData;
  }

  /**
   * View holder for displaying data - date, sendTo and sendText
   */
  public static class ViewHolder extends  RecyclerView.ViewHolder {
    private TextView mTextViewDate;
    private TextView mTextViewSendTo;
    private TextView mTextViewSendText;

    public ViewHolder(View itemView) {
      super(itemView);

      mTextViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
      mTextViewSendTo = (TextView) itemView.findViewById(R.id.textViewSendTo);
      mTextViewSendText = (TextView) itemView.findViewById(R.id.textViewSendText);
    }
  }


  @Override
  public RecycleViewTableCustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_table_datepicker, parent, false);
    ViewHolder vh = new ViewHolder(view);
    return vh;
  }

  @Override
  public void onBindViewHolder(RecycleViewTableCustomAdapter.ViewHolder holder, int position) {
    if ((holder != null) && (mAdapterData != null) && (mAdapterData.size() > 0)) {
      holder.mTextViewDate.setText(DateUtil.convertDateToShortString(mAdapterData.get(position).getDate()));
      holder.mTextViewSendTo.setText(mAdapterData.get(position).getSendTo());
      holder.mTextViewSendText.setText(mAdapterData.get(position).getSendText());
    }
  }

  @Override
  public int getItemCount() {
    return mAdapterData.size();
  }


}
