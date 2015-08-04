package shen.com.androiddemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import shen.com.androiddemo.api.ApiManager;
import shen.com.androiddemo.model.BasicMovieInfo;

/**
 * Created by cfalc on 8/1/15.
 */
public class MovieDetailActivity extends AppCompatActivity {

	private static final String INTENT_EXTRA_MOVIE_ID = "com.shen.androiddemo.movieId";
	private static final String INTENT_EXTRA_MOVIE_TITLE = "com.shen.androiddemo.movieTitle";
	private static final String TAG = MovieDetailActivity.class.getSimpleName();

	@Bind(R.id.poster) ImageView poster;
	@Bind(R.id.title) TextView title;
	@Bind(R.id.runTime) TextView runTime;
	@Bind(R.id.releaseDate) TextView releaseDate;
	@Bind(R.id.score) TextView score;
	@Bind(R.id.userRatings) TextView userRatings;
	@Bind(R.id.overview) TextView overview;

	public static Intent MovieDetailIntentWithIdAndTitle(Context context, String id, String title) {
		return new Intent(context, MovieDetailActivity.class).putExtra(MovieDetailActivity.INTENT_EXTRA_MOVIE_ID, id)
				.putExtra(MovieDetailActivity.INTENT_EXTRA_MOVIE_TITLE, title);
	}

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_detail);
		ButterKnife.bind(this);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setTitle(getIntent().getStringExtra(INTENT_EXTRA_MOVIE_TITLE));
		}
		query();
	}

	public void query() {
		String movieId = getIntent().getStringExtra(INTENT_EXTRA_MOVIE_ID);

		Callback<BasicMovieInfo> callback = new Callback<BasicMovieInfo>() {
			@Override public void success(final BasicMovieInfo basicMovieInfo, Response response) {
				runOnUiThread(new Runnable() {
					@Override public void run() {
						setData(basicMovieInfo);
					}
				});
			}

			@Override public void failure(RetrofitError error) {
				Toast.makeText(MovieDetailActivity.this, R.string.offline_error, Toast.LENGTH_SHORT).show();
			}
		};

		ApiManager.queryForBasicMovieInfoWithCallback(movieId, callback);
	}

	private void setData(BasicMovieInfo info) {
		Uri uri = Uri.parse(Utils.fullPosterUrl(info.posterPath));
		poster.setLayoutParams(
				new RelativeLayout.LayoutParams((int) Utils.getWindowWidth(this) / 3, (int) Utils.getWindowHeight(this) / 3));
		Picasso.with(this).load(uri).fit().centerCrop().into(poster);
		title.setText(info.title);
		releaseDate.setText(String.format(getString(R.string.released), info.releaseDate));
		runTime.setText(String.format(getString(R.string.minutes), info.runtime));
		score.setText(String.format(getString(R.string.avg_rating), info.voteAverage));
		userRatings.setText(String.format(getString(R.string.user_ratings), info.voteCount));
		overview.setText(info.overview);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				onBackPressed();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
