package shen.com.androiddemo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cfalc on 8/1/15.
 */
public class BasicMovieInfo {

	@SerializedName("original_title") public String title;

	@SerializedName("overview") public String overview;

	@SerializedName("release_date") public String releaseDate;

	@SerializedName("id") public String id;

	@SerializedName("poster_path") public String posterPath;

	@SerializedName("runtime") public int runtime;

	@SerializedName("vote_average") public float voteAverage;

	@SerializedName("vote_count") public int voteCount;


}
