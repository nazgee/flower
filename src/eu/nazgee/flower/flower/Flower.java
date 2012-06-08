package eu.nazgee.flower.flower;

import java.util.Random;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
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
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.ease.EaseBounceOut;
import org.andengine.util.modifier.ease.EaseElasticIn;
import org.andengine.util.modifier.ease.EaseElasticOut;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.EaseQuadIn;

import android.util.Log;
import eu.nazgee.flower.activity.game.scene.game.Sky;
import eu.nazgee.game.utils.helpers.Positioner;


public class Flower extends Entity implements ITouchArea{
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int ZINDEX_BLOSSOM = 0;
//	private static final int ZINDEX_WATER = -1;
	private static final int ZINDEX_SEED = -1;
//	private static final int ZINDEX_POT = -2;

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
	private boolean isFried = false;

	private final EntityBlossom mSpriteBlossom;
	private final EntitySeed mEntitySeed;

	private IEntityModifier mDropModifier;
	private IFlowerStateHandler mFlowerStateHandler;
	private final Seed mSeed;
	private final Color mColor;
	// ===========================================================
	// Constructors
	// ===========================================================

	public Flower(float pX, float pY, Seed pSeed,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		this.mSeed = pSeed;

		this.mColor = pSeed.getRandomColor(MathUtils.RANDOM);
		this.mSpriteBlossom = new EntityBlossom(0, 0, pSeed.mTexPlant, pVertexBufferObjectManager, mColor);
		this.mEntitySeed = new EntitySeed(0, 0, pSeed.mTexSeed, pVertexBufferObjectManager, mColor);

		attachChild(mEntitySeed);

		mSpriteBlossom.setZIndex(ZINDEX_BLOSSOM);
		mEntitySeed.setZIndex(ZINDEX_SEED);

		Positioner.setCentered(mEntitySeed, this);
		Positioner.setCenteredTop(mSpriteBlossom, mEntitySeed);

		sortChildren();
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

	public boolean isFried() {
		return isFried;
	}

	private void setFried(boolean isFried) {
		this.isFried = isFried;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public boolean contains(float pX, float pY) {
		return mEntitySeed.contains(pX, pY);
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		return mEntitySeed.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
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
		if (isBloomed() || isFried()) {
			return;
		}

		eLevel old = getLevelSun();
		mSunLevel++;

		switch (getLevelWater()) {
		case LOW:
			animateFry();
			setFried(true);
			if (mFlowerStateHandler != null) {
				mFlowerStateHandler.onFried(this);
			}
			break;
		case NORMAL:
		case HIGH: {
			switch (getLevelSun()) {
			case LOW:
			case NORMAL:
			case HIGH:
				animateBloom();
				setBloomed(true);
				if (mFlowerStateHandler != null) {
					mFlowerStateHandler.onBloomed(this);
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
		if (isBloomed() || isFried()) {
			return;
		}

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
		attachChild(mSpriteBlossom);	// blossom was not attached yet, for performance reasons
		mSpriteBlossom.animateBloom();
	}

	private void animateFry() {
		mEntitySeed.animateFry();
	}

	private void animateWater() {
		mEntitySeed.animateWater();
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
