package eu.nazgee.flower.base.questionscene;

import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.color.Color;

import android.content.Context;
import eu.nazgee.flower.Statics;
import eu.nazgee.game.utils.helpers.Positioner;
import eu.nazgee.game.utils.scene.menu.MenuLoadable;

public abstract class SceneMenuButtons extends MenuLoadable {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	protected final String[] mButtons;
	private CharSequence mText;
	// ===========================================================
	// Constructors
	// ===========================================================
	public SceneMenuButtons(float W, float H, final Camera pCamera,
			VertexBufferObjectManager pVertexBufferObjectManager, String... pButtons) {
		super(W, H, pCamera, pVertexBufferObjectManager);
		mButtons = pButtons;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	abstract protected Font getButtonFont();
	abstract protected ITextureRegion getButtonFace(Text pText);
	abstract protected HorizontalMenuAnimator getMenuAnimator(Engine e, Context c);

	public CharSequence getText() {
		return mText;
	}

	public void setText(CharSequence mText) {
		this.mText = mText;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onLoadResources(Engine e, Context c) {
		// Make sure statics will be available
		Statics.getInstanceSafe(e, c);
	}

	@Override
	public void onLoad(final Engine e, final Context c) {
		this.setMenuAnimator(getMenuAnimator(e, c));

		float texts_width = 0;
		SmartList<Text> texts = new SmartList<Text>(mButtons.length);
		for (String button : mButtons) {
			texts.add(new Text(0, 0, getButtonFont(), button, getVertexBufferObjectManager()));
			texts_width += texts.getLast().getWidth();
		}

		for (Iterator<Text> iterator = texts.iterator(); iterator.hasNext();) {
			Text text = (Text) iterator.next();

			IMenuItem item = addMenuEntry(getW() * text.getWidth() / texts_width, getH() * 0.2f, getButtonFace(text), 666, Color.WHITE, Color.RED, getVertexBufferObjectManager());
			item.attachChild(text);
			Positioner.setCentered(text, item);
		}

		this.buildAnimations();
		setBackgroundEnabled(false);
		reset();
	}

	@Override
	public void onUnload() {
		clearEntityModifiers();
		clearUpdateHandlers();
		clearTouchAreas();
		clearMenuItems();
		detachChildren();
	}

	// ===========================================================
	// Methods
	// ===========================================================
	protected IMenuItem addMenuEntry(float w, float h,
			ITextureRegion pTextureRegion, int pID, Color pSelected,
			Color pUnselected, VertexBufferObjectManager pVBOM) {
		final IMenuItem menuItem;
		menuItem = new ColorMenuItemDecorator(new SpriteMenuItem(pID, w, h, pTextureRegion, pVBOM), pSelected, pUnselected);
		menuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		addMenuItem(menuItem);
		return menuItem;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
