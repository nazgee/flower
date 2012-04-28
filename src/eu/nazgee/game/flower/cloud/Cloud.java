package eu.nazgee.game.flower.cloud;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveByModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;

import eu.nazgee.game.utils.helpers.Positioner;


public class Cloud extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private IEntityModifier mTravelModifier;
	private TravelListener mTravelListener;
	private MyModifierListener mMyModifierListener = new MyModifierListener();
	// ===========================================================
	// Constructors
	// ===========================================================

	public Cloud(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			ISpriteVertexBufferObject pSpriteVertexBufferObject,
			ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pSpriteVertexBufferObject,
				pShaderProgram);
	}

	public Cloud(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			ISpriteVertexBufferObject pSpriteVertexBufferObject) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pSpriteVertexBufferObject);
	}

	public Cloud(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			DrawType pDrawType, ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager,
				pDrawType, pShaderProgram);
	}

	public Cloud(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			DrawType pDrawType) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager,
				pDrawType);
	}

	public Cloud(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager,
				pShaderProgram);
	}

	public Cloud(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
	}

	public Cloud(float pX, float pY, ITextureRegion pTextureRegion,
			ISpriteVertexBufferObject pVertexBufferObject,
			ShaderProgram pShaderProgram) {
		super(pX, pY, pTextureRegion, pVertexBufferObject, pShaderProgram);
	}

	public Cloud(float pX, float pY, ITextureRegion pTextureRegion,
			ISpriteVertexBufferObject pVertexBufferObject) {
		super(pX, pY, pTextureRegion, pVertexBufferObject);
	}

	public Cloud(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			DrawType pDrawType, ShaderProgram pShaderProgram) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager, pDrawType,
				pShaderProgram);
	}

	public Cloud(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			DrawType pDrawType) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager, pDrawType);
	}

	public Cloud(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			ShaderProgram pShaderProgram) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager, pShaderProgram);
	}

	public Cloud(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public TravelListener getTravelListener() {
		return mTravelListener;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	public interface TravelListener {
		void onStarted(Cloud pCloud);
		void onFinished(Cloud pCloud);
	}
	// ===========================================================
	// Methods
	// ===========================================================
	synchronized public void travel(final float pX, final float pY, final float W, final float time, TravelListener pTravelListener) {
		mTravelListener = pTravelListener;
		Positioner.setCentered(this, pX, pY);
		unregisterEntityModifier(mTravelModifier);
		mTravelModifier = new ParallelEntityModifier(
				new SequenceEntityModifier(
						new MoveByModifier(time, W, 0)
						),
				new SequenceEntityModifier(
						new FadeInModifier(time*0.1f),
						new DelayModifier(time * 0.8f),
						new FadeOutModifier(time*0.1f)
						)
				);
		mTravelModifier.setAutoUnregisterWhenFinished(false);
		mTravelModifier.addModifierListener(mMyModifierListener);
		registerEntityModifier(mTravelModifier);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	private class MyModifierListener implements IModifierListener<IEntity> {
		@Override
		public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			synchronized (Cloud.this) {
				if (mTravelListener != null) {
					mTravelListener.onStarted(Cloud.this);
				}
			}
		}
		@Override
		public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
			synchronized (Cloud.this) {
				if (mTravelListener != null) {
					mTravelListener.onFinished(Cloud.this);
				}
			}
		}
	}
}
