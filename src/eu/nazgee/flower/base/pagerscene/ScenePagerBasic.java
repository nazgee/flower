package eu.nazgee.flower.base.pagerscene;

import org.andengine.entity.IEntity;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.nazgee.util.Anchor;

abstract public class ScenePagerBasic<T extends IEntity> extends ScenePager<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final int ROWS;
	private final int COLS;
	private final float EFFECTIVE_PAGE_WIDTH_RATIO;
	private final float PAGE_FLIP_RATIO;
	// ===========================================================
	// Constructors
	// ===========================================================
	public ScenePagerBasic(final float W, final float H,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final int pColums, final int pRows, final float pEffectivePageWidthRatio, final float pPageFlipRatio) {
		super(W, H, pVertexBufferObjectManager, (int) (W * pEffectivePageWidthRatio * pPageFlipRatio));

		COLS = pColums;
		ROWS = pRows;
		EFFECTIVE_PAGE_WIDTH_RATIO = pEffectivePageWidthRatio;
		PAGE_FLIP_RATIO = pPageFlipRatio;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public int getRrows() {
		return ROWS;
	}

	public int getCols() {
		return COLS;
	}

	public float getEffectiveWidth() {
		return getW() * EFFECTIVE_PAGE_WIDTH_RATIO;
	}

	public float getPageFlip() {
		return getEffectiveWidth() * PAGE_FLIP_RATIO;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	protected void attachPage(final IPage<T> pPage, final int pPageNumber) {
		attachChild(pPage);
		Anchor.setPosBottomLeft(pPage, pPageNumber * getEffectiveWidth(), 0);
	}
	// ===========================================================
	// Methods
	// ===========================================================



	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
