package eu.nazgee.flower.base.buttonscene;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.color.Color;

import android.content.Context;
import eu.nazgee.game.utils.scene.menu.MenuLoadable;

public abstract class SceneButtons extends MenuLoadable {
	// ===========================================================
	// Constants
	// ===========================================================
	protected static final int ZINDEX_BACKGROUND = -666;
	// ===========================================================
	// Fields
	// ===========================================================
	protected final CharSequence[] mButtons;
	private CharSequence mText;
	private Rectangle mBackground;
	// ===========================================================
	// Constructors
	// ===========================================================
	public SceneButtons(float W, float H, final Camera pCamera,
			VertexBufferObjectManager pVertexBufferObjectManager, String... pButtons) {
		super(W, H, pCamera, pVertexBufferObjectManager);

		this.mButtons = pButtons;

		this.mBackground = new Rectangle(0, 0, getW(), getH(), getVertexBufferObjectManager());
		this.mBackground.setZIndex(SceneButtons.ZINDEX_BACKGROUND);

		setBackgroundEnabled(false);
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	abstract protected HorizontalMenuAnimator getMenuAnimator(final Engine e, final Context c);
	abstract protected Text prepareText(final CharSequence pText);
	abstract protected IMenuItem prepareMenuItem(final Text pText, final float pTotalWidth, int pID);

	public CharSequence getText() {
		return mText;
	}

	public void setText(CharSequence mText) {
		this.mText = mText;
	}

	public Color getBackgroundColor() {
		return this.mBackground.getColor();
	}

	public void setBackgroundColor(Color pColor) {
		this.mBackground.setColor(pColor);
	}

	public int getButtonId(CharSequence pButtonText) {
		for (int i = 0; i < mButtons.length; i++) {
			CharSequence text = mButtons[i];
			if (text.equals(pButtonText))
				return i;
		}
		return -1;
	}

	public CharSequence getButtonText(int pID) {
		return mButtons[pID];
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onLoad(final Engine e, final Context c) {
		reset();
		this.setMenuAnimator(getMenuAnimator(e, c));
		attachChild(this.mBackground);

		float totalWidth = 0;
		SmartList<Text> texts = new SmartList<Text>(mButtons.length);
		for (CharSequence button : mButtons) {
			texts.add(prepareText(button));
			totalWidth += texts.getLast().getWidth();
		}

		for (int i = 0; i < texts.size(); i++) {
			Text txt = texts.get(i);
			IMenuItem item = prepareMenuItem(txt, totalWidth, getButtonId(txt.getText()));
			addMenuItem(item);
		}

		this.buildAnimations();
		sortChildren(false);
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
	protected IMenuItem populateMenuEntry(float w, float h,
			ITextureRegion pTextureRegion, int pID, Color pSelected,
			Color pUnselected, VertexBufferObjectManager pVBOM) {
		final IMenuItem menuItem;
		menuItem = new ColorMenuItemDecorator(new SpriteMenuItem(pID, w, h, pTextureRegion, pVBOM), pSelected, pUnselected);
		menuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		return menuItem;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
