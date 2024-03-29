package eu.nazgee.util;

import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.util.modifier.ease.EaseStrongIn;
import org.andengine.util.modifier.ease.EaseStrongInOut;
import org.andengine.util.modifier.ease.EaseStrongOut;

public class ModifiersFactory {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	private ModifiersFactory() {

	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public static IEntityModifier shakeYourHead(final int mCount, final float mDuration,
			final float mAngle) {
		final IEntityModifier mod = new LoopEntityModifier(
				new SequenceEntityModifier(
						new RotationModifier(mDuration/4, 0,        -mAngle/2, EaseStrongOut.getInstance()),
						new RotationModifier(mDuration/2, -mAngle/2, mAngle,    EaseStrongInOut.getInstance()),
						new RotationModifier(mDuration/4, mAngle,    0,        EaseStrongIn.getInstance())
						), mCount);
		return mod;
	}

	public static IEntityModifier nodYourHead(final int mCount, final float mDuration,
			final float mFrom, final float mTo) {
		final IEntityModifier mod = new LoopEntityModifier(
				new SequenceEntityModifier(
						new ScaleModifier(mDuration/2, mFrom, mTo, EaseStrongOut.getInstance()),
						new ScaleModifier(mDuration/2, mTo, mFrom, EaseStrongOut.getInstance())
						), mCount);
		return mod;
	}
}
