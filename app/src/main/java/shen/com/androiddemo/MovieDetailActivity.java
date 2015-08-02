package shen.com.androiddemo;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
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

	public static final String INTENT_EXTRA_MOVIE_ID = "com.shen.androiddemo.movieId";
	public static final String INTENT_EXTRA_MOVIE_TITLE = "com.shen.androiddemo.movieTitle";
	private static final String TAG = MovieDetailActivity.class.getSimpleName();

	@Bind(R.id.poster) ImageView poster;
	@Bind(R.id.title) TextView title;
	@Bind(R.id.runTime) TextView runTime;
	@Bind(R.id.releaseDate) TextView releaseDate;
	@Bind(R.id.score) TextView score;
	@Bind(R.id.userRatings) TextView userRatings;
	@Bind(R.id.overview) TextView overview;
	@Bind(R.id.viewTrailer) Button viewTrailer;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_detail);
		ButterKnife.bind(this);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(getIntent().getStringExtra(INTENT_EXTRA_MOVIE_TITLE));
		query();
	}

	public void query() {
		new AsyncTask<Void, Void, Void>() {

			@Override protected Void doInBackground(Void... params) {
				final String movieId = getIntent().getStringExtra(INTENT_EXTRA_MOVIE_ID);
				if (movieId == null) {
					return null;
				}
				ApiManager.getMovieService().getBasicMovieInfo(movieId, new Callback<BasicMovieInfo>() {
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
				});
				return null;
			}
		}.execute();
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
