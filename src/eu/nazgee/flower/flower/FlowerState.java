package eu.nazgee.flower.flower;

import eu.nazgee.misc.State;

public class FlowerState extends State<Flower> implements IFlowerState {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	protected FlowerState(Flower pItem) {
		super(pItem);
	}
	protected FlowerState(State<Flower> pOtherState) {
		super(pOtherState);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	protected void onStateFinished() {	
	}

	@Override
	protected void onStateStarted() {	
	}

	@Override
	public IFlowerState water() {
		return this;
	}

	@Override
	public IFlowerState sun() {
		return this;
	}

	@Override
	public IFlowerState drag() {
		return this;
	}

	@Override
	public IFlowerState drop() {
		return this;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
