package ai.api.sample;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends ArrayAdapter<AIDialogSampleActivity.Images> {
    ArrayList<AIDialogSampleActivity.Images> imageList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public ImageAdapter(Context context, int resource, ArrayList<AIDialogSampleActivity.Images> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        imageList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.imageview = (ImageView) v.findViewById(R.id.card_view);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.imageview.setImageResource(R.drawable.ic_launcher);
        new DownloadImageTask(holder.imageview).execute(imageList.get(position).getImage());
        return v;

    }

    static class ViewHolder {
        public ImageView imageview;

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
