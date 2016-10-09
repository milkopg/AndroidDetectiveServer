package project.android.softuni.bg.androiddetective.fragment.menu;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.webapi.model.Setting;

public class SettingsFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_settings, container, false);

//    List<ResponseObject> cameras= ResponseObject.find(ResponseObject.class, Constants.BROADCAST_NAME + "=?", Constants.RECEIVER_CAMERA);
//    for (ResponseObject responseObject : cameras){
//      responseObject.delete();
//    }
    LinearLayout parentLinear = (LinearLayout) view.findViewById(R.id.layout_linear_fragment_settints);
    LinearLayout rowLinear ;
    List<Setting> settingsList = Setting.find(Setting.class, null, null, null, "NAME ASC", null);

    //iterate dynamically from database and load values
    for (Setting setting : settingsList) {
      rowLinear = new LinearLayout(getContext());
      rowLinear.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
              ViewGroup.LayoutParams.WRAP_CONTENT));

      //Add Setting name TextView
      TextView textViewLabel = new TextView(getContext());
      textViewLabel.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
              ViewGroup.LayoutParams.WRAP_CONTENT));
      textViewLabel.setText(getResources().getText(getResources().getIdentifier(setting.getResourceName(), "string", getContext().getPackageName())));
      rowLinear.addView(textViewLabel);
      //Add Setting value EditText
      EditText editTextValue = new EditText(getContext());
      editTextValue.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.WRAP_CONTENT));

      //make it scrollable horizontally
      editTextValue.setText(setting.getValue());
      editTextValue.setSingleLine(true);
      editTextValue.setMaxLines(1);
      editTextValue.setGravity(Gravity.LEFT);
      rowLinear.addView(editTextValue);

      //add rowLinearLayout to parentLinear
      parentLinear.addView(rowLinear);
    }
    return view;
  }
}
