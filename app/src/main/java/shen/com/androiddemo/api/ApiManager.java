package shen.com.androiddemo.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

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
		RestAdapter retrofit = new RestAdapter.Builder()
				.setEndpoint(API_URL)
				//.setLogLevel(RestAdapter.LogLevel.FULL)
				.setConverter(new GsonConverter(gson))
				.setRequestInterceptor(new RequestInterceptor() {
					@Override public void intercept(RequestInterceptor.RequestFacade request) {
						request.addQueryParam("api_key", API_KEY);
						request.addQueryParam("format", "json");
					}
				})
				.build();

		// Create an instance of our Movie API interface.
		movieService = retrofit.create(MovieService.class);
	}

	public static MovieService getMovieService() {
		if (apiManager == null) {
			apiManager = new ApiManager();
		}
		return apiManager.movieService;
	}
}
