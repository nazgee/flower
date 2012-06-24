package eu.nazgee.flower.flower;

public class FlowerStateWatered extends FlowerState {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	public FlowerStateWatered(Flower pFlower) {
		super(pFlower);
	}

	public FlowerStateWatered(FlowerState pOther) {
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
		getItem().animateWater();
	}

	@Override
	public FlowerState sun() {
		return new FlowerStateBloomed(this);
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
