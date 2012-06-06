package eu.nazgee.flower.pool.popup;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveByModifier;
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

	public void fxPop(final float time) {
		runModifier(populateModifierDefaultPop(time));
	}

	public void fxMoveBy(final float time, final float pX, final float pY) {
		runModifier(populateModifierDefaultMoveBy(time, pX, pY));
	}

	public void fxMoveTo(final float time, final float pX, final float pY) {
		runModifier(populateModifierDefaultMoveBy(time, pX - getX(), pY - getY()));
	}

	public void fxMoveTo(final float time, IEntity pWhere) {
		float pos[] = pWhere.getSceneCenterCoordinates();
		fxMoveTo(time, pos[Constants.VERTEX_INDEX_X], pos[Constants.VERTEX_INDEX_Y]);
	}

	public void put(IEntity pWhere, CharSequence pCharSequence) {
		float pos[] = pWhere.getSceneCenterCoordinates();
		put(pos[Constants.VERTEX_INDEX_X], pos[Constants.VERTEX_INDEX_Y], pCharSequence);
	}

	public void put(final float pX, final float pY, CharSequence pCharSequence) {
		Positioner.setCentered(this, pX, pY);
		setText(pCharSequence);
	}

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
	synchronized protected void runModifier(final IEntityModifier pModifier) {
		unregisterEntityModifier(mEffectModifier);

		mEffectModifier = pModifier;
		mEffectModifier.setAutoUnregisterWhenFinished(false);
		mEffectModifier.addModifierListener(mEffectModifierListener);
		registerEntityModifier(mEffectModifier);
	}

	public IEntityModifier populateModifierDefaultPop(final float time) {
		return new ParallelEntityModifier(
				populateModifierFadeInOut(time),
				new ScaleModifier(time, 0, 1, EaseElasticOut.getInstance())
				);
	}

	public IEntityModifier populateModifierDefaultMoveBy(final float time, final float pX, final float pY) {
		return populateModifierDefaultMoveBy(time, pX, pY, 0.66f);
	}

	public IEntityModifier populateModifierDefaultMoveBy(final float time, final float pX, final float pY, final float pFreezeFactor) {
		final float timeFreeze = time * pFreezeFactor;
		final float timeMove = time * (1-pFreezeFactor);

		return new ParallelEntityModifier(
				populateModifierFadeInOut(time, 0.1f, timeMove/time),
				new ScaleModifier(time, 0, 1, EaseElasticOut.getInstance()),
				new SequenceEntityModifier(
						new DelayModifier(timeFreeze),
						new MoveByModifier(timeMove, pX, pY)
						)
				);
	}

	public IEntityModifier populateModifierFadeInOut(final float time) {
		return populateModifierFadeInOut(time, 0.1f, 0.1f);
	}

	public IEntityModifier populateModifierFadeInOut(final float time, final float pFadingInFactor, final float pFadingOutFactor) {
		return 	new SequenceEntityModifier(
				new FadeInModifier(time * pFadingInFactor),
				new DelayModifier(time * (1 - pFadingInFactor - pFadingOutFactor)),
				new FadeOutModifier(time * pFadingOutFactor)
				);
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
