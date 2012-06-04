package eu.nazgee.flower.activity.game.scene.main;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.nazgee.flower.BaseHUD;

public class HudGame extends BaseHUD {
	// ===========================================================
	// Constants
	// ===========================================================
	// ===========================================================
	// Fields
	// ===========================================================
	// ===========================================================
	// Constructors
	// ===========================================================
	public HudGame(float W, float H,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, 3, pVertexBufferObjectManager);
	}

	public void setTextScore(CharSequence pText) {
		setTextLine(0, pText);
	}

	public void setTextSeeds(CharSequence pText) {
		setTextLine(1, pText);
	}

	public void setTextFlowers(CharSequence pText) {
		setTextLine(2, pText);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	// ===========================================================
	// Methods
	// ===========================================================
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
