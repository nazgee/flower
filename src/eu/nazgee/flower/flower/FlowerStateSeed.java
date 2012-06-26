package eu.nazgee.flower.flower;

public class FlowerStateSeed extends FlowerState {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	public FlowerStateSeed(final Flower pFlower) {
		super(pFlower);
	}

	public FlowerStateSeed(final FlowerState pOther) {
		super(pOther);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public FlowerState water() {
		return new FlowerStateWatered(this);
	}

	@Override
	public FlowerState sun() {
		return new FlowerStateFried(this);
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
