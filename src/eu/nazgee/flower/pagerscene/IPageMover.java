package eu.nazgee.flower.pagerscene;

// ===========================================================
// Inner and Anonymous Classes
// ===========================================================
public interface IPageMover {
	void onProgressSwipe(final ScenePager pScenePager, final IPage pCurrentPage, final float pSwipeDistanceTotal, final float pSwipeDistanceDelta);
	void onCompletedSwipe(final ScenePager pScenePager, final IPage pCurrentPage, final int pNewPageIndex, final int pOldPageIndex);
}