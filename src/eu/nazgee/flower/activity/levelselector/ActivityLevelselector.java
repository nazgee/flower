package eu.nazgee.flower.activity.levelselector;

import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityParameterCallable;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Intent;
import android.util.Log;
import eu.nazgee.flower.TexturesLibrary;
import eu.nazgee.flower.activity.game.ActivityGame;
import eu.nazgee.flower.activity.levelselector.scene.GameLevelItem;
import eu.nazgee.flower.activity.levelselector.scene.SceneLevelselector;
import eu.nazgee.flower.base.pagerscene.ScenePager;
import eu.nazgee.flower.base.pagerscene.ScenePager.IItemClikedListener;
import eu.nazgee.flower.bases.BaseActivityPager;
import eu.nazgee.util.ModifiersFactory;

public class ActivityLevelselector extends BaseActivityPager<GameLevelItem>{

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	public TexturesLibrary mTexturesLibrary = new TexturesLibrary();
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
	protected ScenePager<GameLevelItem> populatePagerScene(final float w, final float h,
			final VertexBufferObjectManager pVertexBufferObjectManager) {
		final ScenePager<GameLevelItem> scene = new SceneLevelselector(w, h, pVertexBufferObjectManager, mTexturesLibrary);
		scene.setItemClikedListener(new IItemClikedListener<GameLevelItem>() {
			@Override
			public void onItemClicked(final GameLevelItem pItem) {
				// launch game activity
				if (!pItem.getLevel().resources.isLocked()) {
					final Intent i = new Intent(ActivityLevelselector.this, ActivityGame.class);
					i.putExtra(ActivityGame.BUNDLE_LEVEL_ID, pItem.getLevel().id);
					startActivityForResult(i, ActivityGame.REQUESTCODE_PLAY_GAME);
				} else {
					pItem.registerEntityModifier(ModifiersFactory.shakeYourHead(3, 0.1f, 20));
//					mSceneInfo.load(getEngine(), ActivityLevelselector.this);
//					getEngine().getScene().setChildScene(mSceneInfo, false, false, true);
				}
			}
		});
		return scene;
	}

	protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
		if (requestCode == ActivityGame.REQUESTCODE_PLAY_GAME) {
//			if (resultCode == RESULT_OK) {
//			}
			callOnEveryItem(new IEntityParameterCallable() {
				@Override
				public void call(IEntity pEntity) {
					GameLevelItem item = (GameLevelItem) pEntity;
					if ((item.getLevel().id == resultCode) ||
						(item.getLevel().id == (resultCode+1))) {
						item.reload(mTexturesLibrary, getVertexBufferObjectManager());
						Log.d(getClass().getSimpleName(), "Reloading level " + item.getLevel().id);
					}
				}
			});
		}
	}

	@Override
	protected void onCreateResources() {
		super.onCreateResources();

		// Load spritesheets
		mTexturesLibrary.loadResources(getEngine(), this);
		mTexturesLibrary.load(getEngine(), this);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}