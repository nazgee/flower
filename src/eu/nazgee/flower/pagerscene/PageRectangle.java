package eu.nazgee.flower.pagerscene;

import java.util.LinkedList;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class PageRectangle extends Rectangle implements IPage {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private LinkedList<IEntity> mItems = new LinkedList<IEntity>();
	private final ILayout mLayout;
	// ===========================================================
	// Constructors
	// ===========================================================
	public PageRectangle(float pX, float pY, float pWidth, float pHeight,
			VertexBufferObjectManager pVertexBufferObjectManager,
			ILayout pLayout) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
		mLayout = pLayout;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public void setItems(IEntity... pItems) {
		mItems.clear();
		mLayout.layoutItems(pItems);
		for (IEntity item : pItems) {
			attachChild(item);
			mItems.add(item);
//			ITouchArea
//
//			Rectangle box = new Rectangle(startX + boxX, boxY, 100, 100,
//					getVertexBufferObjectManager()) {
//				SelectDetector selectDetector = new SelectDetector();
//				ClickDetector clickDetector = new ClickDetector(selectDetector);
//
//				@Override
//				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
//						float pTouchAreaLocalX, float pTouchAreaLocalY) {
//					boolean ret = clickDetector.onTouchEvent(pSceneTouchEvent);
//					Log.d(getClass().getSimpleName(), "area_handled=" + ret);
//					return ret;
//				}
//
//				class SelectDetector implements IClickDetectorListener {
//					@Override
//					public void onClick(
//							org.andengine.input.touch.detector.ClickDetector pClickDetector,
//							int pPointerID, float pSceneX, float pSceneY) {
//						loadLevel(levelToLoad);
//					}
//				}
//			};
		}
	}

	@Override
	public LinkedList<IEntity> getItems() {
		return mItems;
	}

	@Override
	public int getCapacity() {
		return mLayout.getCapacity();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
