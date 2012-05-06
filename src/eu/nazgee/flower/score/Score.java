package eu.nazgee.flower.score;

import eu.nazgee.flower.activity.game.hud.HudGame;

public class Score {
	private HudGame mHud;
	public final Value score = new Value();
	public final Value seeds = new Value();
	public final Value flowers = new Value();

	public void reset() {
		set(0, 0, 0);
	}

	public void set(int pScore, int pSeeds, int pFlowers) {
		score.set(pScore);
		seeds.set(pSeeds);
		flowers.set(pFlowers);
	}
}
