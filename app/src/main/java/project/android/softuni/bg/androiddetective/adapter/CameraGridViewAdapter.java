package project.android.softuni.bg.androiddetective.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

public class CameraGridViewAdapter extends ArrayAdapter<ResponseObject> {

    private Context mContext;
    private int mLayoutResourceId;
    private List<ResponseObject> mAdapterData = new ArrayList<ResponseObject>();

    public CameraGridViewAdapter(Context context, int layoutResourceId, List<ResponseObject> data) {
        super(context, layoutResourceId, data);
        this.mLayoutResourceId = layoutResourceId;
        this.mContext = context;
        this.mAdapterData = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.textViewPictureName);
            holder.image = (ImageView) row.findViewById(R.id.imageViewPictureGrid);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ResponseObject item = mAdapterData.get(position);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        Bitmap bm = BitmapFactory.decodeFile(mAdapterData.get(position).getImagePath() + "/" + mAdapterData.get(position).getSendText(), options);
        holder.image.setImageBitmap(bm);
        holder.imageTitle.setText(item.imageName);

        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}