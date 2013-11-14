package city;

import java.util.concurrent.Semaphore;

public abstract class Vehicle {

	int capacity;
	
	Semaphore guiFinished = new Semaphore(0, true);

}
