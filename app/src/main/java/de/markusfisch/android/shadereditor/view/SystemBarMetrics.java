package de.markusfisch.android.shadereditor.view;

//import static java.security.AccessController.getContext;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import de.markusfisch.android.shadereditor.R;
import de.markusfisch.android.shadereditor.activity.MainActivity;
import de.markusfisch.android.shadereditor.app.ShaderEditorApp;

public class SystemBarMetrics {
	public static void initSystemBars(AppCompatActivity activity) {
		initSystemBars(activity, null);
	}

	public static void initSystemBars(AppCompatActivity activity,
			Rect insets) {
		View mainLayout;
		if (activity != null &&
				(mainLayout = activity.findViewById(R.id.main_layout)) != null &&
				setSystemBarColor(
						activity.getWindow(),
						ShaderEditorApp.preferences.getSystemBarColor(),
						true)) {
			setWindowInsets(mainLayout, insets, activity);
		}
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static boolean setSystemBarColor(
			Window window,
			int color,
			boolean expand) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			return false;
		}
		if (expand) {
			window.getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
							View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
							View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		}
		window.setStatusBarColor(color);
		window.setNavigationBarColor(color);
		return true;
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static void hideNavigation(Window window) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			return;
		}
		window.getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
						View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
						View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
						View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
						View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
						View.SYSTEM_UI_FLAG_FULLSCREEN);
	}

	public static int getToolBarHeight(Context context) {
		TypedValue tv = new TypedValue();
		return context.getTheme().resolveAttribute(
				android.R.attr.actionBarSize,
				tv,
				true) ? TypedValue.complexToDimensionPixelSize(
				tv.data,
				context.getResources().getDisplayMetrics()) : 0;
	}

	private static void setWindowInsets(final View mainLayout,
			final Rect windowInsets, final AppCompatActivity activity) {
		WindowInsetsControllerCompat windowInsetsController =
				WindowCompat.getInsetsController(activity.getWindow(),
						mainLayout);
				//WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
		// Configure the behavior of the hidden system bars.
		windowInsetsController.setSystemBarsBehavior(
				WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
		);

		ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
			windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());

//			if (insets.hasSystemWindowInsets()) {
//				int left = insets.getSystemWindowInsetLeft();
//				int top = insets.getSystemWindowInsetTop();
//				int right = insets.getSystemWindowInsetRight();
//				int bottom = insets.getSystemWindowInsetBottom();
//				mainLayout.setPadding(left, top, right, bottom);
//				if (windowInsets != null) {
//					windowInsets.set(left, top, right, bottom);
//				}
//			}
			//return view.onApplyWindowInsets(windowInsets);

			return insets.consumeSystemWindowInsets();
		});
	}
}
