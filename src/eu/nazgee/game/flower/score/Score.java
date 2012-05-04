package eu.nazgee.game.flower.score;

import org.andengine.util.call.Callback;

import eu.nazgee.game.flower.MainHUD;

public class Score {
	private MainHUD mHud;
	public final Value score = new Value();
	public final Value seeds = new Value();
	public final Value flowers = new Value();

	public Score(MainHUD mHud) {
		this.setHUD(mHud);
		score.setCallbackOnChanged(new Callback<Value>() {
			@Override
			public void onCallback(Value pCallbackValue) {
				if (getHUD() == null)
					return;
				getHUD().setTextScore0(pCallbackValue.get() + "$");
			}
		});

		seeds.setCallbackOnChanged(new Callback<Value>() {
			@Override
			public void onCallback(Value pCallbackValue) {
				if (getHUD() == null)
					return;
				getHUD().setTextScore1("seeds:  " + pCallbackValue.get());
			}
		});

		flowers.setCallbackOnChanged(new Callback<Value>() {
			@Override
			public void onCallback(Value pCallbackValue) {
				if (getHUD() == null)
					return;
				getHUD().setTextScore2("flowers:  " + pCallbackValue.get());
			}
		});
	}

	public MainHUD getHUD() {
		return mHud;
	}

	public void setHUD(MainHUD mHud) {
		this.mHud = mHud;
	}

	public void reset() {
		set(0, 0, 0);
	}

	public void set(int pScore, int pSeeds, int pFlowers) {
		score.set(pScore);
		seeds.set(pSeeds);
		flowers.set(pFlowers);
	}
}
