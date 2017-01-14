package com.yarg.animatronics.controller;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class OldPwmController extends Thread {

	public final static int HERTZ = 50;

	public final static int SUBADR1       = 0x02;
	public final static int SUBADR2       = 0x03;
	public final static int SUBADR3       = 0x04;
	public final static int MODE1         = 0x00;
	public final static int PRESCALE      = 0xFE;
	public final static int LED0_ON_L     = 0x06;
	public final static int LED0_ON_H     = 0x07;
	public final static int LED0_OFF_L    = 0x08;
	public final static int LED0_OFF_H    = 0x09;
	public final static int ALL_LED_ON_L  = 0xFA;
	public final static int ALL_LED_ON_H  = 0xFB;
	public final static int ALL_LED_OFF_L = 0xFC;
	public final static int ALL_LED_OFF_H = 0xFD;

	private static boolean verbose = true;

	// Output Channels
	private static final int DRIVE_CHANNEL = 15;
	private static final int DRIVE_DIRECTION_CHANNEL = 14;
	private static final int TURN_CHANNEL = 13;
	private static final int HEAD_LIFT_CHANNEL = 12;
	private static final int HEAD_TURN_CHANNEL = 11;
	private static final int TALK_CHANNEL = 10;

	// Drive channel max and min
	//	private static final int DRIVE_CHANNEL_PWM_MAX = 4095;
	private static final int DRIVE_CHANNEL_PWM_MIN = 0;

	// Drive direction channel max and min
	private static final int DRIVE_DIRECTION_CHANNEL_PWM_MAX = 4095;
	private static final int DRIVE_DIRECTION_CHANNEL_PWM_MIN = 0;

	// Turn channel max, min and center
	//	private static final int TURN_CHANNEL_PWM_CENTER = 325;
	private static final int TURN_CHANNEL_PWM_MAX = 400;
	private static final int TURN_CHANNEL_PWM_MIN = 250;

	// Head lift channel max, min and center
	//	private static final int HEAD_LIFT_CHANNEL_PWM_CENTER = 200;
	private static final int HEAD_LIFT_CHANNEL_PWM_MAX = 350;
	private static final int HEAD_LIFT_CHANNEL_PWM_MIN = 200;

	// Head turn channel max, min and center
	//	private static final int HEAD_TURN_CHANNEL_PWM_CENTER = 325;
	private static final int HEAD_TURN_CHANNEL_PWM_MAX = 400;
	private static final int HEAD_TURN_CHANNEL_PWM_MIN = 250;

	// Talk channel max, min and center
	//	private static final int TALK_CHANNEL_PWM_CENTER = 295;
	private static final int TALK_CHANNEL_PWM_MAX = 250;
	private static final int TALK_CHANNEL_PWM_MIN = 340;

	private I2CBus    bus;
	private I2CDevice servoDriver;

	// Used for calculating change in acceleration.
	private int previousDriveInput;

	// Used for timing slow down.
	private long lastInputTime;
	private boolean running;

	private static OldPwmController pwmController;

	/**
	 * Get the PWMController singleton.
	 * @return PWMController singleton instance.
	 */
	public static OldPwmController getInstance()
	{
		if (pwmController == null) {
			pwmController = new OldPwmController();
		}
		return pwmController;
	}

	/**
	 * Create the singleton PWM controller instance.
	 */
	private OldPwmController()
	{
		previousDriveInput = 0;
		init(0x40); // 0x40 obtained through sudo i2cdetect -y 1
		lastInputTime = System.currentTimeMillis();
		running = false;
	}

	/**
	 * Start the PWM controller loop.
	 */
	public void startPWMController() {
		running= true;
		this.start();
	}

	/**
	 * Stop the PWM controller loop.
	 */
	public void stopPWMController() {

		running = false;
		this.interrupt();

		try {
			sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Bring robot to stop.
		setPWM(DRIVE_CHANNEL, 0, 0);
		setPWM(TURN_CHANNEL, 0, 0);
		setPWM(HEAD_LIFT_CHANNEL, 0, 0);
		setPWM(HEAD_TURN_CHANNEL, 0, 0);
		setPWM(TALK_CHANNEL, 0, 0);
	}

	/**
	 * Set the drive input.
	 * @param input Input values are expected in the range -100 through +100.
	 */
	public void setDriveInput(int input) {

		if (!running) {
			return;
		}

		// Lock to our input range.
		if (input < -100) {
			input = -100;
		} else if (input > 100) {
			input = 100;
		}

		// Timestamp the current input. If over a certain amount of time
		// the drive will automatically slow down.
		lastInputTime = System.currentTimeMillis();

		if (input == 0) {
			previousDriveInput = previousDriveInput/2;
		} else if (input - previousDriveInput > 0) {
			previousDriveInput++;
		} else if (input - previousDriveInput < 0) {
			previousDriveInput--;
		}

		if (previousDriveInput > 100) {
			previousDriveInput = 100;
		} else if (previousDriveInput < -100) {
			previousDriveInput = -100;
		}

		if (previousDriveInput < 0) {
			// set to reverse
			setPWM(DRIVE_DIRECTION_CHANNEL, 0, DRIVE_DIRECTION_CHANNEL_PWM_MIN);
		} else {
			// set to forward
			setPWM(DRIVE_DIRECTION_CHANNEL, 0, DRIVE_DIRECTION_CHANNEL_PWM_MAX);
		}

		// 1) Calculate the percent of total speed as drivePercentValue.
		// 2) Calculate the percentage of MAX speed to send out as pwm.
		// 3) Make sure pwm value is positive.
		float drivePercentValue = previousDriveInput / 100.0f;
		int pwmValue = (int)(drivePercentValue*DRIVE_DIRECTION_CHANNEL_PWM_MAX);
		pwmValue = Math.abs(pwmValue);

		// If PWM is < 25 don't move This is our change direction buffer.
		if (pwmValue < 5) {
			setPWM(DRIVE_CHANNEL, 0, DRIVE_CHANNEL_PWM_MIN);
		} else {
			setPWM(DRIVE_CHANNEL, 0, pwmValue);
			//			System.out.println("Set drive input: "+pwmValue);
		}
	}

	public void setTurnInput(int input) {
		int pwmValue = calculateServoPWMValue(input, TURN_CHANNEL_PWM_MIN, TURN_CHANNEL_PWM_MAX);
		setPWM(TURN_CHANNEL, 0, pwmValue);
	}

	public void setHeadLiftInput(int input) {
		int pwmValue = calculateServoPWMValue(input, HEAD_LIFT_CHANNEL_PWM_MIN, HEAD_LIFT_CHANNEL_PWM_MAX);
		setPWM(HEAD_LIFT_CHANNEL, 0, pwmValue);
	}

	public void setHeadTurnInput(int input) {
		int pwmValue = calculateServoPWMValue(input, HEAD_TURN_CHANNEL_PWM_MIN, HEAD_TURN_CHANNEL_PWM_MAX);
		setPWM(HEAD_TURN_CHANNEL, 0, pwmValue);
	}

	public void setTalkInput(int isTalking, int talkingLevel, int openMouth) {

		if (openMouth == 1) {
			setPWM(TALK_CHANNEL, 0, TALK_CHANNEL_PWM_MAX);
		} else if (isTalking == 1) {

			int pwmValue = TALK_CHANNEL_PWM_MIN;

			if (talkingLevel == 100) {
				pwmValue = TALK_CHANNEL_PWM_MAX;
			}

			//			int pwmValue = calculateServoPWMValue(talkingLevel, TALK_CHANNEL_PWM_MIN, TALK_CHANNEL_PWM_MAX);
			setPWM(TALK_CHANNEL, 0, pwmValue);
		} else {
			setPWM(TALK_CHANNEL, 0, TALK_CHANNEL_PWM_MIN);
		}
	}

	// Thread to slow vehicle down if no input occurred in last 40ms (25hz update)
	@Override
	public void run() {

		long checkTime;

		while (running) {
			checkTime = System.currentTimeMillis();

			if ((checkTime - lastInputTime) > 500l) {
				setDriveInput(0);
			}

			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// -----------------------------------------------------------------------
	// Private Methods
	// -----------------------------------------------------------------------

	/**
	 * Calculate the servo pwm value based on input and MIN/MAX servo PWM values.
	 * This is used to calculate the rotation of a servo from an open (MAX) to
	 * closed (MIN) position.
	 *
	 * @param input Input in the range of -100 to +100.
	 * @param MIN Minimum PWM value allowed.
	 * @param MAX Maximum PWM value allowed.
	 * @return PWM value corresponding to input.
	 */
	private int calculateServoPWMValue(int input, float MIN, float MAX) {

		if (input < -100) {
			input = -100;
		} else if (input > 100) {
			input = 100;
		}

		// Linear curve
		float slope = (MAX - MIN)/200.0f;
		float result = (input + 100.0f) * slope + MIN;
		return (int)result;
	}

	private void init(int address) {

		try
		{
			// Get I2C bus
			bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version

			if (verbose) {
				System.out.println("Connected to bus. OK.");
			}

			// Get the device itself
			servoDriver = bus.getDevice(address);

			if (verbose) {
				System.out.println("Connected to device. OK.");
			}

			// Reseting
			servoDriver.write(MODE1, (byte)0x00);
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
		}

		setPWMFreq(HERTZ); // Set frequency to 60 Hz
	}

	private void setPWMFreq(int freq)
	{
		float prescaleval = 25000000.0f; // 25MHz
		prescaleval /= 4096.0;           // 12-bit
		prescaleval /= freq;
		prescaleval -= 1.0;
		if (verbose) {
			System.out.println("Setting PWM frequency to " + freq + " Hz");
			System.out.println("Estimated pre-scale: " + prescaleval);
		}

		double prescale = Math.floor(prescaleval + 0.5);

		if (verbose) {
			System.out.println("Final pre-scale: " + prescale);
		}

		try {
			byte oldmode = (byte)servoDriver.read(MODE1);
			byte newmode = (byte)((oldmode & 0x7F) | 0x10); // sleep
			servoDriver.write(MODE1, newmode);              // go to sleep
			servoDriver.write(PRESCALE, (byte)(Math.floor(prescale)));
			servoDriver.write(MODE1, oldmode);

			Thread.sleep(5);

			servoDriver.write(MODE1, (byte)(oldmode | 0x80));
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}

	private void setPWM(int channel, int on, int off) {
		try
		{
			servoDriver.write(LED0_ON_L  + 4 * channel, (byte)(on & 0xFF));
			servoDriver.write(LED0_ON_H  + 4 * channel, (byte)(on >> 8));
			servoDriver.write(LED0_OFF_L + 4 * channel, (byte)(off & 0xFF));
			servoDriver.write(LED0_OFF_H + 4 * channel, (byte)(off >> 8));
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	/*
	private void setServoPulse(int channel, int pulse)
	{
		int pulseLength = 1000000; // 1,000,000 us per second
		pulseLength /= HERTZ;

		if (verbose) {
	      System.out.println(pulseLength + " us per period");
		}
		pulseLength /= 4096;       // 12 bits of resolution
		if (verbose) {
			System.out.println(pulseLength + " us per bit");
		}
		pulse *= 1000;
		pulse /= pulseLength;
		this.setPWM(channel, 0, pulse);
	}
	 */
}
