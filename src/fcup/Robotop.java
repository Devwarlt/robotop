package fcup;
import robocode.*;
import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Robotop - a robot by (your name here)
 */
public class Robotop extends Robot
{
	/**
	 * run: Robotop's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		// body,gun,radar
		setBodyColor(Color.black);
		setGunColor(Color.orange);
		setRadarColor(Color.green);
		setBulletColor(Color.yellow);
		setScanColor(Color.blue);
		
		// Robot main loop
		while(true) {
			// Replace the next 4 lines with any behavior you would like
			ahead(100);
			turnGunRight(360);
			back(100);
			turnGunRight(360);
		}
	}
	

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		//double angle = e.getBearing(); // retorna o angulo do robo adversario
		//double distance = e.getDistance(); //retorna a distancia do robo adversario
		double energyOpponent = e.getEnergy(); // retorna a energia do robo adversario
		double myEnergy = getEnergy();
		int opponents = getOthers();
		
		if(myEnergy > energyOpponent && myEnergy > 70){
			fire(3);
		}else{
			fire(1);
		}
		scan();	//procura por outros robos logo apos atirar
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		turnRight(45);
		back(10);
		//scan();
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		//turnRight(45);
		back(20);
		double positionBody = getHeading();
		double positionWall = e.getBearing();
		if(positionBody == 0 || positionBody == 90 || positionBody == 180 || positionBody == 270){
			turnGunRight(90);
			fire(3);
		}else{
			turnGunRight(positionWall+90);
			fire(3);
		}
	}	
	
	//quando o robo bate com outro robo
	public void onHitRobot(HitRobotEvent e){
		back(10);
		double positionBody = getHeading();
		double positionOpposite = e.getBearing();
		if(positionBody == 0 || positionBody == 90 || positionBody == 180 || positionBody == 270){
			turnGunRight(positionOpposite - 90);
			fire(3);
		}else{
			turnGunRight(positionOpposite);
			fire(3);
		}
	}
}
