package eu.nazgee.flower.pagerscene;

import java.util.LinkedList;

import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.RotationByModifier;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

import android.content.Context;
import eu.nazgee.game.utils.scene.SceneLoadable;

abstract public class ScenePager extends SceneLoadable implements IOnSceneTouchListener, IScrollDetectorListener, IOnAreaTouchListener, IClickDetectorListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private final int mTurnPageThreshold;
	// ===========================================================
	// Fields
	// ===========================================================
	private IItemClikedListener mItemClikedListener;
	private final ClickDetector mClickDetector = new ClickDetector(100, this);
	private final SurfaceScrollDetector mSurfaceScrollDetector = new SurfaceScrollDetector(this);
	private int mScrollDistanceX;
	private LinkedList<IPage> mPages;
	private IPageMover mPageMover;

	private int mCurrentPage;

	// ===========================================================
	// Constructors
	// ===========================================================
	public ScenePager(float W, float H,
			VertexBufferObjectManager pVertexBufferObjectManager,
			final int pTurnPageThreshold) {
		super(W, H, pVertexBufferObjectManager);
		mTurnPageThreshold = pTurnPageThreshold;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public IPageMover getPageMover() {
		return mPageMover;
	}

	public void setPageMover(IPageMover mPageMover) {
		this.mPageMover = mPageMover;
	}
	public IItemClikedListener getItemClikedListener() {
		return mItemClikedListener;
	}
	public void setItemClikedListener(IItemClikedListener mItemClikedListener) {
		this.mItemClikedListener = mItemClikedListener;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	abstract protected IEntity populateItem(int pItem, int pItemOnPage, int pPage);
	abstract protected IPage populatePage(int pPageNumber);
	abstract protected void attachPage(final IPage pPage, int pPageNumber);
	abstract protected int getItemsNumber();

	public interface IItemClikedListener {
		void onItemClicked(IEntity pItem);
	}

	@Override
	public void onLoadResources(Engine e, Context c) {

	}

	@Override
	public void onLoad(Engine e, Context c) {
		setOnAreaTouchListener(this);
		setOnSceneTouchListener(this);
		setOnSceneTouchListenerBindingOnActionDownEnabled(true);
		mPages = preparePages();
		for (IPage page : mPages) {
			for (IEntity item : page.getItems()) {
				registerTouchArea((ITouchArea) item);
			}
		}
	}

	@Override
	public void onUnload() {
		for (IPage page : mPages) {
			for (IEntity item : page.getItems()) {
				unregisterTouchArea((ITouchArea) item);
			}
		}
	}

	IEntity mCurrentlyTouchedItem;
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {

		if (mPages.get(mCurrentPage).getItems().contains(pTouchArea)) {
			boolean ret = false;

			mCurrentlyTouchedItem = (IEntity) pTouchArea;
			ret = mClickDetector.onTouchEvent(pSceneTouchEvent);

			return ret;
		}
		return false;
	}

	@Override
	public void onClick(ClickDetector pClickDetector, int pPointerID,
			float pSceneX, float pSceneY) {
		mCurrentlyTouchedItem.registerEntityModifier(new RotationByModifier(1, 180));
		if (getItemClikedListener() != null) {
			getItemClikedListener().onItemClicked(mCurrentlyTouchedItem);
		}
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		return mSurfaceScrollDetector.onTouchEvent(pSceneTouchEvent);
	}

	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		mScrollDistanceX = 0;
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		mScrollDistanceX += pDistanceX;
		if (mPageMover != null) {
			mPageMover.onProgressSwipe(this, mPages.get(mCurrentPage), mScrollDistanceX, pDistanceX);
		}
	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {

		int oldPage = mCurrentPage;

		if ((mScrollDistanceX > mTurnPageThreshold) && (mCurrentPage > 0)) {
			mCurrentPage--;
		} else if ((mScrollDistanceX < -mTurnPageThreshold)
				&& (mCurrentPage < mPages.size() - 1)) {
			mCurrentPage++;
		}
		mCurrentPage = MathUtils.bringToBounds(0, mPages.size(), mCurrentPage);
		if (mPageMover != null) {
			mPageMover.onCompletedSwipe(this, mPages.get(mCurrentPage), mCurrentPage, oldPage);
		}
	}
	// ===========================================================
	// Methods
	// ===========================================================
	public IPage getPrev(final IPage pPage) {
		int idx = mPages.indexOf(pPage);
		if (idx <= 0)
			return null;
		return mPages.get(idx - 1);
	}

	public IPage getNext(final IPage pPage) {
		int idx = mPages.indexOf(pPage);
		if (idx < 0 || idx+1 == mPages.size())
			return null;
		return mPages.get(idx + 1);
	}

	private LinkedList<IPage> populatePages(int pMinCapacity) {
		LinkedList<IPage> pages = new LinkedList<IPage>();

		int pages_capacity = 0;
		do {
			pages.add(populatePage(pages.size()));
			pages_capacity += pages.getLast().getCapacity();
		} while (pages_capacity < pMinCapacity);

		return pages;
	}

	private LinkedList<IPage> preparePages() {
		LinkedList<IPage> pages = populatePages(getItemsNumber());

		final int total_to_load = getItemsNumber();
		int total_loaded = 0;
		int current_page = 0;

		for (IPage page : pages) {
			final int left_to_load = total_to_load - total_loaded;
			int this_page_to_load = Math.min(page.getCapacity(), left_to_load);

			IEntity items[] = new IEntity[this_page_to_load];
	
			for (int i = 0; i < this_page_to_load; i++) {
				items[i] = populateItem(total_loaded, i, current_page);
				total_loaded++;
			}

			page.setItems(items);
			attachPage(page, current_page);

			current_page++;
		}

		return pages;
	}

}
