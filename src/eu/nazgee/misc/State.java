package eu.nazgee.misc;


public class State<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final T mItem;
	private IStateChangesListener<T> mStateChangeListener;
	// ===========================================================
	// Constructors
	// ===========================================================
	public State(State<T> pOtherState) {
		mItem = pOtherState.mItem;
		mStateChangeListener = pOtherState.mStateChangeListener;
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
	/**
	 * Gets a current listener listening for state transitions
	 * @return
	 */
	public IStateChangesListener<T> getStateChangeListener() {
		return mStateChangeListener;
	}
	/**
	 * Sets a listener listening for state transitions
	 * @return
	 */
	public void setStateChangeListener(IStateChangesListener<T> mStateChangeListener) {
		this.mStateChangeListener = mStateChangeListener;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	/**
	 * Gets called whenever a state transition occurs and {@link State} is not active anymore
	 */
	protected void onStateFinished() {
		if (mStateChangeListener != null) {
			mStateChangeListener.onStateFinished(this);
		}
	}
	/**
	 * Gets called whenever a state transition occurs and {@link State} becomes active
	 */
	protected void onStateStarted() {
		if (mStateChangeListener != null) {
			mStateChangeListener.onStateStarted(this);
		}
	}
	// ===========================================================
	// Methods
	// ===========================================================


	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public interface IStateChangesListener<T> {
		public void onStateStarted(State<T> pState);
		public void onStateFinished(State<T> pState);
	}
}
