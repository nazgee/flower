package eu.nazgee.flower;

import org.andengine.entity.shape.IAreaShape;

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
		return new LayoutLinear(pLayoutAnchor, pItemsAnchor, eDirection.DIR_HORIZONTAL, eRatio.MIN);
	}

	public static LayoutLinear populateHorizontalAlignedCenter(final eAnchorPointXY pLayoutAnchor, final eAnchorPointXY pItemsAnchor) {
		return new LayoutLinear(pLayoutAnchor, pItemsAnchor, eDirection.DIR_HORIZONTAL, eRatio.MIDDLE);
	}

	public static LayoutLinear populateHorizontalAlignedBottom(final eAnchorPointXY pLayoutAnchor, final eAnchorPointXY pItemsAnchor) {
		return new LayoutLinear(pLayoutAnchor, pItemsAnchor, eDirection.DIR_HORIZONTAL, eRatio.MAX);
	}

	public static LayoutLinear populateVerticalAlignedLeft(final eAnchorPointXY pLayoutAnchor, final eAnchorPointXY pItemsAnchor) {
		return new LayoutLinear(pLayoutAnchor, pItemsAnchor, eDirection.DIR_VERTICAL, eRatio.MIN);
	}

	public static LayoutLinear populateVerticalAlignedCenter(final eAnchorPointXY pLayoutAnchor, final eAnchorPointXY pItemsAnchor) {
		return new LayoutLinear(pLayoutAnchor, pItemsAnchor, eDirection.DIR_VERTICAL, eRatio.MIDDLE);
	}

	public static LayoutLinear populateVerticalAlignedRight(final eAnchorPointXY pLayoutAnchor, final eAnchorPointXY pItemsAnchor) {
		return new LayoutLinear(pLayoutAnchor, pItemsAnchor, eDirection.DIR_VERTICAL, eRatio.MAX);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	protected void calculateLayout() {
		mItemsWidth = 0;
		mItemsHeight = 0;

		for (IAreaShape item : mItems) {
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
	public void setItems(IAreaShape... pItems) {
		super.setItems(pItems);
		calculateLayout();
		mPositionScale = 1;
		setItemsPositions(mItemsWidth, mItemsHeight);
	}

	public void setItems(final float pDesiredSize, IAreaShape... pItems) {
		setItems(pDesiredSize, true, pItems);
	}

	public void setItems(final float pDesiredSize, final boolean pStretch, IAreaShape... pItems) {
		super.setItems(pItems);
		calculateLayout();
		boolean squeezingNeeded = pStretch ||
				(mDirection == eDirection.DIR_HORIZONTAL) ?
				(pDesiredSize < mItemsWidth) :
				(pDesiredSize < mItemsHeight);

		if (squeezingNeeded && pItems.length > 1) {
			final IAreaShape last = pItems[pItems.length-1];
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
			for (IAreaShape item : mItems) {
				final float y = baseY + (H - item.getHeight()) * mAlignment.ratio;
				setItemPositionTopLeft(item, baseX + offset * mPositionScale, y, getItemsAnchor());
				offset += item.getWidth();
			}
		} else {
			for (IAreaShape item : mItems) {
				final float x = baseX + (W - item.getWidth()) * mAlignment.ratio;
				setItemPositionTopLeft(item, x, baseY + offset * mPositionScale, getItemsAnchor());
				offset += item.getWidth();
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
