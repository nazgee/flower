package eu.nazgee.flower.score;


public class Score {
	public final Value score = new Value();
	public final Value seeds = new Value();
	public final Value flowers = new Value();

	public void reset() {
		set(0, 0, 0);
	}

	public void set(final int pScore, final int pSeeds, final int pFlowers) {
		score.set(pScore);
		seeds.set(pSeeds);
		flowers.set(pFlowers);
	}
}
