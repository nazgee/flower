package eu.nazgee.flower.activity.levelselector.scene;

import org.andengine.engine.camera.Camera;
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
import org.andengine.util.color.Color;

import eu.nazgee.flower.level.GameLevel;
import eu.nazgee.game.utils.helpers.Positioner;

public class GameLevelItem extends Entity implements ITouchArea{
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final GameLevel mLevel;
	private final Sprite mFrame;
	// ===========================================================
	// Constructors
	// ===========================================================
	public GameLevelItem(GameLevel pLevel, Font pFont, ITextureRegion pFrameTexture, final float W, final float H, VertexBufferObjectManager pVBOM) {
		mLevel = pLevel;
		mFrame = new Sprite(0, 0, W, H, pFrameTexture, pVBOM) {
			@Override
			public 	void setAlpha(final float pAlpha) {
				super.setAlpha(pAlpha * 0.5f);
			}
		};
		if (mLevel.resources.isLocked()) {
			mFrame.setColor(Color.RED);
		}
		final Text text = new Text(0, 0, pFont, "lev=" + pLevel.id, pVBOM);
		text.setColor(Color.BLACK);
		attachChild(mFrame);
		attachChild(text);
		Positioner.setCentered(mFrame, this);
		Positioner.setCentered(text, this);
		setAlpha(1);
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

	@Override
	public boolean isCulled(Camera pCamera) {
		return mFrame.isCulled(pCamera);
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

	public GameLevel getLevel() {
		return mLevel;
	}
}
