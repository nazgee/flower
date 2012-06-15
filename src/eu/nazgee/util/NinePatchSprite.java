package eu.nazgee.util;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class NinePatchSprite extends Entity {

	// ===========================================================
	// Fields
	// ===========================================================
	protected Sprite[] mPatch;
	private NinePatchTextureRegionBatch mRegion;

	// ===========================================================
	// Constructors
	// ===========================================================
	public NinePatchSprite(final NinePatchTextureRegionBatch pRegion,
			final float pX, final float pY, final float pWidth,
			final float pHeight, final VertexBufferObjectManager pVBO) {
		mRegion = pRegion;
		mPatch = new Sprite[9];

		// TOP
		mPatch[NinePatchTextureRegionBatch.TOP_LEFT] = new Sprite(pX, pY,
				mRegion.getTextureRegion(NinePatchTextureRegionBatch.TOP_LEFT),
				pVBO);
		mPatch[NinePatchTextureRegionBatch.TOP_CENTER] = new Sprite(
				pX + mPatch[NinePatchTextureRegionBatch.TOP_LEFT].getWidth(),
				pY,
				mRegion.getTextureRegion(NinePatchTextureRegionBatch.TOP_CENTER),
				pVBO);
		mPatch[NinePatchTextureRegionBatch.TOP_RIGHT] = new Sprite(
				mPatch[NinePatchTextureRegionBatch.TOP_CENTER].getX()
						+ mPatch[NinePatchTextureRegionBatch.TOP_CENTER]
								.getWidth(),
				pY,
				mRegion.getTextureRegion(NinePatchTextureRegionBatch.TOP_RIGHT),
				pVBO);

		// MIDDLE
		mPatch[NinePatchTextureRegionBatch.MIDDLE_LEFT] = new Sprite(
				pX,
				pY + mPatch[NinePatchTextureRegionBatch.TOP_LEFT].getHeight(),
				mRegion.getTextureRegion(NinePatchTextureRegionBatch.MIDDLE_LEFT),
				pVBO);
		mPatch[NinePatchTextureRegionBatch.MIDDLE_CENTER] = new Sprite(
				pX + mPatch[NinePatchTextureRegionBatch.MIDDLE_LEFT].getWidth(),
				mPatch[NinePatchTextureRegionBatch.MIDDLE_LEFT].getY(),
				mRegion.getTextureRegion(NinePatchTextureRegionBatch.MIDDLE_CENTER),
				pVBO);
		mPatch[NinePatchTextureRegionBatch.MIDDLE_RIGHT] = new Sprite(
				mPatch[NinePatchTextureRegionBatch.MIDDLE_CENTER].getX()
						+ mPatch[NinePatchTextureRegionBatch.MIDDLE_CENTER]
								.getWidth(),
				mPatch[NinePatchTextureRegionBatch.MIDDLE_LEFT].getY(),
				mRegion.getTextureRegion(NinePatchTextureRegionBatch.MIDDLE_RIGHT),
				pVBO);

		// BOTTOM
		mPatch[NinePatchTextureRegionBatch.BOTTOM_LEFT] = new Sprite(
				pX,
				mPatch[NinePatchTextureRegionBatch.MIDDLE_LEFT].getY()
						+ mPatch[NinePatchTextureRegionBatch.MIDDLE_LEFT]
								.getHeight(),
				mRegion.getTextureRegion(NinePatchTextureRegionBatch.BOTTOM_LEFT),
				pVBO);
		mPatch[NinePatchTextureRegionBatch.BOTTOM_CENTER] = new Sprite(
				pX + mPatch[NinePatchTextureRegionBatch.BOTTOM_LEFT].getWidth(),
				mPatch[NinePatchTextureRegionBatch.BOTTOM_LEFT].getY(),
				mRegion.getTextureRegion(NinePatchTextureRegionBatch.BOTTOM_CENTER),
				pVBO);
		mPatch[NinePatchTextureRegionBatch.BOTTOM_RIGHT] = new Sprite(
				mPatch[NinePatchTextureRegionBatch.BOTTOM_CENTER].getX()
						+ mPatch[NinePatchTextureRegionBatch.BOTTOM_CENTER]
								.getWidth(),
				mPatch[NinePatchTextureRegionBatch.BOTTOM_LEFT].getY(),
				mRegion.getTextureRegion(NinePatchTextureRegionBatch.BOTTOM_RIGHT),
				pVBO);

		mPatch[NinePatchTextureRegionBatch.TOP_LEFT].setScaleCenter(0, 0);
		mPatch[NinePatchTextureRegionBatch.TOP_CENTER].setScaleCenter(0, 0);
		mPatch[NinePatchTextureRegionBatch.TOP_RIGHT].setScaleCenter(0, 0);

		mPatch[NinePatchTextureRegionBatch.MIDDLE_LEFT].setScaleCenter(0, 0);
		mPatch[NinePatchTextureRegionBatch.MIDDLE_CENTER].setScaleCenter(0, 0);
		mPatch[NinePatchTextureRegionBatch.MIDDLE_RIGHT].setScaleCenter(0, 0);

		mPatch[NinePatchTextureRegionBatch.BOTTOM_LEFT].setScaleCenter(0, 0);
		mPatch[NinePatchTextureRegionBatch.BOTTOM_CENTER].setScaleCenter(0, 0);
		mPatch[NinePatchTextureRegionBatch.BOTTOM_RIGHT].setScaleCenter(0, 0);

		for (Sprite patch : mPatch) {
			attachChild(patch);
		}

		this.setHeight(pHeight);
		this.setWidth(pWidth);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public void setPosition(final float pX, final float pY) {

		float dx = mPatch[NinePatchTextureRegionBatch.TOP_LEFT].getX() - pX;
		float dy = mPatch[NinePatchTextureRegionBatch.TOP_LEFT].getY() - pY;

		// TOP
		mPatch[NinePatchTextureRegionBatch.TOP_LEFT].setPosition(pX, pY);
		mPatch[NinePatchTextureRegionBatch.TOP_CENTER].setPosition(
				mPatch[NinePatchTextureRegionBatch.TOP_CENTER].getX() + dx,
				mPatch[NinePatchTextureRegionBatch.TOP_CENTER].getY() + dy);
		mPatch[NinePatchTextureRegionBatch.TOP_RIGHT].setPosition(
				mPatch[NinePatchTextureRegionBatch.TOP_RIGHT].getX() + dx,
				mPatch[NinePatchTextureRegionBatch.TOP_RIGHT].getY() + dy);

		// MIDDLE
		mPatch[NinePatchTextureRegionBatch.MIDDLE_LEFT].setPosition(pX, pY);
		mPatch[NinePatchTextureRegionBatch.MIDDLE_CENTER].setPosition(
				mPatch[NinePatchTextureRegionBatch.MIDDLE_CENTER].getX() + dx,
				mPatch[NinePatchTextureRegionBatch.MIDDLE_CENTER].getY() + dy);
		mPatch[NinePatchTextureRegionBatch.MIDDLE_RIGHT].setPosition(
				mPatch[NinePatchTextureRegionBatch.MIDDLE_RIGHT].getX() + dx,
				mPatch[NinePatchTextureRegionBatch.MIDDLE_RIGHT].getY() + dy);

		// BOTTOM
		mPatch[NinePatchTextureRegionBatch.BOTTOM_LEFT].setPosition(pX, pY);
		mPatch[NinePatchTextureRegionBatch.BOTTOM_CENTER].setPosition(
				mPatch[NinePatchTextureRegionBatch.BOTTOM_CENTER].getX() + dx,
				mPatch[NinePatchTextureRegionBatch.BOTTOM_CENTER].getY() + dy);
		mPatch[NinePatchTextureRegionBatch.BOTTOM_RIGHT].setPosition(
				mPatch[NinePatchTextureRegionBatch.BOTTOM_RIGHT].getX() + dx,
				mPatch[NinePatchTextureRegionBatch.BOTTOM_RIGHT].getY() + dy);
	}

	public float getX() {
		return mPatch[NinePatchTextureRegionBatch.TOP_LEFT].getX();
	}

	public float getY() {
		return mPatch[NinePatchTextureRegionBatch.TOP_LEFT].getY();
	}

	public void setWidth(final float pWidth) {

		float width = mPatch[NinePatchTextureRegionBatch.TOP_CENTER].getWidthScaled();
		float scaleFactor = pWidth / width;

		mPatch[NinePatchTextureRegionBatch.TOP_CENTER].setScaleX(scaleFactor);
		mPatch[NinePatchTextureRegionBatch.MIDDLE_CENTER].setScaleX(scaleFactor);
		mPatch[NinePatchTextureRegionBatch.BOTTOM_CENTER].setScaleX(scaleFactor);

		float x = mPatch[NinePatchTextureRegionBatch.TOP_CENTER].getX()
				+ mPatch[NinePatchTextureRegionBatch.TOP_CENTER].getWidthScaled();

		mPatch[NinePatchTextureRegionBatch.TOP_RIGHT].setPosition(x, mPatch[NinePatchTextureRegionBatch.TOP_RIGHT].getY());
		mPatch[NinePatchTextureRegionBatch.MIDDLE_RIGHT].setPosition(x, mPatch[NinePatchTextureRegionBatch.MIDDLE_RIGHT].getY());
		mPatch[NinePatchTextureRegionBatch.BOTTOM_RIGHT].setPosition(x, mPatch[NinePatchTextureRegionBatch.BOTTOM_RIGHT].getY());
	}

	public float getWidth() {
		return mPatch[NinePatchTextureRegionBatch.TOP_LEFT].getWidthScaled()
				+ mPatch[NinePatchTextureRegionBatch.TOP_CENTER].getWidthScaled()
				+ mPatch[NinePatchTextureRegionBatch.TOP_RIGHT].getWidthScaled();
	}

	public void setHeight(final float pHeight) {

		float height = mPatch[NinePatchTextureRegionBatch.MIDDLE_CENTER].getHeightScaled();
		float scaleFactor = pHeight	/ height;

		mPatch[NinePatchTextureRegionBatch.MIDDLE_LEFT].setScaleY(scaleFactor);
		mPatch[NinePatchTextureRegionBatch.MIDDLE_CENTER].setScaleY(scaleFactor);
		mPatch[NinePatchTextureRegionBatch.MIDDLE_RIGHT].setScaleY(scaleFactor);

		float y = mPatch[NinePatchTextureRegionBatch.MIDDLE_CENTER].getY()
				+ mPatch[NinePatchTextureRegionBatch.MIDDLE_CENTER].getHeightScaled();

		mPatch[NinePatchTextureRegionBatch.BOTTOM_LEFT].setPosition(mPatch[NinePatchTextureRegionBatch.BOTTOM_LEFT].getX(), y);
		mPatch[NinePatchTextureRegionBatch.BOTTOM_CENTER].setPosition(mPatch[NinePatchTextureRegionBatch.BOTTOM_CENTER].getX(), y);
		mPatch[NinePatchTextureRegionBatch.BOTTOM_RIGHT].setPosition(mPatch[NinePatchTextureRegionBatch.BOTTOM_RIGHT].getX(), y);
	}

	public float getHeight() {
		return mPatch[NinePatchTextureRegionBatch.TOP_LEFT].getHeightScaled()
				+ mPatch[NinePatchTextureRegionBatch.MIDDLE_LEFT].getHeightScaled()
				+ mPatch[NinePatchTextureRegionBatch.BOTTOM_LEFT].getHeightScaled();
	}
}
