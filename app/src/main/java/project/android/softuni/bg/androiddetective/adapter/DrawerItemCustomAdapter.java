package project.android.softuni.bg.androiddetective.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.data.DataModel;

/**
 * Created by Milko G on 22/09/2016.
 */
public class DrawerItemCustomAdapter extends ArrayAdapter<DataModel> {

    private Context mContext;
    private int layoutResourceId;
    private DataModel data[] = null;

  /**
   * DrawerItemCustomAdapter used in DrawerMenus
   * @param mContext
   * @param layoutResourceId - layoutId
   * @param data - DataModel[] data
   */
    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, DataModel[] data) {
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        convertView = inflater.inflate(layoutResourceId, parent, false);

        ImageView imageViewIcon = (ImageView) convertView.findViewById(R.id.imageViewIcon);
        TextView textViewName = (TextView) convertView.findViewById(R.id.textViewName);

        DataModel folder = data[position];

        imageViewIcon.setImageResource(folder.getIcon());
        textViewName.setText(folder.getName());

        return convertView;
    }
}

