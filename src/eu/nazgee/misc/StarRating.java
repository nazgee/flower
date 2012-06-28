package eu.nazgee.misc;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.nazgee.util.Anchor;
import eu.nazgee.util.Anchor.eAnchorPointXY;

public class StarRating extends Entity {
	// ===========================================================
	// Constants
	// ===========================================================
	private final Sprite mStars[];
	// ===========================================================
	// Fields
	// ===========================================================
	// ===========================================================
	// Constructors
	// ===========================================================
	public StarRating(int pStarsNumber, float pX, float pY, final ITextureRegion pTexture, final VertexBufferObjectManager pVBOM) {
		this(pStarsNumber, pX, pY, pTexture, pVBOM, 1);
	}

	public StarRating(int pStarsNumber, float pX, float pY, final float pDesiredWidth, final ITextureRegion pTexture, final VertexBufferObjectManager pVBOM) {
		this(pStarsNumber, pX, pY, pTexture, pVBOM, pDesiredWidth / (pTexture.getWidth() * pStarsNumber));
	}

	public StarRating(int pStarsNumber, float pX, float pY, final ITextureRegion pTexture, final VertexBufferObjectManager pVBOM, final float pScale) {
		super(pX, pY);
		mStars = new Sprite[pStarsNumber];
		final float starWidth = pTexture.getWidth() * pScale;
		final float starHeight = pTexture.getHeight() * pScale;
		setHeight(starHeight);
		setWidth(pStarsNumber * starWidth);

		// Prepare and attach stars
		for (int i = 0; i < mStars.length; i++) {
			mStars[i] = new Sprite(0, 0, starWidth, starHeight, pTexture, pVBOM);
			attachChild(mStars[i]);
			Anchor.setPos(mStars[i],
					eAnchorPointXY.BOTTOM_LEFT.getObjectX(mStars[i]) + i*starWidth,
					eAnchorPointXY.BOTTOM_LEFT.getObjectY(mStars[i]),
					eAnchorPointXY.BOTTOM_LEFT);
		}
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public void setStars(final int pScore) {
		for (int i = 0; i < mStars.length; i++) {
			if (i < pScore) {
				mStars[i].setAlpha(1);
			} else {
				mStars[i].setAlpha(0.3f);
			}
		}
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
