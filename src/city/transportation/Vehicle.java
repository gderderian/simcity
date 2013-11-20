package city.transportation;

import java.util.concurrent.Semaphore;

import agent.Agent;

public abstract class Vehicle extends Agent {

	int capacity;
	
	Semaphore guiFinished = new Semaphore(0, true);

}
