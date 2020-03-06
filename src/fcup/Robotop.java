package fcup;

import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;

/*	FCUP - U.Porto
 * 	Project name: Robotop
 * 
 * 	Author(s):
 * 	- [ID: 201911476] - Amanda Hellen Pereira Delgado
 * 	- [ID: 201911449] - Nádio Dib Fernandes Pontes
 * 
 * 	Professor: José Paulo Leal
 * 	Class: Software Architecture
 */
public class FcupBot extends AdvancedRobot {
	@Override
	public void run() {
		setBodyColor(Color.black);
		setGunColor(Color.orange);
		setRadarColor(Color.green);
		setBulletColor(Color.yellow);
		setScanColor(Color.blue);

		findBattleFieldCenter();
		spinOnBattleFieldCenter();
	}

	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		double distance = e.getDistance(); // retorna a distancia do robo adversario
		double velocityOpponent = e.getVelocity();
		double delta = 4 * getWidth();

		if (velocityOpponent == 0) {
			fire(3);
		} else {
			if (distance < delta) {
				fire(2);
			} else {
				fire(1);
			}
		}
	}

	@Override
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		setMaxVelocity(8);
		fire(1);
		verifyDanger();
		escape();
	}

	@Override
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		findBattleFieldCenter();
	}

	@Override
	public void onHitRobot(HitRobotEvent e) {
		back(4 * getWidth());
		fire(2);
	}

	/**
	 * findBattleFieldCenter: Find relative battlefield center based on robot
	 * coordinates.
	 */
	public void findBattleFieldCenter() {
		double xcenter = getBattleFieldWidth() / 2.0;
		double ycenter = getBattleFieldHeight() / 2.0;
		double x = getX();
		double y = getY();
		double positionBody = getHeading();
		double angle;
		double xDist, yDist;

		// Detect nearest X axis coordinate and move to it based on
		// current robot positions properly.

		if (x > xcenter) {
			if (positionBody >= 0 && positionBody <= 90) {
				angle = 90 + positionBody;
				turnLeft(angle);
			} else if (positionBody > 90 && positionBody < 270) {
				angle = 270 - positionBody;
				turnRight(angle);
			} else {
				angle = positionBody - 270;
				turnLeft(angle);
			}

			xDist = x - xcenter;
			ahead(xDist);

		} else {
			if (positionBody >= 0 && positionBody <= 90) {
				angle = 90 - positionBody;
				turnRight(angle);
			} else if (positionBody > 90 && positionBody < 270) {
				angle = positionBody - 90;
				turnLeft(angle);
			} else {
				angle = 450 - positionBody;
				turnRight(angle);
			}

			xDist = xcenter - x;
			ahead(xDist);
		}

		// update current position body heading angle
		positionBody = getHeading();

		// Detect nearest Y axis coordinate and move to it based on
		// current robot positions properly.

		if (y > ycenter) {
			if (positionBody == 90) {
				turnRight(90);
			} else {
				turnLeft(90);
			}

			yDist = y - ycenter;
			ahead(yDist);
		} else {
			if (positionBody == 90) {
				turnLeft(90);
			} else {
				turnRight(90);
			}

			yDist = ycenter - y;
			ahead(yDist);
		}

	}

	/**
	 * spinOnBattleFieldCenter: Use battlefield center coordinate to spin around it.
	 */
	private void spinOnBattleFieldCenter() {
		// Do a circular loop and verify danger to proceed.
		while (true) {
			setTurnRight(10000);
			setMaxVelocity(5);
			ahead(10000);

			// Verify danger level to avoid wall collisions.
			verifyDanger();
		}
	}

	/**
	 * escape: Execute escape behavior.
	 */
	private void escape() {
		double positionBody = getHeading();
		double delta = 4 * getWidth();

		// Check current robot heading angle and properly adjust
		// new heading angle offset.

		if (positionBody >= 0 && positionBody <= 90) {
			turnLeft(positionBody);
		} else if (positionBody > 90 && positionBody <= 180) {
			turnRight(180 - positionBody);
		} else if (positionBody > 180 && positionBody <= 270) {
			turnRight(270 - positionBody);
		} else {
			turnRight(360 - positionBody);
		}

		ahead(delta);
	}

	/**
	 * verifyDanger: Avoid walls to prevent collisions.
	 */
	private void verifyDanger() {
		double xMax = getBattleFieldWidth() / 2;
		double yMax = getBattleFieldHeight() / 2;
		double x = getX();
		double y = getY();
		double positionBody = getHeading();
		double safeDistance = 2 * getWidth();
		double distanceX;
		double distanceY = yMax - y;

		// Obtain current robot position coordinates and calculates if
		// distance between wall is acceptable. Otherwise, assumes
		// another direction to avoid collisions to the nearest walls.

		if (x > xMax) {
			distanceX = xMax * 2 - x;

			if (y > yMax) {
				distanceY = yMax * 2 - y;

				if (distanceX < safeDistance || distanceY < safeDistance) {
					if (positionBody >= 0 && positionBody <= 90) {
						turnRight(180);
					} else if (positionBody > 90 && positionBody <= 180) {
						turnRight(180 - positionBody);
					} else if (positionBody > 180 && positionBody <= 270) {
						// Do nothing, meanwhile :)
					} else {
						turnLeft(positionBody - 270);
					}
				}
			} else {
				distanceY = y;

				if (distanceX < safeDistance || distanceY < safeDistance) {
					if (positionBody >= 0 && positionBody <= 90) {
						turnLeft(positionBody + 45);
					} else if (positionBody > 90 && positionBody <= 180) {
						turnRight(180);
					} else if (positionBody > 180 && positionBody <= 270) {
						turnRight(360 - positionBody);
					} else {
						// Do nothing, meanwhile :)
					}
				}
			}

			ahead(safeDistance);
		} else {
			distanceX = x;

			if (y > yMax) {
				distanceY = yMax * 2 - y;

				if (distanceX < safeDistance || distanceY < safeDistance) {
					if (positionBody >= 0 && positionBody <= 90) {
						turnRight(90 - positionBody);
					} else if (positionBody > 90 && positionBody <= 180) {
						// Do nothing, meanwhile :)
					} else if (positionBody > 180 && positionBody <= 270) {
						turnLeft(positionBody - 180);
					} else {
						turnRight(180);
					}
				}
			} else {
				distanceY = y;

				if (distanceX < safeDistance || distanceY < safeDistance) {
					if (positionBody >= 0 && positionBody <= 90) {
						// Do nothing, meanwhile :)
					} else if (positionBody > 90 && positionBody <= 180) {
						turnLeft(positionBody - 90);
					} else if (positionBody > 180 && positionBody <= 270) {
						turnRight(180);
					} else {
						turnRight(360 - positionBody);
					}
				}
			}

			ahead(safeDistance);
		}
	}

}
