package eu.nazgee.flower.activity.game;

import org.andengine.util.call.Callback;

import eu.nazgee.flower.activity.game.scene.main.HudGame;
import eu.nazgee.flower.score.Score;
import eu.nazgee.flower.score.Value;

public class GameScore extends Score {
	private HudGame mHud;

	public GameScore(HudGame mHud) {
		this.setHUD(mHud);
		score.setCallbackOnChanged(new Callback<Value>() {
			@Override
			public void onCallback(Value pCallbackValue) {
				if (getHUD() == null)
					return;
				getHUD().setTextScore(pCallbackValue.get() + "$");
			}
		});

		seeds.setCallbackOnChanged(new Callback<Value>() {
			@Override
			public void onCallback(Value pCallbackValue) {
				if (getHUD() == null)
					return;
				getHUD().setTextSeeds("seeds:  " + pCallbackValue.get());
			}
		});

		flowers.setCallbackOnChanged(new Callback<Value>() {
			@Override
			public void onCallback(Value pCallbackValue) {
				if (getHUD() == null)
					return;
				getHUD().setTextFlowers("flowers:  " + pCallbackValue.get());
			}
		});
	}

	public HudGame getHUD() {
		return mHud;
	}

	public void setHUD(HudGame mHud) {
		this.mHud = mHud;
	}
}
