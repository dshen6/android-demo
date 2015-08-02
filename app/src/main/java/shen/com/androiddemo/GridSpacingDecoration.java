package shen.com.androiddemo;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by cfalc on 8/2/15.
 */
public class GridSpacingDecoration extends RecyclerView.ItemDecoration {

	private static final String TAG = GridSpacingDecoration.class.getSimpleName();

	private int marginPx = 0;

	public GridSpacingDecoration(int marginPx) {
		this.marginPx = marginPx;
	}

	@Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		super.getItemOffsets(outRect, view, parent, state);

		int halfSpacing = marginPx / 2;

		int childCount = parent.getAdapter().getItemCount();
		int childIndex = parent.getChildAdapterPosition(view);
		int spanCount = getTotalSpan(parent);
		int spanIndex = childIndex % spanCount;

        /* INVALID SPAN */
		if (spanCount < 1) return;

		outRect.top = halfSpacing;
		outRect.bottom = halfSpacing;
		outRect.left = halfSpacing;
		outRect.right = halfSpacing;

		if (isTopEdge(childIndex, spanCount)) {
			outRect.top = marginPx;
		}

		if (isLeftEdge(spanIndex)) {
			outRect.left = marginPx;
		}

		if (isRightEdge(spanIndex, spanCount)) {
			outRect.right = marginPx;
		}

		if (isBottomEdge(childIndex, childCount, spanCount, spanIndex)) {
			outRect.bottom = marginPx;
		}
	}

	protected int getTotalSpan(RecyclerView parent) {

		RecyclerView.LayoutManager mgr = parent.getLayoutManager();
		if (mgr instanceof GridLayoutManager) {
			return ((GridLayoutManager) mgr).getSpanCount();
		} else if (mgr instanceof StaggeredGridLayoutManager) {
			return ((StaggeredGridLayoutManager) mgr).getSpanCount();
		}

		return -1;
	}

	protected boolean isLeftEdge(int spanIndex) {
		return spanIndex == 0;
	}

	protected boolean isRightEdge(int spanIndex, int spanCount) {
		return spanIndex == spanCount - 1;
	}

	protected boolean isTopEdge(int childIndex, int spanCount) {
		return childIndex < spanCount;
	}

	protected boolean isBottomEdge(int childIndex, int childCount, int spanCount, int spanIndex) {
		return childIndex >= childCount - spanCount + spanIndex;
	}
}
