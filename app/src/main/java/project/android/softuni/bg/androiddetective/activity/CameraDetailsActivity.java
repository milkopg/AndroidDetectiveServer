package project.android.softuni.bg.androiddetective.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import project.android.softuni.bg.androiddetective.R;

public class CameraDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        String title = getIntent().getStringExtra("title");
        String imagePath = getIntent().getStringExtra("image");

        Bitmap bm = BitmapFactory.decodeFile(imagePath);

        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(title);

        ImageView imageView = (ImageView) findViewById(R.id.imageViewPicture);
        imageView.setImageBitmap(bm);
    }
}
