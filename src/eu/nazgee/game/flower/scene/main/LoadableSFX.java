package eu.nazgee.game.flower.scene.main;

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
	private Sound SND_FRY;
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
		SND_FRY = SoundLoader.load(e.getSoundManager(), c, "fry.ogg");
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
	public void onFlowerFry() {
		SND_FRY.play();
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
