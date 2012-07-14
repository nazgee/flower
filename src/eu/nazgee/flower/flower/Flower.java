package eu.nazgee.flower.flower;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.EaseQuadIn;

import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.activity.game.scene.game.Sky;
import eu.nazgee.misc.State;
import eu.nazgee.misc.State.IStateChangesListener;
import eu.nazgee.util.Anchor;
import eu.nazgee.util.Anchor.eAnchorPointXY;
import eu.nazgee.util.Kinematics;


public class Flower extends Entity implements ITouchArea, IFlowerState{
	public static final int GROUND_LEVEL_OFFSET = -20;
	// ===========================================================
	// Constants
	// ===========================================================
	private static final int ZINDEX_BLOSSOM = 0;
	private static final int ZINDEX_SEED = -1;
	// ===========================================================
	// Fields
	// ===========================================================
	private FlowerState mState = new FlowerStateSeed(this);
	private final BlossomParent mEntityBlossom;
	private final Seed mEntitySeed;
	private final LoadableSeed mSeed;

	private IEntityModifier mAnimationModifier;
	private IFlowerListener mStateHandler;
	public Flower(final float pX, final float pY, final LoadableSeed pSeed,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final TexturesLibrary pTexturesLibrary, final EntityDetachRunnablePoolUpdateHandler pDetacher) {
		mSeed = pSeed;
		this.mEntityBlossom = new BlossomParent(0, 0, pTexturesLibrary.getFlower(pSeed.blossomID), pVertexBufferObjectManager, pSeed.getRandomColor());
		this.mEntitySeed = new Seed(0, 0, pTexturesLibrary.getSeed(pSeed.seedID), pTexturesLibrary.getWateredMarker(),
				pVertexBufferObjectManager, pDetacher);

		attachChild(mEntitySeed);
		setSize(mEntitySeed.getWidth(), mEntitySeed.getHeight());

		mEntityBlossom.setZIndex(ZINDEX_BLOSSOM);
		mEntitySeed.setZIndex(ZINDEX_SEED);

		Anchor.setPosCenterAtParent(mEntitySeed, eAnchorPointXY.CENTERED);

		mState.setStateChangeListener(new StateChangeListener());
		sortChildren();
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public LoadableSeed getSeed() {
		return mSeed;
	}

//	public Color getBlossomColor() {
//		return mColor;
//	}

	public IFlowerListener getStateListener() {
		return mStateHandler;
	}

	public void setStateHandler(final IFlowerListener pFlowerStateHandler) {
		mStateHandler = pFlowerStateHandler;
	}

	public Blossom getBlossom() {
		return mEntityBlossom;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public boolean contains(final float pX, final float pY) {
		return mEntitySeed.contains(pX, pY);
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
			final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		return mEntitySeed.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	}

	// ===========================================================
	// Methods
	// ===========================================================
	@Override
	public IFlowerState water() {
		mState = mState.water();
		return mState;
	}

	@Override
	public IFlowerState sun() {
		mState = mState.sun();
		return mState;
	}

	@Override
	public IFlowerState drag() {
		mState = mState.drag();
		return mState;
	}

	@Override
	public IFlowerState drop(final Sky pSky) {
		mState = mState.drop(pSky);
		return mState;
	}

	/**
	 * Animates dropping Flower to the ground from the given position
	 * @param pX
	 * @param pY
	 * @param pSky used to calculate ground level which will be used in the animation
	 */
	public void animateDropFromToGround(final float pX, final float pY, final Sky pSky) {
		animateMove(pX, pY, pX, pSky.getGroundLevelOnScene() + eAnchorPointXY.BOTTOM_MIDDLE.getOffsetYFromDefault(mEntitySeed) + GROUND_LEVEL_OFFSET);
	}

	public void animateDropTo(final float pX_to, final float pY_to) {
		animateMove(getX(), getY(), pX_to, pY_to);
	}

	public void animateDropToGround(final Sky pSky) {
		animateDropTo(getX(), pSky.getGroundLevelOnScene() + eAnchorPointXY.BOTTOM_MIDDLE.getOffsetYFromDefault(mEntitySeed) + GROUND_LEVEL_OFFSET);
	}

	public void animateBloom() {
		attachChild(mEntityBlossom);	// blossom was not attached yet, for performance reasons
		Anchor.setPosCenterAtParent(mEntityBlossom, eAnchorPointXY.CENTERED);

		mEntityBlossom.animateBloom();
		mEntitySeed.animateGrowthAndDetachSelf();
	}

	public void animateFry() {
		mEntitySeed.animateFry();
	}

	public void animateWater() {
		mEntitySeed.animateWater();
	}

	public void animateMove(final float pX_from, final float pY_from, final float pX_to, final float pY_to) {
		final float time = Kinematics.time(Kinematics.GRAVITY_SEED_ACCEL, Math.abs(pY_from - pY_to));
		setPosition(pX_from, pY_from);

		setAnimationModifier(new ParallelEntityModifier(
				new MoveXModifier(time, pX_from, pX_to, EaseLinear.getInstance()),
				new MoveYModifier(time, pY_from, pY_to, EaseQuadIn.getInstance())
			));
	}

	synchronized private void setAnimationModifier(final IEntityModifier pModifier) {
		pModifier.setAutoUnregisterWhenFinished(false);
		unregisterEntityModifier(mAnimationModifier);
		mAnimationModifier = pModifier;
		registerEntityModifier(pModifier);
	}


	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public interface IFlowerListener {
		public void onBloomed(Flower pFlower);
		public void onFried(Flower pFlower);
		public void onDragged(Flower pFlower);
		public void onDropped(Flower pFlower);
	}

	private class StateChangeListener implements IStateChangesListener<Flower> {
		@Override
		public void onStateStarted(final State<Flower> pState) {
			if (mStateHandler == null) {
				return;
			}

			if (pState instanceof FlowerStateBloomed) {
				mStateHandler.onBloomed(Flower.this);
			} else if (pState instanceof FlowerStateFried) {
				mStateHandler.onFried(Flower.this);
			} else if (pState instanceof FlowerStateDragged) {
				mStateHandler.onDragged(Flower.this);
			}
		}
		@Override
		public void onStateFinished(final State<Flower> pState) {
			if (pState instanceof FlowerStateDragged) {
				mStateHandler.onDropped(Flower.this);
			}
		}
	}
}
