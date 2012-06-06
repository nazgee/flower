package eu.nazgee.flower.activity.game;

import java.util.List;

import org.andengine.engine.Engine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.color.Color;

import android.content.Context;
import android.view.KeyEvent;
import eu.nazgee.flower.Consts;
import eu.nazgee.flower.activity.game.scene.ingame.MenuIngame;
import eu.nazgee.flower.activity.game.scene.main.SceneGame;
import eu.nazgee.flower.activity.game.scene.over.MenuGameOver;
import eu.nazgee.flower.activity.game.scene.shop.SceneSeedsShop;
import eu.nazgee.flower.activity.game.scene.shop.SceneSeedsShop.IShoppingListener;
import eu.nazgee.flower.activity.game.scene.shop.SeedItem;
import eu.nazgee.flower.base.pagerscene.ScenePager.IItemClikedListener;
import eu.nazgee.flower.level.GameLevel;
import eu.nazgee.flower.seed.Seed;
import eu.nazgee.game.utils.engine.camera.SmoothTrackingCamera;
import eu.nazgee.game.utils.engine.camera.SmootherEmpty;
import eu.nazgee.game.utils.engine.camera.SmootherLinear;
import eu.nazgee.game.utils.loadable.ILoadableResourceScene;
import eu.nazgee.game.utils.loadable.LoadableResourceSimple;
import eu.nazgee.game.utils.scene.SceneLoader;
import eu.nazgee.game.utils.scene.SceneLoader.ISceneLoaderListener;
import eu.nazgee.game.utils.scene.SceneLoader.eLoadingSceneHandling;
import eu.nazgee.game.utils.scene.SceneLoading;

public class ActivityGame extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final String BUNDLE_LEVEL_ID = "levelid";
	// ===========================================================
	// Fields
	// ===========================================================


	MenuItemClickListener mMenuItemClickListener = new MenuItemClickListener();
	private SceneGame mSceneGame;
	private SceneSeedsShop mSceneShop;
	private MenuIngame mMenuIngame;
	private MenuGameOver mMenuGameOver;
	private SceneLoader mLoader;
	private final MyResources mResources = new MyResources();

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public MyResources getStaticResources() {
		return mResources;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		final SmoothTrackingCamera camera = new SmoothTrackingCamera(0, 0, Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, 0, new SmootherLinear(5), new SmootherEmpty(), new SmootherLinear(5));
		EngineOptions engopts = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT), camera);
		engopts.getRenderOptions().setDithering(true);
		engopts.getAudioOptions().setNeedsSound(true);
		return engopts;
	}

	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		FontFactory.setAssetBasePath("fonts/");

		// Make sure statics are ready to use for anyone
		mResources.loadResources(getEngine(), this);
		mResources.load(getEngine(), this);

		mSceneGame = new SceneGame(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, mResources.ENTITY_DETACH_HANDLER, getVertexBufferObjectManager());
		mSceneShop = new SceneSeedsShop(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, getVertexBufferObjectManager(), GameLevel.LEVEL1, getStaticResources().FONT_DESC, mResources.ENTITY_DETACH_HANDLER);
		mSceneShop.setItemClikedListener(new IItemClikedListener<SeedItem>() {
			@Override
			public void onItemClicked(SeedItem pItem) {
				// play sound? do something?
			}
		});
		mSceneShop.setShoppingListener(new IShoppingListener() {
			@Override
			public void onShoppingFinished(List<Seed> pBoughtItems) {
				mLoader.setChildSceneModalDraw(true); // we do NOT want to see the background scene for the sake of framerate
				loadSubscene(mSceneGame);
			}
		});

		// Create "Loading..." scene that will be used for all loading-related activities
		SceneLoading loadingScene = new SceneLoading(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, getStaticResources().FONT_DESC, "Loading...", getVertexBufferObjectManager());

		mLoader = new SceneLoader(loadingScene);
		mLoader.setLoadingSceneHandling(eLoadingSceneHandling.SCENE_DONT_TOUCH).setLoadingSceneUnload(false);

		/*
		 * Create menu scenes
		 */
		final TextureManager textureManager = getTextureManager();
		final FontManager fontManager = getFontManager();

		final ITexture textureFontHud = new BitmapTextureAtlas(textureManager, 512, 256, TextureOptions.BILINEAR);
		Font mFont = FontFactory.createFromAsset(fontManager, textureFontHud, getAssets(), Consts.MENU_FONT, Consts.CAMERA_HEIGHT*0.10f, true, Color.WHITE.getARGBPackedInt());
		mFont.load();
		final ITexture textureFontHudSmall = new BitmapTextureAtlas(textureManager, 256, 256, TextureOptions.BILINEAR);
		Font mFontDesc = FontFactory.createFromAsset(fontManager, textureFontHudSmall, getAssets(), Consts.MENU_FONT, Consts.CAMERA_HEIGHT*0.06f, true, Color.WHITE.getARGBPackedInt());
		mFontDesc.load();

		mMenuIngame = new MenuIngame(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, getEngine().getCamera(), mFont, getVertexBufferObjectManager());
		mMenuIngame.setOnMenuItemClickListener(mMenuItemClickListener);
		mMenuGameOver = new MenuGameOver(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, getEngine().getCamera(), mFont, mFontDesc, getVertexBufferObjectManager());
		mMenuGameOver.setOnMenuItemClickListener(mMenuItemClickListener);
	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		/*
		 * At first, engine will show "Loading..." scene. mSceneMain will be
		 * set as active scene right after it will be fully loaded (loading takes place in background). 
		 */
		loadScene(mSceneShop);
		return mLoader.getLoadingScene();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_BACK)
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			Scene youngest = SceneLoader.getYoungestScene(getEngine().getScene());

			if (youngest == mMenuIngame) {
				loadSubscene(null);
				return true;
			} else {
				mLoader.setChildSceneModalDraw(false); // we want to see the background scene
				loadSubscene(mMenuIngame);
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onDestroyResources() throws Exception {
		super.onDestroyResources();
		mResources.unload();
		mLoader.unloadEveryYoungerSceneWithGivenCallBack(getEngine().getScene());
	}

	// ===========================================================
	// Methods
	// ===========================================================
	private void restart() {
		mLoader.unloadEveryYoungerSceneWithGivenCallBack(getEngine().getScene());

		mLoader.setLoadingSceneHandling(eLoadingSceneHandling.SCENE_SET_ACTIVE);
		mLoader.getLoadingScene().setBackgroundEnabled(true);
		loadScene(mSceneShop);
	}

	private void loadSubscene(ILoadableResourceScene pScene) {
		if (pScene == null) {
			SceneLoader.unloadYoungestChildSceneCallBack(getEngine().getScene());
		} else {
			mLoader.loadChildSceneNested(pScene, getEngine(), this, null);
		}
	}

	private void loadScene(ILoadableResourceScene pScene) {
		mLoader.loadScene(pScene, getEngine(), this, new ISceneLoaderListener() {
			@Override
			public void onSceneLoaded(Scene pScene) {
				/*
				 * Only the top scene scene has to be loaded with SCENE_DONT_TOUCH. 
				 * Other scenes should be loaded with SCENE_SET_ACTIVE or SCENE_SET_CHILD
				 * to make "Loading..." scene visible
				 */
				mLoader.setLoadingSceneHandling(eLoadingSceneHandling.SCENE_SET_CHILD);
				mLoader.getLoadingScene().setBackgroundEnabled(false);
			}
		});
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public class MyResources extends LoadableResourceSimple {
		public EntityDetachRunnablePoolUpdateHandler ENTITY_DETACH_HANDLER;
		public Font FONT_DESC;

		@Override
		public void onLoadResources(Engine e, Context c) {
		}

		@Override
		public void onLoad(Engine e, Context c) {
			e.unregisterUpdateHandler(ENTITY_DETACH_HANDLER);

			ENTITY_DETACH_HANDLER = new EntityDetachRunnablePoolUpdateHandler();
			e.registerUpdateHandler(ENTITY_DETACH_HANDLER);

			final TextureManager textureManager = e.getTextureManager();
			final FontManager fontManager = e.getFontManager();

			final ITexture font_texture = new BitmapTextureAtlas(textureManager, 512, 256, TextureOptions.BILINEAR);
			FONT_DESC = FontFactory.createFromAsset(fontManager, font_texture, c.getAssets(), Consts.MENU_FONT, Consts.CAMERA_HEIGHT*0.1f, true, Color.WHITE.getARGBPackedInt());
			FONT_DESC.load();
		}

		@Override
		public void onUnload() {
			FONT_DESC.unload();
		}
	}

	private class MenuItemClickListener implements IOnMenuItemClickListener {
		@Override
		public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
				float pMenuItemLocalX, float pMenuItemLocalY) {
			if (pMenuScene == mMenuIngame) {
				switch (pMenuItem.getID()) {
				case MenuIngame.MENU_GO_MAIN:
					finish();
					break;
				case MenuIngame.MENU_RESET:
					restart();
					return true;
				default:
					break;
				}
			} else if (pMenuScene == mMenuGameOver) {
				switch (pMenuItem.getID()) {
				case MenuIngame.MENU_GO_MAIN:
					finish();
					break;
				case MenuIngame.MENU_RESET:
					restart();
					return true;
				default:
					break;
				}
			}
			return false;
		}
	}
}