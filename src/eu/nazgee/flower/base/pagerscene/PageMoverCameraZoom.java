package eu.nazgee.flower.base.pagerscene;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.IEntity;
import org.andengine.util.math.MathUtils;


public class PageMoverCameraZoom<T extends IEntity> extends PageMoverCamera<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mScaleWhenOffscreen;

	// ===========================================================
	// Constructors
	// ===========================================================
	public PageMoverCameraZoom(final float pScaleWhenOffscreen, SmoothCamera mCamera, float pStepPerPage) {
		super(mCamera, pStepPerPage);
		this.mScaleWhenOffscreen = pScaleWhenOffscreen;
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

		final float value = MathUtils.bringToBounds(0, 1, 1 - Math.abs(pSwipeDistanceTotal) / getStepPerPage());

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
	/**
	 * 
	 * @param pScenePager
	 * @param pPage
	 * @param pValue accepted range is (1=page is in screen center) ... (0=page is offscreen)
	 * @param pDirection
	 */
	protected void setPageState(final ScenePager<T> pScenePager, final IPage<T> pPage, final float pValue, final float pDirection) {

		setPageState(pPage, pValue, pDirection, 0);	// current page

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

		setPageState(pPrev, 1-pValue, pDirection, -1);	// previous page
		setPageState(pNext, 1-pValue, pDirection, 1);	// next page
	}

	protected void setPageState(final IPage<T> pPage, final float pValue, final float pDirection, int index) {
		if (pPage == null)
			return;

		pPage.setAlpha(MathUtils.bringToBounds(0.5f, 1, pValue));

		// we want to have scale center moved around depending on a pValue
		float pHalfWidth = pPage.getWidth()/2;
		float pHalfHeight = pPage.getHeight()/2;
		if (index > 0) {
			pPage.setScaleCenter(pHalfWidth - pHalfWidth * (1 - pValue), pHalfHeight);
		} else if (index < 0) {
			pPage.setScaleCenter(pHalfWidth + pHalfWidth * (1 - pValue), pHalfHeight);
		} else {
			if (pDirection > 0) {
				pPage.setScaleCenter(pHalfWidth - pHalfWidth * (1 - pValue), pHalfHeight);
			} else {
				pPage.setScaleCenter(pHalfWidth + pHalfWidth * (1 - pValue), pHalfHeight);
			}
		}

		// we want to get linear transition between mScaleWhenOffscreen and 1
		float a = 1 - mScaleWhenOffscreen;
		pPage.setScale(mScaleWhenOffscreen + a*pValue);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
