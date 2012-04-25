package eu.nazgee.game.flower;

import java.util.Iterator;
import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationByModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.ease.EaseBounceOut;

import android.content.Context;
import eu.nazgee.game.utils.helpers.AtlasLoader;
import eu.nazgee.game.utils.helpers.TiledTextureRegionFactory;
import eu.nazgee.game.utils.loadable.SimpleLoadableResource;
import eu.nazgee.game.utils.scene.SceneLoadable;

public class SceneMain extends SceneLoadable {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	MyResources mResources = new MyResources();
	MainHUD mHud;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public SceneMain(float W, float H,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pVertexBufferObjectManager);
		
		mHud = new MainHUD(W, H, pVertexBufferObjectManager);
		getLoader().install(mResources);
		getLoader().install(mHud);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onLoadResources(Engine e, Context c) {
		/*
		 * No need to do anything special here. If this method is called, it means
		 * that all our resources must have been loaded already.
		 * i.e.: mResources were installed in SceneMain constructor, and were
		 * loaded right before this method was called.
		 */
	}

	@Override
	public void onLoad(Engine e, Context c) {
		// Attach HUD to this scene (it should be loaded already)
		e.getCamera().setHUD(mHud);
		
		Random r = new Random();
		for (int i = 0; i < 100; i++) {
			/*
			 * Choose random texture
			 */
			ITextureRegion tex = mResources.TEXS_FLOWERS.getTextureRegion(
					r.nextInt(mResources.TEXS_FLOWERS.getTileCount()));

			/*
			 *  Create a sprite
			 */
			Sprite s = new Sprite(getW() * r.nextFloat(), getH() * r.nextFloat(),
					tex, getVertexBufferObjectManager());
			
			/*
			 *  Add some fancy effects
			 */
			final float scale = 2 * r.nextFloat();
			s.registerEntityModifier(
					new LoopEntityModifier(
						new SequenceEntityModifier(
							new ScaleModifier(1, 1, scale),
							new RotationByModifier(1, r.nextFloat() * 360),
							new ScaleModifier(1, scale, 1, EaseBounceOut.getInstance())
						)
					)
				);
			s.setColor(r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat() + 0.25f);

			/*
			 * Attach it to the scene, so it gets drawn
			 */
			attachChild(s);
		}
	}

	@Override
	public void onUnload() {
		/*
		 *  We do not need anything of theese anymore- kill all children and
		 *  get rid of anything else that might want to run without any reason 
		 */
		detachChildren();
		clearEntityModifiers();
		clearUpdateHandlers();

		/*
		 * Detach HUD from the camera it was connected to - it is not a children
		 * to parent relationship, so detachChildren()/detachSelf() won't work.
		 * Hud will be unloaded automatically by the loader
		 */
		mHud.getCamera().setHUD(null);
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	private static class MyResources extends SimpleLoadableResource {
		public ITiledTextureRegion TEXS_FLOWERS;
		public ITextureRegion TEX_FACE;
		public ITextureRegion TEX_BG_FAR;
		public ITextureRegion TEX_BG_CLOSE;
		public ITextureRegion TEX_GROUND;
		private BuildableBitmapTextureAtlas[] mAtlases;

		@Override
		public void onLoadResources(Engine e, Context c) {
			mAtlases = new BuildableBitmapTextureAtlas[2];
			for (int i = 0; i < mAtlases.length; i++) {
				mAtlases[i] = new BuildableBitmapTextureAtlas(e.getTextureManager(), 1024, 1024);
			}
			/*
			 * Create nicely named shortcuts to our atlases (textures)
			 */
			BuildableBitmapTextureAtlas atlasFlower = mAtlases[0];
			BuildableBitmapTextureAtlas atlasScene = mAtlases[1];

			/*
			 * Fill our texture with regions that we would like to use
			 */
			TEX_FACE = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					atlasScene, c, "face_box.png");
			TEX_BG_FAR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					atlasScene, c, "scene/bg-close.png");
			TEX_BG_CLOSE = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					atlasScene, c, "scene/bg-far.png");
			TEX_GROUND = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					atlasScene, c, "scene/ground.png");
			/*
			 *  note: SVGs must be rasterized before rendering to texture, so size must be provided
			 */
			TEXS_FLOWERS = TiledTextureRegionFactory.loadTilesSVG(c, "gfx/", "flowers",
					atlasFlower, Consts.FLOWER_TEX_WIDTH, Consts.FLOWER_TEX_HEIGHT);
		}

		@Override
		public void onLoad(Engine e, Context c) {
			/*
			 *  build and load all our atlases (places regions on texture and sends it to MCU)
			 */
			AtlasLoader.buildAndLoad(mAtlases);

			/*
			 *  Pretend it takes some time, so we can see "Loading..." scene
			 */
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		@Override
		public void onUnload() {
			for (BuildableBitmapTextureAtlas atlas : mAtlases) {
				atlas.unload();
			}
		}
	}
}