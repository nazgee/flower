package eu.nazgee.flower.activity.levelselector;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.modifier.ease.EaseStrongIn;
import org.andengine.util.modifier.ease.EaseStrongInOut;
import org.andengine.util.modifier.ease.EaseStrongOut;

import android.content.Intent;
import android.os.Bundle;
import eu.nazgee.flower.Consts;
import eu.nazgee.flower.activity.game.ActivityGame;
import eu.nazgee.flower.activity.levelselector.scene.GameLevelItem;
import eu.nazgee.flower.activity.levelselector.scene.SceneLevelselector;
import eu.nazgee.flower.base.pagerscene.ScenePager;
import eu.nazgee.flower.base.pagerscene.ScenePager.IItemClikedListener;
import eu.nazgee.flower.base.questionscene.SceneQuestion;

public class ActivityLevelselector extends SimpleBaseGameActivity{

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private Camera mCamera;
	private ScenePager<GameLevelItem> mScenePager;
	private SceneQuestion mSceneInfo;
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

		mSceneInfo = new SceneQuestion(mCamera.getWidth(), mCamera.getHeight(), mCamera,
				getVertexBufferObjectManager(),
				"this is very long placeholder which I will use for testing questions scene", HorizontalAlign.CENTER, "button", "ok");

		mSceneInfo.loadResources(getEngine(), ActivityLevelselector.this);
		mSceneInfo.setOnMenuItemClickListener(new IOnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
					float pMenuItemLocalX, float pMenuItemLocalY) {
				getEngine().getScene().back();
				mSceneInfo.unload();
				return true;
			}
		});
		mScenePager = new SceneLevelselector(mCamera.getWidth(), mCamera.getHeight(), getVertexBufferObjectManager());
//		mScenePager.getLoader().install(mSceneQuestion);
		mScenePager.loadResources(getEngine(), this);
		mScenePager.load(getEngine(), this);
		mScenePager.setItemClikedListener(new IItemClikedListener<GameLevelItem>() {
			@Override
			public void onItemClicked(GameLevelItem pItem) {
				// launch game activity
				if (!pItem.getLevel().resources.isLocked()) {
					Intent i = new Intent(ActivityLevelselector.this, ActivityGame.class);
					i.putExtra(ActivityGame.BUNDLE_LEVEL_ID, pItem.getLevel().id);
					startActivityForResult(i, 0);
				} else {
					pItem.registerEntityModifier(nodYourHead(3, 0.1f, 20));
					mSceneInfo.load(getEngine(), ActivityLevelselector.this);
					getEngine().getScene().setChildScene(mSceneInfo, false, false, true);
				}
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
	// ===========================================================
	// Methods
	// ===========================================================
	public IEntityModifier nodYourHead(int mNodsCount,
			float mNodDuration,
			float mNodAngle) {
		IEntityModifier mod = new LoopEntityModifier(
				new SequenceEntityModifier(
						new RotationModifier(mNodDuration/4, 0,        -mNodAngle/2, EaseStrongOut.getInstance()),
						new RotationModifier(mNodDuration/2, -mNodAngle/2, mNodAngle,    EaseStrongInOut.getInstance()),
						new RotationModifier(mNodDuration/4, mNodAngle,    0,        EaseStrongIn.getInstance())
						), mNodsCount);
		return mod;
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================





}