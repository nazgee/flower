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
	public FlowerStateSeed(Flower pFlower) {
		super(pFlower);
	}

	public FlowerStateSeed(FlowerState pOther) {
		super(pOther);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public IFlowerState water() {
		return new FlowerStateWatered(this);
	}

	@Override
	public IFlowerState sun() {
		return new FlowerStateFried(this);
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
