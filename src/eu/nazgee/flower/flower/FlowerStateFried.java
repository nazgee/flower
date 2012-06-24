package eu.nazgee.flower.flower;

public class FlowerStateFried extends FlowerState {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	public FlowerStateFried(Flower pFlower) {
		super(pFlower);
	}

	public FlowerStateFried(FlowerState pOther) {
		super(pOther);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	protected void onStateStarted() {
		super.onStateStarted();
		getItem().animateFry();
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
