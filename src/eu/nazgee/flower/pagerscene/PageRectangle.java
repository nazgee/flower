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
