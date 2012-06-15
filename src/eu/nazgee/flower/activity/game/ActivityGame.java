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
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePack;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackLoader;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackTextureRegionLibrary;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.exception.TexturePackParseException;
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
import org.andengine.util.debug.Debug;

import android.content.Context;
import android.view.KeyEvent;
import eu.nazgee.flower.Consts;
import eu.nazgee.flower.FisheyeShaderProgram;
import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.activity.game.scene.game.SceneGame;
import eu.nazgee.flower.activity.game.scene.game.SceneGame.IGameListener;
import eu.nazgee.flower.activity.game.scene.ingame.MenuIngame;
import eu.nazgee.flower.activity.game.scene.over.MenuGameLost;
import eu.nazgee.flower.activity.game.scene.shop.SceneSeedsShop;
import eu.nazgee.flower.activity.game.scene.shop.SceneSeedsShop.IShoppingListener;
import eu.nazgee.flower.activity.game.scene.shop.SeedItem;
import eu.nazgee.flower.base.pagerscene.ScenePager.IItemClikedListener;
import eu.nazgee.flower.flower.Seed;
import eu.nazgee.flower.level.GameLevel;
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
	private MenuGameLost mMenuGameOver;
	private SceneLoader mLoader;
	private final MyResources mResources = new MyResources();
	private TexturesLibrary mTexturesLibrary = new TexturesLibrary();


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

		// Load spritesheets
		mTexturesLibrary.loadResources(getEngine(), this);
		mTexturesLibrary.load(getEngine(), this);

		// Make sure custom shaders are available
		getEngine().getShaderProgramManager().loadShaderProgram(FisheyeShaderProgram.getInstance());

		// Make sure statics are ready to use for anyone
		mResources.loadResources(getEngine(), this);
		mResources.load(getEngine(), this);

		// SCENE: seeds shop
		mSceneShop = new SceneSeedsShop(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, getVertexBufferObjectManager(), GameLevel.LEVEL1, getStaticResources().FONT, mResources.ENTITY_DETACH_HANDLER, mTexturesLibrary);
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

		// SCENE: gameplay
		mSceneGame = new SceneGame(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, getVertexBufferObjectManager(), mResources.ENTITY_DETACH_HANDLER, mSceneShop.getShop(), mTexturesLibrary);
		mSceneGame.setGameListerner(new IGameListener() {
			@Override
			public void onGameFinished() {
				mLoader.setChildSceneModalDraw(false); // we do WANT to see the background scene for the sake of wow-factor :)
				loadSubscene(mMenuGameOver);
			}
		});

		// SCENE: in-game menu
		mMenuIngame = new MenuIngame(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, getEngine().getCamera(), mResources.FONT, getVertexBufferObjectManager());
		mMenuIngame.setOnMenuItemClickListener(mMenuItemClickListener);

		// SCENE: game over menu
		mMenuGameOver = new MenuGameLost(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, getEngine().getCamera(), mResources.FONT, mResources.FONT, getVertexBufferObjectManager(), mTexturesLibrary);
		mMenuGameOver.setOnMenuItemClickListener(mMenuItemClickListener);
		mMenuGameOver.setDescription("this is a game over scene stub");

		// Create "Loading..." scene that will be used for all loading-related activities
		SceneLoading loadingScene = new SceneLoading(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, getStaticResources().FONT, "Loading...", getVertexBufferObjectManager());

		mLoader = new SceneLoader(loadingScene);
		mLoader.setLoadingSceneHandling(eLoadingSceneHandling.SCENE_DONT_TOUCH).setLoadingSceneUnload(false);
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

			if (youngest == mMenuGameOver || youngest == mMenuGameOver) {
				return false;
			} else if (youngest == mMenuIngame) {
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
		public Font FONT;

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
			FONT = FontFactory.createFromAsset(fontManager, font_texture, c.getAssets(), Consts.MENU_FONT, Consts.CAMERA_HEIGHT*0.1f, true, Color.WHITE.getARGBPackedInt());
			FONT.load();
		}

		@Override
		public void onUnload() {
			FONT.unload();
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