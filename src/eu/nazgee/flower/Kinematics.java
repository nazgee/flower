package eu.nazgee.flower;

import android.util.FloatMath;

public class Kinematics {
	public static float time(final float accel, final float distance) {
		return FloatMath.sqrt(2*Math.abs(distance/accel));
	}
	public static float distance(final float accel, final float time) {
		return accel*time*time/2;
	}
}
