package project.android.softuni.bg.androiddetective.fragment.base;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
  private static final String TAG = CallerFragment.class.getSimpleName();
  private List<ResponseObject> mAdapterData;
  private String mBroadcastName;
  private RecycleViewCustomAdapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;
  private RecyclerView mRecyclerView;
  private EditText editTextFromDate;
  private EditText editTextToDate;
  private DatePickerDialog fromDatePickerDialog;
  private DatePickerDialog toDatePickerDialog;
  private SimpleDateFormat dateFormatter;

  private Calendar fromCalendar;
  private Calendar toCalendar;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
    View rootView = inflater.inflate(R.layout.fragment_data, container, false);

    editTextFromDate = (EditText) rootView.findViewById(R.id.editTextFromDate);
    editTextFromDate.setInputType(InputType.TYPE_NULL);
    editTextFromDate.requestFocus();

    editTextToDate = (EditText) rootView.findViewById(R.id.editTextToDate);
    editTextToDate.setInputType(InputType.TYPE_NULL);
    editTextToDate.requestFocus();

    dateFormatter = new SimpleDateFormat(Constants.DATE_FORMAT_SHORT_DATE, Locale.US);
    setBroadcastName(bundle.getString(Constants.BROADCAST_NAME));

    mAdapterData = new ArrayList<>();
    mAdapterData = ResponseObject.find(ResponseObject.class, Constants.BROADCAST_NAME + "=?", getBroadcastName());

    mAdapter = new RecycleViewCustomAdapter(mAdapterData);
    mLayoutManager = new LinearLayoutManager(getContext());

    mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setHasFixedSize(false);

    setDateTimeField();

    return rootView;
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
        filterData();
      }

    }, fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH), fromCalendar.get(Calendar.DAY_OF_MONTH));
    fromDatePickerDialog.updateDate(fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH) , 1);
    editTextFromDate.setText(dateFormatter.format(fromCalendar.getTime()));

    toDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
      public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        initCalendarValues(toCalendar, year, monthOfYear, dayOfMonth, 23, 59, 59);
        filterData();
        editTextToDate.setText(dateFormatter.format(toCalendar.getTime()));
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
    if (view.getId() == R.id.editTextFromDate) {
      fromDatePickerDialog.show();
    } else if (view.getId() == R.id.editTextToDate) {
      toDatePickerDialog.show();
    }
  }

  private void filterData() {
    if ((fromCalendar) == null || (toCalendar == null)) return;
    Select<ResponseObject> select = Select.from(ResponseObject.class).where(Condition.prop(Constants.BROADCAST_NAME).like(getBroadcastName()), Condition.prop("DATE").gt(fromCalendar.getTime().getTime()), Condition.prop("DATE").lt(toCalendar.getTime().getTime())).orderBy("DATE DESC");

    if (select != null) {
      Iterator<ResponseObject> iterator = select.iterator();
      if (iterator != null && iterator.hasNext())
        mAdapterData.clear();

      while (iterator.hasNext()) {
        mAdapterData.add(iterator.next());
        if (!iterator.hasNext()) {
          mAdapter.notifyDataSetChanged();
        }
      }
    }
  }

  protected void setBroadcastName(String broadcastName) {
    this.mBroadcastName = broadcastName;
  }

  public String getBroadcastName() {
    return mBroadcastName;
  }
}
