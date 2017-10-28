import robocode.AlphaBot;
import robocode.ScannedRobotEvent;

public class TransCoders extends AlphaBot {
	
	double energy = 0;
	
	@Override
	public void run() {
		while(true) {
			energy = getEnergy();
			System.out.println(energy);
			
			if(energy > 50) {
				ahead(100);
				back(100);
				turnGunLeft(360);
				scan();
			}
			else {
				turnGunLeft(180);
				ahead(500);
				back(500);
				turnGunLeft(180);
				scan();
			}
			
		}
	}

	@Override
	public void onScannedRobot(ScannedRobotEvent event) {
		double power = 10 - event.getDistance() % 10;
		fireBullet(power);
	}
	
	
	
	
	
	
	
	
}



