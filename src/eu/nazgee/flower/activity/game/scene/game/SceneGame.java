package eu.nazgee.flower.activity.game.scene.game;

import java.util.LinkedList;
import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.Constants;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.color.Color;
import org.andengine.util.math.MathUtils;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;

import eu.nazgee.flower.Consts;
import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.activity.game.GameScore;
import eu.nazgee.flower.activity.game.scene.shop.SeedsShop;
import eu.nazgee.flower.activity.game.sound.LoadableSFX;
import eu.nazgee.flower.flower.Flower;
import eu.nazgee.flower.flower.Seed;
import eu.nazgee.flower.flower.Flower.IFlowerStateHandler;
import eu.nazgee.flower.flower.Flower.eLevel;
import eu.nazgee.flower.pool.cloud.Cloud;
import eu.nazgee.flower.pool.popup.PopupPool;
import eu.nazgee.flower.pool.popup.PopupPool.PopupItem;
import eu.nazgee.flower.pool.waterdrop.WaterDrop;
import eu.nazgee.flower.pool.waterdrop.WaterDrop.IWaterDropListener;
import eu.nazgee.flower.sun.Sun;
import eu.nazgee.flower.sun.Sun.TravelListener;
import eu.nazgee.game.utils.engine.camera.SmoothTrackingCamera;
import eu.nazgee.game.utils.helpers.AtlasLoader;
import eu.nazgee.game.utils.helpers.TiledTextureRegionFactory;
import eu.nazgee.game.utils.loadable.LoadableResourceSimple;
import eu.nazgee.game.utils.scene.SceneLoadable;
import eu.nazgee.game.utils.track.TrackVector;

public class SceneGame extends SceneLoadable{
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final GameScore mScore = new GameScore(null);
	private final LoadableSFX mSFX;
	private final MyResources mResources = new MyResources();
	private final HudGame mHud;
	private final LoadableParallaxBackground mLoadableParallaxBackground;

	private PopupPool mPopupPool;
	private Sky mSky;
	private CloudLayer mCloudLayer;
	private Sprite mGround;
	private Sun mSun;

	private final LinkedList<Flower> mFlowers = new LinkedList<Flower>();
	private final FlowerListener mFlowerListener = new FlowerListener();
	private final EntityDetachRunnablePoolUpdateHandler mDetacher;
	private final SeedsShop mSeedsShop; // TODO change it to list/array/whatever. No need to keep the whole shop here
	private IGameListener mGameListerner;
	private final TexturesLibrary mTexturesLibrary;

	// ===========================================================
	// Constructors
	// ===========================================================
	public SceneGame(float W, float H,
			VertexBufferObjectManager pVertexBufferObjectManager,
			final EntityDetachRunnablePoolUpdateHandler pEntityDetachRunnablePoolUpdateHandler, final SeedsShop pSeedsShop,
			TexturesLibrary pTexturesLibrary) {
		super(W, H, pVertexBufferObjectManager);
		this.mDetacher = pEntityDetachRunnablePoolUpdateHandler;
		this.mSeedsShop = pSeedsShop;
		this.mTexturesLibrary = pTexturesLibrary;
		
		mSFX = new LoadableSFX();
		mHud = new HudGame(W, H, pVertexBufferObjectManager);
		mLoadableParallaxBackground = new LoadableParallaxBackground(pVertexBufferObjectManager);
		getLoader().install(mResources);
		getLoader().install(mLoadableParallaxBackground);
		getLoader().install(mSFX);
		getLoader().install(mHud);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public IGameListener getGameListerner() {
		return mGameListerner;
	}

	public void setGameListerner(IGameListener mGameListerner) {
		this.mGameListerner = mGameListerner;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onLoadResources(Engine e, Context c) {
		/*
		 * No need to do anything special here.
		 */
	}

	@Override
	public void onLoad(Engine e, Context c) {
		final VertexBufferObjectManager vbom = this.getVertexBufferObjectManager();

		Random rand = MathUtils.RANDOM;

		SmoothTrackingCamera camera = (SmoothTrackingCamera) e.getCamera();
		camera.setHUD(mHud);
		mScore.setHUD(mHud);
		mScore.score.set(0);
		mScore.flowers.set(0);
		mScore.seeds.set(mSeedsShop.getSeedsInBasket().size());

		mPopupPool = new PopupPool(mResources.FONT_POPUP, mDetacher, vbom);

		/*
		 * Prepare fancy background- everything was loaded by mLoadableBacground
		 * so all we need to do call setBackground. We also save a shortcut
		 * to the sprite representing ground level
		 */
		setBackground(mLoadableParallaxBackground.getLoadedBacground());
		mGround = mLoadableParallaxBackground.getLoadedGroundSprite();

		/*
		 * Create new virtual sky- this object is used to calculate how high
		 * above ground level entities are placed
		 */
		mSky = new Sky(mGround.getY());

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
		mSun.travel(0, getH()/2, getW() * 1.5f, getH()/2, 3, new SunTravelListener());
		camera.setTracking(mSun, new TrackVector(new Vector2(camera.getWidth() * 0.25f, 0)), 0);

		/*
		 * Create layer of Clouds
		 */
		mCloudLayer = new CloudLayer(0, 0, getW() * 1.5f, getH()/3,
				getW() * 0.1f, 10, 0.2f, 0.2f, 6, mSky,
				mTexturesLibrary.getClouds(), mTexturesLibrary.getRainDrop(), 
				mTexturesLibrary.getRainSplash(),
				mDetacher,
				vbom);
		attachChild(mCloudLayer);
		mCloudLayer.setWaterDropListener(new IWaterDropListener() {
			@Override
			public void onHitTheGround(WaterDrop pWaterDrop) {
				handleFlowerRain(pWaterDrop);
			}
		});

		/*
		 * Create some flowers
		 */
		for (int i = 0; i < mSeedsShop.getSeedsInBasket().size(); i++) {
			Seed seed = mSeedsShop.getSeedsInBasket().get(i);

			/*
			 *  Create a sprite
			 */
			Flower flower = new Flower(0, 0, seed, getVertexBufferObjectManager(), mTexturesLibrary);
			flower.setZIndex(-1);
			flower.setFlowerStateHandler(mFlowerListener);

			flower.setPosition(getW() * rand.nextFloat(), getH() * rand.nextFloat());
			/*
			 * Attach it to the scene, so it gets drawn and updated
			 */
			attachChild(flower);
			/*
			 * Attach it to the list of dragable items
			 */
			mFlowers.add(flower);
			registerTouchArea(flower);
			postRunnable(new FlowerTouchRunnable(flower, true));
		}

		/*
		 * Register update update handler used to change sun's rays target, and
		 * to tell flowers that they were exposed to sun
		 */
		this.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				float pos[] = mSun.getSceneCenterCoordinates();
				Cloud cloud = mCloudLayer.getHighestCloudAtX(pos[Constants.VERTEX_INDEX_X], pos[Constants.VERTEX_INDEX_Y]);
				if (cloud != null) {
					mSun.setRaysTargetCenter(cloud, mSky);
				} else {
					mSun.setRaysTargetTop(mGround, mSky);
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
		mHud.getCamera().setHUD(null);
	}




	// ===========================================================
	// Methods
	// ===========================================================

	private void handleFlowerSun() {
		for (Flower flower : mFlowers) {
			/*
			 * We are interested in shining only at flowers that are on the ground level 
			 */
			if (mSky.getHeightOnSky(flower) == 0 && mSun.isShiningAt(flower)) {
				flower.stateSun();
			}
		}
	}

	private void handleFlowerRain(WaterDrop pWaterDrop) {
		float pos[] = pWaterDrop.getSceneCenterCoordinates();
		for (Flower flower : mFlowers) {
			if (flower.contains(pos[0], pos[1]) && (flower.getLevelWater() == eLevel.LOW)) {
				flower.stateWater();
				break; // only one flower gets watered
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public interface IGameListener {
		public void onGameFinished();
	}

	private class FlowerListener implements IFlowerStateHandler {
		@Override
		public void onBloomed(Flower pFlower) {
			SceneGame.this.postRunnable(new FlowerDeactivateRunnable(pFlower));
			mSFX.onFlowerBloom();
			mScore.score.inc(100);
			mScore.flowers.inc(1);
			mScore.seeds.dec(1);

			PopupItem item = mPopupPool.obtainPoolItem();
			item.getEntity().put(pFlower, "+100$");
			item.getEntity().fxPop(1.5f);
			attachChild(item.getEntity());
		}

		@Override
		public void onFried(Flower pFlower) {
			SceneGame.this.postRunnable(new FlowerDeactivateRunnable(pFlower));
			mSFX.onFlowerFry();
			mScore.seeds.dec(1);

			PopupItem item = mPopupPool.obtainPoolItem();
			item.getEntity().put(pFlower, "fried!");
			item.getEntity().fxPop(1f);
			attachChild(item.getEntity());
		}

		@Override
		public void onWaterLevelChanged(Flower pFlower, eLevel pOld, eLevel pNew) {
		}

		@Override
		public void onSunLevelChanged(Flower pFlower, eLevel pOld, eLevel pNew) {
		}

		class FlowerDeactivateRunnable implements Runnable {
			private final Flower mFlower;

			public FlowerDeactivateRunnable(Flower mFlower) {
				this.mFlower = mFlower;
			}

			@Override
			public void run() {
				mFlowers.remove(mFlower);
				mFlower.stateDropTo(mFlower.getX(), getH());
			}
		}
	}

	private class SunTravelListener implements TravelListener {
		@Override
		public void onStarted(Sun pSun) {
		}
		@Override
		public void onFinished(Sun pSun) {
			//pSun.travel(0, getH()/2, getW() * 1.5f, getH()/2, 40, this);
			if (null != getGameListerner()) {
				getGameListerner().onGameFinished();
			}
		}
	}

	private class MyAreaTouchListener implements IOnAreaTouchListener {
		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
				ITouchArea pTouchArea, float pTouchAreaLocalX,
				float pTouchAreaLocalY) {

			if (mFlowers.contains(pTouchArea)) {
				Flower flower = (Flower) pTouchArea;
				flower.setPosition(pSceneTouchEvent.getX(), pSceneTouchEvent.getY() + Consts.TOUCH_OFFSET_Y);

				if (pSceneTouchEvent.isActionUp() || pSceneTouchEvent.isActionDown()) {
					SceneGame.this.postRunnable(new FlowerTouchRunnable(flower, pSceneTouchEvent.isActionUp()));
				}
				return true;
			}
			return false;
		}
	}

	private class FlowerTouchRunnable implements Runnable {
		private final Flower mFlower;
		private final boolean mUp;
		public FlowerTouchRunnable(Flower pSprite, boolean pIsUp) {
			mFlower = pSprite;
			mUp = pIsUp;
		}
		@Override
		public void run() {
			if (mUp) {
				mFlower.stateDropToGround(mSky);
			}
		}
	}

	private static class MyResources extends LoadableResourceSimple {
		public Font FONT_POPUP;
		private BitmapTextureAtlas mFontAtlas;

		@Override
		public void onLoadResources(Engine e, Context c) {
		}

		@Override
		public void onLoad(Engine e, Context c) {
			final TextureManager textureManager = e.getTextureManager();
			final FontManager fontManager = e.getFontManager();

			mFontAtlas = new BitmapTextureAtlas(textureManager, 512, 256, TextureOptions.BILINEAR);
			FONT_POPUP = FontFactory.createFromAsset(fontManager, mFontAtlas, c.getAssets(), Consts.HUD_FONT, Consts.CAMERA_HEIGHT*0.1f, true, Color.WHITE.getARGBPackedInt());
			FONT_POPUP.load();
		}

		@Override
		public void onUnload() {

			FONT_POPUP.unload();
			mFontAtlas.unload();
		}
	}
}