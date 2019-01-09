package com.moviesapp.webservice;


public class ApiConstants {

    public static int LOG_STATUS = 0; // Change to 1 for production app

    public static final String SHARED_PREF = "UpcomingMovieApp";

    public static final String BASE_URL = "https://api.themoviedb.org/3/genre/";

    public static final String GET_MOVIES_GENRE_URL = BASE_URL + "movie/list?api_key=f17e9c5e6c34ad9dc2bf6aab852c0cc7";

    public static final String GET_MOVIES_LIST_URL = "/movies?api_key=f17e9c5e6c34ad9dc2bf6aab852c0cc7";

    public static final String BASE_DETAIL_URL = "https://api.themoviedb.org/3/movie/";

    public static final String MOVIE_DETAIL_URL = "?api_key=f17e9c5e6c34ad9dc2bf6aab852c0cc7";

    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500/";

}
