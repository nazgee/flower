package eu.nazgee.flower.flower;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.ease.EaseBounceOut;
import org.andengine.util.modifier.ease.EaseLinear;

import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.activity.game.scene.game.Sky;
import eu.nazgee.flower.flower.EntityBlossom.IBlossomListener;
import eu.nazgee.util.Anchor;
import eu.nazgee.util.Anchor.eAnchorPointXY;


public class Flower extends Entity implements ITouchArea{
	public static final int BOTTOM_BELOW_GROUND = 20;
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int ZINDEX_BLOSSOM = 0;
	private static final int ZINDEX_SEED = -1;

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

	private final EntityBlossomParent mEntityBlossom;
	private final EntitySeed mEntitySeed;

	private IEntityModifier mDropModifier;
	private IFlowerStateHandler mFlowerStateHandler;
	private final Seed mSeed;
	private final Color mColor;
	private final EntityDetachRunnablePoolUpdateHandler mDetacher;
	// ===========================================================
	// Constructors
	// ===========================================================

	public Flower(float pX, float pY, final Seed pSeed,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final TexturesLibrary pTexturesLibrary, final EntityDetachRunnablePoolUpdateHandler pDetacher) {
		mSeed = pSeed;
		mDetacher = pDetacher;

		this.mColor = pSeed.getRandomColor(MathUtils.RANDOM);
		this.mEntityBlossom = new EntityBlossomParent(0, 0, pTexturesLibrary.getFlower(pSeed.blossomID), pVertexBufferObjectManager, mColor);
		this.mEntitySeed = new EntitySeed(0, 0, pTexturesLibrary.mSpritesheetMisc.getTexturePackTextureRegionLibrary().get(pSeed.seedID), pVertexBufferObjectManager, mColor, pDetacher);

		attachChild(mEntitySeed);

		mEntityBlossom.setZIndex(ZINDEX_BLOSSOM);
		mEntitySeed.setZIndex(ZINDEX_SEED);

		Anchor.setPosCenterAtParent(mEntitySeed, eAnchorPointXY.CENTERED);

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

	public void setBlossomListener(final IBlossomListener pBlossomListener) {
		mEntityBlossom.setBlossomListener(pBlossomListener);
	}

	public IBlossomListener getBlossomListener(final IBlossomListener pBlossomListener) {
		return mEntityBlossom.getBlossomListener();
	}

	public Seed getSeed() {
		return mSeed;
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
		stateDropFromTo(pX, pY, pX, pSky.getGroundLevelOnScene() + eAnchorPointXY.CENTERED.getObjectY(mEntitySeed) - BOTTOM_BELOW_GROUND);
	}

	synchronized public void stateDropToGround(Sky pSky) {
		stateDropTo(getX(), pSky.getGroundLevelOnScene() + eAnchorPointXY.CENTERED.getObjectY(mEntitySeed) - BOTTOM_BELOW_GROUND);
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
		attachChild(mEntityBlossom);	// blossom was not attached yet, for performance reasons
		Anchor.setPosCenterAtParent(mEntityBlossom, eAnchorPointXY.CENTERED);

		mEntityBlossom.animateBloom();
		mEntitySeed.animateGrowthAndDetachSelf();
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
