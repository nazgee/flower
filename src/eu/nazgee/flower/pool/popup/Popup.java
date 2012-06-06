package eu.nazgee.flower.pool.popup;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.Constants;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;
import org.andengine.util.modifier.ease.EaseElasticOut;

import eu.nazgee.flower.pool.popup.PopupPool.PopupItem;
import eu.nazgee.game.utils.helpers.Positioner;


public class Popup extends Text {
	// ===========================================================
	// Constants
	// ===========================================================

	private final PopupItem mPopupItem;
	private final EffectModifierListener mEffectModifierListener = new EffectModifierListener();

	public Popup(float pX, float pY, IFont pFont, CharSequence pText,
			int pCharactersMaximum, TextOptions pTextOptions,
			VertexBufferObjectManager pVertexBufferObjectManager, PopupItem popupItem) {
		super(pX, pY, pFont, pText, pCharactersMaximum, pTextOptions,
				pVertexBufferObjectManager);
		mPopupItem = popupItem;
	}

	// ===========================================================
	// Fields
	// ===========================================================
	private IEntityModifier mEffectModifier;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	/**
	 * Places a popup in a given location.
	 * 
	 * @note PopupItem of this Popup WILL be automagically recycled after
	 * animation end. NO need to call scheduleDetachAndRecycle() manually on it!
	 * 
	 * @param pX
	 * @param pY
	 * @param pCharSequence
	 */
	synchronized public void pop(final float pX, final float pY, CharSequence pCharSequence, final float time) {
		unregisterEntityModifier(mEffectModifier);

		setText(pCharSequence);
		Positioner.setCentered(this, pX, pY);

		mEffectModifier = new ParallelEntityModifier(
				new SequenceEntityModifier(
						new ScaleModifier(time, 0, 1, EaseElasticOut.getInstance())
						),
				new SequenceEntityModifier(
						new FadeInModifier(time*0.1f),
						new DelayModifier(time * 0.8f),
						new FadeOutModifier(time*0.1f)
						)
				);
		mEffectModifier.setAutoUnregisterWhenFinished(false);
		mEffectModifier.addModifierListener(mEffectModifierListener);
		registerEntityModifier(mEffectModifier);
	}

	synchronized public void pop(IEntity pEntity, CharSequence pCharSequence, final float time) {
		float pos[] = pEntity.getSceneCenterCoordinates();
		pop(pos[Constants.VERTEX_INDEX_X], pos[Constants.VERTEX_INDEX_Y], pCharSequence, time);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private class EffectModifierListener implements IModifierListener<IEntity> {
		@Override
		public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
		}
		@Override
		public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
			mPopupItem.scheduleDetachAndRecycle();
		}
	}
}
