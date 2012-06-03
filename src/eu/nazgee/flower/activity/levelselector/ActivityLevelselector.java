package eu.nazgee.flower.activity.levelselector;

import org.andengine.engine.Engine;
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
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.pool.EntityDetachRunnablePoolUpdateHandler;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.ease.EaseStrongIn;
import org.andengine.util.modifier.ease.EaseStrongInOut;
import org.andengine.util.modifier.ease.EaseStrongOut;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import eu.nazgee.flower.Consts;
import eu.nazgee.flower.activity.game.ActivityGame;
import eu.nazgee.flower.activity.levelselector.scene.GameLevelItem;
import eu.nazgee.flower.activity.levelselector.scene.SceneLevelselector;
import eu.nazgee.flower.base.buttonscene.SceneButtons;
import eu.nazgee.flower.base.buttonscene.SceneButtonsMessagebox;
import eu.nazgee.flower.base.pagerscene.ScenePager;
import eu.nazgee.flower.base.pagerscene.ScenePager.IItemClikedListener;

public class ActivityLevelselector extends SimpleBaseGameActivity{

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private ScenePager<GameLevelItem> mScenePager;
	private SceneButtons mSceneInfo;
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
		Camera camera = new SmoothCamera(0, 0, Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT,
				Consts.CAMERA_WIDTH * 3, Consts.CAMERA_HEIGHT * 3, 1);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new FillResolutionPolicy(), camera);
	}

	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		FontFactory.setAssetBasePath("fonts/");

		// Make sure, that statics are usable for anyone
		Statics.getInstanceSafe(getEngine(), this);
	}

	@Override
	protected Scene onCreateScene() {
		mEngine.registerUpdateHandler(new FPSLogger());
		Camera camera = getEngine().getCamera();

		mSceneInfo = new SceneButtonsMessagebox(camera.getWidth(), camera.getHeight(),
				camera, getVertexBufferObjectManager(),
				Statics.getInstanceUnsafe().FONT_DESC,
				Statics.getInstanceUnsafe().FONT_DESC,
				"This level is not unlocked yet!", "ok");

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

		mScenePager = new SceneLevelselector(camera.getWidth(), camera.getHeight(), getVertexBufferObjectManager());
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
	/**
	 * This kind of singleton class should be implemented and used per-activity,
	 * to avoid memory leaks.
	 * @author nazgee
	 *
	 */
	public static class Statics {
		private static Statics mInstance;
		public final EntityDetachRunnablePoolUpdateHandler ENTITY_DETACH_HANDLER;
		public final Font FONT_DESC;

		private Statics(Engine e, Context c) {
			ENTITY_DETACH_HANDLER = new EntityDetachRunnablePoolUpdateHandler();
			e.registerUpdateHandler(ENTITY_DETACH_HANDLER);

			final TextureManager textureManager = e.getTextureManager();
			final FontManager fontManager = e.getFontManager();

			final ITexture font_texture = new BitmapTextureAtlas(textureManager, 512, 256, TextureOptions.BILINEAR);
			FONT_DESC = FontFactory.createFromAsset(fontManager, font_texture, c.getAssets(), Consts.MENU_FONT, Consts.CAMERA_HEIGHT*0.1f, true, Color.WHITE.getARGBPackedInt());
			FONT_DESC.load();
		}

		static public synchronized Statics getInstanceSafe(Engine e, Context c) {
			if (!isInitialized()) {
				mInstance = new Statics(e, c);
			}
			return mInstance;
		}

		static public synchronized Statics getInstanceUnsafe() {
			if (!isInitialized()) {
				throw new RuntimeException("You have not initialized statics!");
			}
			return mInstance;
		}

		static public synchronized boolean isInitialized() {
			return (mInstance != null);
		}
	}

}