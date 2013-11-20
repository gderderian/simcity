package city.transportation;

import java.util.concurrent.Semaphore;

import agent.Agent;

public abstract class Vehicle extends Agent {

	public int capacity = 0;
	
	Semaphore guiFinished;

	protected Vehicle(){
	}
}
