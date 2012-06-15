package eu.nazgee.util;

import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import android.graphics.Rect;

/**
 * http://kvadratin.blogspot.com/
 * 
 * @author bargatin
 * @since 2012-02-29
 */
public class NinePatchTextureRegionBatch extends TextureRegion {

	// ===========================================================
	// Constants
	// ===========================================================
	public final static int TOP_LEFT = 0;
	public final static int TOP_CENTER = 1;
	public final static int TOP_RIGHT = 2;
	public final static int MIDDLE_LEFT = 3;
	public final static int MIDDLE_CENTER = 4;
	public final static int MIDDLE_RIGHT = 5;
	public final static int BOTTOM_LEFT = 6;
	public final static int BOTTOM_CENTER = 7;
	public final static int BOTTOM_RIGHT = 8;

	// ===========================================================
	// Fields
	// ===========================================================
	private TextureRegion[] mRegions;
	private Rect mCenterPatch;
	private Rect mField;

	// ===========================================================
	// Constructors
	// ===========================================================
	public NinePatchTextureRegionBatch(final ITexture pTexture,
			final int pTexturePositionX, final int pTexturePositionY,
			final int pWidth, final int pHeight, final Rect pCenterPatch,
			final Rect pField) {
		super(pTexture, pTexturePositionX, pTexturePositionY, pWidth, pHeight);

		mRegions = new TextureRegion[9];
		mCenterPatch = pCenterPatch;
		mField = pField;

		int x = pTexturePositionX;
		int y = pTexturePositionY;
		int width = pWidth;
		int height = pHeight;

		pCenterPatch.offset(x, y);

		mRegions[TOP_LEFT] = TextureRegionFactory.extractFromTexture(pTexture,
				x, y, pCenterPatch.left - x, pCenterPatch.top - y, true);

		mRegions[TOP_CENTER] = TextureRegionFactory.extractFromTexture(
				pTexture, pCenterPatch.left, y, pCenterPatch.width(),
				pCenterPatch.top - y, true);

		mRegions[TOP_RIGHT] = TextureRegionFactory.extractFromTexture(pTexture,
				pCenterPatch.left + pCenterPatch.width(), y, (width + x)
						- (pCenterPatch.left + pCenterPatch.width()),
				pCenterPatch.top - y, true);

		mRegions[MIDDLE_LEFT] = TextureRegionFactory.extractFromTexture(
				pTexture, x, pCenterPatch.top, pCenterPatch.left - x,
				pCenterPatch.height(), true);

		mRegions[MIDDLE_CENTER] = TextureRegionFactory.extractFromTexture(
				pTexture, pCenterPatch.left, pCenterPatch.top,
				pCenterPatch.width(), pCenterPatch.height(), true);

		mRegions[MIDDLE_RIGHT] = TextureRegionFactory.extractFromTexture(
				pTexture, pCenterPatch.left + pCenterPatch.width(),
				pCenterPatch.top, (width + x)
						- (pCenterPatch.left + pCenterPatch.width()),
				pCenterPatch.height(), true);

		mRegions[BOTTOM_LEFT] = TextureRegionFactory.extractFromTexture(
				pTexture, x, pCenterPatch.top + pCenterPatch.height(),
				pCenterPatch.left - x, (height + y)
						- (pCenterPatch.top + pCenterPatch.height()), true);

		mRegions[BOTTOM_CENTER] = TextureRegionFactory
				.extractFromTexture(pTexture, pCenterPatch.left,
						pCenterPatch.top + pCenterPatch.height(),
						pCenterPatch.width(), (height + y)
								- (pCenterPatch.top + pCenterPatch.height()),
						true);

		mRegions[BOTTOM_RIGHT] = TextureRegionFactory
				.extractFromTexture(
						pTexture,
						pCenterPatch.left + pCenterPatch.width(),
						pCenterPatch.top + pCenterPatch.height(),
						(width + x)
								- (pCenterPatch.left + pCenterPatch.width()),
						(height + y)
								- (pCenterPatch.top + pCenterPatch.height()),
						true);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Rect getCenterPatch() {
		return mCenterPatch;
	}

	public Rect getField() {
		return mField;
	}

	public TextureRegion getTextureRegion(final int pRegionIndex) {
		return mRegions[pRegionIndex];
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
