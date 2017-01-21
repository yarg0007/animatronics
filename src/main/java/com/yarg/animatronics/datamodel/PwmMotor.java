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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class PwmMotor {

	private ArrayList<AnimationKey> animationKeys = new ArrayList<>();
	private int pwmChannel = 1;

	/**
	 * Maximum tick value to achieve maximum rotation.
	 * @return Maximum tick value.
	 */
	public abstract int getMaxTick();

	/**
	 * Minimum tick value to achieve minimum rotation.
	 * @return Minimum tick value.
	 */
	public abstract int getMinTick();

	/**
	 * Maximum rotation angle for the motor.
	 * @return Maximum rotation angle for the motor.
	 */
	public abstract double getMaxAngle();

	/**
	 * Minimum rotation angle for the motor.
	 * @return Minimum rotation angle for the motor.
	 */
	public abstract double getMinAngle();

	/**
	 * Name or ID of the motor. Used when referencing the motor for consumers."
	 * @return Name or ID of the motor.
	 */
	public abstract String getMotorId();

	/**
	 * Calculate the number of ticks per millisecond based on the specified signaling frequency.
	 * @param signalFrequencyHz The signal frequency used with the motor. Comes from PWM board.
	 * @return Number of ticks per second for the subclass motor.
	 */
	public static int getTicksPerMillisecond(int signalFrequencyHz) {

		int millisecondRefresh = 1000 /  signalFrequencyHz;
		int ticksPerMillisecond = 4096 / millisecondRefresh;
		return ticksPerMillisecond;
	}

	/**
	 * Get the channel on the PWM board that this motor is connected to.
	 * @return The PWM channel for this motor.
	 */
	public int getPwmChannel() {
		return pwmChannel;
	}

	/**
	 * Set the channel on the PWM board that this motor is connected to.
	 * @param pwmChannel PWM channel for this motor.
	 */
	public void setPwmChannel(int pwmChannel) {
		this.pwmChannel = pwmChannel;
	}

	/**
	 * Get the number of animation keys set for this motor.
	 * @return Number of animation keys set for this motor.
	 */
	public int getNumberOfKeys() {
		return animationKeys.size();
	}

	/**
	 * Get the first animation key index.
	 * @return First animation key index, or -1 if there aren't any animation keys set.
	 */
	public int getFirstAnimationKeyIndex() {
		if (animationKeys.size() == 0) {
			return -1;
		}

		return 0;
	}

	/**
	 * Get the last animation key index.
	 * @return Last animation key index, or -1 if there aren't any animation keys set.
	 */
	public int getLastAnimationKeyIndex() {
		return animationKeys.size() - 1;
	}

	/**
	 * Get the tick value at the animation index specified.
	 * @param index Animation index to retrieve tick value for.
	 * @return Tick value.
	 */
	public int getTickAtIndex(int index) {
		return animationKeys.get(index).getTick();
	}

	/**
	 * Set the tick value at the animation index specified. Clamped to the max and min tick values defined for the
	 * motor.
	 * @param index Index to set the tick value for.
	 * @param tick Tick value to set at the index specified.
	 */
	public void setTickAtIndex(int index, int tick) {

		if (tick > getMaxTick()) {
			tick = getMaxTick();
		} else if (tick < getMinTick()) {
			tick = getMinTick();
		}

		animationKeys.get(index).setTick(tick);
	}

	/**
	 * Get the time in milliseconds of the animation index specified.
	 * @param index Animation index to retrieve tick value for.
	 * @return Time in milliseconds from the start of the animation sequence.
	 */
	public long getTimeAtIndex(int index) {
		return animationKeys.get(index).getTime();
	}

	/**
	 * Set the time of the animation index specified. The time specified must be greater than the previous animation
	 * key's time and less than the next animation key's time.
	 * @param index Animation index to set the time for.
	 * @param time Time, in milliseconds, from the start of the animation (0) to set this animtion key time to.
	 */
	public void setTimeAtIndex(int index, long time) {

		if (index > 0 && getTimeAtIndex(index - 1) >= time) {
			throw new IllegalArgumentException("Time specified must be greater than the time of the previous key.");
		} else if (index < (animationKeys.size() - 1) && getTimeAtIndex(index + 1) <= time) {
			throw new IllegalArgumentException("Time specified must be less than the time of the next key.");
		}

		animationKeys.get(index).setTime(time);
	}

	/**
	 * Set the rotation angle for the animation key at specified index. Clamped to the max and min angle values defined
	 * for the motor.
	 * @param index Animation key index to set rotation for.
	 * @param angle Angle to set.
	 */
	public void setAngleAtIndex(int index, double angle) {

		if (angle > getMaxAngle()) {
			angle = getMaxAngle();
		} else if (angle < getMinAngle()) {
			angle = getMinAngle();
		}

		int ticks = convertAngleToTicks(angle);
		setTickAtIndex(index, ticks);
	}

	/**
	 * Get the angle of rotation at the specified animation key index.
	 * @param index Animation key index to get rotation for.
	 * @return Angle of rotation set at the specified animation key index.
	 */
	public double getAngleAtIndex(int index) {

		int ticks = getTickAtIndex(index);
		return convertTicksToAngle(ticks);
	}

	/**
	 * Add a new animation key to the end of the animation sequence. Animation key created will
	 * have the same tick value as the previous key and a time one second after the previous key.
	 * @return Index of the newly created animation key.
	 */
	public int addAnimationKey() {

		int tick = 0;
		long time = 0L;

		if (animationKeys.size() > 0) {
			tick = animationKeys.get(animationKeys.size()-1).getTick();
			time = animationKeys.get(animationKeys.size()-1).getTime() + 1000L;
		}

		AnimationKey newKey = new AnimationKey(tick, time);
		animationKeys.add(newKey);
		return (animationKeys.size() - 1);
	}

	/**
	 * Add a new animation key at the specified index. If index is between two keys the
	 * key created will have a tick and time values half way between the nearest neighbors.
	 * If the index is greater than the size of the animation key sequence a new key will be
	 * added on the end. If the index is less than or equal to 0 index 0 will be returned and
	 * no new key will be added.
	 * @param index Index to create animation key at.
	 * @return Index of the key created.
	 * @throws IllegalArgumentException.
	 */
	public int addAnimationKeyAtIndex(int index) {

		if (index < 0) {
			throw new IllegalArgumentException("Index must be a positive value.");
		}

		if (index >= animationKeys.size()) {
			return addAnimationKey();
		} else if (index <= 0) {
			return 0;
		}

		int tick = 0;
		long time = 0L;

		// Calculate tick between nearest neighbors.
		// Calculate time between nearest neighbors.
		int nextTick = animationKeys.get(index).getTick();
		int previousTick = animationKeys.get(index - 1).getTick();
		tick = (nextTick - previousTick) / 2 + previousTick;

		long nextTime = animationKeys.get(index).getTime();
		long previousTime = animationKeys.get(index - 1).getTime();
		time = (nextTime - previousTime) / 2 + previousTime;

		if (time <= previousTime || time >= nextTime) {
			throw new IllegalArgumentException("Unable to subdivide time any further at the index specified.");
		}

		AnimationKey newKey = new AnimationKey(tick, time);
		animationKeys.add(index, newKey);
		return index;
	}

	/**
	 * Remove the animation key at the specified index.
	 * @param index
	 */
	public void removeKeyAtIndex(int index) {
		animationKeys.remove(index);
	}

	/**
	 * Convert angle to ticks for the motor defined in the subclass.
	 * @param angle Angle to convert.
	 * @return Angle converted to ticks.
	 */
	protected int convertAngleToTicks(double angle) {

		double angleDelta = getMaxAngle() - getMinAngle();
		int tickDelta = getMaxTick() - getMinTick();
		double fromMinAngleDelta = angle - getMinAngle();
		int ticksConverted = (int) (tickDelta * fromMinAngleDelta / angleDelta + getMinTick());
		return ticksConverted;
	}

	/**
	 * Convert ticks to angle for the motor defined in the subclass.
	 * @param ticks Ticks to convert.
	 * @return Ticks converted to degrees.
	 */
	protected double convertTicksToAngle(int ticks) {

		int fromMinTickDelta = ticks - getMinTick();
		int tickDelta = getMaxTick() - getMinTick();
		double angleDelta = getMaxAngle() - getMinAngle();
		double angleConverted = fromMinTickDelta * angleDelta / tickDelta + getMinAngle();
		return angleConverted;
	}

	/**
	 * Return a copy of the animation keys list for this motor.
	 * @return Copy of the animation keys list for this motor.
	 */
	protected List<AnimationKey> getAnimationKeys() {
		ArrayList<AnimationKey> copyOfAnimationKeys = new ArrayList<>();
		copyOfAnimationKeys.addAll(animationKeys);
		return copyOfAnimationKeys;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(33, 19)
				.append(animationKeys)
				.append(pwmChannel)
				.append(getMaxAngle())
				.append(getMinAngle())
				.append(getMaxTick())
				.append(getMinTick())
				.append(getMotorId())
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		} else if (!(obj instanceof PwmMotor)) {
			return false;
		}

		PwmMotor compareObj = (PwmMotor) obj;
		return new EqualsBuilder()
				.append(getAnimationKeys(), compareObj.getAnimationKeys())
				.append(getPwmChannel(), compareObj.getPwmChannel())
				.append(getMaxAngle(), compareObj.getMaxAngle())
				.append(getMinAngle(), compareObj.getMinAngle())
				.append(getMaxTick(), compareObj.getMaxTick())
				.append(getMinTick(), compareObj.getMinTick())
				.append(getMotorId(), compareObj.getMotorId())
				.isEquals();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
				.append("Animation Keys", getAnimationKeys())
				.append("Pwm Channel", getPwmChannel())
				.append("Max Angle", getMaxAngle())
				.append("Min Angle", getMinAngle())
				.append("Max Tick", getMaxTick())
				.append("Min Tick", getMinTick())
				.append("Motor Id", getMotorId())
				.toString();
	}


}
