package eu.nazgee.game.flower;

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
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public SceneMain(float W, float H,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pVertexBufferObjectManager);
		getLoader().install(mResources);
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
		private BuildableBitmapTextureAtlas mAtlas;

		@Override
		public void onLoadResources(Engine e, Context c) {
			/*
			 * Create our texture atlas- single (1k x 1k) texture is enough for now
			 */
			mAtlas = new BuildableBitmapTextureAtlas(e.getTextureManager(),
					1024, 1024);

			/*
			 * Fill our texture with regions that we would like to use
			 */
			TEX_FACE = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					mAtlas, c, "face_box.png");
			/*
			 *  note: SVGs must be rasterized before rendering to texture, so size must be provided
			 */
			TEXS_FLOWERS = TiledTextureRegionFactory.loadTilesSVG(c, "gfx/", "flowers",
					mAtlas, Consts.FLOWER_TEX_WIDTH, Consts.FLOWER_TEX_HEIGHT);
		}

		@Override
		public void onLoad(Engine e, Context c) {
			/*
			 *  build and load atlas (places regions on texture and sends it to MCU)
			 */
			AtlasLoader.buildAndLoad(mAtlas);

			/*
			 *  Pretend it takes some time, so we can see "Loading..." scene
			 */
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		@Override
		public void onUnload() {
			mAtlas.unload();
		}
	}
}