package eu.nazgee.flower.flower;

import eu.nazgee.flower.activity.game.scene.game.Sky;

public interface IFlowerState {
	IFlowerState water();
	IFlowerState sun();
	IFlowerState drag();
	IFlowerState drop(Sky pSky);
}
