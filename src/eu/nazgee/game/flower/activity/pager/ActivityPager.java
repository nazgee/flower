package eu.nazgee.game.flower.activity.pager;

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
import eu.nazgee.game.flower.Consts;
import eu.nazgee.game.flower.MainActivity;
import eu.nazgee.game.flower.activity.pager.ScenePager.IItemClikedListener;

public class ActivityPager extends SimpleBaseGameActivity{
	private Camera mCamera;



	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		mCamera = new SmoothCamera(0, 0, Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT, Consts.CAMERA_WIDTH * 3, Consts.CAMERA_HEIGHT * 3, 1);

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

		ScenePager s = new ScenePagerLevel(mCamera.getWidth(), mCamera.getHeight(), getVertexBufferObjectManager());
		s.loadResources(getEngine(), this);
		s.load(getEngine(), this);
		s.setItemClikedListener(new IItemClikedListener() {
			@Override
			public void onItemClicked(IEntity pItem) {
				// launch game activity
				Intent i = new Intent(ActivityPager.this, MainActivity.class);
				startActivityForResult(i, 0);
			}
		});
		return s;
	}
}