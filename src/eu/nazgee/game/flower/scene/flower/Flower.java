package eu.nazgee.game.flower.scene.flower;

import java.util.Random;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.ease.EaseBounceOut;
import org.andengine.util.modifier.ease.EaseElasticOut;
import org.andengine.util.modifier.ease.EaseLinear;

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
	private boolean isBloomed = false;

	private final Sprite mSpriteBlossom;
	private final Sprite mSpritePot;
	private final AnimatedSprite mSpriteWater;

	private IEntityModifier mDropModifier;
	private IFlowerStateHandler mFlowerStateHandler;
	// ===========================================================
	// Constructors
	// ===========================================================

	public Flower(float pX, float pY, ITextureRegion pTextureRegion,
			ITextureRegion pPotTextureRegion,
			ITiledTextureRegion pWaterTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {

		mSpriteBlossom = new Sprite(0, 0, pTextureRegion, pVertexBufferObjectManager);
		mSpritePot = new Sprite(0, 0, pPotTextureRegion, pVertexBufferObjectManager);
		mSpriteWater = new AnimatedSprite(0, 0, pWaterTextureRegion, pVertexBufferObjectManager);

		/*
		 * Do not attach it just yet- no need to do it this early. It will be
		 * attached when flower will bloom
		 */
		// attachChild(mSpriteBlossom); 
		attachChild(mSpritePot);
		attachChild(mSpriteWater);
		mSpriteBlossom.setZIndex(ZINDEX_BLOSSOM);
		mSpritePot.setZIndex(ZINDEX_POT);
		mSpriteWater.setZIndex(ZINDEX_WATER);

		Positioner.setCentered(mSpritePot, this);
		Positioner.setCentered(mSpriteWater, this);
		Positioner.setCenteredTop(mSpriteBlossom, mSpritePot);

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
	public IFlowerStateHandler getFlowerStateHandler() {
		return mFlowerStateHandler;
	}

	public void setFlowerStateHandler(IFlowerStateHandler pFlowerStateHandler) {
		mFlowerStateHandler = pFlowerStateHandler;
	}

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

	public boolean isBloomed() {
		return isBloomed;
	}

	private void setBloomed(boolean isBloomed) {
		this.isBloomed = isBloomed;
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
	synchronized public void stateDropFromToGround(final float pX, final float pY, Sky pSky) {
		stateDropFromTo(pX, pY, pX, pY + pSky.getHeightOnSky(pY));
	}

	synchronized public void stateDropToGround(Sky pSky) {
		stateDropTo(getX(), getY() + pSky.getHeightOnSky(getY()));
	}

	synchronized public void stateDropTo(final float pX_to, final float pY_to) {
		stateDropFromTo(getX(), getY(), pX_to, pY_to);
	}

	synchronized public void stateDropFromTo(final float pX_from, final float pY_from, final float pX_to, final float pY_to) {
		animateMove(pX_from, pY_from, pX_to, pY_to);
	}


	/**
	 * Increases the sunlight level to which the flower was exposed. Results
	 * in blooming, if flower was watered well enough.
	 */
	public void stateSun() {
		eLevel old = getLevelSun();
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
				if (!isBloomed()) {
					animateBloom();
					setBloomed(true);
					if (mFlowerStateHandler != null) {
						mFlowerStateHandler.onBloomed(this);
					}
				}
			}
		}
		}

		if (mFlowerStateHandler != null && old != getLevelSun()) {
			mFlowerStateHandler.onSunLevelChanged(this, old, getLevelSun());
		}
	}

	/**
	 * Increases the water level of the flower
	 */
	public void stateWater() {
		eLevel old = getLevelWater();
		mWaterLevel++;

		switch (getLevelWater()) {
		case LOW:
		break;
		case NORMAL:
			animateWater();
		break;
		case HIGH:
		}

		if (mFlowerStateHandler != null && old != getLevelWater()) {
			mFlowerStateHandler.onWaterLevelChanged(this, old, getLevelWater());
		}
	}

	private void animateBloom() {
		if (!mSpriteBlossom.hasParent()) {
			attachChild(mSpriteBlossom);
			sortChildren();
		}

		Log.d(getClass().getSimpleName(), "animateBloom();");

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
		Log.d(getClass().getSimpleName(), "animateWater();");
		mSpriteWater.animate(100, false);
	}

	private void animateMove(final float pX_from, final float pY_from, final float pX_to, final float pY_to) {
		final float time = 1;
		setPosition(pX_from, pY_from);
		unregisterEntityModifier(mDropModifier);

		mDropModifier = new ParallelEntityModifier(
					new MoveXModifier(time, pX_from, pX_to, EaseLinear.getInstance()),
					new MoveYModifier(time, pY_from, pY_to, EaseBounceOut.getInstance())
				);
		mDropModifier.setAutoUnregisterWhenFinished(false);
		registerEntityModifier(mDropModifier);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public interface IFlowerStateHandler {
		public void onBloomed(Flower pFlower);
		public void onFried(Flower pFlower);
		public void onWaterLevelChanged(Flower pFlower, eLevel pOld, eLevel pNew);
		public void onSunLevelChanged(Flower pFlower, eLevel pOld, eLevel pNew);
	}

}
