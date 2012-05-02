package eu.nazgee.game.flower;

import java.util.Random;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.ease.EaseBounceOut;
import org.andengine.util.modifier.ease.EaseElasticOut;

import android.util.Log;

import eu.nazgee.game.flower.scene.Sky;
import eu.nazgee.game.utils.helpers.Positioner;


public class Flower extends Entity implements ITouchArea{
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int ZINDEX_BLOSSOM = 0;
	private static final int ZINDEX_WATER = -1;
	private static final int ZINDEX_POT = -2;

	public enum eLevel {
		LOW,
		NORMAL,
		HIGH
	}
	// ===========================================================
	// Fields
	// ===========================================================
	private int mWaterLevel;
	private int mSunLevel;
	private IEntityModifier mDropModifier;
//	private FlowerModifierListener mCloudModifierListener = new FlowerModifierListener();
	private final Sprite mSpriteBlossom;
	private final Sprite mSpritePot;
	private final AnimatedSprite mSpriteWater;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Flower(float pX, float pY, ITextureRegion pTextureRegion,
			ITextureRegion pPotTextureRegion,
			ITiledTextureRegion pWaterTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
//		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);

		mSpriteBlossom = new Sprite(0, 0, pTextureRegion, pVertexBufferObjectManager);
		mSpritePot = new Sprite(0, 0, pPotTextureRegion, pVertexBufferObjectManager);
		mSpriteWater = new AnimatedSprite(0, 0, pWaterTextureRegion, pVertexBufferObjectManager);

		/*
		 * Do not attach it just yet- no need to do it this early
		 */
//		attachChild(mSpriteBlossom); 
		attachChild(mSpritePot);
		attachChild(mSpriteWater);
		mSpriteBlossom.setZIndex(ZINDEX_BLOSSOM);
		mSpritePot.setZIndex(ZINDEX_POT);
		mSpriteWater.setZIndex(ZINDEX_WATER);

		Positioner.setCentered(mSpriteBlossom, this);
		Positioner.setCentered(mSpritePot, this);
		Positioner.setCentered(mSpriteWater, this);

		Random rand = new Random();
		Color col = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
		mSpriteBlossom.setColor(col);
		mSpritePot.setColor(col);

		sortChildren();
		setScale(0.75f);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public eLevel getLevelSun() {
		return getLevel(0, 2, mSunLevel);
	}

	public eLevel getLevelWater() {
		return getLevel(0, 2, mWaterLevel);
	}

	private eLevel getLevel(int low, int high, int value) {
		if (value <= low)
			return eLevel.LOW;

		if (value >= high)
			return eLevel.HIGH;

		return eLevel.NORMAL;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public boolean contains(float pX, float pY) {
		return mSpritePot.contains(pX, pY);
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		return mSpritePot.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	}
	// ===========================================================
	// Methods
	// ===========================================================
	/**
	 * Animates dropping Flower to the ground from the given position
	 * @param pX
	 * @param pY
	 * @param pSky used to calculate ground level which will be used in the animation
	 */
	synchronized public void put(final float pX, final float pY, Sky pSky) {
		final float time = 1;
		setPosition(pX, pY);
		unregisterEntityModifier(mDropModifier);

		final float height = pSky.getHeightOnSky(pY);
		mDropModifier = new ParallelEntityModifier(
				new SequenceEntityModifier(
						new MoveYModifier(time, getY(), getY() + height, EaseBounceOut.getInstance())
						)
				);
		mDropModifier.setAutoUnregisterWhenFinished(false);
		registerEntityModifier(mDropModifier);
	}

	public void sun() {
		mSunLevel++;

		switch (getLevelWater()) {
		case LOW:
			break;
		case NORMAL:
		case HIGH: {
			switch (getLevelSun()) {
			case LOW:
			case NORMAL:
			case HIGH:
				animateBloom();
			}
		}
		}

	}

	public void water() {
		mWaterLevel++;

		switch (getLevelWater()) {
		case LOW:
		break;
		case NORMAL:
			animateWater();
		break;
		case HIGH:
		}
	}

	public boolean isBloomed() {
		return (mSpriteBlossom.hasParent());
	}

	public boolean isWatered() {
		return (mSpriteBlossom.hasParent());
	}

	private void animateBloom() {
		if (isBloomed()) {
			Log.w(getClass().getSimpleName(), "animateBloom(); already bloomed!");
			return;
		}
		Log.d(getClass().getSimpleName(), "animateBloom(); blooming");

		attachChild(mSpriteBlossom);
		sortChildren();

		final float time = 1;
		IEntityModifier bloomer = new ParallelEntityModifier(
				new SequenceEntityModifier(
						new ColorModifier(time, Color.GREEN, mSpriteBlossom.getColor())
						),
				new SequenceEntityModifier(
						new ScaleModifier(time, 0, 1f, EaseElasticOut.getInstance())
						)
				);
		mSpriteBlossom.registerEntityModifier(bloomer);
	}

	private void animateWater() {
		mSpriteWater.animate(100, false);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
//	@Override
//	public boolean collidesWith(IShape pOtherShape) {
//		return mSpritePot.collidesWith(pOtherShape);
//	}
//	@Override
//	public boolean isBlendingEnabled() {
//		return mSpritePot.isBlendingEnabled();
//	}
//	@Override
//	public void setBlendingEnabled(boolean pBlendingEnabled) {
//		mSpritePot.setBlendingEnabled(pBlendingEnabled);
//	}
//	@Override
//	public int getBlendFunctionSource() {
//		return mSpritePot.getBlendFunctionSource();
//	}
//	@Override
//	public int getBlendFunctionDestination() {
//		return mSpritePot.getBlendFunctionDestination();
//	}
//	@Override
//	public void setBlendFunctionSource(int pBlendFunctionSource) {
//		mSpritePot.setBlendFunctionSource(pBlendFunctionSource);
//	}
//	@Override
//	public void setBlendFunctionDestination(int pBlendFunctionDestination) {
//		mSpritePot.setBlendFunctionDestination(pBlendFunctionDestination);
//	}
//	@Override
//	public void setBlendFunction(int pBlendFunctionSource,
//			int pBlendFunctionDestination) {
//		mSpritePot.setBlendFunction(pBlendFunctionSource, pBlendFunctionDestination);
//	}
//	@Override
//	public VertexBufferObjectManager getVertexBufferObjectManager() {
//		return mSpritePot.getVertexBufferObjectManager();
//	}
//	@Override
//	public IVertexBufferObject getVertexBufferObject() {
//		return mSpritePot.getVertexBufferObject();
//	}
//	@Override
//	public ShaderProgram getShaderProgram() {
//		return mSpritePot.getShaderProgram();
//	}
//	@Override
//	public void setShaderProgram(ShaderProgram pShaderProgram) {
//		mSpritePot.setShaderProgram(pShaderProgram);
//	}


//	private class FlowerModifierListener implements IModifierListener<IEntity> {
//		@Override
//		public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
//			synchronized (Flower.this) {
//				if (mFlowerListener != null) {
//				}
//			}
//		}
//		@Override
//		public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
//			synchronized (Flower.this) {
//				if (mFlowerListener != null) {
//				}
//			}
//		}
//	}
}
