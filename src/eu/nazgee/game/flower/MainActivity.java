package eu.nazgee.game.flower;

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
import org.andengine.util.color.Color;

import android.content.Context;
import android.view.KeyEvent;
import eu.nazgee.game.flower.scene.ingame.MenuIngame;
import eu.nazgee.game.flower.scene.main.SceneMain;
import eu.nazgee.game.flower.scene.over.MenuGameOver;
import eu.nazgee.game.utils.engine.camera.SmoothTrackingCamera;
import eu.nazgee.game.utils.engine.camera.SmootherEmpty;
import eu.nazgee.game.utils.engine.camera.SmootherLinear;
import eu.nazgee.game.utils.loadable.ILoadableResourceScene;
import eu.nazgee.game.utils.loadable.SimpleLoadableResource;
import eu.nazgee.game.utils.scene.SceneLoader;
import eu.nazgee.game.utils.scene.SceneLoader.ISceneLoaderListener;
import eu.nazgee.game.utils.scene.SceneLoader.eLoadingSceneHandling;
import eu.nazgee.game.utils.scene.SceneLoading;

public class MainActivity extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	MyResources mResources = new MyResources();
	MenuItemClickListener mMenuItemClickListener = new MenuItemClickListener();
	private SceneMain mSceneMain;
	private MenuIngame mMenuIngame;
	private MenuGameOver mMenuGameOver;
	private SceneLoader mLoader;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
//		final Camera camera = new SmoothCamera(0, 0, Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, Consts.CAMERA_WIDTH * 2.5f, Consts.CAMERA_HEIGHT * 2.5f, 0);
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

		// Load all our resources- these are the only resources
		// that have to be loaded manually (mResources.FONT_MENU will be ready to use)
		mResources.getLoader().install(new Statics());
		mResources.loadResources(getEngine(), this);
		mResources.load(getEngine(), this);

		mSceneMain = new SceneMain(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, getVertexBufferObjectManager());

		// Create "Loading..." scene that will be used for all loading-related activities
		SceneLoading loadingScene = new SceneLoading(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, mResources.FONT_MENU, "Loading...", getVertexBufferObjectManager());

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
		 * set as active scene right after it will be fully loaded (loading takes plac in background). 
		 */
		loadMainScene();
		return mLoader.getLoadingScene();
	}

	private void loadMainScene() {
		mLoader.loadScene(mSceneMain, getEngine(), this, new ISceneLoaderListener() {
			@Override
			public void onSceneLoaded(Scene pScene) {
				/*
				 * Only first scene has to be loaded with SCENE_DONT_TOUCH. 
				 * Other scenes should be loaded with SCENE_SET_ACTIVE or SCENE_SET_CHILD
				 * to make "Loading..." scene visible
				 */
				mLoader.setLoadingSceneHandling(eLoadingSceneHandling.SCENE_SET_CHILD);
				mLoader.setChildSceneModalDraw(false).setChildSceneModalTouch(true).setChildSceneModalUpdate(true);
				mLoader.getLoadingScene().setBackgroundEnabled(false);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_BACK)
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			if (getEngine().getScene() == mSceneMain) {
				loadSubscene(mMenuIngame);
				return true;
			} else {
				throw new RuntimeException("wtf?!");
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	protected void loadSubscene(ILoadableResourceScene pScene) {
		if (getEngine().getScene().getChildScene() != null) {
			mLoader.unloadEveryYoungerScene(getEngine().getScene()); // unload current childmenu
			getEngine().getScene().back();
		} else {
			mLoader.loadChildScene(mMenuIngame, getEngine(), this, null);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public void restartScene() {
		mLoader.unloadEveryYoungerScene(getEngine().getScene()); // unload current childmenu
		getEngine().getScene().back();

		mSceneMain.unload();
		mLoader.setLoadingSceneHandling(eLoadingSceneHandling.SCENE_SET_ACTIVE);
		mLoader.getLoadingScene().setBackgroundEnabled(true);
		loadMainScene();
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
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
					restartScene();
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
					restartScene();
					return true;
				default:
					break;
				}
			}
			return false;
		}
	}

	private static class MyResources extends SimpleLoadableResource {
		public Font FONT_MENU;

		@Override
		public void onLoadResources(Engine e, Context c) {
		}

		@Override
		public void onLoad(Engine e, Context c) {
			final TextureManager textureManager = e.getTextureManager();
			final FontManager fontManager = e.getFontManager();

			final ITexture font_texture = new BitmapTextureAtlas(textureManager, 512, 256, TextureOptions.BILINEAR);
			FONT_MENU = FontFactory.createFromAsset(fontManager, font_texture, c.getAssets(), Consts.MENU_FONT, Consts.CAMERA_HEIGHT*0.10f, true, Color.WHITE.getARGBPackedInt());
			FONT_MENU.load();
		}

		@Override
		public void onUnload() {
			FONT_MENU.unload();
		}
	}
}