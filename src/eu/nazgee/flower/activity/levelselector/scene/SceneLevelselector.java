package eu.nazgee.flower.activity.levelselector.scene;

import java.util.LinkedList;

import org.andengine.engine.Engine;
import org.andengine.entity.IEntity;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.content.Context;

import eu.nazgee.flower.Consts;
import eu.nazgee.flower.pagerscene.ArrayLayout;
import eu.nazgee.flower.pagerscene.IPage;
import eu.nazgee.flower.pagerscene.PageMoverCamera;
import eu.nazgee.flower.pagerscene.PageRectangle;
import eu.nazgee.flower.pagerscene.ScenePager;
import eu.nazgee.flower.pagerscene.ArrayLayout.eAnchorPointXY;
import eu.nazgee.game.utils.loadable.SimpleLoadableResource;

public class SceneLevelselector extends ScenePager{

	private final MyResources mResources = new MyResources();

	public SceneLevelselector(float W, float H,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pVertexBufferObjectManager);
		getLoader().install(mResources);
	}

	

	@Override
	public void onLoad(Engine e, Context c) {
		super.onLoad(e, c);
		setPageMover(new PageMoverCamera(e.getCamera(), getW()));
	}

	@Override
	protected LinkedList<IPage> populatePages() {
		LinkedList<IPage> pages = new LinkedList<IPage>();
		int mItemsCount = 60;
		int mCapacity = 0;
		do {
			pages.add(new PageRectangle(0, 0, getW(), getH(), 
					getVertexBufferObjectManager(),
					new ArrayLayout(3, 3, getW(), getH(), eAnchorPointXY.CENTERED)));
			mCapacity += pages.getLast().getCapacity();
		} while (mCapacity < mItemsCount);

		int itemsLeftToLoad = mItemsCount;
		for (int i = 0; i < pages.size(); i++) {
			IPage page = pages.get(i);
			int itemsToLoad = Math.min(page.getCapacity(), itemsLeftToLoad);
			IEntity items[] = new IEntity[itemsToLoad];
			for (int j = 0; j < items.length; j++) {
				items[j] = new Text(0, 0, mResources.FONT, "Lvl" + (i*page.getCapacity() + j), getVertexBufferObjectManager());
				
			}
			itemsLeftToLoad -= itemsToLoad;
			page.setItems(items);
			page.setPosition(i * getW(), 0);
			page.setColor(Color.GREEN);
			attachChild(page);
		}

		return pages;
	}

	private static class MyResources extends SimpleLoadableResource {
		public volatile Font FONT;

		@Override
		public void onLoadResources(Engine e, Context c) {
		}

		@Override
		public void onLoad(Engine e, Context c) {
			final TextureManager textureManager = e.getTextureManager();
			final FontManager fontManager = e.getFontManager();

			final ITexture font_texture = new BitmapTextureAtlas(textureManager, 512, 256, TextureOptions.BILINEAR);
			FONT = FontFactory.createFromAsset(fontManager, font_texture, c.getAssets(), Consts.HUD_FONT, Consts.CAMERA_HEIGHT*0.1f, true, Color.WHITE.getARGBPackedInt());
			FONT.load();
		}

		@Override
		public void onUnload() {
			FONT.unload();
		}
	}
}
