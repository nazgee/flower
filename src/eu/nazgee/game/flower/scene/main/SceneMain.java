package eu.nazgee.game.flower.scene.main;

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
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.Constants;
import org.andengine.util.color.Color;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;

import eu.nazgee.game.flower.Consts;
import eu.nazgee.game.flower.MainHUD;
import eu.nazgee.game.flower.flower.Flower;
import eu.nazgee.game.flower.flower.Flower.IFlowerStateHandler;
import eu.nazgee.game.flower.flower.Flower.eLevel;
import eu.nazgee.game.flower.pool.cloud.Cloud;
import eu.nazgee.game.flower.pool.popup.PopupItem;
import eu.nazgee.game.flower.pool.popup.PopupPool;
import eu.nazgee.game.flower.pool.waterdrop.WaterDrop;
import eu.nazgee.game.flower.pool.waterdrop.WaterDrop.IWaterDropListener;
import eu.nazgee.game.flower.score.Score;
import eu.nazgee.game.flower.sun.Sun;
import eu.nazgee.game.flower.sun.Sun.TravelListener;
import eu.nazgee.game.utils.engine.camera.SmoothTrackingCamera;
import eu.nazgee.game.utils.helpers.AtlasLoader;
import eu.nazgee.game.utils.helpers.TiledTextureRegionFactory;
import eu.nazgee.game.utils.loadable.SimpleLoadableResource;
import eu.nazgee.game.utils.scene.SceneLoadable;
import eu.nazgee.game.utils.track.TrackVector;

public class SceneMain extends SceneLoadable{
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int SEEDS_COUNT = 20;
	// ===========================================================
	// Fields
	// ===========================================================
	private final Score mScore = new Score(null);
	private final LoadableSFX mSFX;
	private final MyResources mResources = new MyResources();
	private final MainHUD mHud;
	private final LoadableParallaxBackground mLoadableParallaxBackground;

	private PopupPool mPopupPool;
	private Sky mSky;
	private CloudLayer mCloudLayer;
	private Sprite mGround;
	private Sun mSun;

	private final LinkedList<Flower> mFlowers = new LinkedList<Flower>();
	private final FlowerListener mFlowerListener = new FlowerListener();

	// ===========================================================
	// Constructors
	// ===========================================================
	public SceneMain(float W, float H,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pVertexBufferObjectManager);
		
		mSFX = new LoadableSFX();
		mHud = new MainHUD(W, H, pVertexBufferObjectManager);
		mLoadableParallaxBackground = new LoadableParallaxBackground(pVertexBufferObjectManager);
		getLoader().install(mResources);
		getLoader().install(mLoadableParallaxBackground);
		getLoader().install(mSFX);
		getLoader().install(mHud);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

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

		Random r = new Random();
		SmoothTrackingCamera camera = (SmoothTrackingCamera) e.getCamera();
		camera.setHUD(mHud);
		mScore.setHUD(mHud);
		mScore.set(0, SEEDS_COUNT, 0);

		mPopupPool = new PopupPool(mResources.FONT_POPUP, vbom);

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
		mSun = new Sun(0, 0, mResources.TEX_SUN, mResources.TEXS_SUNSHINE, vbom);
		attachChild(mSun);
		mSun.travel(0, getH()/2, getW() * 1.5f, getH()/2, 60, new SunTravelListener());
		camera.setTracking(mSun, new TrackVector(new Vector2(camera.getWidth() * 0.25f, 0)), 0);

		/*
		 * Create layer of Clouds
		 */
		mCloudLayer = new CloudLayer(0, 0, getW() * 1.5f, getH()/3,
				getW() * 0.1f, 10, 0.2f, 0.2f, 6, mSky,
				mResources.TEXS_CLOUDS, mResources.TEX_WATERDROP, 
				mResources.TEXS_SPLASH, vbom);
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
		for (int i = 0; i < SEEDS_COUNT; i++) {
			/*
			 * Choose random texture
			 */
			ITextureRegion tex = mResources.TEXS_FLOWERS.getTextureRegion(
					r.nextInt(mResources.TEXS_FLOWERS.getTileCount()));

			/*
			 *  Create a sprite
			 */
			Flower flower = new Flower(0, 0, tex, mResources.TEX_POT, mResources.TEXS_POT_WATER, getVertexBufferObjectManager());
			flower.setZIndex(-1);
			flower.setFlowerStateHandler(mFlowerListener);

			flower.setPosition(getW() * r.nextFloat(), getH() * r.nextFloat());
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
	private class FlowerListener implements IFlowerStateHandler {
		@Override
		public void onBloomed(Flower pFlower) {
			SceneMain.this.postRunnable(new FlowerDeactivateRunnable(pFlower));
			mSFX.onFlowerBloom();
			mScore.score.inc(100);
			mScore.flowers.inc(1);
			mScore.seeds.dec(1);

			PopupItem item = mPopupPool.obtainPoolItem();
			item.getEntity().pop(pFlower, "+100$", 1.5f);
			attachChild(item.getEntity());
		}

		@Override
		public void onFried(Flower pFlower) {
			SceneMain.this.postRunnable(new FlowerDeactivateRunnable(pFlower));
			mSFX.onFlowerFry();
			mScore.seeds.dec(1);

			PopupItem item = mPopupPool.obtainPoolItem();
			item.getEntity().pop(pFlower, "fried!", 1f);
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
			pSun.travel(0, getH()/2, getW() * 1.5f, getH()/2, 40, this);
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
					SceneMain.this.postRunnable(new FlowerTouchRunnable(flower, pSceneTouchEvent.isActionUp()));
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

	private static class MyResources extends SimpleLoadableResource {
		public ITextureRegion TEX_SUN;
		public ITiledTextureRegion TEXS_SUNSHINE;

		public ITextureRegion TEX_WATERDROP;
		public ITiledTextureRegion TEXS_SPLASH;

		public ITextureRegion TEX_POT;
		public ITiledTextureRegion TEXS_FLOWERS;
		public ITiledTextureRegion TEXS_POT_WATER;

		public ITiledTextureRegion TEXS_CLOUDS;

		private BuildableBitmapTextureAtlas[] mAtlases;
		public Font FONT_POPUP;
		private ITexture mFontAtlas;

		@Override
		public void onLoadResources(Engine e, Context c) {
			mAtlases = new BuildableBitmapTextureAtlas[1];
			for (int i = 0; i < mAtlases.length; i++) {
				mAtlases[i] = new BuildableBitmapTextureAtlas(e.getTextureManager(), 1024, 1024, TextureOptions.REPEATING_BILINEAR);
			}
			/*
			 * Create nicely named shortcuts to our atlases (textures)
			 */
			BuildableBitmapTextureAtlas atlasFlower = mAtlases[0];
			BuildableBitmapTextureAtlas atlasWaterdrop = mAtlases[0];
			BuildableBitmapTextureAtlas atlasSunshine = mAtlases[0];
			BuildableBitmapTextureAtlas atlasClouds = mAtlases[0];
			BuildableBitmapTextureAtlas atlasPot = mAtlases[0];

			/*
			 * Fill our texture with regions that we would like to use
			 */
			TEX_SUN = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					atlasSunshine, c, "sun.png");
			TEXS_POT_WATER = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
					atlasPot, c, "pot/water.png", 1, 5);
			TEX_POT = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					atlasPot, c, "pot/pot.png");
			TEX_WATERDROP = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					atlasWaterdrop, c, "drop.png");
			TEXS_SPLASH = TiledTextureRegionFactory.loadTiles(c, "gfx/", "splash",
					atlasWaterdrop);
			TEXS_CLOUDS = TiledTextureRegionFactory.loadTiles(c, "gfx/", "clouds",
					atlasClouds);
			TEXS_SUNSHINE = TiledTextureRegionFactory.loadTiles(c, "gfx/", "shine",
					atlasSunshine);
//			TEXS_SUNSHINE = BitmapTextureAtlasTextureRegionFactory.createTiledFromAssetDirectory(
//					atlasSunshine, c.getAssets(), "shine");
			/*
			 *  note: SVGs must be rasterized before rendering to texture, so size must be provided
			 */
			TEXS_FLOWERS = TiledTextureRegionFactory.loadTilesSVG(c, "gfx/", "flowers",
					atlasFlower, Consts.FLOWER_TEX_WIDTH, Consts.FLOWER_TEX_HEIGHT);
//			TEXS_FLOWERS = SVGBitmapTextureAtlasTextureRegionFactory.createTiledFromAssetDirectory(
//					atlasFlower, c, "flowers", Consts.FLOWER_TEX_WIDTH, Consts.FLOWER_TEX_HEIGHT);

		}

		@Override
		public void onLoad(Engine e, Context c) {
			/*
			 *  build and load all our atlases (places regions on texture and sends it to GPU)
			 */
			AtlasLoader.buildAndLoad(mAtlases);

			final TextureManager textureManager = e.getTextureManager();
			final FontManager fontManager = e.getFontManager();

			mFontAtlas = new BitmapTextureAtlas(textureManager, 512, 256, TextureOptions.BILINEAR);
			FONT_POPUP = FontFactory.createFromAsset(fontManager, mFontAtlas, c.getAssets(), Consts.HUD_FONT, Consts.CAMERA_HEIGHT*0.1f, true, Color.WHITE.getARGBPackedInt());
			FONT_POPUP.load();
		}

		@Override
		public void onUnload() {
			for (BuildableBitmapTextureAtlas atlas : mAtlases) {
				atlas.unload();
			}
			FONT_POPUP.unload();
			mFontAtlas.unload();
		}
	}
}