package eu.nazgee.flower.activity.levelselector;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.content.Intent;
import android.os.Bundle;
import eu.nazgee.flower.Consts;
import eu.nazgee.flower.activity.game.ActivityGame;
import eu.nazgee.flower.activity.levelselector.scene.GameLevelItem;
import eu.nazgee.flower.activity.levelselector.scene.SceneLevelselector;
import eu.nazgee.flower.pagerscene.ScenePager;
import eu.nazgee.flower.pagerscene.ScenePager.IItemClikedListener;

public class ActivityLevelselector extends SimpleBaseGameActivity{
	private Camera mCamera;
	private ScenePager<GameLevelItem> mScenePager;



	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		mCamera = new SmoothCamera(0, 0, Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT,
				Consts.CAMERA_WIDTH * 3, Consts.CAMERA_HEIGHT * 3, 1);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new FillResolutionPolicy(), mCamera);
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

		mScenePager = new SceneLevelselector(mCamera.getWidth(), mCamera.getHeight(), getVertexBufferObjectManager());
		mScenePager.loadResources(getEngine(), this);
		mScenePager.load(getEngine(), this);
		mScenePager.setItemClikedListener(new IItemClikedListener<GameLevelItem>() {
			@Override
			public void onItemClicked(GameLevelItem pItem) {
				// launch game activity
				Intent i = new Intent(ActivityLevelselector.this, ActivityGame.class);
				startActivityForResult(i, 0);
			}
		});
		return mScenePager;
	}

	@Override
	public void onDestroyResources() throws Exception {
		super.onDestroyResources();
		if (mScenePager != null)
			mScenePager.unload();
	}
}