package eu.nazgee.flower.activity.game.scene.shop;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityParameterCallable;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import eu.nazgee.flower.flower.Seed;
import eu.nazgee.game.utils.helpers.Positioner;

public class SeedItem extends Entity implements ITouchArea{
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final Seed mSeed;
	private final Sprite mSpriteFrame;
	private final Sprite mSpriteSeed;
	private final Sprite mSpriteBlossoms[];
	// ===========================================================
	// Constructors
	// ===========================================================
	public SeedItem(Seed pSeed, Font pFont, ITextureRegion pFrameTexture, VertexBufferObjectManager pVBOM) {
		mSeed = pSeed;

		mSpriteSeed = new Sprite(0, 0, pSeed.mTexSeed, pVBOM);
		mSpriteBlossoms = new Sprite[pSeed.col_plant.length];
		for (int i = 0; i < mSpriteBlossoms.length; i++) {
			mSpriteBlossoms[i] = new Sprite(0, 0, pSeed.mTexPlant, pVBOM);
			mSpriteBlossoms[i].setColor(pSeed.col_plant[i]);
		}

		mSpriteFrame = new Sprite(0, 0, pFrameTexture, pVBOM) {
			@Override
			public 	void setAlpha(final float pAlpha) {
				super.setAlpha(pAlpha * 0.5f);
			}
		};
		if (mSeed.resources.isLocked()) {
			mSpriteFrame.setColor(Color.RED);
		}
		final Text text = new Text(0, 0, pFont, "seed=" + pSeed.id, pVBOM);
		text.setColor(Color.BLACK);

		attachChild(mSpriteFrame);
		int blossomX = 0;
		for (Sprite blossom : mSpriteBlossoms) {
			mSpriteFrame.attachChild(blossom);
			blossom.setX(blossomX);
			blossomX += blossom.getWidth();
		}
		attachChild(mSpriteSeed);
		attachChild(text);

		Positioner.setCentered(mSpriteFrame, this);
		Positioner.setCentered(mSpriteSeed, this);
		Positioner.setCentered(text, this);

		setAlpha(1);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public boolean contains(float pX, float pY) {
		return mSpriteFrame.contains(pX, pY);
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		return mSpriteFrame.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	} 
	// ===========================================================
	// Methods
	// ===========================================================
	@Override
	public void setAlpha(final float pAlpha) {
		super.setAlpha(0);
		super.callOnChildren(new IEntityParameterCallable() {
			@Override
			public void call(IEntity pEntity) {
				pEntity.setAlpha(pAlpha);
			}
		});
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public Seed getSeed() {
		return mSeed;
	}
}
