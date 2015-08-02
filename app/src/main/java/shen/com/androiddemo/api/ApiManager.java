package shen.com.androiddemo.api;

import android.os.AsyncTask;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import shen.com.androiddemo.model.BasicMovieInfo;
import shen.com.androiddemo.model.NowPlayingResponse;

/**
 * Created by cfalc on 8/1/15.
 */
public class ApiManager {
	public static final String API_URL = "http://api.themoviedb.org/3";
	public static final String API_KEY = "ebea8cfca72fdff8d2624ad7bbf78e4c";
	private static final String TAG = "MovieApi";
	private static ApiManager apiManager = null;

	private MovieService movieService;

	private ApiManager() {
		setupRestClients();
	}

	private void setupRestClients() {
		Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

		// Create a very simple REST adapter which points to the MovieAPI endpoint.
		RestAdapter retrofit = new RestAdapter.Builder().setEndpoint(API_URL)
				//.setLogLevel(RestAdapter.LogLevel.FULL)
				.setConverter(new GsonConverter(gson)).setRequestInterceptor(new RequestInterceptor() {
					@Override public void intercept(RequestInterceptor.RequestFacade request) {
						request.addQueryParam("api_key", API_KEY);
						request.addQueryParam("format", "json");
					}
				}).build();

		// Create an instance of our Movie API interface.
		movieService = retrofit.create(MovieService.class);
	}

	private static MovieService getMovieService() {
		if (apiManager == null) {
			apiManager = new ApiManager();
		}
		return apiManager.movieService;
	}

	public static void queryForNowPlayingWithCallback(final Callback<NowPlayingResponse> callback) {
		new AsyncTask<Void, Void, Void>() {

			@Override protected Void doInBackground(Void... params) {
				ApiManager.getMovieService().getNowPlaying(1, callback);
				return null;
			}
		}.execute();
	}

	public static void queryForBasicMovieInfoWithCallback(String movieId, final Callback<BasicMovieInfo> callback) {
		new AsyncTask<String, Void, Void>() {

			@Override protected Void doInBackground(String... params) {
				if (params.length < 1) {
					return null;
				}
				final String movieId = params[0];
				if (movieId == null) {
					return null;
				}
				ApiManager.getMovieService().getBasicMovieInfo(movieId, callback);
				return null;
			}
		}.execute(movieId);
	}
}
