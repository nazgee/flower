package eu.nazgee.game.flower.scene;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;

import android.content.Context;
import eu.nazgee.game.utils.helpers.SoundLoader;
import eu.nazgee.game.utils.loadable.SimpleLoadableResource;

public class LoadableSFX extends SimpleLoadableResource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private Sound SND_CASH;
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onLoadResources(Engine e, Context c) {
		SoundFactory.setAssetBasePath("sfx/");
	}

	@Override
	public void onLoad(Engine e, Context c) {
		SND_CASH = SoundLoader.load(e.getSoundManager(), c, "cash.ogg");
	}

	@Override
	public void onUnload() {
	}
	// ===========================================================
	// Methods
	// ===========================================================
	public void onFlowerBloom() {
		SND_CASH.play();
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
