package eu.nazgee.util;

import org.andengine.entity.IEntity;

public class LayoutLinear extends LayoutBase {

	// ===========================================================
	// Constants
	// ===========================================================
	public enum eDirection {
		DIR_HORIZONTAL,
		DIR_VERTICAL
	}
	// ===========================================================
	// Fields
	// ===========================================================
	protected final eDirection mDirection;
	protected final eRatio mAlignment;
	protected float mPositionScale = 1;
	private float mItemsWidth;
	private float mItemsHeight;
	// ===========================================================
	// Constructors
	// ===========================================================
	protected LayoutLinear(final eAnchorPointXY pLayoutAnchor, final eAnchorPointXY pItemsAnchor,
			final eDirection pDirection, final eRatio pAlignment) {
		super(pLayoutAnchor, pItemsAnchor);
		this.mDirection = pDirection;
		this.mAlignment = pAlignment;
	}

	public static LayoutLinear populateHorizontalAlignedTop(final eAnchorPointXY pLayoutAnchor, final eAnchorPointXY pItemsAnchor) {
		return new LayoutLinear(pLayoutAnchor, pItemsAnchor, eDirection.DIR_HORIZONTAL, eRatio.ZERO);
	}

	public static LayoutLinear populateHorizontalAlignedCenter(final eAnchorPointXY pLayoutAnchor, final eAnchorPointXY pItemsAnchor) {
		return new LayoutLinear(pLayoutAnchor, pItemsAnchor, eDirection.DIR_HORIZONTAL, eRatio.HALF);
	}

	public static LayoutLinear populateHorizontalAlignedBottom(final eAnchorPointXY pLayoutAnchor, final eAnchorPointXY pItemsAnchor) {
		return new LayoutLinear(pLayoutAnchor, pItemsAnchor, eDirection.DIR_HORIZONTAL, eRatio.ONE);
	}

	public static LayoutLinear populateVerticalAlignedLeft(final eAnchorPointXY pLayoutAnchor, final eAnchorPointXY pItemsAnchor) {
		return new LayoutLinear(pLayoutAnchor, pItemsAnchor, eDirection.DIR_VERTICAL, eRatio.ZERO);
	}

	public static LayoutLinear populateVerticalAlignedCenter(final eAnchorPointXY pLayoutAnchor, final eAnchorPointXY pItemsAnchor) {
		return new LayoutLinear(pLayoutAnchor, pItemsAnchor, eDirection.DIR_VERTICAL, eRatio.HALF);
	}

	public static LayoutLinear populateVerticalAlignedRight(final eAnchorPointXY pLayoutAnchor, final eAnchorPointXY pItemsAnchor) {
		return new LayoutLinear(pLayoutAnchor, pItemsAnchor, eDirection.DIR_VERTICAL, eRatio.ONE);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	protected void calculateLayout() {
		mItemsWidth = 0;
		mItemsHeight = 0;

		for (IEntity item : mItems) {
			if (mDirection == eDirection.DIR_HORIZONTAL) {
				mItemsWidth += item.getWidth();
				mItemsHeight = item.getHeight() > mItemsHeight ? item.getHeight() : mItemsHeight;
			} else {
				mItemsWidth  = item.getWidth() > mItemsWidth ? item.getWidth() : mItemsWidth;
				mItemsHeight+= item.getHeight();
			}
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void setItems(IEntity... pItems) {
		super.setItems(pItems);
		calculateLayout();
		mPositionScale = 1;
		setItemsPositions(mItemsWidth, mItemsHeight);
	}

	public void setItems(final float pDesiredSize, IEntity... pItems) {
		setItems(pDesiredSize, true, pItems);
	}

	public void setItems(final float pDesiredSize, final boolean pStretch, IEntity... pItems) {
		super.setItems(pItems);
		calculateLayout();
		boolean squeezingNeeded = pStretch ||
				(mDirection == eDirection.DIR_HORIZONTAL) ?
				(pDesiredSize < mItemsWidth) :
				(pDesiredSize < mItemsHeight);

		if (squeezingNeeded && pItems.length > 1) {
			final IEntity last = pItems[pItems.length-1];
			final float lastW = last.getWidth();
			final float lastH = last.getWidth();
	
			mPositionScale = (mDirection == eDirection.DIR_HORIZONTAL) ?
					(pDesiredSize - lastW) / (mItemsWidth - lastW) :
					(pDesiredSize - lastH) / (mItemsHeight - lastH);

			if (mDirection == eDirection.DIR_HORIZONTAL) {
				mItemsWidth = pDesiredSize;
			} else {
				mItemsHeight = pDesiredSize;
			}
			setItemsPositions(mItemsWidth, mItemsHeight);
		} else {
			mPositionScale = 1;
			setItemsPositions(mItemsWidth, mItemsHeight);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================
	protected void setItemsPositions(float W, float H) {

		final float baseX = -getAnchor().x.getValue(W);
		final float baseY = -getAnchor().y.getValue(H);

//		Log.e("aaa", "W=" + W + "; baseX=" + baseX + "; scale=" + mPositionScale + "; count=" + mItems.size());

		float offset = 0;
		if (mDirection == eDirection.DIR_HORIZONTAL) {
			for (IEntity item : mItems) {
				final float y = baseY + (H - item.getHeight()) * mAlignment.ratio;
				setPositionTopLeft(item, baseX + offset * mPositionScale, y);
				offset += item.getWidth();
			}
		} else {
			for (IEntity item : mItems) {
				final float x = baseX + (W - item.getWidth()) * mAlignment.ratio;
				setPositionTopLeft(item, x, baseY + offset * mPositionScale);
				offset += item.getWidth();
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
