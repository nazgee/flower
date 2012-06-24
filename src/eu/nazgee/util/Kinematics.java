package eu.nazgee.util;

import android.util.FloatMath;

public class Kinematics {
	public static float GRAVITY_WATER_ACCEL = 250;
	public static float GRAVITY_SEED_ACCEL = GRAVITY_WATER_ACCEL * 2;

	public static float time(final float accel, final float distance) {
		return FloatMath.sqrt(2*Math.abs(distance/accel));
	}
	public static float distance(final float accel, final float time) {
		return accel*time*time/2;
	}
}
