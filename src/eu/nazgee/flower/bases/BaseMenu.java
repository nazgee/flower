package eu.nazgee.flower.bases;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import eu.nazgee.game.utils.scene.menu.SceneMenu;

/**
 * HUD awara menu scene- it gets rid of HUD when loaded, and restores it when unloaded
 * @author nazgee
 *
 */
public class BaseMenu extends SceneMenu {
	private HUD mPreviousHUD;

	public BaseMenu(final float W, final float H, final Camera pCamera, final Font pFont, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(W, H, pCamera, pFont, pVertexBufferObjectManager);
		setBackgroundEnabled(false);
	}

	@Override
	public void onLoadResources(final Engine e, final Context c) {
	}

	@Override
	public void onLoad(final Engine e, final Context c) {
		super.onLoad(e, c);
		mPreviousHUD = e.getCamera().getHUD();
		e.getCamera().setHUD(null);
	}

	@Override
	public void onUnload() {
		if (mPreviousHUD != null) {
			mPreviousHUD.getCamera().setHUD(mPreviousHUD);
		}
	}
}