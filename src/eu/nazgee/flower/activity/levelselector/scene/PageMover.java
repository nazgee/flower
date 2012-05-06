package eu.nazgee.flower.activity.levelselector.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.util.math.MathUtils;

import eu.nazgee.flower.pagerscene.IPage;
import eu.nazgee.flower.pagerscene.PageMoverCamera;
import eu.nazgee.flower.pagerscene.ScenePager;

public class PageMover<T extends IEntity> extends PageMoverCamera<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	public PageMover(Camera mCamera, float pStepPerPage) {
		super(mCamera, pStepPerPage);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onProgressSwipe(final ScenePager<T> pScenePager, final IPage<T> pCurrentPage, float pSwipeDistanceTotal,
			float pSwipeDistanceDelta) {
		super.onProgressSwipe(pScenePager, pCurrentPage, pSwipeDistanceTotal, pSwipeDistanceDelta);

		final float value = MathUtils.bringToBounds(0, 1, 1 - Math.abs(pSwipeDistanceTotal) / mStepPerPage);

		setPageState(pScenePager, pCurrentPage, value, pSwipeDistanceTotal);
	}

	@Override
	public void onCompletedSwipe(final ScenePager<T> pScenePager, final IPage<T> pCurrentPage, int pNewPageIndex,
			int pOldPageIndex) {
		super.onCompletedSwipe(pScenePager, pCurrentPage, pNewPageIndex, pOldPageIndex);
		setPageState(pScenePager, pCurrentPage, 1, 0);
	}
	// ===========================================================
	// Methods
	// ===========================================================
	private void setPageState(final ScenePager<T> pScenePager, final IPage<T> pPage, final float pValue, final float pDirection) {

		setPageState(pPage, pValue, pDirection);

		IPage<T> pPrev = null;
		IPage<T> pNext = null;
		if (pDirection > 0) {
			pPrev = pScenePager.getPrev(pPage);
		} else if (pDirection < 0) {
			pNext = pScenePager.getNext(pPage);
		} else {
			pPrev = pScenePager.getPrev(pPage);
			pNext = pScenePager.getNext(pPage);
		}

		setPageState(pPrev, 1-pValue, pDirection);
		setPageState(pNext, 1-pValue, pDirection);
	}

	private void setPageState(final IPage<T> pPage, final float pValue, final float pDirection) {
		if (pPage == null)
			return;

		pPage.setAlpha(MathUtils.bringToBounds(0.5f, 1, pValue));
		pPage.setScaleY(1.5f - pValue * 0.5f);
//		if (pDirection > 0) {
//			pPage.setRotation(90 - pValue * 90);
//		} else {
//			pPage.setRotation(-90 + pValue * 90);
//		}
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
