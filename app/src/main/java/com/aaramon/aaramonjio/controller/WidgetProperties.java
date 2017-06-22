package com.aaramon.aaramonjio.controller;

import android.content.Context;
import android.graphics.Typeface;

public class WidgetProperties {
	private static Typeface tfRobotoRegular;
	private static Typeface tfRobotoMedium;
	private static Typeface tfRobotoLight;
	private static Typeface tfRobotoBold;
	private static Typeface tfRocketDiner;

	public static Typeface setTextTypefaceRobotoRegular(Context context) {

		if (tfRobotoRegular == null) {
			tfRobotoRegular = Typeface.createFromAsset(context.getAssets(),
					"Roboto-Regular_2.ttf");
		}
		return tfRobotoRegular;
	}

	public static Typeface setTextTypefaceRobotoBold(Context context) {

		if (tfRobotoBold == null) {
			tfRobotoBold = Typeface.createFromAsset(context.getAssets(),
					"Roboto-Bold_2.ttf");
		}
		return tfRobotoBold;
	}

	public static Typeface setTextTypefaceRobotoLight(Context context) {

		if (tfRobotoLight == null) {
			tfRobotoLight = Typeface.createFromAsset(context.getAssets(),
					"Roboto-Light_2.ttf");
		}
		return tfRobotoLight;
	}

	public static Typeface setTextTypefaceRobotoMedium(Context context) {

		if (tfRobotoMedium == null) {
			tfRobotoMedium = Typeface.createFromAsset(context.getAssets(),
					"Roboto-Medium_2.ttf");
		}
		return tfRobotoMedium;
	}

	public static Typeface setTextTypefaceRocketDiner(Context context) {

		if (tfRocketDiner == null) {
			tfRocketDiner = Typeface.createFromAsset(context.getAssets(),
					"RocketDinerCondensed_0.otf");
		}
		return tfRocketDiner;
	}

}
