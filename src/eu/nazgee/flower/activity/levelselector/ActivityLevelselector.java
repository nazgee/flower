package eu.nazgee.flower.activity.levelselector;

import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.ease.EaseStrongIn;
import org.andengine.util.modifier.ease.EaseStrongInOut;
import org.andengine.util.modifier.ease.EaseStrongOut;

import android.content.Intent;
import eu.nazgee.flower.BaseActivityPager;
import eu.nazgee.flower.activity.game.ActivityGame;
import eu.nazgee.flower.activity.levelselector.scene.GameLevelItem;
import eu.nazgee.flower.activity.levelselector.scene.SceneLevelselector;
import eu.nazgee.flower.base.pagerscene.ScenePager;
import eu.nazgee.flower.base.pagerscene.ScenePager.IItemClikedListener;
import eu.nazgee.flower.level.GameLevel;

public class ActivityLevelselector extends BaseActivityPager<GameLevelItem>{

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

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
		ScenePager<GameLevelItem> scene = new SceneLevelselector(w, h, getStaticResources().FONT_DESC, pVertexBufferObjectManager, GameLevel.LEVEL1);
		scene.setItemClikedListener(new IItemClikedListener<GameLevelItem>() {
			@Override
			public void onItemClicked(GameLevelItem pItem) {
				// launch game activity
				if (!pItem.getLevel().resources.isLocked()) {
					Intent i = new Intent(ActivityLevelselector.this, ActivityGame.class);
//					Intent i = new Intent(ActivityLevelselector.this, ActivityShop.class);
					i.putExtra(ActivityGame.BUNDLE_LEVEL_ID, pItem.getLevel().id);
					startActivityForResult(i, 0);
				} else {
					pItem.registerEntityModifier(nodYourHead(3, 0.1f, 20));
					mSceneInfo.load(getEngine(), ActivityLevelselector.this);
					getEngine().getScene().setChildScene(mSceneInfo, false, false, true);
				}
			}
		});
		return scene;
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
//	/**
//	 * This kind of singleton class should be implemented and used per-activity,
//	 * to avoid memory leaks.
//	 * @author nazgee
//	 *
//	 */
//	public static class Statics {
//		private static Statics mInstance;
//		public final EntityDetachRunnablePoolUpdateHandler ENTITY_DETACH_HANDLER;
//		public final Font FONT_DESC;
//
//		private Statics(Engine e, Context c) {
//			ENTITY_DETACH_HANDLER = new EntityDetachRunnablePoolUpdateHandler();
//			e.registerUpdateHandler(ENTITY_DETACH_HANDLER);
//
//			final TextureManager textureManager = e.getTextureManager();
//			final FontManager fontManager = e.getFontManager();
//
//			final ITexture font_texture = new BitmapTextureAtlas(textureManager, 512, 256, TextureOptions.BILINEAR);
//			FONT_DESC = FontFactory.createFromAsset(fontManager, font_texture, c.getAssets(), Consts.MENU_FONT, Consts.CAMERA_HEIGHT*0.1f, true, Color.WHITE.getARGBPackedInt());
//			FONT_DESC.load();
//		}
//
//		static public synchronized Statics getInstanceSafe(Engine e, Context c) {
//			if (!isInitialized()) {
//				mInstance = new Statics(e, c);
//			}
//			return mInstance;
//		}
//
//		static public synchronized Statics getInstanceUnsafe() {
//			if (!isInitialized()) {
//				throw new RuntimeException("You have not initialized statics!");
//			}
//			return mInstance;
//		}
//
//		static public synchronized boolean isInitialized() {
//			return (mInstance != null);
//		}
//	}
}