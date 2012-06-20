package eu.nazgee.flower.activity.levelselector;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Intent;
import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.activity.game.ActivityGame;
import eu.nazgee.flower.activity.levelselector.scene.GameLevelItem;
import eu.nazgee.flower.activity.levelselector.scene.SceneLevelselector;
import eu.nazgee.flower.base.pagerscene.ScenePager;
import eu.nazgee.flower.base.pagerscene.ScenePager.IItemClikedListener;
import eu.nazgee.flower.bases.BaseActivityPager;
import eu.nazgee.flower.level.GameLevel;
import eu.nazgee.util.ModifiersFactory;

public class ActivityLevelselector extends BaseActivityPager<GameLevelItem>{

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	public TexturesLibrary mTexturesLibrary = new TexturesLibrary();
//	protected SceneButtons mSceneInfo;
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
	protected ScenePager<GameLevelItem> populatePagerScene(float w, float h,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		ScenePager<GameLevelItem> scene = new SceneLevelselector(w, h, getStaticResources().FONT_DESC, pVertexBufferObjectManager, mTexturesLibrary, GameLevel.LEVEL1);
		scene.setItemClikedListener(new IItemClikedListener<GameLevelItem>() {
			@Override
			public void onItemClicked(GameLevelItem pItem) {
				// launch game activity
				if (!pItem.getLevel().resources.isLocked()) {
					Intent i = new Intent(ActivityLevelselector.this, ActivityGame.class);
					i.putExtra(ActivityGame.BUNDLE_LEVEL_ID, pItem.getLevel().id);
					startActivityForResult(i, 0);
				} else {
					pItem.registerEntityModifier(ModifiersFactory.shakeYourHead(3, 0.1f, 20));
//					mSceneInfo.load(getEngine(), ActivityLevelselector.this);
//					getEngine().getScene().setChildScene(mSceneInfo, false, false, true);
				}
			}
		});
		return scene;
	}

	@Override
	protected void onCreateResources() {
		// Load spritesheets
		mTexturesLibrary.loadResources(getEngine(), this);
		mTexturesLibrary.load(getEngine(), this);

		super.onCreateResources();
	}

//	@Override
//	protected Scene onCreateScene() {
//		Camera camera = getEngine().getCamera();
//		mSceneInfo = new SceneButtonsMessagebox(camera.getWidth(), camera.getHeight(),
//				camera, getVertexBufferObjectManager(),
//				getStaticResources().FONT_DESC,
//				getStaticResources().FONT_DESC,
//				"This level is not unlocked yet!",
//				mTexturesLibrary,
//				"ok");
//
//		mSceneInfo.loadResources(getEngine(), this);
//		mSceneInfo.setOnMenuItemClickListener(new IOnMenuItemClickListener() {
//			@Override
//			public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
//					float pMenuItemLocalX, float pMenuItemLocalY) {
//				getEngine().getScene().back();
//				mSceneInfo.unload();
//				return true;
//			}
//		});
//
//		return super.onCreateScene();
//	}

	

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}