package com.yarg.animatronics.controller;

import java.io.IOException;
import java.util.HashMap;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.yarg.animatronics.datamodel.PwmBoard;
import com.yarg.animatronics.datamodel.boards.PCA6985PwmBoard;
import com.yarg.animatronics.log.Logger;
import com.yarg.animatronics.log.Logger.SEVERITY;

public class PCA69855Controller {

	protected HashMap<Integer, I2CDevice> boardAddressToI2cDeviceMap = new HashMap<>();
	protected boolean verbose;

	private I2CBus i2cBus;

	/**
	 * Configured with I2C Bus set to BUS_1 and verbose output set to false.
	 * @throws IOException
	 * @throws UnsupportedBusNumberException
	 */
	public PCA69855Controller() throws UnsupportedBusNumberException, IOException {
		this(I2CBus.BUS_1, false);
	}

	/**
	 * Configured with the I2C Bus set to BUS_1 and verbose specified by the consumer.
	 * @param verbose Enable verbose logging of controller execution.
	 * @throws IOException
	 * @throws UnsupportedBusNumberException
	 */
	public PCA69855Controller(boolean verbose) throws UnsupportedBusNumberException, IOException {
		this(I2CBus.BUS_1, verbose);
	}

	/**
	 * Configured with the I2C Bus and verbose specified by the consumer.
	 * @param i2cBusNumber I2CBus number to use.
	 * @param verbose Enable verbose logging of controller execution.
	 * @throws IOException
	 * @throws UnsupportedBusNumberException
	 */
	public PCA69855Controller(int i2cBusNumber, boolean verbose) throws UnsupportedBusNumberException, IOException {

		this.verbose = verbose;

		i2cBus = I2CFactory.getInstance(i2cBusNumber);
	}

	/**
	 * Add a board to the controller, configuring the I2C frequency and resetting the device.
	 * @param board Board to add to the controller.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void addBoard(PwmBoard board) throws IOException, InterruptedException {

		Logger.log(SEVERITY.INFO, "Adding board (address: " + board.getPwmBoardAddress() + ").", verbose);

		Integer boardAddress = board.getPwmBoardAddress();
		if (boardAddressToI2cDeviceMap.containsKey(boardAddress)) {
			Logger.log(SEVERITY.INFO, "Board with address : " + board.getPwmBoardAddress() + ", already configured.", verbose);
			return;
		}

		I2CDevice i2cDevice = i2cBus.getDevice(board.getPwmBoardAddress());

		resetDevice(i2cDevice);
		setPWMFreq(board.getFrequency(), i2cDevice);
		boardAddressToI2cDeviceMap.put(board.getPwmBoardAddress(), i2cDevice);
	}

	/**
	 * Remove the board from the controller, resetting the device in the process.
	 * @param board Board to remove from the controller.
	 * @throws IOException
	 */
	public void removeBoard(PwmBoard board) throws IOException {
		I2CDevice device = boardAddressToI2cDeviceMap.remove(board.getPwmBoardAddress());
		resetDevice(device);
	}

	public void writePwm(int boardAddress, int channel, int on, int off) throws IOException {

		I2CDevice device = boardAddressToI2cDeviceMap.get(Integer.valueOf(boardAddress));

		if (device == null) {
			throw new IllegalArgumentException("Unknown board address specified: " + boardAddress + ".");
		}

		if (channel < 0 || channel > 15) {
			throw new IllegalArgumentException("Unknown channel specified: " + channel + ".");
		}

		// Write the twelve bits of the on signal.
		device.write(PCA6985PwmBoard.LED0_ON_L + 4 * channel, (byte) (on & 0xFF));
		device.write(PCA6985PwmBoard.LED0_ON_H + 4 * channel, (byte) (on >> 8));

		// Write the twelve bits of the off signal.
		device.write(PCA6985PwmBoard.LED0_OFF_L + 4 * channel, (byte) (off & 0xFF));
		device.write(PCA6985PwmBoard.LED0_OFF_H + 4 * channel, (byte) (off >> 8));
	}

	/**
	 * Reset the device.
	 * @param i2cDevice Device to reset.
	 * @throws IOException
	 */
	protected void resetDevice(I2CDevice i2cDevice) throws IOException {
		i2cDevice.write(PCA6985PwmBoard.MODE1, (byte)0x00);
	}

	/**
	 * Set the PWM frequency for the i2c device.
	 * @param freq Signaling frequency for communicating with the device.
	 * @param i2cDevice Device to set signaling frequency for.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected void setPWMFreq(int freq, I2CDevice i2cDevice) throws IOException, InterruptedException
	{
		// This prescale equation is taken from the PCA9685 datasheet.
		// Assumes oscillator clock frequency is set to 25 MHz.

		float prescaleval = 25000000.0f;
		prescaleval /= (4096.0 * freq);
		prescaleval -= 1.0;
		double prescale = Math.floor(prescaleval + 0.5);

		if (verbose) {
			Logger.log(SEVERITY.INFO, "Setting PWM frequency to " + freq + " Hz.", verbose);
			Logger.log(SEVERITY.INFO, "Calculated pre-scale: " + prescaleval + ".", verbose);
			Logger.log(SEVERITY.INFO, "Final pre-scale: " + prescale + ".", verbose);
		}

		byte oldmode = (byte) i2cDevice.read(PCA6985PwmBoard.MODE1);
		byte newmode = (byte) ((oldmode & 0x7F) | 0x10);
		i2cDevice.write(PCA6985PwmBoard.MODE1, newmode);
		i2cDevice.write(PCA6985PwmBoard.PRE_SCALE, (byte) prescale);
		i2cDevice.write(PCA6985PwmBoard.MODE1, oldmode);

		// Sleep according to oscillator wake and run.
		// 500 microseconds = 0.5 milliseconds.
		Thread.sleep(1);

		i2cDevice.write(PCA6985PwmBoard.MODE1, (byte)(oldmode | 0x80));

	}
}
