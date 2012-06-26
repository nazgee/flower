package eu.nazgee.flower.activity.levelselector.scene;

import org.andengine.engine.Engine;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import eu.nazgee.flower.bases.BaseHUD;

public class HudLevelselector extends BaseHUD {

	// ===========================================================
	// Constants
	// ===========================================================
	protected static int ZINDEX_GRADIENT = -1;
	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	public HudLevelselector(final float W, final float H,
			final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, 0, pVertexBufferObjectManager);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onLoad(final Engine e, final Context c) {
		super.onLoad(e, c);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
