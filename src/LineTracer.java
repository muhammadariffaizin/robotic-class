import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class LineTracer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NXTRegulatedMotor leftWheel = Motor.A;
		NXTRegulatedMotor rightWheel = Motor.D;
		
		//Robot Configuration
//		EV3ColorSensor leftColor = new EV3ColorSensor(SensorPort.S1);
//		EV3ColorSensor rightColor = new EV3ColorSensor(SensorPort.S4);
		EV3ColorSensor color = new EV3ColorSensor(SensorPort.S4);
		
		//Configuration
		int HALF_SECOND = 50;
		int SPEED = 200;
		int ACCELERATION = 2000;
//		leftWheel.setSpeed(HALF_SECOND);
		leftWheel.setAcceleration(ACCELERATION);
		leftWheel.setSpeed(SPEED);
		rightWheel.setAcceleration(ACCELERATION);
		rightWheel.setSpeed(SPEED);
		
//		SampleProvider spLeft = leftColor.getColorIDMode();
		SampleProvider sp = color.getColorIDMode();
		
//		int sampleSizeLeft = spLeft.sampleSize();
//		float[] sampleLeft = new float[sampleSizeLeft];
		int sampleSize = sp.sampleSize();
		float[] sample = new float[sampleSize];
		
		while(true) {
//			spLeft.fetchSample(sampleLeft, 0);
			sp.fetchSample(sample, 0);
			
//			System.out.println("Speed" + leftWheel.getSpeed() + " " + leftWheel.getAcceleration() + " " + leftWheel.getMaxSpeed());
			System.out.println("Sample=" + (int)sample[0]);
			
//			int resLeft = (int)sampleLeft[0];
			int res = (int)sample[0];
			
//			if(resLeft == 7) {
//				leftWheel.stop();
//			} else {
//				leftWheel.backward();
//			}
			
			if (res == 1 || res == 2 || res == 3 || res == 7) {
				leftWheel.backward();
				rightWheel.stop();
			} else if (res == 6) {
				leftWheel.stop();
				rightWheel.backward();
			} else {
				leftWheel.stop();
				rightWheel.stop();
			} 
			
			Delay.msDelay(HALF_SECOND);
			
			if (Button.ESCAPE.isDown()) {
				break;
			}
		}
		
	}

}
