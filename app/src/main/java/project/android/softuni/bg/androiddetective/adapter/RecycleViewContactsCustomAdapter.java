package project.android.softuni.bg.androiddetective.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.util.DateUtil;
import project.android.softuni.bg.androiddetective.webapi.model.Contact;

/**
 * Created by Milko on 30.9.2016 г..
 */

public class RecycleViewContactsCustomAdapter extends RecyclerView.Adapter<RecycleViewContactsCustomAdapter.ViewHolder>{
  List<Contact> mAdapterData;

  public RecycleViewContactsCustomAdapter(List<Contact> mAdapterData) {
    this.mAdapterData = mAdapterData;
  }

  public static class ViewHolder extends  RecyclerView.ViewHolder {
    private TextView mTextViewContactName;
    private TextView mTextViewPhoneNumber;
    private TextView mTextViewEmail;

    public ViewHolder(View itemView) {
      super(itemView);

      mTextViewContactName = (TextView) itemView.findViewById(R.id.textViewContactName);
      mTextViewPhoneNumber = (TextView) itemView.findViewById(R.id.textViewContactPhoneNumber);
      mTextViewEmail = (TextView) itemView.findViewById(R.id.textViewContactEmail);
    }
  }


  @Override
  public RecycleViewContactsCustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_contacts_label_row, parent, false);
    ViewHolder vh = new ViewHolder(view);
    return vh;
  }

  @Override
  public void onBindViewHolder(RecycleViewContactsCustomAdapter.ViewHolder holder, int position) {
    if ((holder != null) && (mAdapterData != null) && (mAdapterData.size() > 0)) {
      holder.mTextViewContactName.setText(mAdapterData.get(position).getName());
      holder.mTextViewPhoneNumber.setText(mAdapterData.get(position).getPhoneNumber());
      holder.mTextViewEmail.setText(mAdapterData.get(position).getEmail());
    }
  }

  @Override
  public int getItemCount() {
    return mAdapterData.size();
  }
}
