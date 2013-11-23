package city.transportation;

import java.util.List;
import java.util.concurrent.Semaphore;

import city.gui.VehicleGui;

import agent.Agent;
import astar.AStarNode;
import astar.AStarTraversal;
import astar.Position;

public abstract class Vehicle extends Agent {

	public int capacity = 0;
	
	String type;
	
	Position currentPosition;
	AStarTraversal aStar;
	Semaphore guiFinished;
	
	VehicleGui gui;

	protected Vehicle(AStarTraversal aStarTraversal) {
		super();
		this.aStar = aStarTraversal;
	}
	
	public void msgGuiFinished() {
		guiFinished.release();
	}

	
	void moveTo(int x, int y) {
		Position p = new Position(x, y);
		
		if(currentPosition.distance(p) > 16) {
			//intermediate movement.
		}
		guiMoveFromCurrentPositionTo(p);
	}
	
	/*void DoGoTo(String location) {
		int x = cityMap.getX(location);
		int y = cityMap.getY(location);

	    gui.moveTo(130 + (currentPosition.getX() * 30), 70 + (currentPosition.getY() * 30));
	    
	    //Give animation time to move to square.
	    try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	void guiMoveFromCurrentPositionTo(Position to){
		//System.out.println("[Gaut] " + guiWaiter.getName() + " moving from " + currentPosition.toString() + " to " + to.toString());

		AStarNode aStarNode = (AStarNode)aStar.generalSearch(currentPosition, to);
		List<Position> path = aStarNode.getPath();
		Boolean firstStep   = true;
		Boolean gotPermit   = true;

		for (Position tmpPath: path) {
		    //The first node in the path is the current node. So skip it.
		    if (firstStep) {
			firstStep   = false;
			continue;
		    }

		    //Try and get lock for the next step.
		    int attempts    = 1;
		    gotPermit       = new Position(tmpPath.getX(), tmpPath.getY()).moveInto(aStar.getGrid());

		    //Did not get lock. Lets make n attempts.
		    while (!gotPermit && attempts < 3) {
			//System.out.println("[Gaut] " + guiWaiter.getName() + " got NO permit for " + tmpPath.toString() + " on attempt " + attempts);

			//Wait for 1sec and try again to get lock.
			try { Thread.sleep(1000); }
			catch (Exception e){}

			gotPermit   = new Position(tmpPath.getX(), tmpPath.getY()).moveInto(aStar.getGrid());
			attempts ++;
		    }

		    //Did not get lock after trying n attempts. So recalculating path.            
		    if (!gotPermit) {
			//System.out.println("[Gaut] " + guiWaiter.getName() + " No Luck even after " + attempts + " attempts! Lets recalculate");
		    	path.clear(); aStarNode=null;
		    	guiMoveFromCurrentPositionTo(to);
		    	break;
		    }

		    //Got the required lock. Lets move.
		    //System.out.println("[Gaut] " + guiWaiter.getName() + " got permit for " + tmpPath.toString());
		    currentPosition.release(aStar.getGrid());
		    currentPosition = new Position(tmpPath.getX(), tmpPath.getY ());
		    print("Moving to " + currentPosition.getX() + ", " + currentPosition.getY());
		    gui.moveTo(130 + (currentPosition.getX() * 30), 70 + (currentPosition.getY() * 30));
		    
		    //Give animation time to move to square.
		    try {
				guiFinished.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*
		boolean pathTaken = false;
		while (!pathTaken) {
		    pathTaken = true;
		    //print("A* search from " + currentPosition + "to "+to);
		    AStarNode a = (AStarNode)aStar.generalSearch(currentPosition,to);
		    if (a == null) {//generally won't happen. A* will run out of space first.
			System.out.println("no path found. What should we do?");
			break; //dw for now
		    }
		    //dw coming. Get the table position for table 4 from the gui
		    //now we have a path. We should try to move there
		    List<Position> ps = a.getPath();
		    Do("Moving to position " + to + " via " + ps);
		    for (int i=1; i<ps.size();i++){//i=0 is where we are
			//we will try to move to each position from where we are.
			//this should work unless someone has moved into our way
			//during our calculation. This could easily happen. If it
			//does we need to recompute another A* on the fly.
			Position next = ps.get(i);
			if (next.moveInto(aStar.getGrid())){
			    //tell the layout gui
			    guiWaiter.move(next.getX(),next.getY());
			    currentPosition.release(aStar.getGrid());
			    currentPosition = next;
			}
			else {
			    System.out.println("going to break out path-moving");
			    pathTaken = false;
			    break;
			}
		    }
		}
		*/
	    }
	
}
