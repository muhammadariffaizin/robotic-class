import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;

public class LineMazeSolverv1 {

	public static void main(String[] args) {
		RegulatedMotor leftMotor = Motor.A;
		RegulatedMotor rightMotor = Motor.D;
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S4);
		EV3UltrasonicSensor ultraSensor = 	new EV3UltrasonicSensor(SensorPort.S2);
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		
		int SPEED = 150;
		int DELAY = 50;
		float LOWER = 0.15f;
		float UPPER = 0.55f;
		
		// Initialize sampleFetcher
		float redSample[];
		SensorMode redMode = colorSensor.getRedMode();
		redSample = new float[redMode.sampleSize()];
		
		float ultraSample[];
		SensorMode distanceMode = ultraSensor.getMode("Distance");
		ultraSample = new float[distanceMode.sampleSize()];
		
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
			lcd.drawString(String.valueOf(redSample[0]), 1, 2);
			lcd.drawString(String.valueOf(ultraSample[0]), 1, 3);
			
			
			// Correct direction
			if (LOWER <= redSample[0] && redSample[0] <= LOWER) {
				leftMotor.backward();
				rightMotor.backward();
			}
			else if (redSample[0] < UPPER) { 
				leftMotor.stop();
				rightMotor.backward();
			}
			else if (redSample[0] > UPPER) { 
				leftMotor.backward();
				rightMotor.stop();
			}
			
			// Allow for some time before self-correcting
			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {}
			
			if (Button.ESCAPE.isDown()) {
				break;
			}
		}
		
		leftMotor.stop();
		rightMotor.stop();
		colorSensor.close();
	}

}
