package shen.com.androiddemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import shen.com.androiddemo.api.ApiManager;
import shen.com.androiddemo.model.NowPlayingResponse;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.OnItemClickDelegate {

	@Bind(R.id.listView) RecyclerView listView;
	private MovieListAdapter adapter;

	private static final String TAG = MainActivity.class.getSimpleName();

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		adapter = new MovieListAdapter(this);
		adapter.setOnItemClickListener(this);
		listView.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
		listView.addItemDecoration(
				new GridSpacingDecoration(getResources().getDimensionPixelOffset(R.dimen.movie_grid_view_margin)));
		listView.setAdapter(adapter);
		query();
	}

	public void query() {
		Callback<NowPlayingResponse> callback = new Callback<NowPlayingResponse>() {
			@Override public void success(final NowPlayingResponse nowPlayingResponse, Response response) {
				runOnUiThread(new Runnable() {
					@Override public void run() {
						adapter.setData(nowPlayingResponse.movies);
					}
				});
			}

			@Override public void failure(RetrofitError error) {
				Toast.makeText(MainActivity.this, R.string.offline_error, Toast.LENGTH_SHORT).show();
			}
		};

		ApiManager.queryForNowPlayingWithCallback(callback);
	}

	@Override public void movieSelected(String movieId, String title) {
		Intent intent = MovieDetailActivity.MovieDetailIntentWithIdAndTitle(this, movieId, title);
		startActivity(intent);
	}
}
