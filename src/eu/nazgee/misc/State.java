package eu.nazgee.misc;


public abstract class State<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final T mItem;
	// ===========================================================
	// Constructors
	// ===========================================================
	public State(State<T> pOtherState) {
		mItem = pOtherState.getItem();
		pOtherState.onStateFinished();
		onStateStarted();
	}

	public State(T pItem) {
		mItem = pItem;
		onStateStarted();
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * Retrieves an instance of an item with which this state is associated
	 * @return
	 */
	public T getItem() {
		return mItem;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	/**
	 * Gets called whenever a state transition occurs and {@link State} is not active anymore
	 */
	abstract protected void onStateFinished();
	/**
	 * Gets called whenever a state transition occurs and {@link State} becomes active
	 */
	abstract protected void onStateStarted();
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
