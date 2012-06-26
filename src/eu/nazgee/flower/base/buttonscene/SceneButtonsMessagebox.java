//package eu.nazgee.flower.base.buttonscene;
//
//import org.andengine.engine.Engine;
//import org.andengine.engine.camera.Camera;
//import org.andengine.entity.IEntity;
//import org.andengine.entity.scene.menu.item.IMenuItem;
//import org.andengine.entity.text.AutoWrap;
//import org.andengine.entity.text.Text;
//import org.andengine.entity.text.TextOptions;
//import org.andengine.opengl.font.Font;
//import org.andengine.opengl.vbo.VertexBufferObjectManager;
//import org.andengine.util.HorizontalAlign;
//import org.andengine.util.color.Color;
//
//import android.content.Context;
//import eu.nazgee.flower.Consts;
//import eu.nazgee.flower.TexturesLibrary;
//import eu.nazgee.game.utils.helpers.Positioner;
//
//public class SceneButtonsMessagebox extends SceneButtons {
//	// ===========================================================
//	// Constants
//	// ===========================================================
//	private final float BUTTON_HEIGHT = Consts.CAMERA_HEIGHT * 0.2f;
//	// ===========================================================
//	// Fields
//	// ===========================================================
//	private final Font mFontButton;
//	private final Font mFontText;
//	private final TexturesLibrary mTexturesLibrary;
//	// ===========================================================
//	// Constructors
//	// ===========================================================
//	public SceneButtonsMessagebox(float W, float H, Camera pCamera,
//			VertexBufferObjectManager pVertexBufferObjectManager,
//			Font pFontText, Font pFontButton, CharSequence pText,
//			final TexturesLibrary pTexturesLibrary,
//			String... pButtons) {
//		super(W, H, pCamera, pVertexBufferObjectManager, pButtons);
//		this.mFontButton = pFontButton;
//		this.mFontText = pFontText;
//		this.mTexturesLibrary = pTexturesLibrary;
//
//		setText(pText);
//		setBackgroundColor(new Color(0, 0, 0, 0.0f));
//
//
//	}
//
//	// ===========================================================
//	// Getter & Setter
//	// ===========================================================
//
//	// ===========================================================
//	// Methods for/from SuperClass/Interfaces
//	// ===========================================================
//	@Override
//	protected HorizontalMenuAnimator getMenuAnimator(final Engine e, final Context c) {
//		return new HorizontalMenuAnimator();
//	}
//
//	@Override
//	protected Text prepareText(final CharSequence pText) {
//		return new Text(0, 0, mFontButton, pText, getVertexBufferObjectManager());
//	}
//
//	@Override
//	protected IMenuItem prepareMenuItem(final Text pText, final float pTotalWidth, int pID) {
//		IMenuItem item = createMenuButton(getW() * pText.getWidth() / pTotalWidth, BUTTON_HEIGHT, mTexturesLibrary.getFactory(), pID, Color.RED, Color.WHITE, getVertexBufferObjectManager());
//		item.attachChild(pText);
//		LayoutBase.setSiblingItemPositionCenter(pText, item);
//		return item;
//	}
//
//	@Override
//	public void onLoadResources(Engine e, Context c) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onLoad(final Engine e, final Context c) {
//		super.onLoad(e, c);
//		prepareContent(e, c);
//		IEntity bg = mTexturesLibrary.getFactory().populateFrameMessageBox(getW(), getH(), getVertexBufferObjectManager());
//		bg.setZIndex(-1000);
//		this.attachChild(bg);
//		sortChildren();
//	}
//
//	// ===========================================================
//	// Methods
//	// ===========================================================
//	private void prepareContent(final Engine e, final Context c) {
//		Text text = new Text(0, 0, this.mFontText, getText(),
//				new TextOptions(AutoWrap.WORDS, getW() * 0.9f, HorizontalAlign.CENTER,Text.LEADING_DEFAULT),
//				getVertexBufferObjectManager());
//		attachChild(text);
//		text.setPosition((getW() - text.getWidth())/2,
//				(getH()	- BUTTON_HEIGHT - text.getHeight()) / 2);
//
//		sortChildren(false);
//	}
//	// ===========================================================
//	// Inner and Anonymous Classes
//	// ===========================================================
//}
