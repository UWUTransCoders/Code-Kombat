/*
 * 
 *  state 
 *  0 - initial  - Wall
 *  1 - low energy
 * */



import robocode.AlphaBot;
import robocode.BravoBot;
import robocode.BulletHitEvent;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;
import java.awt.*;

public class  TransCoders extends BravoBot {

	boolean peek; // Don't turn if there's a robot there
	double moveAmount; // How much to move
	int state = 0;
	
	/*============= check state =============== */
	public void checkState() {
		if(getEnergy() < 50) {
			state = 1;
		}
	}
	
	

	/**
	 * run: Move around the walls
	 */
	public void run() {
		// Set colors
		setBodyColor(Color.red);
		setGunColor(Color.red);
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
			//checkState();
			
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
			else {
				/* ============== BATTLE =========== */
				ahead(100);
				turnRight(90);
				back(100);
				/* ============== BATTLE =========== */
			}
		}
	}

	/**
	 * onHitRobot:  Move away a bit.
	 */
	public void onHitRobot(HitRobotEvent e) {
		//if(state == 0) {
			/* ============== WALL =========== */
			
			// If he's in front of us, set back up a bit.
			if (e.getBearing() > -90 && e.getBearing() < 90) {
				back(100);
			} // else he's in back of us, so set ahead a bit.
			else {
				ahead(100);
			}
			/* ============== WALL =========== */
		//}
	}

	/**
	 * onScannedRobot:  Fire!
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		//if(state == 0) {
			/* ============== WALL =========== */
			fire(2);
			//smartFire(e.getDistance());
			// Note that scan is called automatically when the robot is moving.
			// By calling it manually here, we make sure we generate another scan event if there's a robot on the next
			// wall, so that we do not start moving up it until it's gone.
			if (peek) {
				scan();
			}
			/* ============== WALL =========== */
		//}
	}

	@Override
	public void onBulletHit(BulletHitEvent event) {
		//back(100);
		if(getOthers() < 7) {
			turnRight(90);
			ahead(moveAmount);
		}
	}
	
	public void smartFire(double robotDistance) {
		if (robotDistance > 200 || getEnergy() < 15) {
			fire(1);
		} else if (robotDistance > 50) {
			fire(2);
		} else {
			fire(3);
		}
	}
	
	
}
