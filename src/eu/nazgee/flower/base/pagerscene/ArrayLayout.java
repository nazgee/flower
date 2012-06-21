package eu.nazgee.flower.base.pagerscene;

import java.util.LinkedList;

import org.andengine.entity.IEntity;

import android.util.Log;
import eu.nazgee.util.Anchor;

public class ArrayLayout implements ILayout {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final int mRows;
	private final int mCols;
	private final float mW;
	private final float mH;
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public ArrayLayout(int mRows, int mCols, float mW, float mH) {
		super();
		this.mRows = mRows;
		this.mCols = mCols;
		this.mW = mW;
		this.mH = mH;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void layoutItems(LinkedList<? extends IEntity> pItems) {
		final int items = pItems.size();
		int i = 0;

		final float cw = mW/mCols;
		final float rh = mH/mRows;
		for (int r = 0; r < mRows; r++) {
			for (int c = 0; c < mCols; c++) {
				i = r*mCols + c;
				if (i >= items) {
					break;
				}
				final float baseX = c * cw;
				final float baseY = mH - r * rh;
				Anchor.setPosTopLeft(pItems.get(i), baseX, baseY);
				Log.d("layout", "x=" + pItems.get(i).getX() + " y=" + pItems.get(i).getY());
			}
		}

		if ((i + 1) < items) {
			Log.e(getClass().getSimpleName(), "Too many items given!");
		}
	}

	@Override
	public int getCapacity() {
		return mRows * mCols;
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
