package project.android.softuni.bg.androiddetective.fragment.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.adapter.RecycleViewContactsCustomAdapter;
import project.android.softuni.bg.androiddetective.util.QueriesUtil;
import project.android.softuni.bg.androiddetective.webapi.model.Contact;

/**
 * Created by Milko on 7.10.2016 г..
 */

public class ContactsFragment extends Fragment implements View.OnClickListener{
  private TextView mTextViewContactName;
  private TextView mTextViewPhoneNumber;
  private TextView mTextViewEmail;

  private RecyclerView.Adapter mAdapter;
  private List<Contact> mAdapterData;
  private RecyclerView.LayoutManager mLayoutManager;
  private RecyclerView mRecyclerView;


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_contacts_data, container, false);

    mTextViewContactName = (TextView) view.findViewById(R.id.textViewContactName);
    mTextViewPhoneNumber = (TextView) view.findViewById(R.id.textViewContactPhoneNumber);
    mTextViewEmail = (TextView) view.findViewById(R.id.textViewContactEmail);

    mTextViewContactName.setOnClickListener(this);
    mTextViewPhoneNumber.setOnClickListener(this);
    mTextViewEmail.setOnClickListener(this);

    mAdapterData = Contact.find(Contact.class, null, null, null, "CONTACT_ID Desc", null);
    Collections.sort(mAdapterData, new Comparator<Contact>() {
      @Override
      public int compare(Contact c1, Contact c2) {
        if (c1.getContactId() < c2.getContactId())
          return 1;
        if (c1.getContactId() > c2.getContactId())
          return -1;
        return 0;
      }
    });
    mAdapter = new RecycleViewContactsCustomAdapter(mAdapterData);

    mLayoutManager = new LinearLayoutManager(getContext());

    mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewContactData);
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setHasFixedSize(false);

    return view;
  }

  @Override
  public void onClick(View view) {
    String columnName = getDatabaseColumnNameByViewId(view.getId());
    boolean ascending = QueriesUtil.getAscOrDescSorting(columnName);
    String orderBy = ascending ? "ASC" : "DESC";
    mAdapterData.clear();
    mAdapterData.addAll(Contact.find(Contact.class, null, null, null, columnName + " " + orderBy, null));
    mAdapter.notifyDataSetChanged();
  }

  private String getDatabaseColumnNameByViewId(int viewId) {
    String columnName = "";
    switch (viewId) {
      case  R.id.textViewContactName :
        columnName = "NAME";
        break;
      case R.id.textViewContactPhoneNumber :
        columnName = "PHONE_NUMBER";
        break;
      case R.id.textViewContactEmail :
        columnName = "EMAIL";
        break;
 }
    return columnName;
  }
}
