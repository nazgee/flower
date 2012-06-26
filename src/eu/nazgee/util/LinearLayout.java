package eu.nazgee.util;

import java.util.LinkedList;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;

public class LinearLayout extends Entity {

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
	protected LinkedList<IEntity> mItems = new LinkedList<IEntity>();
	protected eDirection mDirection;

	// ===========================================================
	// Constructors
	// ===========================================================
	public LinearLayout(final float W, final float H, final eDirection pDirection) {
		super(0, 0, W, H);
		mDirection = pDirection;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	private float getItemSizeMain(final IEntity pEntity) {
		return (mDirection == eDirection.DIR_HORIZONTAL) ? pEntity.getWidth() : pEntity.getHeight();
	}
	private float getItemSizeExtra(final IEntity pEntity) {
		return (mDirection != eDirection.DIR_HORIZONTAL) ? pEntity.getWidth() : pEntity.getHeight();
	}
	private float getLayoutSizeMain() {
		return (mDirection == eDirection.DIR_HORIZONTAL) ? getWidth() : getHeight();
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public void setItems(final boolean pStretch, final IEntity... pItems) {
		// get rid of old items
		mItems.clear();
		detachChildren();

		// attach new items
		float totalSize = 0;
		float maxSize = 0;
		for (final IEntity item : pItems) {
			mItems.add(item);
			attachChild(item);

			totalSize += getItemSizeMain(item);
			maxSize = Math.max(maxSize, getItemSizeExtra(item));
		}

		final boolean squeezingNeeded = pStretch || (getLayoutSizeMain() < totalSize);

		if (squeezingNeeded && pItems.length > 1) {
			final IEntity last = pItems[pItems.length-1];
			final float lastItemSize = getItemSizeMain(last);

			final float scale = (getLayoutSizeMain() - lastItemSize) / (totalSize - lastItemSize);
			setItemsPositions(getLayoutSizeMain(), scale);
		} else {
			setItemsPositions(totalSize, 1);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================
	protected void setItemsPositions(final float pSize, final float pPositionScale) {

		final float basePosition = 0;

//		Log.e("aaa", "W=" + W + "; baseX=" + baseX + "; scale=" + mPositionScale + "; count=" + mItems.size());

		float offset = 0;
		for (final IEntity item : mItems) {
			final float pos = basePosition + offset * pPositionScale;
			offset += getItemSizeMain(item);

			if (mDirection == eDirection.DIR_HORIZONTAL) {
				Anchor.setPosCenterLeft(item, pos, getHeight()/2);
			} else {
				Anchor.setPosBottomMiddle(item, getWidth()/2, pos);
			}
		}

	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
