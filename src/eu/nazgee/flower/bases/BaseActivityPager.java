package eu.nazgee.flower.bases;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntityParameterCallable;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.os.Bundle;
import eu.nazgee.flower.Consts;
import eu.nazgee.flower.base.pagerscene.ScenePager;

public abstract class BaseActivityPager<T extends Entity> extends SimpleBaseGameActivity{

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
//	protected SceneButtons mSceneInfo;
	private ScenePager<T> mScenePager;
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	abstract protected ScenePager<T> populatePagerScene(final float w, final float h, final VertexBufferObjectManager pVertexBufferObjectManager);

	@Override
	protected void onCreate(final Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
//		final SmoothTrackingCamera camera = new SmoothTrackingCamera(0, 0, Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, 0, new SmootherLinear(5), new SmootherEmpty(), new SmootherLinear(5));
		final Camera camera = new SmoothCamera(0, 0, Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, Consts.CAMERA_WIDTH * 3, Consts.CAMERA_HEIGHT * 3, 1);

		final EngineOptions engopts = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new FillResolutionPolicy(), camera);
		engopts.getRenderOptions().setDithering(true);
		return engopts;
	}

	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		FontFactory.setAssetBasePath("fonts/");
	}

	@Override
	protected Scene onCreateScene() {
		mEngine.registerUpdateHandler(new FPSLogger());
		final Camera camera = getEngine().getCamera();

		mScenePager = populatePagerScene(camera.getWidth(), camera.getHeight(), getVertexBufferObjectManager());
		mScenePager.loadResources(getEngine(), this);
		mScenePager.load(getEngine(), this);

		return mScenePager;
	}

	@Override
	public void onDestroyResources() throws IOException {
		super.onDestroyResources();
		if (mScenePager != null) {
			mScenePager.unload();
		}

	}
	// ===========================================================
	// Methods
	// ===========================================================
	public void callOnEveryItem(IEntityParameterCallable pCallable) {
		mScenePager.callOnEveryItem(pCallable);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}