package eu.nazgee.game.flower;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.ui.activity.SimpleAsyncGameActivity;
import org.andengine.util.progress.IProgressListener;

public class MainActivity extends SimpleAsyncGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private SceneMain mSceneMain;

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
	public void onCreateResourcesAsync(final IProgressListener pProgressListener) throws Exception {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		FontFactory.setAssetBasePath("font/");

		/* Comfortably load the resources asynchronously, adding artificial pauses between each step. */
		pProgressListener.onProgressChanged(0);
		Thread.sleep(1000);
		pProgressListener.onProgressChanged(20);
		Thread.sleep(1000);
		pProgressListener.onProgressChanged(40);
		Thread.sleep(1000);
		pProgressListener.onProgressChanged(60);
		Thread.sleep(1000);
		pProgressListener.onProgressChanged(80);
		Thread.sleep(1000);
		pProgressListener.onProgressChanged(100);
		
		mSceneMain = new SceneMain(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, getVertexBufferObjectManager());
		
		// loadResources() and load() are usually called from a "loader" whic shows "loading..." scene.
		// Let's call it manually for the sake of simplicity
		mSceneMain.loadResources(getEngine(), this);
		mSceneMain.load(getEngine(), this);
	}

	@Override
	public Scene onCreateSceneAsync(final IProgressListener pProgressListener) throws Exception {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		return mSceneMain;
	}

	@Override
	public void onPopulateSceneAsync(final Scene pScene, final IProgressListener pProgressListener) throws Exception {
		/* nothing to do, scene already is populated */
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}