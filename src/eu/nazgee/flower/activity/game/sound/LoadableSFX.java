package eu.nazgee.flower.activity.game.sound;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;

import android.content.Context;
import eu.nazgee.game.utils.helpers.SoundLoader;
import eu.nazgee.game.utils.loadable.LoadableResourceSimple;

public class LoadableSFX extends LoadableResourceSimple {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private Sound SND_CASH;
	private Sound SND_FRY;
	private Sound SND_BLOOM[];

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
		SND_BLOOM = SoundLoader.loadMultiple(e.getSoundManager(), c, "sfx/", "bloom");
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
	public void onBloom(int pID) {
		if (pID >= SND_BLOOM.length) {
			pID = SND_BLOOM.length - 1;
		}
		SND_BLOOM[pID].play();
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
