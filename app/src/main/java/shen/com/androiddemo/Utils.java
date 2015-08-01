package shen.com.androiddemo;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by cfalc on 8/1/15.
 */
public class Utils {
	private static final String posterURL = "http://image.tmdb.org/t/p/w342";
	private static int WindowWidth = 0;

	public static String fullPosterUrl(String path) {
		return posterURL + path;
	}

	public static float getWindowWidth(Context context) {
		if (WindowWidth == 0) {
			WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			Display display = manager.getDefaultDisplay();
			Point p = new Point();
			display.getSize(p);
			WindowWidth = p.x;
		}
		return WindowWidth;
	}

	public static float getWindowHeight(Context context) {
		return getWindowWidth(context) * 1.5f;
	}
}
