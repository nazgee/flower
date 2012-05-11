package eu.nazgee.flower.base.pagerscene;

import org.andengine.entity.IEntity;

// ===========================================================
// Inner and Anonymous Classes
// ===========================================================
public interface IPageMover<T extends IEntity> {
	void onProgressSwipe(final ScenePager<T> pScenePager, final IPage<T> pCurrentPage, final float pSwipeDistanceTotal, final float pSwipeDistanceDelta);
	void onCompletedSwipe(final ScenePager<T> pScenePager, final IPage<T> pCurrentPage, final int pNewPageIndex, final int pOldPageIndex);
}