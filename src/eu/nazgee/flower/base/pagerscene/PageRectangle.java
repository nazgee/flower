package eu.nazgee.flower.base.pagerscene;

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

	private final LinkedList<T> mItems = new LinkedList<T>();
	private final ILayout mLayout;
	// ===========================================================
	// Constructors
	// ===========================================================
	public PageRectangle(final float pX, final float pY, final float pWidth, final float pHeight,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final ILayout pLayout) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
		mLayout = pLayout;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public void setItems(final LinkedList<T> pItems) {
		for (final T item : mItems) {
			detachChild(item);
		}
		mItems.clear();
		mLayout.layoutItems(pItems);
		for (final T item : pItems) {
			attachChild(item);
			item.attachChild(new Rectangle(0, 0, 5, 5, getVertexBufferObjectManager()));
			attachChild(new Rectangle(item.getX(), item.getY(), 5, 5, getVertexBufferObjectManager()));
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
