/*
 * 
 *  state 
 *  0 - initial  - Wall
 *  1 - low energy
 * */




import robocode.BulletHitEvent;
import robocode.CharlieBot;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;
import java.awt.*;

public class  TransCoders extends CharlieBot {

	boolean peek; // Don't turn if there's a robot there
	double moveAmount; // How much to move
	int state = 1;
	
	/*============= check state =============== */
	public void checkState() {
		out.println(state);
		if(getEnergy() < 80 && getOthers() > 5) {
			state = 1;
			turnLeft(getHeading() % 90);
			ahead(moveAmount);
		}
		else if(getEnergy() < 80 && getOthers() < 5) {
			state = 0;
		}
		else {
			state = 0;
		}
		
	}
	
	

	/**
	 * run: Move around the walls
	 */
	public void run() {
		// Set colors
		setBodyColor(Color.BLACK);
		setGunColor(Color.BLACK);
		setRadarColor(Color.red);
		setBulletColor(Color.red);
		setScanColor(Color.red);

		// Initialize moveAmount to the maximum possible for this battlefield.
		moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
		// Initialize peek to false
		peek = false;

		// turnLeft to face a wall.
		// getHeading() % 90 means the remainder of
		// getHeading() divided by 90.
		turnLeft(getHeading() % 90);
		ahead(moveAmount);
		// Turn the gun to turn right 90 degrees.
		peek = true;
		turnRight(90);
		turnRight(90);

		while (true) {
			checkState();
			
			if(state == 0) {
				/* ============== WALL =========== */
				// Look before we turn when ahead() completes.
				peek = true;
				// Move up the wall
				ahead(moveAmount);
				// Don't look now
				peek = false;
				// Turn to the next wall
				turnRight(90);
				/* ============== WALL =========== */
			}
			else if(state == 1) {
				/* ============== BATTLE =========== */
				ahead(100);
				turnRight(360);
				back(100);
				/* ============== BATTLE =========== */
			}
		}
	}

	/**
	 * onHitRobot:  Move away a bit.
	 */
	public void onHitRobot(HitRobotEvent e) {
		if(state == 0) {
			/* ============== WALL =========== */
			
			// If he's in front of us, set back up a bit.
			if (e.getBearing() > -90 && e.getBearing() < 90) {
				back(100);
			} // else he's in back of us, so set ahead a bit.
			else {
				ahead(100);
			}
			/* ============== WALL =========== */
		}
		else if(state == 1) {
			if (e.getBearing() > -10 && e.getBearing() < 10) {
				fire(2);
			}
			if (e.isMyFault()) {
				turnRight(10);
			}
		}
	}

	@Override
	public void onRobotDetected(ScannedRobotEvent event) {
		if(state == 0) {
			int power = (int) Math.abs(20 - event.getDistance() % 10) + 1;
			fire(power);
			//smartFire(e.getDistance());
			// Note that scan is called automatically when the robot is moving.
			// By calling it manually here, we make sure we generate another scan event if there's a robot on the next
			// wall, so that we do not start moving up it until it's gone.
			if (peek) {
				scan();
			}
		}
		else if(state == 1) {
			if (event.getBearing() > -10 && event.getBearing() < 10) {
				fire(3);
			}

		}
	}



	/**
	 * onScannedRobot:  Fire!
	 */


	@Override
	public void onBulletHit(BulletHitEvent event) {
		//back(100);
		if(state == 0) {
			if(getEnergy() < event.getEnergy() + 20) {
				turnLeft(getHeading() % 180);
				ahead(moveAmount);
			}
		}
		else if(state == 1) {
			if(getEnergy() < 40) {
				state = 0;
			}
			else {
				back(50);
				turnRight(90);
				ahead(moveAmount);
			}
		}
		/*if(getOthers() < 7) {
			turnRight(90);
			ahead(moveAmount);
		}*/
	}
	

	
	
}
