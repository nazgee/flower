package eu.nazgee.flower.activity.levelselector.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.content.Context;
import eu.nazgee.flower.BaseHUD;
import eu.nazgee.flower.GradientRectangle;

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
	public HudLevelselector(float W, float H,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, 0, pVertexBufferObjectManager);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onLoad(Engine e, Context c) {
		super.onLoad(e, c);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
