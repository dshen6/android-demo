package shen.com.androiddemo.api;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import shen.com.androiddemo.model.NowPlayingResponse;

/**
 * Created by cfalc on 8/1/15.
 */
public interface MovieService {

	@GET("/movie/now_playing")
	void getNowPlaying(@Query("page") int page, Callback<NowPlayingResponse> callback);
}
