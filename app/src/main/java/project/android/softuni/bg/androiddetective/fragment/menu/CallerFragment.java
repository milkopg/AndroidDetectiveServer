package project.android.softuni.bg.androiddetective.fragment.menu;

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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.adapter.RecycleViewCustomAdapter;
import project.android.softuni.bg.androiddetective.util.Constants;
import project.android.softuni.bg.androiddetective.webapi.model.Response;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseBase;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

/**
 * Created by Milko G on 22/09/2016.
 */
public class CallerFragment extends Fragment implements View.OnClickListener{
  private static final String TAG = CallerFragment.class.getSimpleName();
  private ConcurrentHashMap<String, ResponseObject> dataMap;
  private List<ResponseObject> mAdapterData;
  private List<ResponseObject> mAdapterDateFiltered;
  private RecycleViewCustomAdapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;
  private RecyclerView mRecyclerView;
  private EditText editTextFromDate;
  private EditText editTextToDate;
  private DatePickerDialog fromDatePickerDialog;
  private DatePickerDialog toDatePickerDialog;
  private SimpleDateFormat dateFormatter;
  private Calendar fromDate;
  private Calendar toDate;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_data, container, false);

    editTextFromDate = (EditText) rootView.findViewById(R.id.editTextFromDate);
    editTextFromDate.setInputType(InputType.TYPE_NULL);
    editTextFromDate.requestFocus();

    editTextToDate = (EditText) rootView.findViewById(R.id.editTextToDate);
    editTextToDate.setInputType(InputType.TYPE_NULL);
    editTextToDate.requestFocus();


    dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    mAdapterData  = new ArrayList<ResponseObject>(ResponseBase.getDataMap().values());
    mAdapterDateFiltered = new ArrayList<>();
//    for (ResponseObject object : mAdapterData) {
//      if (object.broadcastName.equals(Constants.RECEIVER_CALL)) {
//        mAdapterDateFiltered.add(object);
//      }
//    }

    //ResponseObject.findAll(ResponseObject.class);
    //mAdapterDateFiltered = ResponseObject.listAll(ResponseObject.class);
    mAdapterDateFiltered = ResponseObject.find(ResponseObject.class, Constants.BROADCAST_NAME + "=?", Constants.RECEIVER_CALL);
    //Book.find(Book.class, "author = ?", new String{getId()})

    mAdapter  = new RecycleViewCustomAdapter(mAdapterDateFiltered);
    mLayoutManager = new LinearLayoutManager(getContext());

    mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setHasFixedSize(true);

    setDateTimeField();

    return rootView;
  }

  private void setDateTimeField() {
    editTextFromDate.setOnClickListener(this);
    editTextToDate.setOnClickListener(this);

    final Calendar fromCalendar = Calendar.getInstance();
    fromDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

      public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        //fromDate = Calendar.getInstance();
        fromCalendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
        editTextFromDate.setText(dateFormatter.format(fromCalendar.getTime()));
      }

    },fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH), fromCalendar.get(Calendar.DAY_OF_MONTH));

    final Calendar toCalendar = Calendar.getInstance();
    toDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
      public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        //toDate = Calendar.getInstance();
        toCalendar.set(year, monthOfYear, dayOfMonth, 23, 59, 59);

       // mAdapterDateFiltered = ResponseObject.find(ResponseObject.class, "CAST(strftime('%s', " + dateFormatter.format(toDate.getTime()) + ")  AS  integer) <=", "CAST(strftime('%s', " +  dateFormatter.format(Calendar.getInstance().getTime()) + ") AS  integer)");
       // mAdapterDateFiltered = ResponseObject.find(ResponseObject.class, "DATE" + ">?", toDate.getTime().getTime());

        Select<ResponseObject>  select = Select.from(ResponseObject.class).where(Condition.prop(Constants.BROADCAST_NAME).like(Constants.RECEIVER_CALL), Condition.prop("DATE").gt(fromCalendar.getTime().getTime()), Condition.prop("DATE").lt(toCalendar.getTime().getTime()));
        if (select != null) {
          Iterator<ResponseObject> iterator = select.iterator();
          while (iterator.hasNext()) {
            mAdapterDateFiltered.add(iterator.next());
            if (!iterator.hasNext()) {
              mAdapterDateFiltered.clear();
              mAdapter.notifyDataSetChanged();
              mRecyclerView.invalidate();
            }
          }

          //ResponseObject.findWithQuery(ResponseObject.class, )
        }

        editTextToDate.setText(dateFormatter.format(toCalendar.getTime()));
      }

    },toCalendar.get(Calendar.YEAR), toCalendar.get(Calendar.MONTH), toCalendar.get(Calendar.DAY_OF_MONTH));
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.editTextFromDate) {
      fromDatePickerDialog.show();
    } else if (view.getId() == R.id.editTextToDate) {
      toDatePickerDialog.show();
    }
  }
}
