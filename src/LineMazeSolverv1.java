import java.util.Arrays;
import java.util.List;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;

public class LineMazeSolverv1 {
	static RegulatedMotor leftMotor = Motor.A;
	static RegulatedMotor rightMotor = Motor.D;
	static NXTRegulatedMotor neck = Motor.B;

	static EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S4);
	static EV3UltrasonicSensor ultraSensor = 	new EV3UltrasonicSensor(SensorPort.S2);
	static EV3 ev3 = (EV3) BrickFinder.getLocal();
	static TextLCD lcd = ev3.getTextLCD();
	static int SPEED = 250;
	static int DELAY = 50;
	static int TURN_DELAY = 2000;
	static int FORWARD_DELAY = 1500;
	static float LOWER = 0.15f;
	static float UPPER = 0.55f;
	static int isStop = 0;

	public static void main(String[] args) {
		// Initialize sampleFetcher
		float redSample[];
		SensorMode redMode = colorSensor.getRedMode();
		redSample = new float[redMode.sampleSize()];
		
		float ultraSample[];
		SensorMode distanceMode = ultraSensor.getMode("Distance");
		ultraSample = new float[distanceMode.sampleSize()];
		
		int MODE = 2;
		
		// Hard-coded values
		leftMotor.setSpeed(SPEED);
		rightMotor.setSpeed(SPEED);
		
		// Start moving the robot
		leftMotor.backward(); // backward because of gears
		rightMotor.backward();
		
//		Motor.B.forward();
		
		while (true) {
			redMode.fetchSample(redSample, 0);
			distanceMode.fetchSample(ultraSample, 0);
			// Output sample data
			lcd.clear();
			lcd.drawString(String.valueOf(ultraSample[0]), 1, 2);
			lcd.drawString(String.valueOf(redSample[0]), 1, 3);
			
			if(Button.UP.isDown()) {
				MODE = 1;
				leftMotor.stop();
				rightMotor.stop();
			} else if(Button.DOWN.isDown()) {
				MODE = 2;
				leftMotor.stop();
				rightMotor.stop();
			}
			
			if(MODE == 1) {
				LineSolver(redSample[0]);				
			} else if(MODE == 2) {
				MazeSolver(ultraSample[0]);
			}
			
			// Allow for some time before self-correcting
			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {}
			

			if (Button.ESCAPE.isDown()) {
				isStop = 1;
			}
			
			if (isStop == 1) {
				break;
			}
		}
		
		leftMotor.stop();
		rightMotor.stop();
		colorSensor.close();
	}
	
	public static void LineSolver(float data) {
		// LINE FOLLOWER
		// Correct direction
		if (LOWER <= data && data <= LOWER) {
			leftMotor.backward();
			rightMotor.backward();
		}
		else if (data < UPPER) { 
			leftMotor.stop();
			rightMotor.backward();
		}
		else if (data > UPPER) { 
			leftMotor.backward();
			rightMotor.stop();
		}
	}
	
	public static void MazeSolver(float data) {
		// MAZE SOLVER
		float leftDist = 0;
		float rightDist = 0;
		
		AStarDriver maze = new AStarDriver();
		
		List<Integer> moves = maze.getMove();
		
		System.out.println(Arrays.toString(moves.toArray()));
		for (int i = 0; i < moves.size(); i++) {
		    if(moves.get(i) == 0) {
			    MazeForward();
		    } else if(moves.get(i) == 1) {
			    MazeTurnLeft();
		    } else if(moves.get(i) == 2) {
			    MazeTurnRight();
		    }

			if (Button.ESCAPE.isDown()) {
				isStop = 1;
			}
		}
//		if (data > 0.07f) {
//			leftMotor.backward();
//			rightMotor.backward();
//		} else {
//			leftMotor.stop();
//			rightMotor.stop();
//
//			neck.rotateTo(90);
//			rightDist = data;
//			neck.rotateTo(-90);
//			leftDist = data;
//			neck.rotateTo(0);
//			neck.stop();
//
//			lcd.clear();
//			lcd.drawString(String.valueOf(leftDist), 1, 2);
//			lcd.drawString(String.valueOf(rightDist), 1, 3);
//
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {}
//		}
	}

	private static void MazeForward() {
		// TODO Auto-generated method stub
		leftMotor.backward();
		rightMotor.backward();

		try {
			Thread.sleep(FORWARD_DELAY);
		} catch (InterruptedException e) {}
		
		leftMotor.stop();
		rightMotor.stop();
	}

	private static void MazeTurnLeft() {
		rightMotor.backward();
		
		try {
			Thread.sleep(TURN_DELAY);
		} catch (InterruptedException e) {}
		
		rightMotor.stop();
	}
	
	private static void MazeTurnRight() {
		leftMotor.backward();
		
		try {  
			Thread.sleep(TURN_DELAY);
		} catch (InterruptedException e) {}
		
		leftMotor.stop();
	}
}
