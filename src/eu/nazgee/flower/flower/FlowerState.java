package eu.nazgee.flower.flower;

import android.util.Log;
import eu.nazgee.flower.activity.game.scene.game.Sky;
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
		super.onStateFinished();
	}

	@Override
	protected void onStateStarted() {	
		super.onStateStarted();
	}

	@Override
	public FlowerState water() {
		return this;
	}

	@Override
	public FlowerState sun() {
		return this;
	}

	@Override
	public FlowerState drag() {
		return new FlowerStateDragged(this);
	}

	@Override
	public FlowerState drop(Sky pSky) {
		return this;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
