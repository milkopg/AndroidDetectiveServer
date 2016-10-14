package project.android.softuni.bg.androiddetective.fragment.base;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.adapter.RecycleViewTableCustomAdapter;
import project.android.softuni.bg.androiddetective.fragment.menu.CallerFragment;
import project.android.softuni.bg.androiddetective.util.Constants;
import project.android.softuni.bg.androiddetective.util.QueriesUtil;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

/**
 * Created by Milko on 4.10.2016 г..
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
  private static final String TAG = CallerFragment.class.getSimpleName();
  private List<ResponseObject> mAdapterData;
  private String mBroadcastName;
  private RecyclerView.Adapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;
  private RecyclerView mRecyclerView;
  private EditText editTextFromDate;
  private EditText editTextToDate;
  private DatePickerDialog fromDatePickerDialog;
  private DatePickerDialog toDatePickerDialog;
  private SimpleDateFormat dateFormatter;

  private TextView mTextViewDateLabel;
  private TextView mTextViewSendToLabel;
  private TextView mTextViewSendTextLabel;

  private Calendar fromCalendar;
  private Calendar toCalendar;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
    int layoutId = bundle.getInt(Constants.LAYOUT_ID);

    View rootView = inflater.inflate(layoutId, container, false);

    editTextFromDate = (EditText) rootView.findViewById(R.id.editTextFromDate);
    editTextFromDate.setInputType(InputType.TYPE_NULL);
    editTextFromDate.requestFocus();

    editTextToDate = (EditText) rootView.findViewById(R.id.editTextToDate);
    editTextToDate.setInputType(InputType.TYPE_NULL);
    editTextToDate.requestFocus();

    mTextViewDateLabel = (TextView) rootView.findViewById(R.id.textViewDateLabel);
    mTextViewSendToLabel = (TextView) rootView.findViewById(R.id.textViewSendToLabel);
    mTextViewSendTextLabel = (TextView) rootView.findViewById(R.id.textViewSendTextLabel);

    mTextViewDateLabel.setOnClickListener(this);
    mTextViewSendTextLabel.setOnClickListener(this);
    mTextViewSendToLabel.setOnClickListener(this);

    dateFormatter = new SimpleDateFormat(Constants.DATE_FORMAT_SHORT_DATE, Locale.US);
    setBroadcastName(bundle.getString(Constants.BROADCAST_NAME));

    mAdapterData = new ArrayList<>();
    mAdapterData = QueriesUtil.getResponseObjectByBroadcastName(getBroadcastName());

    mAdapter = new RecycleViewTableCustomAdapter(mAdapterData);
    mLayoutManager = new LinearLayoutManager(getContext());

    mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewTableData);
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setHasFixedSize(false);

    setDateTimeField();

    //setupPinch();

    return rootView;
  }

  private void setupPinch() {
   final ScaleGestureDetector mScaleDetector =
            new ScaleGestureDetector(getContext(), new MyPinchListener());
    mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        return true;
      }
    });
  }

  static class MyPinchListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
      Log.d("TAG", "PINCH! OUCH!");
      return true;
    }
  }

  private void setDateTimeField() {

    fromCalendar = Calendar.getInstance();
    toCalendar = Calendar.getInstance();

    initCalendarValues(fromCalendar, fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH), 1, 0 , 0, 0);
    initCalendarValues(toCalendar, toCalendar.get(Calendar.YEAR), toCalendar.get(Calendar.MONTH), toCalendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);

    editTextFromDate.setOnClickListener(this);
    editTextToDate.setOnClickListener(this);

    fromDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

      public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        initCalendarValues(fromCalendar, year, monthOfYear, dayOfMonth, 0 , 0, 0);
        editTextFromDate.setText(dateFormatter.format(fromCalendar.getTime()));
        QueriesUtil.orderTableData(ResponseObject.class, mAdapter, mAdapterData, mBroadcastName, "DATE", fromCalendar, toCalendar);
      }

    }, fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH), fromCalendar.get(Calendar.DAY_OF_MONTH));
    fromDatePickerDialog.updateDate(fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH) , 1);
    editTextFromDate.setText(dateFormatter.format(fromCalendar.getTime()));

    toDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
      public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        initCalendarValues(toCalendar, year, monthOfYear, dayOfMonth, 23, 59, 59);
        editTextToDate.setText(dateFormatter.format(toCalendar.getTime()));
        QueriesUtil.orderTableData(ResponseObject.class, mAdapter, mAdapterData, mBroadcastName, "DATE", fromCalendar, toCalendar);
      }

    }, toCalendar.get(Calendar.YEAR), toCalendar.get(Calendar.MONTH), toCalendar.get(Calendar.DAY_OF_MONTH));
    toDatePickerDialog.updateDate(toCalendar.get(Calendar.YEAR), toCalendar.get(Calendar.MONTH), toCalendar.get(Calendar.DAY_OF_MONTH));
    editTextToDate.setText(dateFormatter.format(toCalendar.getTime()));
  }

  private void initCalendarValues(Calendar calendar, int year, int month, int day, int hours, int minutes, int seconds) {
    calendar.set(year, month, day, hours , minutes, seconds);
  }

  @Override
  public void onClick(View view) {
    String columnOrderName;
    switch (view.getId()) {
      case R.id.editTextFromDate :
        fromDatePickerDialog.show();
        break;
      case R.id.editTextToDate:
        toDatePickerDialog.show();
        break;
      case R.id.textViewDateLabel:
        columnOrderName = QueriesUtil.getDatabaseColumnNameByViewId(view.getId());
        QueriesUtil.orderTableData(ResponseObject.class, mAdapter, mAdapterData, mBroadcastName,  columnOrderName, fromCalendar, toCalendar);
        break;
      case R.id.textViewSendToLabel:
        columnOrderName = QueriesUtil.getDatabaseColumnNameByViewId(view.getId());
        QueriesUtil.orderTableData(ResponseObject.class, mAdapter, mAdapterData, mBroadcastName, columnOrderName, fromCalendar, toCalendar);
        break;
      case R.id.textViewSendTextLabel:
        columnOrderName = QueriesUtil.getDatabaseColumnNameByViewId(view.getId());
        QueriesUtil.orderTableData(ResponseObject.class, mAdapter, mAdapterData, mBroadcastName, columnOrderName, fromCalendar, toCalendar);
    }
  }

  protected void setBroadcastName(String broadcastName) {
    this.mBroadcastName = broadcastName;
  }

  protected String getBroadcastName() {
    return mBroadcastName;
  }
}
