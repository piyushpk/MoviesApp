package com.moviesapp.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.moviesapp.R;
import com.moviesapp.interfaces.ApiServiceCaller;
import com.moviesapp.ui.adapter.MovieAdapter;
import com.moviesapp.utility.App;
import com.moviesapp.utility.CommonUtils;
import com.moviesapp.webservice.ApiConstants;
import com.moviesapp.webservice.JsonResponse;
import com.moviesapp.webservice.WebRequests;

import java.util.ArrayList;

public class MainActivity extends ParentActivity implements ApiServiceCaller {

    private Context mContext;
    private RecyclerView recyclerMovie;
    private Spinner spinnerGenre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView txtToolbarTitle = findViewById(R.id.txt_title);
        txtToolbarTitle.setText(R.string.app_name);

        recyclerMovie = findViewById(R.id.recycler_upcoming_movies);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerMovie.setLayoutManager(linearLayoutManager);

        spinnerGenre = findViewById(R.id.spinner_genre);

        getMovieGenre();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getMovieGenre() {
        showLoadingDialog();
        if (CommonUtils.getInstance(this).checkConnectivity(mContext)) {
            JsonObjectRequest request = WebRequests.callPostMethod(null, Request.Method.GET, ApiConstants.GET_MOVIES_GENRE_URL,
                    ApiConstants.GET_MOVIES_GENRE_URL, this);
            App.getInstance().addToRequestQueue(request, ApiConstants.GET_MOVIES_GENRE_URL);
        } else {
            CommonUtils.showSnack(mContext, getString(R.string.error_internet_not_connected));
        }
    }

    private void getMovieList(String genreId) {
        showLoadingDialog();
        if (CommonUtils.getInstance(this).checkConnectivity(mContext)) {

            JsonObjectRequest request = WebRequests.callPostMethod(null, Request.Method.GET,
                    ApiConstants.BASE_URL + genreId + ApiConstants.GET_MOVIES_LIST_URL,
                    ApiConstants.GET_MOVIES_LIST_URL, this);
            App.getInstance().addToRequestQueue(request, ApiConstants.GET_MOVIES_LIST_URL);

        } else {
            CommonUtils.showSnack(mContext, getString(R.string.error_internet_not_connected));
        }
    }

    @Override
    public void onAsyncSuccess(final JsonResponse jsonResponse, String label) {
        dismissLoadingDialog();
        switch (label) {
            case ApiConstants.GET_MOVIES_GENRE_URL: {
                if (jsonResponse != null) {
                    if (jsonResponse.genres != null) {
                        ArrayList<String> list = new ArrayList<>();
                        for (int i = 0; i < jsonResponse.genres.size(); i++) {
                            list.add(jsonResponse.genres.get(i).name);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, list);
                        spinnerGenre.setAdapter(adapter);
                        spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String genreId = jsonResponse.genres.get(position).id;
                                getMovieList(genreId);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    }
                }
                break;
            }
            case ApiConstants.GET_MOVIES_LIST_URL: {
                if (jsonResponse != null) {
                    MovieAdapter upcomingMovieAdapter = new MovieAdapter(mContext, jsonResponse.results);
                    recyclerMovie.setAdapter(upcomingMovieAdapter);
                }
                break;
            }
        }
    }

    @Override
    public void onAsyncFail(String message, String label, NetworkResponse response) {
        dismissLoadingDialog();
        switch (label) {
            case ApiConstants.GET_MOVIES_GENRE_URL: {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_MOVIES_LIST_URL: {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }
            break;
            default:
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onAsyncCompletelyFail(String message, String label) {
        dismissLoadingDialog();
        switch (label) {
            case ApiConstants.GET_MOVIES_GENRE_URL: {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_MOVIES_LIST_URL: {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }
            break;
            default:
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}