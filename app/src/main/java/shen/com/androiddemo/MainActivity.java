package shen.com.androiddemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
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

		listView.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
		adapter = new MovieListAdapter(this);
		adapter.setOnItemClickListener(this);
		listView.setAdapter(adapter);
	}

	@Override protected void onResume() {
		super.onResume();
		query();
	}

	public void query() {

		new AsyncTask<Void, Void, Void>() {

			@Override protected Void doInBackground(Void... params) {
				ApiManager.getMovieService().getNowPlaying(1, new Callback<NowPlayingResponse>() {
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
				});
				return null;
			}
		}.execute();
	}

	@Override public void movieSelected(String movieId, String title) {
		Intent intent =
				new Intent(this, MovieDetailActivity.class).putExtra(MovieDetailActivity.INTENT_EXTRA_MOVIE_ID, movieId)
						.putExtra(MovieDetailActivity.INTENT_EXTRA_MOVIE_TITLE, title);
		startActivity(intent);
	}

}
