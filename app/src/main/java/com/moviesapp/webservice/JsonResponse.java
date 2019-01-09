package com.moviesapp.webservice;

import com.moviesapp.model.MovieModel;

import java.util.ArrayList;

public class JsonResponse {

    public String original_title;
    public String overview;
    public String original_language;
    public String popularity;
    public String release_date;
    public String revenue;
    public String backdrop_path;
    public String poster_path;

    public ArrayList<MovieModel> results;
    public ArrayList<MovieModel> genres;
}
