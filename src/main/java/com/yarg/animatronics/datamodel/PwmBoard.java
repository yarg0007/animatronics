package com.yarg.animatronics.datamodel;

import java.util.ArrayList;
import java.util.List;

public abstract class PwmBoard {

	protected ArrayList<PwmMotor> motors = new ArrayList<>();
	protected int channelSize = 16;

	public abstract int getFrequency();

	public List<PwmMotor> getAttachedMotors() {
		ArrayList<PwmMotor> motorsCopy = new ArrayList<>();
		motorsCopy.addAll(motors);
		return motorsCopy;
	}

	public void addMotor(PwmMotor motor) {

		if (motors.size() >= channelSize) {
			throw new ArrayStoreException("Unable to add motor: " + motor.getMotorId() + ", to the PWM board. No more channels left.");
		}

		motors.add(motor);
	}
}
