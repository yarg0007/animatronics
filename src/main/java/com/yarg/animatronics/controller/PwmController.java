package com.yarg.animatronics.controller;

import java.io.IOException;
import java.util.HashMap;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

public class PwmController {

	private HashMap<Integer, I2CDevice> boardAddressToBusMap = new HashMap<>();
	private int i2cBus = I2CBus.BUS_1;

	/**
	 * Set the Raspberry PI I2C bus if the default is not valid for your configuration.
	 * @param i2cBus I2CBus.BUS_1-17.
	 */
	public void setRaspberryPiI2cBus(int i2cBus) {

	}

	public void sendSignalToMotor(int pwmBoardAddress, int frequency) throws UnsupportedBusNumberException, IOException {

		I2CDevice pwmBoard = boardAddressToBusMap.get(pwmBoardAddress);

		if (pwmBoard == null) {

			I2CBus bus = I2CFactory.getInstance(i2cBus);
			I2CDevice newPwmBoard = bus.getDevice(pwmBoardAddress);


		}

		//		pwmBoard
		//		setPWMFreq(HERTZ); // Set frequency to 60 Hz
	}
}
