package eu.nazgee.flower;

import java.util.LinkedList;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.shape.IAreaShape;



public class LayoutBase extends Entity {
	// ===========================================================
	// Constants
	// ===========================================================
	public enum eAnchorPoint {
		MIN(0),
		MIDDLE(0.5f),
		MAX(1);

		public final float align;

		private eAnchorPoint(float align) {
			this.align = align;
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

		public final eAnchorPoint x;
		public final eAnchorPoint y;

		private eAnchorPointXY(eAnchorPoint pAnchorPointX, eAnchorPoint pAnchorPointY) {
			x = pAnchorPointX;
			y = pAnchorPointY;
		}
	}
	// ===========================================================
	// Fields
	// ===========================================================
	protected LinkedList<IAreaShape> mItems = new LinkedList<IAreaShape>();
	private final eAnchorPointXY mAnchorPoint;
	// ===========================================================
	// Constructors
	// ===========================================================
	public LayoutBase(final eAnchorPointXY pLayoutAnchor) {
		mAnchorPoint = pLayoutAnchor;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public eAnchorPointXY getLayoutAnchor() {
		return mAnchorPoint;
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