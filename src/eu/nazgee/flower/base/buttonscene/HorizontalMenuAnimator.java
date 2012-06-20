//package eu.nazgee.flower.base.buttonscene;
//
//import java.util.ArrayList;
//
//import org.andengine.entity.modifier.MoveModifier;
//import org.andengine.entity.scene.menu.animator.MenuSceneAnimator;
//import org.andengine.entity.scene.menu.item.IMenuItem;
//import org.andengine.util.adt.align.HorizontalAlign;
//import org.andengine.util.adt.align.VerticalAlign;
//import org.andengine.util.modifier.ease.IEaseFunction;
//
//public class HorizontalMenuAnimator extends MenuSceneAnimator {
//	// ===========================================================
//	// Constants
//	// ===========================================================
//
//	private static final float BUTTONS_MOVE_TIME = 0.5f;
//	// ===========================================================
//	// Fields
//	// ===========================================================
//	private VerticalAlign mVerticalAlign;
//	private float mVerticalOffset;
//	// ===========================================================
//	// Constructors
//	// ===========================================================
//
//	public HorizontalMenuAnimator() {
//		this(HorizontalAlign.CENTER, VerticalAlign.BOTTOM, 0);
//	}
//
//	public HorizontalMenuAnimator(final HorizontalAlign pHorizontalAlign, final VerticalAlign pVerticalAlign, float pVerticalOffset){
//		super(pHorizontalAlign, pVerticalAlign);
//		mVerticalAlign = pVerticalAlign;
//		setVerticalOffset(pVerticalOffset);
//	}
//
//	// ===========================================================
//	// Getter & Setter
//	// ===========================================================
//	public float getVerticalOffset() {
//		return mVerticalOffset;
//	}
//
//	public void setVerticalOffset(float mVerticalOffset) {
//		this.mVerticalOffset = mVerticalOffset;
//	}
//	// ===========================================================
//	// Methods for/from SuperClass/Interfaces
//	// ===========================================================
//
//	@Override
//	public void buildAnimations(final ArrayList<IMenuItem> pMenuItems, final float pCameraWidth, final float pCameraHeight) {
//		final IEaseFunction easeFunction = this.mEaseFunction;
//		final float overallWidth = this.getOverallWidth(pMenuItems);
//		final float overallHeight = pMenuItems.get(0).getHeight();
//
//		final float baseY;
//		switch (this.mVerticalAlign) {
//			case TOP:
//				baseY = 0 + getVerticalOffset();
//				break;
//			case BOTTOM:
//				baseY = pCameraHeight - overallHeight + getVerticalOffset();
//				break;
//			case CENTER:
//			default:
//				baseY = (pCameraHeight - overallHeight) * 0.5f + getVerticalOffset();
//				break;
//		}
//
//		final float baseX;
//		switch(this.mHorizontalAlign) {
//			case LEFT:
//				baseX = 0;
//				break;
//			case RIGHT:
//				baseX = pCameraWidth - overallWidth;
//				break;
//			case CENTER:
//			default:
//				baseX = (pCameraWidth - overallWidth) * 0.5f;
//				break;
//		}
//	
//		float offsetX = 0;
//		final int menuItemCount = pMenuItems.size();
//		for(int i = 0; i < menuItemCount; i++) {
//			final IMenuItem menuItem = pMenuItems.get(i);
//
//			final MoveModifier moveModifier = new MoveModifier(BUTTONS_MOVE_TIME, -overallWidth, baseX + offsetX, baseY, baseY, easeFunction);
//			moveModifier.setAutoUnregisterWhenFinished(false);
//			menuItem.registerEntityModifier(moveModifier);
//
//			offsetX += menuItem.getWidth() + this.mMenuItemSpacing;
//		}
//	}
//
//	@Override
//	public void prepareAnimations(final ArrayList<IMenuItem> pMenuItems, final float pCameraWidth, final float pCameraHeight) {
//		final float maximumWidth = this.getMaximumWidth(pMenuItems);
//		final float overallHeight = this.getOverallHeight(pMenuItems);
//
//		final float baseY = (pCameraHeight - overallHeight) * 0.5f;
//
//		final float menuItemSpacing = this.mMenuItemSpacing;
//
//		float offsetY = 0;
//		final int menuItemCount = pMenuItems.size();
//		for(int i = 0; i < menuItemCount; i++) {
//			final IMenuItem menuItem = pMenuItems.get(i);
//
//			menuItem.setPosition(-maximumWidth, baseY + offsetY);
//
//			offsetY += menuItem.getHeight() + menuItemSpacing;
//		}
//	}
//
//	// ===========================================================
//	// Methods
//	// ===========================================================
//	protected float getOverallWidth(final ArrayList<IMenuItem> pMenuItems) {
//		float overallWidth = 0;
//		for(int i = pMenuItems.size() - 1; i >= 0; i--) {
//			final IMenuItem menuItem = pMenuItems.get(i);
//			overallWidth += menuItem.getWidth();
//		}
//
//		overallWidth += (pMenuItems.size() - 1) * this.mMenuItemSpacing;
//		return overallWidth;
//	}
//	// ===========================================================
//	// Inner and Anonymous Classes
//	// ===========================================================
//
//
//}
