package com.example.finnkino;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<Movie> movieList;
    private LayoutInflater mInflater;


    MovieAdapter(Context context, ArrayList<Movie> movieList) {
        this.mInflater = LayoutInflater.from(context);
        this.movieList = movieList;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_items, parent, false);
        return new ViewHolder(view);
    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.startTime.setText(movie.getStartTimeString() + " -");
        holder.endTime.setText(movie.getEndTimeString());
        holder.movieName.setText(movie.getMovieName());
        holder.auditorium.setText(movie.getAuditorium());
        holder.theatre.setText(movie.getTheatre());
        setImage(holder, movie);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return movieList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView movieName;
        TextView startTime;
        TextView endTime;
        TextView theatre;
        TextView auditorium;
        ImageView picture;

        ViewHolder(View itemView) {
            super(itemView);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            movieName = itemView.findViewById(R.id.movieName);
            theatre = itemView.findViewById(R.id.theatre);
            auditorium = itemView.findViewById(R.id.auditorium);
            picture = itemView.findViewById(R.id.picture);
        }
        public ImageView getPicture() { return picture; }
    }

//https://medium.com/@crossphd/android-image-loading-from-a-string-url-6c8290b82c5e
    private void setImage(ViewHolder holder, Movie movie) {
        class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
            ImageView imageView;

            public DownloadImageTask(ImageView bmImage) {
                this.imageView = bmImage;
            }

            protected Bitmap doInBackground(String... urls) {
                String url = urls[0];
                Bitmap bitmap = null;
                try {
                    InputStream in = new java.net.URL(url).openStream();
                    bitmap = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    System.out.println(e);
                }
                return bitmap;
            }

            protected void onPostExecute(Bitmap result) {
                imageView.setImageBitmap(result);
            }
        }
        new DownloadImageTask(holder.getPicture()).execute(movie.getPicture());
    }
}