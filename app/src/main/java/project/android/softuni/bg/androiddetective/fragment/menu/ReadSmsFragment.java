package project.android.softuni.bg.androiddetective.fragment.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.concurrent.ConcurrentHashMap;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseBase;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

/**
 * Created by Milko G on 22/09/2016.
 */
public class ReadSmsFragment extends Fragment {
  private TextView mTextViewReadSms;
  private ConcurrentHashMap<String, ResponseObject> dataMap;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_read_sms, container, false);
    mTextViewReadSms = (TextView) rootView.findViewById(R.id.text_view_read_sms);
    setTextViewText();
    return rootView;
  }

  private void setTextViewText() {
    dataMap = ResponseBase.getDataMap();
    if (dataMap == null) return;
    if (mTextViewReadSms == null) return;
    StringBuilder builder = new StringBuilder();

    for ( ConcurrentHashMap.Entry<String, ResponseObject> entry : dataMap.entrySet()) {
      builder.append("Id:").append(entry.getValue().id).append("\n");
      builder.append("Broadcast name: ").append(entry.getValue().broadcastName).append("\n");
      builder.append("Date: ").append(entry.getValue().date).append("\n");
      builder.append("Send to: ").append(entry.getValue().sendTo).append("\n");
      builder.append("Send text: ").append(entry.getValue().sendText).append("\n");
      builder.append("Notes ").append(entry.getValue().notes).append("\n");
    }
    mTextViewReadSms.setText(builder.toString());
  }
}
