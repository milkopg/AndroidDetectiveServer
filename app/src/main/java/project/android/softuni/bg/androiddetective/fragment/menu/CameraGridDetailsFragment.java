package project.android.softuni.bg.androiddetective.fragment.menu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import project.android.softuni.bg.androiddetective.R;

/**
 * Created by Milko on 6.10.2016 г..
 */

public class CameraGridDetailsFragment extends Fragment {
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.details_activity, container, false);

    String title = getArguments().getString("title");
    String imagePath = getArguments().getString("imagePath");

    TextView titleTextView = (TextView) view.findViewById(R.id.title);
    titleTextView.setText(title);

    ImageView imageView = (ImageView) view.findViewById(R.id.imageViewPicture);

    Bitmap bm = BitmapFactory.decodeFile(imagePath);
    imageView.setImageBitmap(bm);

    return view;
  }
}
