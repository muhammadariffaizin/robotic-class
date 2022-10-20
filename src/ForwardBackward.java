import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class ForwardBackward {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NXTRegulatedMotor leftWheel = Motor.A;
		NXTRegulatedMotor rightWheel = Motor.D;
		
		//Robot Configuration
		EV3ColorSensor leftColor = new EV3ColorSensor(SensorPort.S1);
		EV3ColorSensor rightColor = new EV3ColorSensor(SensorPort.S4);
		
		//Configuration
		int HALF_SECOND = 5;
		
		SampleProvider spLeft = leftColor.getColorIDMode();
		SampleProvider spRight = rightColor.getColorIDMode();
		
		int sampleSizeLeft = spLeft.sampleSize();
		float[] sampleLeft = new float[sampleSizeLeft];
		int sampleSizeRight = spRight.sampleSize();
		float[] sampleRight = new float[sampleSizeRight];
		
		while(true) {
			spLeft.fetchSample(sampleLeft, 0);
			spRight.fetchSample(sampleRight, 0);
			
			System.out.println("Sample=" + (int)sampleLeft[0] + " " + (int)sampleRight[0]);
			
			int resLeft = (int)sampleLeft[0];
			int resRight = (int)sampleRight[0];
			
			if(resLeft == 7) {
				leftWheel.stop();
			} else {
				leftWheel.backward();
			}
			
			if(resRight == 7) {
				rightWheel.stop();
			} else {
				rightWheel.backward();
			}
			
			Delay.msDelay(HALF_SECOND);
			
			if (Button.ESCAPE.isDown()) {
				break;
			}
		}
		
	}

}
