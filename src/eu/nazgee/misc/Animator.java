package eu.nazgee.misc;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;

public class Animator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private IEntity mEntity = null;
	private IEntityModifier mAnimationModifier;
	// ===========================================================
	// Constructors
	// ===========================================================
	public Animator(final IEntity mEntity) {
		this.mEntity = mEntity;
	}

	public Animator() {
		this.mEntity = null;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public IEntity getEntity() {
		return mEntity;
	}

	public synchronized void setEntity(final IEntity pNewEntity) {
		if (mEntity != null) {
			mEntity.unregisterEntityModifier(mAnimationModifier);
		}
		mEntity = pNewEntity;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================


	// ===========================================================
	// Methods
	// ===========================================================
	public synchronized void runModifier(final IEntityModifier pModifier) {
		if (mEntity == null) {
			return;
		}

		pModifier.setAutoUnregisterWhenFinished(false);
		mEntity.unregisterEntityModifier(mAnimationModifier);
		mEntity.registerEntityModifier(pModifier);
		mAnimationModifier = pModifier;
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}

