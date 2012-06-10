package eu.nazgee.flower.activity.levelselector.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.content.Context;
import eu.nazgee.flower.BaseHUD;
import eu.nazgee.flower.Gradient3Way;
import eu.nazgee.flower.Gradient3Way.eGradientPosition;

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
		Camera camera = e.getCamera();

		final float gradW = camera.getWidth() * (1 - SceneLevelselector.PAGE_WIDTH_EFFECTIVE);
		Gradient3Way grad = new Gradient3Way(camera.getWidth() - gradW, 0, gradW, camera.getHeight(), 0.1f, getVertexBufferObjectManager());
		attachChild(grad);
		Color col = new Color(0,0,0,1);
		grad.setColor(col);
		grad.setAlpha(0.9f);
		col.setAlpha(0);
		grad.setGradientColor(eGradientPosition.GRADIENT_LEFT, col);
		grad.setZIndex(ZINDEX_GRADIENT);

		sortChildren();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
