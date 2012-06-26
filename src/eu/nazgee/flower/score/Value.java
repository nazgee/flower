package eu.nazgee.flower.score;

import org.andengine.util.call.Callback;
import org.andengine.util.math.MathUtils;

public class Value {
	private int mValue;
	private final int mMax;
	private final int mMin;
	private Callback<Value> mCallback;

	public Value() {
		this(0, Integer.MAX_VALUE, 0);
	}

	public Value(final int mValue) {
		this(mValue, Integer.MAX_VALUE, 0);
	}

	public Value(final int mValue, final int max, final int min) {
		this.set(mValue);
		mMax = max;
		mMin = min;
	}

	public int get() {
		return mValue;
	}

	public void set(final int pValue) {
		this.mValue = pValue;
		clamp();
		triggerCallback();
	}

	public int inc(final int pValue) {
		mValue += pValue;
		clamp();
		triggerCallback();
		return mValue;
	}

	public int dec(final int pValue) {
		mValue -= pValue;
		clamp();
		triggerCallback();
		return mValue;
	}

	public Callback<Value> getCallbackOnChanged() {
		return mCallback;
	}

	public void setCallbackOnChanged(final Callback<Value> mCallback) {
		this.mCallback = mCallback;
	}

	private void triggerCallback() {
		if (getCallbackOnChanged() != null) {
			getCallbackOnChanged().onCallback(this);
		}
	}
	protected void clamp() {
		mValue = MathUtils.bringToBounds(mMin, mMax, mValue);
	}
}
