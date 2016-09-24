package project.android.softuni.bg.androiddetective.fragment.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.android.softuni.bg.androiddetective.R;

/**
 * Created by Milko G on 22/09/2016
 */
public class TableFragment extends Fragment {
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_table, container, false);
    return rootView;
  }

}
