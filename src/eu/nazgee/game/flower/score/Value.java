package eu.nazgee.game.flower.score;

import org.andengine.util.call.Callback;
import org.andengine.util.math.MathUtils;

public class Value {
	private int mValue;
	private int mMax;
	private int mMin;
	private Callback<Value> mCallback;

	public Value() {
		this(0, Integer.MAX_VALUE, 0);
	}

	public Value(int mValue) {
		this(mValue, Integer.MAX_VALUE, 0);
	}

	public Value(int mValue, int max, int min) {
		this.set(mValue);
		mMax = max;
		mMin = min;
	}

	public int get() {
		return mValue;
	}

	public void set(int pValue) {
		this.mValue = pValue;
		clamp();
		triggerCallback();
	}

	public int inc(int pValue) {
		mValue += pValue;
		clamp();
		triggerCallback();
		return mValue;
	}

	public int dec(int pValue) {
		mValue -= pValue;
		clamp();
		triggerCallback();
		return mValue;
	}

	public Callback<Value> getCallbackOnChanged() {
		return mCallback;
	}

	public void setCallbackOnChanged(Callback<Value> mCallback) {
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
