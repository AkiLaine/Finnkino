package com.example.finnkino;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import android.content.Context;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finnkino.MovieTheaters;
import com.example.finnkino.R;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private MovieTheaters movieTheaters = MovieTheaters.getInstance();
    private Movies movies = Movies.getInstance();
    private ArrayList<Movie> resetMovies = new ArrayList<Movie>();
    private EditText startTimeText;
    private EditText endTimeText;
    private EditText movieNameText;
    private EditText dateText;
    private Spinner spinner;
    private MovieAdapter mAdapter;
    private RecyclerView movieRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        startTimeText = findViewById(R.id.startTimeText);
        endTimeText = findViewById(R.id.endTimeText);
        dateText = findViewById(R.id.dateText);
        movieNameText = findViewById(R.id.movieNameText);
        spinner = findViewById(R.id.spinner);
        movieRecycler = findViewById(R.id.movieRecycler);
        //Initializing theaters
        movieTheaters.readXML();
        //Putting theater names to spinner
        String[] theaternames = movieTheaters.getNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, theaternames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //Clearing input fields
        resetInterface();
    }

    public void searchButton(View v) {
        String theater = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
        Integer theaterId = movieTheaters.getId(theater);
        String date = dateText.getText().toString();
        String startTime = startTimeText.getText().toString();
        String endTime = endTimeText.getText().toString();
        String movieName = movieNameText.getText().toString();
        //Searching for movies with set criteria
        Movies.SearchParameters parameters = new Movies.SearchParameters(theaterId, date, startTime, endTime, movieName);
        movies.resetMovies();
        if (theaterId == 1029) {
            movies.readAllMovies(theaterId, date);
        }
        else {
            movies.readMovies(theaterId, date);
        }
        ArrayList<Movie> movieList = new ArrayList<>(movies.filterMovies(parameters));
        //Filling recycler with found movies
        updateRecycler(movieList);
        spinner.setSelection(0);
    }

    private void updateRecycler (ArrayList<Movie> movies) {
        movieRecycler.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(movieRecycler.getContext(), LinearLayoutManager.VERTICAL);
        movieRecycler.addItemDecoration(dividerItemDecoration);
        mAdapter = new MovieAdapter(this, movies);
        movieRecycler.setAdapter(mAdapter);
    }

    public void clearButton(View view) {
        resetInterface();
    }


    private void resetInterface() {
        // Set date to current
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        String formatDate = format.format(date);
        dateText.setText(formatDate);
        startTimeText.setText("");
        endTimeText.setText("");
        movieNameText.setText("");
        spinner.setSelection(0);
        movies.resetMovies();
        updateRecycler(resetMovies);
    }
}

