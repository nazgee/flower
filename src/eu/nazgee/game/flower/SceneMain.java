package eu.nazgee.game.flower;

import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import eu.nazgee.game.utils.helpers.AtlasLoader;
import eu.nazgee.game.utils.helpers.TiledTextureRegionFactory;
import eu.nazgee.game.utils.loadable.SimpleLoadableResource;
import eu.nazgee.game.utils.scene.SceneLoadable;

public class SceneMain extends SceneLoadable {

	MyResources mResources = new MyResources();

	public SceneMain(float W, float H,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pVertexBufferObjectManager);
		getLoader().install(mResources);
	}

	@Override
	public void onLoadResources(Engine e, Context c) {
	}

	@Override
	public void onLoad(Engine e, Context c) {
		Random r = new Random();
		for (int i = 0; i < 100; i++) {
			// calculate id of random texture
			int tileid = r.nextInt(mResources.TEXS_FLOWERS.getTileCount());

			// Create sprite
			Sprite s = new Sprite(getW() * r.nextFloat(), getH() * r.nextFloat(),
					mResources.TEXS_FLOWERS.getTextureRegion(tileid),
					getVertexBufferObjectManager());
			
			// Add some fancy effects
			s.registerEntityModifier(new ScaleModifier(1 + 10 * r.nextFloat(),
					2 * r.nextFloat() + 0.5f, 2 * r.nextFloat()));

			attachChild(s);
		}
	}

	@Override
	public void onUnload() {
		detachChildren();
		clearEntityModifiers();
		clearUpdateHandlers();
	}

	private static class MyResources extends SimpleLoadableResource {
		public ITiledTextureRegion TEXS_FLOWERS;
		public ITextureRegion TEX_FACE;
		private BuildableBitmapTextureAtlas mAtlas;

		@Override
		public void onLoadResources(Engine e, Context c) {
			mAtlas = new BuildableBitmapTextureAtlas(e.getTextureManager(),
					1024, 1024);
			TEX_FACE = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(mAtlas, c, "face_box.png");
			TEXS_FLOWERS = TiledTextureRegionFactory.loadTilesSVG(c, "gfx/", "flowers", mAtlas, Consts.FLOWER_TEX_WIDTH, Consts.FLOWER_TEX_HEIGHT);
		}

		@Override
		public void onLoad(Engine e, Context c) {
			AtlasLoader.buildAndLoad(mAtlas);

			// Pretend it takes some time, so we can see "Loading..." scene
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