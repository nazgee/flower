package eu.nazgee.flower.pagerscene;

// ===========================================================
// Inner and Anonymous Classes
// ===========================================================
public interface IPageMover {
	void onProgressSwipe(final IPage pCurrentPage, final float pSwipeDistanceTotal, final float pSwipeDistanceDelta);
	void onCompletedSwipe(final IPage pCurrentPage, final int pNewPageIndex, final int pOldPageIndex);
}