package eu.nazgee.flower.activity.levelselector.scene;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityParameterCallable;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.nazgee.flower.level.GameLevel;
import eu.nazgee.game.utils.helpers.Positioner;

public class GameLevelItem extends Entity implements ITouchArea{
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	final private Sprite mFrame;
	// ===========================================================
	// Constructors
	// ===========================================================
	public GameLevelItem(GameLevel pLevel, Font pFont, ITextureRegion pFrameTexture, VertexBufferObjectManager pVBOM) {
		mFrame = new Sprite(0, 0, pFrameTexture, pVBOM);
		final Text text = new Text(0, 0, pFont, "lev=" + pLevel.id, pVBOM);
		attachChild(mFrame);
		attachChild(text);
		Positioner.setCentered(mFrame, this);
		Positioner.setCentered(text, this);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public boolean contains(float pX, float pY) {
		return mFrame.contains(pX, pY);
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		return mFrame.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	} 
	// ===========================================================
	// Methods
	// ===========================================================
	@Override
	public void setAlpha(final float pAlpha) {
		super.setAlpha(0);
		super.callOnChildren(new IEntityParameterCallable() {
			@Override
			public void call(IEntity pEntity) {
				pEntity.setAlpha(pAlpha);
			}
		});
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
