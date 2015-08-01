package shen.com.androiddemo;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import shen.com.androiddemo.model.MovieInfo;

/**
 * Created by cfalc on 8/1/15.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

	private Context context;
	private List<MovieInfo> movies;
	private OnItemClickDelegate delegate;
	private ViewGroup.MarginLayoutParams cachedLayoutParams;

	private static final String TAG = MovieListAdapter.class.getSimpleName();

	public MovieListAdapter(Context context) {
		this.context = context;
		movies = new ArrayList<>();
		cachedLayoutParams = new ViewGroup.MarginLayoutParams((int) Utils.getWindowWidth(context) / 2,
				(int) Utils.getWindowHeight(context) / 2);
	}

	public interface OnItemClickDelegate {
		void movieSelected(String movieId);
	}

	public void setOnItemClickListener(OnItemClickDelegate delegate) {
		this.delegate = delegate;
	}

	@Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
		view.setLayoutParams(cachedLayoutParams);
		return new ViewHolder(view);
	}

	@Override public void onBindViewHolder(ViewHolder holder, int position) {
		MovieInfo movie = movies.get(position);
		Uri uri = Uri.parse(Utils.fullPosterUrl(movie.posterPath));
		Picasso.with(context).load(uri).into(holder.poster);
		holder.title.setText(movie.title);
	}

	@Override public int getItemCount() {
		return movies.size();
	}

	public void setData(List<MovieInfo> data) {
		this.movies = data;
		notifyDataSetChanged();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		@Bind(R.id.title) TextView title;
		@Bind(R.id.poster) ImageView poster;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					if (delegate != null) {
						delegate.movieSelected(movies.get(ViewHolder.this.getAdapterPosition()).id);
					}
				}
			});
		}
	}
}
