package eu.nazgee.game.flower;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
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
import eu.nazgee.game.utils.loadable.SimpleLoadableResource;
import eu.nazgee.game.utils.scene.SceneLoader;
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
	private SceneMain mSceneMain;
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
		final Camera camera = new Camera(0, 0, Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT);
		return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT), camera);
	}

	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		FontFactory.setAssetBasePath("fonts/");

		// Load all our resources- these are the only resources
		// that have to be loaded manually (mResources.FONT_MENU will be ready to use)
		mResources.loadResources(getEngine(), this);
		mResources.load(getEngine(), this);

		mSceneMain = new SceneMain(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, getVertexBufferObjectManager());

		// Create "Loading..." scene that will be used for all loading-related activities
		SceneLoading loadingScene = new SceneLoading(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, mResources.FONT_MENU, "Loading...", getVertexBufferObjectManager());

		mLoader = new SceneLoader(loadingScene);
		mLoader.setLoadingSceneHandling(eLoadingSceneHandling.SCENE_SET_ACTIVE).setLoadingSceneUnload(false);

		// loadResources() and load() are usually called from a "loader" whic shows "loading..." scene.
		// Let's call it manually for the sake of simplicity
		// UPDATE: do not load it manually - mLoader will do it from onCreateScene()
//		mSceneMain.loadResources(getEngine(), this);
//		mSceneMain.load(getEngine(), this);
	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());


		/*
		 * At first, engine will show "Loading..." scene. mSceneMain will be
		 * set as active scene right after it will be fully loaded (loading takes plac in background). 
		 */
		mLoader.loadScene(mSceneMain, getEngine(), this, null);
		return mLoader.getLoadingScene();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	private static class MyResources extends SimpleLoadableResource {
		public Font FONT_MENU;

		@Override
		public void onLoadResources(Engine e, Context c) {
			final TextureManager textureManager = e.getTextureManager();
			final FontManager fontManager = e.getFontManager();

			final ITexture font_texture = new BitmapTextureAtlas(textureManager, 512, 256, TextureOptions.BILINEAR);
			FONT_MENU = FontFactory.createFromAsset(fontManager, font_texture, c.getAssets(), Consts.MENU_FONT, Consts.CAMERA_HEIGHT*0.10f, true, Color.WHITE.getARGBPackedInt());
		}

		@Override
		public void onLoad(Engine e, Context c) {
			FONT_MENU.load();
		}

		@Override
		public void onUnload() {
			FONT_MENU.unload();
		}
	}
}