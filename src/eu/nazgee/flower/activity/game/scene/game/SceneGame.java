package eu.nazgee.flower.activity.game.scene.game;

import java.util.LinkedList;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.Constants;
import org.andengine.util.adt.color.Color;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.math.MathUtils;

import android.content.Context;
import eu.nazgee.flower.Consts;
import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.activity.game.GameScore;
import eu.nazgee.flower.activity.game.sound.LoadableSFX;
import eu.nazgee.flower.flower.EntityBlossom;
import eu.nazgee.flower.flower.EntityBlossom.IBlossomListener;
import eu.nazgee.flower.flower.Flower;
import eu.nazgee.flower.flower.Flower.IFlowerListener;
import eu.nazgee.flower.flower.Seed;
import eu.nazgee.flower.level.GameLevel;
import eu.nazgee.flower.pool.butterfly.Butterfly;
import eu.nazgee.flower.pool.butterfly.ButterflyPool;
import eu.nazgee.flower.pool.cloud.Cloud;
import eu.nazgee.flower.pool.popup.PopupPool;
import eu.nazgee.flower.pool.popup.PopupPool.PopupItem;
import eu.nazgee.flower.pool.rainbow.Rainbow;
import eu.nazgee.flower.pool.rainbow.RainbowPool;
import eu.nazgee.flower.pool.waterdrop.WaterDrop;
import eu.nazgee.flower.pool.waterdrop.WaterDrop.IWaterDropListener;
import eu.nazgee.flower.sun.Sun;
import eu.nazgee.flower.sun.Sun.ISunListener;
import eu.nazgee.game.utils.scene.SceneLoadable;
import eu.nazgee.util.Anchor;
import eu.nazgee.util.Anchor.eAnchorPointXY;

public class SceneGame extends SceneLoadable{
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int ZINDEX_CLOUD = 3;
	private static final int ZINDEX_SUN = 2;
	private static final int ZINDEX_BUTTERFLY = 1;
	private static final int ZINDEX_SEED = 0;
	private static final int ZINDEX_BLOSSOM = -1;
	private static final int ZINDEX_RAINBOW = -2;

	private static final int BUTTERFLIES_NUMBER = 3;
	// ===========================================================
	// Fields
	// ===========================================================
	private Context mContext;
	private final GameScore mScore = new GameScore(null);
	private final LoadableSFX mSFX;
	private final HudGame mHud;

	private PopupPool mPopupPool;
	private RainbowPool mRainbowPool;
	private ButterflyPool mButterflyPool;
	private Sky mSky;
	private CloudLayer mCloudLayer;
	private Sun mSun;
	private Entity mSunTrackingHandle;

	private final LinkedList<Flower> mFlowers = new LinkedList<Flower>();
	private final FlowerListener mFlowerListener = new FlowerListener();
	private final BlossomListener mBlossomListener = new BlossomListener();
	private final EntityDetachRunnablePoolUpdateHandler mDetacher;
	private IGameListener mGameListerner;
	private final TexturesLibrary mTexturesLibrary;
	private GameBackground mBG;
	private GameLevel mGameLevel;

	// ===========================================================
	// Constructors
	// ===========================================================
	public SceneGame(final float W, final float H,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final EntityDetachRunnablePoolUpdateHandler pEntityDetachRunnablePoolUpdateHandler, final TexturesLibrary pTexturesLibrary) {
		super(W, H, pVertexBufferObjectManager);
		this.mDetacher = pEntityDetachRunnablePoolUpdateHandler;
		this.mTexturesLibrary = pTexturesLibrary;

		mSFX = new LoadableSFX();
		mHud = new HudGame(W, H, mTexturesLibrary, pVertexBufferObjectManager);
		getLoader().install(mSFX);
		getLoader().install(mHud);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public IGameListener getGameListerner() {
		return mGameListerner;
	}
	public void setGameListerner(final IGameListener mGameListerner) {
		this.mGameListerner = mGameListerner;
	}

	public GameLevel getGameLevel() {
		return mGameLevel;
	}
	public void setGameLevel(final GameLevel mGameLevel) {
		this.mGameLevel = mGameLevel;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onLoadResources(final Engine e, final Context c) {
		/*
		 * Prepare fancy background. We also save a shortcut
		 * to the sprite representing ground level
		 */
		mBG = new GameBackground(e.getCamera(), mTexturesLibrary, getVertexBufferObjectManager());
	}

	@Override
	public void onLoad(final Engine e, final Context c) {
		mContext = c;

		// prepare some shortcuts
		final VertexBufferObjectManager vbom = getVertexBufferObjectManager();
		final float levelW = getGameLevel().level_width;
		final Camera camera = e.getCamera();

		setBackground(mBG);

		camera.setHUD(mHud);
		mScore.setHUD(mHud);
		mScore.score.set(0);
		mScore.flowers.set(0);
		mScore.seeds.set(getGameLevel().getSeedsAccumulatedSoFar().size());

		// prepare objects pools
		mPopupPool = new PopupPool(mTexturesLibrary.getFontPopUp(), mDetacher, vbom);
		mRainbowPool = new RainbowPool(mTexturesLibrary.getRainbow(), mDetacher, vbom);
		mButterflyPool = new ButterflyPool(mTexturesLibrary.getButterfly(), mDetacher, vbom);

		// make sure we have something in pool ready to be utilized
		mPopupPool.batchAllocatePoolItems(5);
		mRainbowPool.batchAllocatePoolItems(2);
		mButterflyPool.batchAllocatePoolItems(9);

		/*
		 * Create new virtual sky- this object is used to calculate how high
		 * above ground level entities are placed
		 */
		mSky = new Sky(GameBackground.SOLID_GND_HEIGHT);

		/*
		 * Register touch area listener, which will listen for the touches of
		 * objects registered via registerTouchArea() method
		 */
		setOnAreaTouchListener(new MyAreaTouchListener());
		setTouchAreaBindingOnActionDownEnabled(true);
		setTouchAreaBindingOnActionMoveEnabled(true);

		/*
		 * Create a Sun and let the camera track it
		 */
		mSun = new Sun(0, 0, mTexturesLibrary.getSun(),
				mTexturesLibrary.getSunRays(), vbom);
		attachChild(mSun);
		mSun.setZIndex(ZINDEX_SUN);
		mSun.travel(0, getH()/2, levelW, getH()/2, getGameLevel().daylight_time, new SunListener());
		mSunTrackingHandle = new Entity(camera.getWidth() * 0.2f, 0);
		mSun.attachChild(mSunTrackingHandle);

		camera.setChaseEntity(mSunTrackingHandle);

		/*
		 * Create layer of Clouds
		 */
		mCloudLayer = new CloudLayer(0, getH() * 0.66f, levelW, getH() * 0.33f,
				getW() * 0.1f, 10, 0.2f, 0.2f, 6, mSky,
				mTexturesLibrary.getClouds(), mTexturesLibrary.getRainDrop(),
				mTexturesLibrary.getRainSplash(),
				mDetacher,
				vbom);
		attachChild(mCloudLayer);
		mCloudLayer.setZIndex(ZINDEX_CLOUD);
		mCloudLayer.setWaterDropListener(new IWaterDropListener() {
			@Override
			public void onHitTheGround(final WaterDrop pWaterDrop) {
				handleFlowerRain(pWaterDrop);
			}
		});

		/*
		 * Create some flowers
		 */
		for (int i = 0; i < getGameLevel().getSeedsAccumulatedSoFar().size(); i++) {
			final Seed seed = getGameLevel().getSeedsAccumulatedSoFar().get(i);
			if (!seed.resources.isLoaded()) {
				seed.resources.onLoadResources(e, c);
				seed.resources.onLoad(e, c);
			}

			/*
			 *  Create a flower
			 */
			final Flower flower = new Flower(0, 0, seed, getVertexBufferObjectManager(), mTexturesLibrary, mDetacher);
			flower.setZIndex(-1);
			flower.setFlowerStateHandler(mFlowerListener);
			flower.setBlossomListener(mBlossomListener);

			/*
			 * Make sure flower seed is placed somewhere in the level width area
			 */
			final float flowerX =MathUtils.random(flower.getWidth(), levelW - flower.getWidth());
			flower.setPosition(flowerX, MathUtils.random(mSky.getGroundLevelOnScene(), getH()));

			/*
			 * Attach it to the scene, so it gets drawn and updated
			 */
			attachChild(flower);
			flower.setZIndex(ZINDEX_SEED);
			/*
			 * Attach it to the list of draggable items
			 */
			mFlowers.add(flower);
			registerTouchArea(flower);
			flower.drag();
			flower.drop(mSky);
		}

		/*
		 * Register update update handler used to change sun's rays target, and
		 * to tell flowers that they were exposed to sun
		 */
		this.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void onUpdate(final float pSecondsElapsed) {
				final float pos[] = mSun.getSceneCenterCoordinates();
				final Cloud cloud = mCloudLayer.getHighestCloudAtX(pos[Constants.VERTEX_INDEX_X], pos[Constants.VERTEX_INDEX_Y]);
				if (cloud != null) {
					mSun.setRaysTargetCenter(cloud, mSky);
				} else {
					mSun.setRaysTarget(GameBackground.SOLID_GND_HEIGHT, mSky);
				}

				handleFlowerSun();
			}
			@Override
			public void reset() {
			}
		});

		sortChildren();
	}

	@Override
	public void onUnload() {
		mContext = null;
		/*
		 *  We do not need anything of these anymore- kill all children and
		 *  get rid of anything else that might want to run without any reason
		 */
		detachChildren();
		clearEntityModifiers();
		clearUpdateHandlers();
		clearTouchAreas();
		setOnAreaTouchListener(null);
		mFlowers.clear();

		/*
		 * Detach HUD from the camera it was connected to - it is not a children
		 * to parent relationship, so detachChildren()/detachSelf() won't work.
		 * Hud will be unloaded automatically by the loader
		 */
		mHud.getCamera().setChaseEntity(null);
		mHud.getCamera().setHUD(null);
	}




	// ===========================================================
	// Methods
	// ===========================================================

	private void handleFlowerSun() {
		for (final Flower flower : mFlowers) {
			/*
			 * We are interested in flowers on the ground level, on which sun
			 * is currently shining
			 */
			if (!mSky.isAboveGroundBottom(flower) && mSun.isShiningAt(flower)) {
				flower.sun();
			}
		}
	}

	private void handleFlowerRain(final WaterDrop pWaterDrop) {
		final float pos[] = pWaterDrop.getSceneCenterCoordinates();
		for (final Flower flower : mFlowers) {
			if (flower.contains(pos[0], pos[1])) {
				flower.water();
				break; // only one flower gets watered
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	/**
	 * Interface to be used by activity for reloading scenes after SceneGame is finished
	 * @author nazgee
	 */
	public interface IGameListener {
		public void onGameFinished();
	}

	private class BlossomListener implements IBlossomListener {
		@Override
		public void onBlooming(final EntityBlossom pBlossom) {
			mSFX.onBloom(pBlossom.getBlossomID());
		}

		@Override
		public void onBloomed(final EntityBlossom pBlossom) {
			mScore.score.inc(100);
			mScore.flowers.inc(1);
			mScore.seeds.dec(1);

			/*
			 * Create a +100 text popup
			 */
			final PopupItem item = mPopupPool.obtainPoolItem();
			item.getEntity().put(pBlossom, "+100$");
			item.getEntity().fxPop(0.5f);
			attachChild(item.getEntity());
		}
	}

	private class FlowerListener implements IFlowerListener {
		@Override
		public void onBloomed(final Flower pFlower) {
			SceneGame.this.postRunnable(new DeactivateFlowerTouchesRunnable(pFlower));
			pFlower.setZIndex(ZINDEX_BLOSSOM);
			sortChildren(false);
			final float x = pFlower.getX();
			final float y = pFlower.getY();
			if (pFlower.getSeed().unlock(mContext)) {
				/*
				 * Create a rainbow
				 */
				final Rainbow item = mRainbowPool.obtainPoolItem().getEntity();
				attachChild(item);
				item.setZIndex(ZINDEX_RAINBOW);
				Anchor.setPosBottomMiddleAtSibling(item, pFlower, eAnchorPointXY.CENTERED);
				item.fxPopOutWithText( mTexturesLibrary.getFontPopUp(), "new\nflower\nfound!", Color.PINK);
				sortChildren(false);
			} else {
				/*
				 * Create butterflies
				 */
				for (int i=0; i<BUTTERFLIES_NUMBER; i++) {
					final Butterfly item = mButterflyPool.obtainPoolItem().getEntity();
					attachChild(item);
					item.setZIndex(ZINDEX_BUTTERFLY);
					item.setPosition(x, y);
					item.fxFlyAround(x, y);
				}
				sortChildren(false);
			}
		}

		@Override
		public void onFried(final Flower pFlower) {
			SceneGame.this.postRunnable(new DeactivateFlowerTouchesRunnable(pFlower));
			mSFX.onFlowerFry();
			mScore.seeds.dec(1);

			/*
			 * Create a "fried" text popup
			 */
			final PopupItem item = mPopupPool.obtainPoolItem();
			item.getEntity().put(pFlower, "fried!");
			item.getEntity().fxPop(1f);
			attachChild(item.getEntity());
		}

		@Override
		public void onDragged(final Flower pFlower) {
			mHud.setActiveFlower(pFlower);
		}

		@Override
		public void onDropped(final Flower pFlower) {
			mHud.hideActiveFlower();
		}

		/**
		 * This class should be used to post a runnable which will remove Flower from the active flowers list
		 * @author nazgee
		 */
		class DeactivateFlowerTouchesRunnable implements Runnable {
			private final Flower mFlower;

			public DeactivateFlowerTouchesRunnable(final Flower mFlower) {
				this.mFlower = mFlower;
			}

			@Override
			public void run() {
				mFlowers.remove(mFlower);
			}
		}
	}

	private class SunListener implements ISunListener {
		@Override
		public void onStarted(final Sun pSun) {
		}
		@Override
		public void onFinished(final Sun pSun) {
			if (null != getGameListerner()) {
				getGameListerner().onGameFinished();
			}
		}
	}

	private class MyAreaTouchListener implements IOnAreaTouchListener {
		@Override
		public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
				final ITouchArea pTouchArea, final float pTouchAreaLocalX,
				final float pTouchAreaLocalY) {

			if (mFlowers.contains(pTouchArea)) {
				final Flower flower = (Flower) pTouchArea;
				flower.setPosition(pSceneTouchEvent.getX(), pSceneTouchEvent.getY() + Consts.TOUCH_OFFSET_Y);
				flower.drag();
				if (pSceneTouchEvent.isActionUp()) {
					flower.drop(mSky);
				}
				return true;
			}
			return false;
		}
	}
}