package project.android.softuni.bg.androiddetective.fragment.menu;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Visibility;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.util.QueriesUtil;
import project.android.softuni.bg.androiddetective.webapi.model.Setting;

public class SettingsFragment extends Fragment implements View.OnClickListener {
  private Button mButtonOk;
  private Button mButtonCancel;
  private LinearLayout mParentLinear;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_settings, container, false);

    mButtonOk = (Button) view.findViewById(R.id.buttonSettingsOk);
    mButtonCancel = (Button) view.findViewById(R.id.buttonSettingsCancel);

    mButtonOk.setOnClickListener(this);
    mButtonCancel.setOnClickListener(this);

//    List<ResponseObject> cameras= ResponseObject.find(ResponseObject.class, Constants.BROADCAST_NAME + "=?", Constants.RECEIVER_CAMERA);
//    for (ResponseObject responseObject : cameras){
//      responseObject.delete();
//    }
    mParentLinear = (LinearLayout) view.findViewById(R.id.layout_linear_fragment_settints);
    LinearLayout rowLinear;
    List<Setting> settingsList = Setting.find(Setting.class, null, null, null, "NAME ASC", null);

    //iterate dynamically from database and load values
    for (Setting setting : settingsList) {
      rowLinear = new LinearLayout(getContext());
      rowLinear.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.WRAP_CONTENT));

      //hidden to keep database key
      TextView textViewHidden = new TextView(getContext());
      textViewHidden.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
              ViewGroup.LayoutParams.WRAP_CONTENT));
      textViewHidden.setVisibility(View.GONE);
      textViewHidden.setText(setting.getResourceName());
      rowLinear.addView(textViewHidden);


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


      //add rowLinearLayout to mParentLinear
      mParentLinear.addView(rowLinear);
    }
    return view;
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.buttonSettingsOk:
        onConfirm();
        break;
      case R.id.buttonSettingsCancel:
        onCancel();
        break;
    }
  }

  public void onConfirm() {
    final int childcount = mParentLinear.getChildCount();
    for (int i = 0; i < childcount; i++) {
      if (mParentLinear.getChildAt(i) instanceof LinearLayout) {
        LinearLayout innerLinear = ((LinearLayout) (mParentLinear.getChildAt(i)));
        String dbkey = null;
        for (int j = 0; j < innerLinear.getChildCount(); j++) {
          View view = ((TextView) (innerLinear.getChildAt(j)));
          if (view instanceof TextView) {
            TextView hidden = (TextView) view;
            if (hidden.getVisibility() == View.GONE) {
              dbkey = (String) hidden.getText();
            }
          }
          if (view instanceof EditText) {
            EditText et = (EditText) view;
            Setting setting = QueriesUtil.getSettingByDatabaseKey(dbkey);
            if (setting != null) {
              setting.setValue(et.getText().toString());
              setting.save();
            }
          }
        }
      }
    }
  }

  public void onCancel() {

  }

  private interface OnSettingButtonClickListener {
    public void onConfirm();

    public void onCancel();
  }
}
