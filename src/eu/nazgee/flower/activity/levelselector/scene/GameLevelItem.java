package eu.nazgee.flower.activity.levelselector.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityParameterCallable;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.sprite.NineSliceSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import eu.nazgee.flower.EntitiesFactory;
import eu.nazgee.flower.level.GameLevel;
import eu.nazgee.util.Anchor;
import eu.nazgee.util.Anchor.eAnchorPointXY;

public class GameLevelItem extends Entity implements ITouchArea{
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final GameLevel mLevel;
	private final NineSliceSprite mFrame;
	// ===========================================================
	// Constructors
	// ===========================================================
	public GameLevelItem(final GameLevel pLevel, final Font pFont, final EntitiesFactory pFactory,
			final float W, final float H, final VertexBufferObjectManager pVBOM) {
		super(0, 0, W, H);
		mLevel = pLevel;

		mFrame = pFactory.populateFrameLevel(W, H, pVBOM, mLevel.resources.isLocked());

		final Text text = new Text(0, 0, pFont, "level " + pLevel.id, pVBOM);
		text.setColor(Color.BLACK);
		attachChild(mFrame);
		attachChild(text);
		Anchor.setPosCenterAtParent(mFrame, eAnchorPointXY.CENTERED);
		text.setPosition(getWidth()/2, getHeight()/2);
		setAlpha(1);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public boolean contains(final float pX, final float pY) {
		return mFrame.contains(pX, pY);
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
			final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		return mFrame.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	}

	@Override
	public boolean isCulled(final Camera pCamera) {
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
			public void call(final IEntity pEntity) {
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
