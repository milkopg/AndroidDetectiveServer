package project.android.softuni.bg.androiddetective.fragment.base;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.adapter.RecycleViewCustomAdapter;
import project.android.softuni.bg.androiddetective.fragment.menu.CallerFragment;
import project.android.softuni.bg.androiddetective.util.Constants;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

/**
 * Created by Milko on 4.10.2016 г..
 */

public abstract class BaseFragment extends android.support.v4.app.Fragment{
  private List<ResponseObject> mAdapterData;
  private static final String TAG = CallerFragment.class.getSimpleName();
  protected RecycleViewCustomAdapter mAdapter;
  protected RecyclerView.LayoutManager mLayoutManager;
  protected RecyclerView mRecyclerView;
  protected EditText editTextFromDate;
  protected EditText editTextToDate;
  protected DatePickerDialog fromDatePickerDialog;
  protected DatePickerDialog toDatePickerDialog;
  protected SimpleDateFormat dateFormatter;

  protected Calendar fromCalendar;
  protected Calendar toCalendar;

  @RequiresApi(api = Build.VERSION_CODES.M)
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_data, container, false);

    editTextFromDate = (EditText) rootView.findViewById(R.id.editTextFromDate);
    editTextFromDate.setInputType(InputType.TYPE_NULL);
    editTextFromDate.requestFocus();

    editTextToDate = (EditText) rootView.findViewById(R.id.editTextToDate);
    editTextToDate.setInputType(InputType.TYPE_NULL);
    editTextToDate.requestFocus();

    dateFormatter = new SimpleDateFormat(Constants.DATE_FORMAT_SHORT_DATE, Locale.US);
    mLayoutManager = new LinearLayoutManager(getContext());

    mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setHasFixedSize(false);

    return rootView;
  }

  protected void setAdapterData(List<ResponseObject> adapterData) {
    mAdapterData = adapterData;
  }

  protected abstract void populateData(String receiverName);
  protected abstract void initDateFields();

}
