package project.android.softuni.bg.androiddetective.fragment.menu;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.util.Constants;
import project.android.softuni.bg.androiddetective.util.QueriesUtil;
import project.android.softuni.bg.androiddetective.webapi.model.Setting;

public class SettingsFragment extends Fragment implements View.OnClickListener {
  private static final String TAG = SettingsFragment.class.getSimpleName();
  private Button mButtonOk;
  private Button mButtonCancel;
  private LinearLayout mParentLinear;
  private View mView;
  private HashMap<Integer, String> mEditTextIdsMap;

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mView = inflater.inflate(R.layout.fragment_settings, container, false);
    mEditTextIdsMap = new HashMap<>();

    mButtonOk = (Button) mView.findViewById(R.id.buttonSettingsOk);
    mButtonCancel = (Button) mView.findViewById(R.id.buttonSettingsCancel);

    mButtonOk.setOnClickListener(this);
    mButtonCancel.setOnClickListener(this);

    fillSettings();

    return mView;
  }

  /**
   * Iterate dynamically over database settings and create all necessary textView and EditText as are needed
   */
  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
  private void fillSettings() {
    View view = mView;
    mParentLinear = (LinearLayout) view.findViewById(R.id.layout_linear_fragment_settints);
    mParentLinear.removeAllViews();
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
      editTextValue.setId(View.generateViewId());
      mEditTextIdsMap.put(editTextValue.getId(), setting.getResourceName());
      rowLinear.addView(editTextValue);

      //add rowLinearLayout to mParentLinear
      mParentLinear.addView(rowLinear);
    }
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.buttonSettingsOk:
        if (validateInput())
            onConfirm();
        break;
      case R.id.buttonSettingsCancel:
        onCancel();
        break;
    }
  }

  /**
   * make InputValidation if field is empty, if url is not valid and valid RabbitMq URI
   * @return
   */
  private boolean validateInput() {
    boolean valid = true;
    for (Map.Entry<Integer, String > entry : mEditTextIdsMap.entrySet()) {
      EditText editText = (EditText) mView.findViewById(entry.getKey());
      if (editText.getText().toString().length() == 0) {
        editText.setError(getString(R.string.fields_cannot_be_blank));
        valid = false;
      }

      if (Constants.SETTING_JSON_BLOB_API_URL_STRING_NAME.equals(entry.getValue())) {
        if (!editText.getText().toString().startsWith("https")) {
          editText.setError(getString(R.string.not_valid_url));
          valid = false;
        }
      }

      if (Constants.SETTING_RABBIT_MQ_URI_STRING_NAME.equals(entry.getValue())) {
        if (!editText.getText().toString().startsWith("amqp")) {
          editText.setError((getString(R.string.not_valid_rabbitmq_url)));
          valid = false;
        }
      }
    }
    Log.d(TAG, "validateInput valid : " + valid);
    return valid;
  }

  /**
   * Saving all data to database. We create a hiddenTextView holding database key for translations.
   */
  public void onConfirm() {
    final int childCount = mParentLinear.getChildCount();
    for (int i = 0; i < childCount; i++) {
      if (mParentLinear.getChildAt(i) instanceof LinearLayout) {
        LinearLayout innerLinear = ((LinearLayout) (mParentLinear.getChildAt(i)));
        String databaseKey = null;
        for (int j = 0; j < innerLinear.getChildCount(); j++) {
          View view = ((innerLinear.getChildAt(j)));
          if (view instanceof TextView) {
            TextView hidden = (TextView) view;
            if (hidden.getVisibility() == View.GONE) {
              databaseKey = (String) hidden.getText();
            }
          }
          if (view instanceof EditText) {
            EditText et = (EditText) view;
            Setting setting = QueriesUtil.getSettingByDatabaseKey(databaseKey);
            if (setting != null) {
              setting.setValue(et.getText().toString());
              setting.save();
            }
          }
        }
      }
    }
    Toast.makeText(getContext(), R.string.save_sucessfull, Toast.LENGTH_SHORT).show();
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
  public void onCancel() {
    fillSettings();
    Toast.makeText(getContext(), R.string.setting_are_restored_from_database, Toast.LENGTH_SHORT).show();
  }
}
