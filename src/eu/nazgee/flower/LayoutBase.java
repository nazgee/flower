package eu.nazgee.flower;

import java.util.LinkedList;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.shape.IAreaShape;



public class LayoutBase extends Entity {
	// ===========================================================
	// Constants
	// ===========================================================
	public enum eRatio {
		MIN(0),
		MIDDLE(0.5f),
		MAX(1);

		public final float ratio;
		public float getValue(final float pSize) {
			return ratio * pSize;
		}

		private eRatio(float align) {
			this.ratio = align;
		}
	}

	public enum eAnchorPointXY {
		TOP_LEFT(eRatio.MIN, eRatio.MIN),
		TOP_MIDDLE(eRatio.MIDDLE, eRatio.MIN),
		TOP_RIGHT(eRatio.MAX, eRatio.MIN),
		CENTERED_LEFT(eRatio.MIN, eRatio.MIDDLE),
		CENTERED(eRatio.MIDDLE, eRatio.MIDDLE),
		CENTERED_RIGHT(eRatio.MAX, eRatio.MIDDLE),
		BOTTOM_LEFT(eRatio.MIN, eRatio.MAX),
		BOTTOM_MIDDLE(eRatio.MIDDLE, eRatio.MAX),
		BOTTOM_RIGHT(eRatio.MAX, eRatio.MAX);

		public final eRatio x;
		public final eRatio y;

		private eAnchorPointXY(eRatio pAnchorPointX, eRatio pAnchorPointY) {
			x = pAnchorPointX;
			y = pAnchorPointY;
		}
	}
	// ===========================================================
	// Fields
	// ===========================================================
	protected LinkedList<IAreaShape> mItems = new LinkedList<IAreaShape>();
	private final eAnchorPointXY mAnchorPoint;
	private final eAnchorPointXY mItemsAnchor;
	// ===========================================================
	// Constructors
	// ===========================================================
	public LayoutBase(final eAnchorPointXY pLayoutAnchor, final eAnchorPointXY pItemsAnchor) {
		this.mAnchorPoint = pLayoutAnchor;
		this.mItemsAnchor = pItemsAnchor;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public eAnchorPointXY getAnchor() {
		return mAnchorPoint;
	}

	public eAnchorPointXY getItemsAnchor() {
		return mItemsAnchor;
	}

	public void setItems(IAreaShape ... pItems) {
		mItems.clear();
		detachChildren();

		for (IAreaShape item : pItems) {
			mItems.add(item);
			attachChild(item);
		}

		buildLayout();
	}

	/**
	 * Sets a position of a given item taking into account given anchor point
	 * 
	 * @param pItem item to be placed
	 * @param x position in a TOP-LEFT coorfinates
	 * @param y position in a TOP-LEFT coorfinates
	 * @param pAnchor anchor point of given item
	 */
	public static void setItemPositionTopLeft(final IAreaShape pItem, final float x, final float y, final eAnchorPointXY pAnchor) {
		final float offX = pAnchor.x.getValue(pItem.getWidth());
		final float offY = pAnchor.y.getValue(pItem.getHeight());

		pItem.setPosition(x + offX, y + offY);
	}

	public static void setItemPositionBottomRight(final IAreaShape pItem, final float x, final float y, final eAnchorPointXY pAnchor) {
		final float offX = pAnchor.x.getValue(pItem.getWidth()) - pItem.getWidth();
		final float offY = pAnchor.y.getValue(pItem.getHeight()) - pItem.getHeight();

		pItem.setPosition(x + offX, y + offY);
	}

	public static void setItemPositionCenter(final IAreaShape pItem, final float x, final float y, final eAnchorPointXY pAnchor) {
		final float offX = pAnchor.x.getValue(pItem.getWidth()) - pItem.getWidth()/2;
		final float offY = pAnchor.y.getValue(pItem.getHeight()) - pItem.getHeight()/2;

		pItem.setPosition(x + offX, y + offY);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	protected void buildLayout() {
		
	}
	// ===========================================================
	// Methods
	// ===========================================================



	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}