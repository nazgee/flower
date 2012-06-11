package eu.nazgee.flower.activity.game.scene.shop;

import org.andengine.engine.camera.Camera;
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

import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.TexturesLibrary.TexturesMain;
import eu.nazgee.flower.flower.Seed;
import eu.nazgee.game.utils.helpers.Positioner;

public class SeedItem extends Entity implements ITouchArea{
	// ===========================================================
	// Constants
	// ===========================================================
	public static int ZINDEX_LOCK = 5;
	// ===========================================================
	// Fields
	// ===========================================================
	private final Seed mSeed;
	private final Sprite mSpriteFrame;
	private final Sprite mSpriteSeed;
	private final Sprite mSpriteBlossoms[];
	private final TexturesLibrary mTexturesLibrary;
	// ===========================================================
	// Constructors
	// ===========================================================
	public SeedItem(Seed pSeed, Font pFont, ITextureRegion pFrameTexture, VertexBufferObjectManager pVBOM, TexturesLibrary pTexturesLibrary) {
		this.mSeed = pSeed;
		this.mTexturesLibrary = pTexturesLibrary;

		this.mSpriteSeed = new Sprite(0, 0, pTexturesLibrary.getMain().get(pSeed.seedID), pVBOM);
		this.mSpriteBlossoms = new Sprite[pSeed.col_plant.length];
		for (int i = 0; i < this.mSpriteBlossoms.length; i++) {
			this.mSpriteBlossoms[i] = new Sprite(0, 0, pTexturesLibrary.getMain().get(pSeed.blossomID), pVBOM);
			this.mSpriteBlossoms[i].setColor(pSeed.col_plant[i]);
		}

		this.mSpriteFrame = new Sprite(0, 0, pFrameTexture, pVBOM) {
			@Override
			public 	void setAlpha(final float pAlpha) {
				super.setAlpha(pAlpha * 0.5f);
			}
		};

		final Text text = new Text(0, 0, pFont, "seed=" + pSeed.id, pVBOM);
		text.setColor(Color.BLACK);

		attachChild(this.mSpriteFrame);
		int blossomX = 0;
		for (Sprite blossom : mSpriteBlossoms) {
			this.mSpriteFrame.attachChild(blossom);
			blossom.setX(blossomX);
			blossomX += blossom.getWidth();
		}
		attachChild(this.mSpriteSeed);
		attachChild(text);

		/*
		 * Create a lock icon on top of Item
		 */
		if (mSeed.resources.isLocked()) {
			this.mSpriteFrame.setColor(Color.RED);

			Sprite locked = new Sprite(0, 0, pTexturesLibrary.getMain().get(TexturesMain.ICONS_LOCK_ID), pVBOM);
			locked.setZIndex(ZINDEX_LOCK);
			attachChild(locked);
			Positioner.setCentered(locked, this);
		}

		Positioner.setCentered(this.mSpriteFrame, this);
		Positioner.setCentered(this.mSpriteSeed, this);
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

	@Override
	public boolean isCulled(Camera pCamera) {
		return mSpriteFrame.isCulled(pCamera);
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
