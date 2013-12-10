package city.transportation;

import interfaces.Car;
import interfaces.Person;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import city.CityMap;
import city.PersonAgent;

import activityLog.ActivityLog;
import activityLog.ActivityTag;
import astar.AStarTraversal;
import astar.Position;

public class CrashCar extends Vehicle {
	private boolean test = false;
	String name = "Crash test!";
	int num;
	boolean crashed = false;

	PersonAgent target;

	private CrashCar otherCrashCar;	

	ActivityTag tag = ActivityTag.CAR;

	public CrashCar(AStarTraversal aStar, CityMap map, int num) {
		super(aStar, map);

		this.num = num;
		capacity = 1;
		type = "crash" + Integer.toString(num);
		guiFinished = new Semaphore(0, true);

		if(num == 1) 
			currentPosition = new Position(16, 20);
		else
			currentPosition = new Position(25, 13);

		if(aStar != null)
			currentPosition.moveInto(aStar.getGrid());
	}

	private void log(String msg){
		print(msg);
		if(!test)
			ActivityLog.getInstance().logActivity(tag, msg, name, false);
	}

	public void thisIsATest() {
		test = true;
	}

	public void msgICrashedIntoYou(CrashCar c) {
		otherCrashCar = c;
		crashed = true;
		stateChanged();
	}

	protected boolean pickAndExecuteAnAction() {
		if(!crashed) {
			DriveToCrashSite();
		} else {	
			gui.crashed();
			log("AAAHHHHHHHHH");
			burnAway();
		}

		return false;
	}

	private void DriveToCrashSite() {
		gui.setVisible();

		if(num == 1) {
			guiMoveFromCurrentPositionTo(new Position(9, 13));
			log("Oh no, it appears that I have broken down in the middle of the road!");
		} else if(num == 2) {
			guiMoveFromCurrentPositionTo(new Position(10, 13));
			currentPosition.release(aStar.getGrid());
			gui.moveTo(390, 450);
			try {
				guiFinished.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			otherCrashCar.msgICrashedIntoYou(this);
			crashed = true;
			stateChanged();
		} else if(num == 3) {
			guiMoveFromCurrentPositionTo(new Position(19, 13));
			hitAndRun();
		}
		gui.setVisible();
	}

	public void setOtherCrashCar(CrashCar c) {
		otherCrashCar = c;
	}

	public void setTarget(PersonAgent p) {
		target = p;
	}

	private void hitAndRun() {
		target.msgImRunningYouOver();
		guiMoveFromCurrentPositionTo(new Position(15, 20));
		currentPosition.release(aStar.getGrid());
		gui.moveTo(570, 1000);
		gui.setInvisible();
		stopThread();
	}

	private void burnAway() {
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				if(num == 1) {
					currentPosition.release(aStar.getGrid());
				}
				gui.setInvisible();
				stopThread();
			}
		}, 10000	);
	}
}
