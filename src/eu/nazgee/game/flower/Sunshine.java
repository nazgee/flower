package eu.nazgee.game.flower;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.nazgee.game.utils.helpers.Positioner;

public class Sunshine extends Entity {
	private final Sprite mSpriteTop;
	private final Sprite mSpriteMiddle;
	private final Sprite mSpriteBottom;
	public Sunshine (final ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVBO) {
		mSpriteTop = new Sprite(0, 0, pTiledTextureRegion.getTextureRegion(0), pVBO);
		mSpriteMiddle = new Sprite(0, 0, pTiledTextureRegion.getTextureRegion(1), pVBO);
		mSpriteBottom = new Sprite(0, 0, pTiledTextureRegion.getTextureRegion(2), pVBO);

		attachChild(mSpriteTop);
		attachChild(mSpriteMiddle);
		attachChild(mSpriteBottom);

		Positioner.setCentered(mSpriteTop, 0, 0);
		Positioner.setCenteredX(mSpriteMiddle, 0);
		Positioner.setCenteredX(mSpriteBottom, 0);

		mSpriteMiddle.setScaleCenterY(0);
		mSpriteMiddle.setY(mSpriteTop.getY() + mSpriteTop.getHeight());

		setRaysLength(300);
	}

	public void setRaysLength(final float pLength) {
		final float scale = pLength/mSpriteMiddle.getHeight();
		mSpriteMiddle.setScaleY(scale);
		mSpriteBottom.setY(mSpriteMiddle.getY() + pLength);
	}
}
