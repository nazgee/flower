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
	protected void restart() {

	}

	protected FlowerState createFromInstance(FlowerState pOtherState) {
		if (pOtherState instanceof FlowerStateSeed) {
			return new FlowerStateSeed(this);
		} else if (pOtherState instanceof FlowerStateDragged) {
			return new FlowerStateDragged(this);
		} else if (pOtherState instanceof FlowerStateBloomed) {
			return new FlowerStateBloomed(this);
		} else if (pOtherState instanceof FlowerStateFried) {
			return new FlowerStateFried(this);
		} else if (pOtherState instanceof FlowerStateWatered) {
			return new FlowerStateWatered(this);
		}
		return null;
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
