package eu.nazgee.flower.pagerscene;

import java.util.LinkedList;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class PageRectangle<T extends IEntity> extends Rectangle implements IPage<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private LinkedList<T> mItems = new LinkedList<T>();
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
	public void setItems(LinkedList<T> pItems) {
		mItems.clear();
		mLayout.layoutItems(pItems);
		for (T item : pItems) {
			attachChild(item);
			mItems.add(item);
		}
	}

	@Override
	public LinkedList<T> getItems() {
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
