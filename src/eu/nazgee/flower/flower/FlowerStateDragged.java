package eu.nazgee.flower.flower;

import android.util.Log;
import eu.nazgee.flower.activity.game.scene.game.Sky;

public class FlowerStateDragged extends FlowerState {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final FlowerState mOldState;

	// ===========================================================
	// Constructors
	// ===========================================================
	public FlowerStateDragged(Flower pFlower) {
		super(pFlower);
		mOldState = null;
	}

	public FlowerStateDragged(FlowerState pOther) {
		super(pOther);
		mOldState = pOther;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public FlowerState drop(Sky pSky) {
		getItem().animateDropToGround(pSky);
//		Log.w(getClass().getSimpleName(), "drop " + getItem());
		return mOldState;
	}

	@Override
	public FlowerState drag() {
		return this;
	}

	@Override
	protected void onStateStarted() {
		super.onStateStarted();
//		Log.w(getClass().getSimpleName(), "drag " + getItem());
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
