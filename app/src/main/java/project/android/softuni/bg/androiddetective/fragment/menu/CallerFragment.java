package project.android.softuni.bg.androiddetective.fragment.menu;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.adapter.RecycleViewCustomAdapter;
import project.android.softuni.bg.androiddetective.fragment.base.BaseFragment;
import project.android.softuni.bg.androiddetective.util.Constants;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

/**
 * Created by Milko G on 22/09/2016.
 */
public class CallerFragment extends BaseFragment implements View.OnClickListener{
  private static final String TAG = CallerFragment.class.getSimpleName();
  private List<ResponseObject> mAdapterData;

  @TargetApi(Build.VERSION_CODES.N)
  @RequiresApi(api = Build.VERSION_CODES.M)
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);;

    initDateFields();

    mAdapterData = new ArrayList<>();
    populateData(Constants.RECEIVER_CALL);
    mAdapter = new RecycleViewCustomAdapter(mAdapterData);
    mRecyclerView.setAdapter(mAdapter);

    return view;
  }


  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.editTextFromDate) {
      fromDatePickerDialog.show();
    } else if (view.getId() == R.id.editTextToDate) {
      toDatePickerDialog.show();
    }
  }

  @Override
  protected void initDateFields() {

    fromCalendar = Calendar.getInstance();
    fromCalendar.set(fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH), 1);
    toCalendar = Calendar.getInstance();

    editTextFromDate.setOnClickListener(this);
    editTextToDate.setOnClickListener(this);

    initCalendarValues(fromCalendar, fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH), 1, 0 , 0, 0);
    initCalendarValues(toCalendar, fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH), fromCalendar.get(Calendar.MONTH), 23, 59, 59);


    fromDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

      public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        initCalendarValues(fromCalendar, year, monthOfYear, dayOfMonth, 0 , 0, 0);
        editTextFromDate.setText(dateFormatter.format(fromCalendar.getTime()));
        populateData(Constants.RECEIVER_CALL);
      }

    }, fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH), fromCalendar.get(Calendar.DAY_OF_MONTH));
    fromDatePickerDialog.updateDate(fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH), 1);

    editTextFromDate.setText(dateFormatter.format(fromCalendar.getTime()));

    toDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
      public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        initCalendarValues(toCalendar, fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH), dayOfMonth, 23, 59, 59);
        populateData(Constants.RECEIVER_CALL);
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
  protected void populateData(String receiverName) {
    if ((fromCalendar) == null || (toCalendar == null)) return;
    Select<ResponseObject> select = Select.from(ResponseObject.class).where(Condition.prop(Constants.BROADCAST_NAME).like(receiverName), Condition.prop("DATE").gt(fromCalendar.getTime().getTime()), Condition.prop("DATE").lt(toCalendar.getTime().getTime()));

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
}
