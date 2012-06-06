package eu.nazgee.flower;

import org.andengine.entity.modifier.RotationByModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class BaseButton extends Sprite implements IClickDetectorListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	ClickDetector mDetector = new ClickDetector(this);
	private IButtonListener mButtonListener;
	// ===========================================================
	// Constructors
	// ===========================================================
	public BaseButton(float pX, float pY,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
	}

	public BaseButton(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public IButtonListener getButtonListener() {
		return mButtonListener;
	}

	public void setButtonListener(IButtonListener mHudListener) {
		this.mButtonListener = mHudListener;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onClick(ClickDetector pClickDetector, int pPointerID,
			float pSceneX, float pSceneY) {

		registerEntityModifier(new RotationByModifier(0.1f, 30));
		if (null != getButtonListener()) {
			getButtonListener().onClicked(this);
		}
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		return mDetector.onTouchEvent(pSceneTouchEvent);
	}

	// ===========================================================
	// Methods
	// ===========================================================


	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public interface IButtonListener {
		public void onClicked(BaseButton pButton);
	}
}
