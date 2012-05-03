package eu.nazgee.game.flower.scene;

import java.util.LinkedList;
import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.Constants;

import android.content.Context;
import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;

import eu.nazgee.game.flower.Consts;
import eu.nazgee.game.flower.MainHUD;
import eu.nazgee.game.flower.pool.cloud.Cloud;
import eu.nazgee.game.flower.pool.waterdrop.WaterDrop;
import eu.nazgee.game.flower.scene.CloudLayer.IRainDropListener;
import eu.nazgee.game.flower.scene.flower.Flower;
import eu.nazgee.game.flower.scene.flower.Flower.IFlowerStateHandler;
import eu.nazgee.game.flower.scene.flower.Flower.eLevel;
import eu.nazgee.game.flower.scene.sun.Sun;
import eu.nazgee.game.flower.scene.sun.Sun.TravelListener;
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

	// ===========================================================
	// Fields
	// ===========================================================
	private MyResources mResources = new MyResources();
	private MainHUD mHud;
	LinkedList<Flower> mFlowers = new LinkedList<Flower>();
	private SmoothTrackingCamera mCamera;
	private Sun mSun;
	private CloudLayer mCloudLayer;
	private Sky mSky;
	private Sprite mGround;


	private final FlowerListener mFlowerListener = new FlowerListener();
	// ===========================================================
	// Constructors
	// ===========================================================
	public SceneMain(float W, float H,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pVertexBufferObjectManager);
		
		mHud = new MainHUD(W, H, pVertexBufferObjectManager);
		getLoader().install(mResources);
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
		 * No need to do anything special here. If this method is called, it means
		 * that all our resources must have been loaded already.
		 * i.e.: mResources were installed in SceneMain constructor, and were
		 * loaded right before this method was called.
		 */
	}

	@Override
	public void onLoad(Engine e, Context c) {
		Random r = new Random();
		mCamera = (SmoothTrackingCamera) e.getCamera();
		mCamera.setHUD(mHud);

		/*
		 * Prepare fancy background		
		 */
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		final Sprite bgSky = new Sprite(0, 0, mResources.TEX_SKY, vertexBufferObjectManager);
		final Sprite bgFar = new Sprite(0, getH() - mResources.TEX_GROUND.getHeight() - mResources.TEX_BG_FAR.getHeight(), mResources.TEX_BG_FAR, vertexBufferObjectManager);
		final Sprite bgClose = new Sprite(0, getH() - mResources.TEX_GROUND.getHeight() - mResources.TEX_BG_CLOSE.getHeight(), mResources.TEX_BG_CLOSE, vertexBufferObjectManager);
		mGround = new Sprite(0, getH() - mResources.TEX_GROUND.getHeight(), mResources.TEX_GROUND, vertexBufferObjectManager);
//		final Sprite bgGrass = new Sprite(0, getH() - mResources.TEX_GRASS.getHeight()*0.7f, mResources.TEX_GRASS, vertexBufferObjectManager);
		final ParallaxBackground paralaxBG = new CameraParallaxBackground(0, 0, 0, mCamera);
		paralaxBG.attachParallaxEntity(new ParallaxEntity(-0.1f, bgSky));
		paralaxBG.attachParallaxEntity(new ParallaxEntity(-0.25f, bgFar));
		paralaxBG.attachParallaxEntity(new ParallaxEntity(-0.5f, bgClose));
		paralaxBG.attachParallaxEntity(new ParallaxEntity(-1f, mGround));
//		paralaxBG.attachParallaxEntity(new ParallaxEntity(-1.5f, bgGrass));
		setBackground(paralaxBG);

		mSky = new Sky(mGround.getY());

		/*
		 * Register touch area listener, which will listen for the touches of
		 * registered objects
		 */
		setOnAreaTouchListener(new MyAreaTouchListener());
		setTouchAreaBindingOnActionDownEnabled(true);
		setTouchAreaBindingOnActionMoveEnabled(true);

		/**
		 * Create a Sun
		 */
		mSun = new Sun(0, 0, mResources.TEX_SUN, mResources.TEXS_SUNSHINE, vertexBufferObjectManager);
		attachChild(mSun);
		mSun.travel(0, getH()/2, getW() * 1.5f, getH()/2, 60, new SunTravelListener());

		/**
		 * Create layer of Clouds
		 */
		mCloudLayer = new CloudLayer(0, 0, getW() * 1.5f, getH()/3,
				getW() * 0.1f, 10, 0.2f, 0.2f, 6, mSky,
				mResources.TEXS_CLOUDS, mResources.TEX_WATERDROP, 
				mResources.TEXS_SPLASH, vertexBufferObjectManager);
		attachChild(mCloudLayer);
		mCloudLayer.setRainDropListener(new IRainDropListener() {
			@Override
			public void onRainDrop(WaterDrop pWaterDrop) {
				handleFlowerRain(pWaterDrop);
			}
		});

		/*
		 * Create some flowers
		 */
		for (int i = 0; i < 20; i++) {
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
			postRunnable(new TouchHandler(flower, true));
		}

		mCamera.setTracking(mSun, new TrackVector(new Vector2(Consts.CAMERA_WIDTH*0.25f, 0)), 0);
		/*
		 * update sun rays
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
		 *  We do not need anything of theese anymore- kill all children and
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
			if (mSun.isShiningAt(flower)) {
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
			SceneMain.this.postRunnable(new FlowerIdleMaker(pFlower));
		}

		@Override
		public void onFried(Flower pFlower) {
		}

		@Override
		public void onWaterLevelChanged(Flower pFlower, eLevel pOld, eLevel pNew) {
		}

		@Override
		public void onSunLevelChanged(Flower pFlower, eLevel pOld, eLevel pNew) {
		}

		class FlowerIdleMaker implements Runnable {
			private final Flower mFlower;

			public FlowerIdleMaker(Flower mFlower) {
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
				if ((pTouchArea instanceof Flower)) {
					Flower flower = (Flower) pTouchArea;
					flower.setPosition(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());

					if (pSceneTouchEvent.isActionUp() || pSceneTouchEvent.isActionDown()) {
						SceneMain.this.postRunnable(new TouchHandler(flower, pSceneTouchEvent.isActionUp()));
					}
				}
				return true;
			}
			return false;
		}
	}

	private class TouchHandler implements Runnable {
		private final Flower mFlower;
		private final boolean mUp;
		public TouchHandler(Flower pSprite, boolean pIsUp) {
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

	/**
	 * Listens to scene touch events and moves the camera
	 * background
	 * @author nazgee
	 */
	private static class MyTouchListener implements IOnSceneTouchListener {
		private float mTouchX = 0, mTouchOffsetX = 0;
		private Camera mCamera;
		private ParallaxBackground mParallaxBackground;

		public MyTouchListener(Camera pCamera,
				ParallaxBackground pParallaxBackground) {
			mCamera = pCamera;
			mParallaxBackground = pParallaxBackground;
		}

		@Override
		public boolean onSceneTouchEvent(Scene pScene, TouchEvent pTouchEvent) {
			if (pTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
				mTouchX = pTouchEvent.getMotionEvent().getX();
			} else if (pTouchEvent.getAction() == MotionEvent.ACTION_MOVE) {
				float newX = pTouchEvent.getMotionEvent().getX();

				mTouchOffsetX = (newX - mTouchX);
				float newScrollX = mCamera.getCenterX() - mTouchOffsetX;

				mCamera.setCenter(newScrollX, mCamera.getCenterY());
				mTouchX = newX;
			}
			return true;
		}
	}

	private static class MyResources extends SimpleLoadableResource {
		public ITiledTextureRegion TEXS_FLOWERS;
		public ITiledTextureRegion TEXS_CLOUDS;
		public ITiledTextureRegion TEXS_SPLASH;
		public ITiledTextureRegion TEXS_SUNSHINE;
		public ITiledTextureRegion TEXS_POT_WATER;
		public ITextureRegion TEX_POT;
		public ITextureRegion TEX_BG_FAR;
		public ITextureRegion TEX_BG_CLOSE;
		public ITextureRegion TEX_GRASS;
		public ITextureRegion TEX_GROUND;
		private ITextureRegion TEX_SKY;
		private ITextureRegion TEX_SUN;
		private ITextureRegion TEX_WATERDROP;
		private BuildableBitmapTextureAtlas[] mAtlases;

		@Override
		public void onLoadResources(Engine e, Context c) {
			mAtlases = new BuildableBitmapTextureAtlas[3];
			mAtlases[0] =  new BuildableBitmapTextureAtlas(e.getTextureManager(), 962, 482, TextureOptions.REPEATING_BILINEAR);
			for (int i = 1; i < mAtlases.length; i++) {
				mAtlases[i] = new BuildableBitmapTextureAtlas(e.getTextureManager(), 1024, 1024, TextureOptions.REPEATING_BILINEAR);
			}
			/*
			 * Create nicely named shortcuts to our atlases (textures)
			 */
			BuildableBitmapTextureAtlas atlasSky = mAtlases[0];
			BuildableBitmapTextureAtlas atlasFlower = mAtlases[1];
			BuildableBitmapTextureAtlas atlasSplash = mAtlases[1];
			BuildableBitmapTextureAtlas atlasSunshine = mAtlases[1];
			BuildableBitmapTextureAtlas atlasClouds = mAtlases[1];
			BuildableBitmapTextureAtlas atlasScene = mAtlases[2];

			/*
			 * Fill our texture with regions that we would like to use
			 */
			TEX_SKY = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					atlasSky, c, "scene/skies/azure.jpeg");
			TEX_BG_FAR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					atlasScene, c, "scene/bg-far.png");
			TEX_BG_CLOSE = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					atlasScene, c, "scene/bg-close.png");
			TEX_GROUND = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					atlasScene, c, "scene/ground.png");
			TEX_GRASS = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					atlasScene, c, "scene/grass.png");
			TEX_SUN = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					atlasScene, c, "sun.png");
			TEXS_POT_WATER = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
					atlasScene, c, "pot/water.png", 1, 5);
			TEX_POT = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					atlasScene, c, "pot/pot.png");
			TEX_WATERDROP = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					atlasScene, c, "drop.png");
			TEXS_CLOUDS = TiledTextureRegionFactory.loadTiles(c, "gfx/", "clouds",
					atlasClouds);
			TEXS_SPLASH = TiledTextureRegionFactory.loadTiles(c, "gfx/", "splash",
					atlasSplash);
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
			 *  build and load all our atlases (places regions on texture and sends it to MCU)
			 */
			AtlasLoader.buildAndLoad(mAtlases);

			/*
			 *  Pretend it takes some time, so we can see "Loading..." scene
			 */
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		@Override
		public void onUnload() {
			for (BuildableBitmapTextureAtlas atlas : mAtlases) {
				atlas.unload();
			}
		}
	}
}