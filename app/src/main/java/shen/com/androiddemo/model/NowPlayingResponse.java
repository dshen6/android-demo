package shen.com.androiddemo.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by cfalc on 8/1/15.
 */
public class NowPlayingResponse {

	@SerializedName("results") public List<MovieInfo> movies;

}
