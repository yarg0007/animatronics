package com.yarg.animatronics.datamodel;

public class TowerProMG995R extends PwmMotor {

	@Override
	public int getMaxTick() {
		return 410;
	}

	@Override
	public int getMinTick() {
		return 205;
	}

	@Override
	public double getMaxAngle() {
		return 90.0;
	}

	@Override
	public double getMinAngle() {
		return -90.0;
	}

	@Override
	public String getMotorId() {
		return "TowerPro MG995R";
	}

	@Override
	public int getSignalFrequency() {
		return 50;
	}

}
