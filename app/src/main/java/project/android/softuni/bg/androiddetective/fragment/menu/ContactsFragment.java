package project.android.softuni.bg.androiddetective.fragment.menu;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.adapter.RecycleViewContactsCustomAdapter;
import project.android.softuni.bg.androiddetective.adapter.RecycleViewImageCustomAdapter;
import project.android.softuni.bg.androiddetective.util.Constants;
import project.android.softuni.bg.androiddetective.webapi.model.Contact;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

/**
 * Created by Milko on 7.10.2016 г..
 */

public class ContactsFragment extends Fragment {
  private RecyclerView.Adapter mAdapter;
  private List<Contact> mAdapterData;
  private RecyclerView.LayoutManager mLayoutManager;
  private RecyclerView mRecyclerView;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_contacts_data, container, false);

    mAdapterData = Contact.find(Contact.class, null, null, null, "CONTACT_ID Desc", null);
    mAdapter = new RecycleViewContactsCustomAdapter(mAdapterData);

    mLayoutManager = new LinearLayoutManager(getContext());

    mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewContactData);
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setHasFixedSize(false);
    return view;
  }
}
