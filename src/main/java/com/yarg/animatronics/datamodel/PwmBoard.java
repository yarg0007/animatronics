package com.yarg.animatronics.datamodel;

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

import java.util.ArrayList;
import java.util.List;

public abstract class PwmBoard {

	protected ArrayList<PwmMotor> motors = new ArrayList<>();
	private int pwmBoardAddress;

	/**
	 * Get the signaling frequency of the board.
	 * @return Signaling frequency of the board.
	 */
	public abstract int getFrequency();

	/**
	 * Get the number of channels available on the board.
	 * @return Number of channels available on the board.
	 */
	public abstract int getNumberOfChannels();

	/**
	 * Get the address of the PWM board that this motor is connected to.
	 * @return The address of the PWM board for this motor.
	 */
	public int getPwmBoardAddress() {
		return pwmBoardAddress;
	}

	/**
	 * Set the address of the PWM board that this motor is connected to.
	 * @param pwmBoardAddress The address of the PWM board for this motor.
	 */
	public void setPwmBoardAddress(int pwmBoardAddress) {
		this.pwmBoardAddress = pwmBoardAddress;
	}

	/**
	 * Get a copy of all of the attached motors.
	 * @return Copy of all of the attached motors.
	 */
	public List<PwmMotor> getAttachedMotors() {
		ArrayList<PwmMotor> motorsCopy = new ArrayList<>();
		motorsCopy.addAll(motors);
		return motorsCopy;
	}

	/**
	 * Add a motor to the board. If there are no more available channels to connect motors to an ArrayStoreException
	 * will be thrown. If there is another motor already assigned to the same channel an ArrayStoreException will be
	 * thrown. If the channels specified does not fit in the range available on the board then an ArrayStoreException
	 * will be thrown.
	 * @param motor Motor to add to board.
	 */
	public void addMotor(PwmMotor motor) {

		if (motor.getPwmChannel() > getNumberOfChannels()) {
			throw new ArrayStoreException("Unable to add motor with PWM channel index greater than the number of available channels on the board.");
		} else if (motor.getPwmChannel() < 1) {
			throw new ArrayStoreException("Unable to add motor with PWM channel index less than 1.");
		}

		for (PwmMotor existingMotor : motors) {
			if (existingMotor.getPwmChannel() == motor.getPwmChannel()) {
				throw new ArrayStoreException("PWM channel is already in use by another motor on this board. Please choose a different channel.");
			}
		}

		motors.add(motor);
	}
}
