package eu.nazgee.flower;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.Entity;
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
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.color.Color;

import android.content.Context;
import android.os.Bundle;
import eu.nazgee.flower.base.buttonscene.SceneButtons;
import eu.nazgee.flower.base.buttonscene.SceneButtonsMessagebox;
import eu.nazgee.flower.base.pagerscene.ScenePager;
import eu.nazgee.game.utils.loadable.LoadableResourceSimple;

public abstract class BaseActivityPager<T extends Entity> extends SimpleBaseGameActivity{

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	protected SceneButtons mSceneInfo;
	private ScenePager<T> mScenePager;
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
	abstract protected ScenePager<T> populatePagerScene(final float w, final float h, final VertexBufferObjectManager pVertexBufferObjectManager);

	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
//		final SmoothTrackingCamera camera = new SmoothTrackingCamera(0, 0, Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, 0, new SmootherLinear(5), new SmootherEmpty(), new SmootherLinear(5));
		Camera camera = new SmoothCamera(0, 0, Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, Consts.CAMERA_WIDTH * 3, Consts.CAMERA_HEIGHT * 3, 1);

		EngineOptions engopts = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new FillResolutionPolicy(), camera);
		engopts.getRenderOptions().setDithering(false);
		return engopts;
	}

	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		FontFactory.setAssetBasePath("fonts/");

		mResources.loadResources(getEngine(), this);
		mResources.load(getEngine(), this);
	}

	@Override
	protected Scene onCreateScene() {
		mEngine.registerUpdateHandler(new FPSLogger());
		Camera camera = getEngine().getCamera();

		mScenePager = populatePagerScene(camera.getWidth(), camera.getHeight(), getVertexBufferObjectManager());
		mScenePager.loadResources(getEngine(), this);
		mScenePager.load(getEngine(), this);

		return mScenePager;
	}

	@Override
	public void onDestroyResources() throws Exception {
		super.onDestroyResources();
		if (mScenePager != null)
			mScenePager.unload();

		mResources.unload();
	}
	// ===========================================================
	// Methods
	// ===========================================================

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
}