package eu.nazgee.flower;

import org.andengine.entity.primitive.Rectangle;
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
	protected final float mPositionScale;
	protected final eRatio mAlignment;
	// ===========================================================
	// Constructors
	// ===========================================================
	protected LayoutLinear(final eAnchorPointXY pLayoutAnchor, final eAnchorPointXY pItemsAnchor,
			final eDirection pDirection, final float pPositionScale, final eRatio pAlignment) {
		super(pLayoutAnchor, pItemsAnchor);
		this.mDirection = pDirection;
		this.mPositionScale = pPositionScale;
		this.mAlignment = pAlignment;
	}

	public static LayoutLinear populateHorizontalAlignedTop(final eAnchorPointXY pLayoutAnchor, final eAnchorPointXY pItemsAnchor, final float pPositionScale) {
		return new LayoutLinear(pLayoutAnchor, pItemsAnchor, eDirection.DIR_HORIZONTAL, pPositionScale, eRatio.MIN);
	}

	public static LayoutLinear populateHorizontalAlignedCenter(final eAnchorPointXY pLayoutAnchor, final eAnchorPointXY pItemsAnchor, final float pPositionScale) {
		return new LayoutLinear(pLayoutAnchor, pItemsAnchor, eDirection.DIR_HORIZONTAL, pPositionScale, eRatio.MIDDLE);
	}

	public static LayoutLinear populateHorizontalAlignedBottom(final eAnchorPointXY pLayoutAnchor, final eAnchorPointXY pItemsAnchor, final float pPositionScale) {
		return new LayoutLinear(pLayoutAnchor, pItemsAnchor, eDirection.DIR_HORIZONTAL, pPositionScale, eRatio.MAX);
	}

	public static LayoutLinear populateVerticalAlignedLeft(final eAnchorPointXY pLayoutAnchor, final eAnchorPointXY pItemsAnchor, final float pPositionScale) {
		return new LayoutLinear(pLayoutAnchor, pItemsAnchor, eDirection.DIR_VERTICAL, pPositionScale, eRatio.MIN);
	}

	public static LayoutLinear populateVerticalAlignedCenter(final eAnchorPointXY pLayoutAnchor, final eAnchorPointXY pItemsAnchor, final float pPositionScale) {
		return new LayoutLinear(pLayoutAnchor, pItemsAnchor, eDirection.DIR_VERTICAL, pPositionScale, eRatio.MIDDLE);
	}

	public static LayoutLinear populateVerticalAlignedRight(final eAnchorPointXY pLayoutAnchor, final eAnchorPointXY pItemsAnchor, final float pPositionScale) {
		return new LayoutLinear(pLayoutAnchor, pItemsAnchor, eDirection.DIR_VERTICAL, pPositionScale, eRatio.MAX);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	@Override
	protected void buildLayout() {
		super.buildLayout();

		float width = 0;
		float height = 0;

		for (IAreaShape item : mItems) {
			if (mDirection == eDirection.DIR_HORIZONTAL) {
				width += item.getWidth();
				height = item.getHeight() > height ? item.getHeight() : height;
			} else {
				width  = item.getWidth() > width ? item.getWidth() : width;
				height+= item.getHeight();
			}
		}

		setItemsPositions(width, height);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	// ===========================================================
	// Methods
	// ===========================================================
	protected void setItemsPositions(float W, float H) {

		if (mDirection == eDirection.DIR_HORIZONTAL) {
			W *= mPositionScale;
		} else {
			H *= mPositionScale;
		}

		float baseX = -getAnchor().x.getValue(W);
		float baseY = -getAnchor().y.getValue(H);


		
		if (mDirection == eDirection.DIR_HORIZONTAL) {
			for (IAreaShape item : mItems) {
				float y = baseY + (H - item.getHeight()) * mAlignment.ratio;
				setItemPositionTopLeft(item, baseX, y, getItemsAnchor());
				baseX += item.getWidth() * mPositionScale;
			}
		} else {
			for (IAreaShape item : mItems) {
				float x = baseX + (W - item.getWidth()) * mAlignment.ratio;
				setItemPositionTopLeft(item, x, baseY, getItemsAnchor());
				baseY += item.getHeight() * mPositionScale;
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
