package com.moviesapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.moviesapp.R;
import com.moviesapp.interfaces.ApiServiceCaller;
import com.moviesapp.ui.adapter.ImageSlidingAdapter;
import com.moviesapp.utility.App;
import com.moviesapp.utility.CommonUtils;
import com.moviesapp.webservice.ApiConstants;
import com.moviesapp.webservice.JsonResponse;
import com.moviesapp.webservice.WebRequests;

import java.util.ArrayList;

public class MovieDetailActivity extends ParentActivity implements ApiServiceCaller {

    private Context mContext;
    private String movieId;
    private TextView txtMovieOverview, txtMovieLanguage, txtMoviePopularity, txtReleaseDate, txtRevenue;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ViewPager viewPagerImage;
    private LinearLayout linearDotPanel;
    private int dotsCount;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mContext = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            movieId = intent.getStringExtra(getString(R.string.movie_id));
        } else {
            Toast.makeText(mContext, getString(R.string.error_data_not_present_at_this_moment), Toast.LENGTH_SHORT).show();
            finish();
        }

        txtMovieOverview = findViewById(R.id.txt_movie_overview);
        txtMovieLanguage = findViewById(R.id.txt_movie_language);
        txtMoviePopularity = findViewById(R.id.txt_movie_popularity);
        txtReleaseDate = findViewById(R.id.txt_release_date);
        txtRevenue = findViewById(R.id.txt_revenue);

        viewPagerImage = findViewById(R.id.viewPager);
        linearDotPanel = findViewById(R.id.linear_slider_dots);

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);

        getMovieDetails();
    }

    private void getMovieDetails() {
        showLoadingDialog();
        if (CommonUtils.getInstance(this).checkConnectivity(mContext)) {

            JsonObjectRequest request = WebRequests.callPostMethod(null, Request.Method.GET,
                    ApiConstants.BASE_DETAIL_URL + movieId + ApiConstants.MOVIE_DETAIL_URL,
                    ApiConstants.MOVIE_DETAIL_URL, this);
            App.getInstance().addToRequestQueue(request, ApiConstants.MOVIE_DETAIL_URL);

        } else {
            CommonUtils.showSnack(mContext, getString(R.string.error_internet_not_connected));
        }
    }

    private void setImageViewPager(ArrayList<String> upcomingMoviesModels) {

        ImageSlidingAdapter viewPagerAdapter = new ImageSlidingAdapter(mContext, upcomingMoviesModels);

        viewPagerImage.setAdapter(viewPagerAdapter);

        dotsCount = viewPagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {

            dots[i] = new ImageView(mContext);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            linearDotPanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPagerImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onAsyncSuccess(JsonResponse jsonResponse, String label) {
        dismissLoadingDialog();
        switch (label) {
            case ApiConstants.MOVIE_DETAIL_URL: {
                if (jsonResponse != null) {

                    collapsingToolbarLayout.setTitle(jsonResponse.original_title);
                    txtMovieOverview.setText(getString(R.string.overview) + " : " + jsonResponse.overview);
                    txtMovieLanguage.setText(jsonResponse.original_language);
                    txtMoviePopularity.setText(jsonResponse.popularity);
                    txtReleaseDate.setText(jsonResponse.release_date);
                    txtRevenue.setText(jsonResponse.revenue);

                    ArrayList<String> images = new ArrayList<>();
                    images.add(jsonResponse.backdrop_path);
                    images.add(jsonResponse.poster_path);
                    setImageViewPager(images);
                }
                break;
            }
        }
    }

    @Override
    public void onAsyncFail(String message, String label, NetworkResponse response) {
        dismissLoadingDialog();
        switch (label) {
            case ApiConstants.MOVIE_DETAIL_URL: {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
            default:
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    @Override
    public void onAsyncCompletelyFail(String message, String label) {
        dismissLoadingDialog();
        switch (label) {
            case ApiConstants.MOVIE_DETAIL_URL: {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
            default:
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
