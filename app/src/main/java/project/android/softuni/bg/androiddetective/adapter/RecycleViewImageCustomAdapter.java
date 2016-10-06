package project.android.softuni.bg.androiddetective.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

/**
 * Created by Milko on 6.10.2016 г..
 */

public class RecycleViewImageCustomAdapter extends  RecyclerView.Adapter<RecycleViewImageCustomAdapter.ViewHolder> {

  private List<ResponseObject> mAdapterData;
  private Context mContext;

  public RecycleViewImageCustomAdapter(List<ResponseObject> mAdapterData, Context mContext) {
    this.mAdapterData = mAdapterData;
    this.mContext = mContext;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageViewCameraPicture;
    private TextView textViewCameraPictureInfo;
    public ViewHolder(View itemView) {
      super(itemView);
      imageViewCameraPicture = (ImageView) itemView.findViewById(R.id.imageViewPictureCamera);
      textViewCameraPictureInfo = (TextView) itemView.findViewById(R.id.textViewPictureDate);
    }
  }

  @Override
  public RecycleViewImageCustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_image_row, parent, false);
    ViewHolder vh = new ViewHolder(view);
    return vh;
  }

  @Override
  public void onBindViewHolder(RecycleViewImageCustomAdapter.ViewHolder holder, int position) {
    //Picasso.with(mContext).load(mAdapterData.get(position).imagePath + "/" + mAdapterData.get(position).imageName).resize(240, 120).into(holder.imageViewCameraPicture);

    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inSampleSize = 2;
    Bitmap bm = BitmapFactory.decodeFile(mAdapterData.get(position).imagePath + "/" + mAdapterData.get(position).imageName, options);
    holder.imageViewCameraPicture.setImageBitmap(bm);

    holder.textViewCameraPictureInfo.setText(mAdapterData.get(position).imageName);
    ///data/data/project.android.softuni.bg.androiddetective/app_images/image_1.jpg
  }

  @Override
  public int getItemCount() {
    return mAdapterData.size();
  }


}
