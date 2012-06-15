package eu.nazgee.util;

import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
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

	// ===========================================================
	// Constructors
	// ===========================================================
	public NinePatchTextureRegionBatch(ITextureRegion pRegion,
			final Rect pCenterPatch) {
		this(pRegion.getTexture(), pRegion.getTextureX(), pRegion.getTextureY(),
				pRegion.getWidth(), pRegion.getHeight(),
				pCenterPatch);
	}

	public NinePatchTextureRegionBatch(final ITexture pTexture,
			final float pTexturePositionX, final float pTexturePositionY,
			final float pWidth, final float pHeight, final Rect pCenterPatch) {
		super(pTexture, pTexturePositionX, pTexturePositionY, pWidth, pHeight);

		mRegions = new TextureRegion[9];
		mCenterPatch = pCenterPatch;

		int x = (int) pTexturePositionX;
		int y = (int) pTexturePositionY;
		int width = (int) pWidth;
		int height = (int) pHeight;

		pCenterPatch.offset(x, y);

		mRegions[TOP_LEFT] = TextureRegionFactory.extractFromTexture(pTexture,
				x, y, pCenterPatch.left - x, pCenterPatch.top - y, false);

		mRegions[TOP_CENTER] = TextureRegionFactory.extractFromTexture(
				pTexture, pCenterPatch.left, y, pCenterPatch.width(),
				pCenterPatch.top - y, false);

		mRegions[TOP_RIGHT] = TextureRegionFactory.extractFromTexture(pTexture,
				pCenterPatch.left + pCenterPatch.width(), y, (width + x)
						- (pCenterPatch.left + pCenterPatch.width()),
				pCenterPatch.top - y, false);

		mRegions[MIDDLE_LEFT] = TextureRegionFactory.extractFromTexture(
				pTexture, x, pCenterPatch.top, pCenterPatch.left - x,
				pCenterPatch.height(), false);

		mRegions[MIDDLE_CENTER] = TextureRegionFactory.extractFromTexture(
				pTexture, pCenterPatch.left, pCenterPatch.top,
				pCenterPatch.width(), pCenterPatch.height(), false);

		mRegions[MIDDLE_RIGHT] = TextureRegionFactory.extractFromTexture(
				pTexture, pCenterPatch.left + pCenterPatch.width(),
				pCenterPatch.top, (width + x)
						- (pCenterPatch.left + pCenterPatch.width()),
				pCenterPatch.height(), false);

		mRegions[BOTTOM_LEFT] = TextureRegionFactory.extractFromTexture(
				pTexture, x, pCenterPatch.top + pCenterPatch.height(),
				pCenterPatch.left - x, (height + y)
						- (pCenterPatch.top + pCenterPatch.height()), false);

		mRegions[BOTTOM_CENTER] = TextureRegionFactory
				.extractFromTexture(pTexture, pCenterPatch.left,
						pCenterPatch.top + pCenterPatch.height(),
						pCenterPatch.width(), (height + y)
								- (pCenterPatch.top + pCenterPatch.height()),
						false);

		mRegions[BOTTOM_RIGHT] = TextureRegionFactory
				.extractFromTexture(
						pTexture,
						pCenterPatch.left + pCenterPatch.width(),
						pCenterPatch.top + pCenterPatch.height(),
						(width + x)
								- (pCenterPatch.left + pCenterPatch.width()),
						(height + y)
								- (pCenterPatch.top + pCenterPatch.height()),
						false);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Rect getCenterPatch() {
		return mCenterPatch;
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
