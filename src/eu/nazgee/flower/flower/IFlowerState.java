package eu.nazgee.flower.flower;

public interface IFlowerState {
	IFlowerState water();
	IFlowerState sun();
	IFlowerState drag();
	IFlowerState drop();
}
