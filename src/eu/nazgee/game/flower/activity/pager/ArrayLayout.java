package eu.nazgee.game.flower.activity.pager;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;

import android.util.Log;

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
	private final eAnchorPointXY mAnchorPoint;

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public ArrayLayout(int mRows, int mCols, float mW, float mH) {
		this(mRows, mCols, mW, mH, eAnchorPointXY.CENTERED);
	}

	public ArrayLayout(int mRows, int mCols, float mW, float mH, eAnchorPointXY pAnchorPoint) {
		super();
		this.mRows = mRows;
		this.mCols = mCols;
		this.mW = mW;
		this.mH = mH;
		this.mAnchorPoint = pAnchorPoint;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void layoutItems(IEntity... pItems) {
		final int items = pItems.length;
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
				final float baseY = r * rh;
				final float offX = mAnchorPoint.x.mul * cw / mAnchorPoint.x.div;
				final float offY = mAnchorPoint.y.mul * rh / mAnchorPoint.y.div;
				pItems[i].setPosition(baseX + offX, baseY + offY);
			}
		}

		if (i < items) {
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
	public enum eAnchorPoint {
		MIN(0, 2),
		MIDDLE(1, 2),
		MAX(2, 2);

		public final float mul;
		public final float div;

		private eAnchorPoint(float multiplier, float divider) {
			this.mul = multiplier;
			this.div = divider;
		}
	}
	public enum eAnchorPointXY {
		TOP_LEFT(eAnchorPoint.MIN, eAnchorPoint.MIN),
		TOP_RIGHT(eAnchorPoint.MIDDLE, eAnchorPoint.MIN),
		TOP_MIDDLE(eAnchorPoint.MAX, eAnchorPoint.MIN),
		CENTERED_LEFT(eAnchorPoint.MIN, eAnchorPoint.MIDDLE),
		CENTERED(eAnchorPoint.MIDDLE, eAnchorPoint.MIDDLE),
		CENTERED_RIGHT(eAnchorPoint.MAX, eAnchorPoint.MIDDLE),
		BOTTOM_LEFT(eAnchorPoint.MIN, eAnchorPoint.MAX),
		BOTTOM_RIGHT(eAnchorPoint.MIDDLE, eAnchorPoint.MAX),
		BOTTOM_MIDDLE(eAnchorPoint.MAX, eAnchorPoint.MAX);

		public eAnchorPoint x;
		public eAnchorPoint y;

		private eAnchorPointXY(eAnchorPoint pAnchorPointX, eAnchorPoint pAnchorPointY) {
			x = pAnchorPointX;
			y = pAnchorPointY;
		}
	}

}
