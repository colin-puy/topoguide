package fr.colin.topoguide.views.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class ImagesGridAdapter extends ArrayAdapter<Bitmap> {

   private final List<Bitmap> objects;
   private final Context context;
   private final int imageViewResourceId;

   public ImagesGridAdapter(Context context, int imageViewResourceId, List<Bitmap> objects) {
      super(context, imageViewResourceId, objects);
      this.context = context;
      this.imageViewResourceId = imageViewResourceId;
      this.objects = objects;
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
      ImageView imageView;
      if (convertView == null) {
         LayoutInflater inflater = LayoutInflater.from(context);
         imageView = (ImageView) inflater.inflate(imageViewResourceId, null);
      } else {
          imageView = (ImageView) convertView;
      }
      imageView.setImageBitmap(objects.get(position));
      return imageView;
   }
}
