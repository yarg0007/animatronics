package com.yarg.animatronics.datamodel;

import java.util.ArrayList;

public abstract class PwmMotor {

	private ArrayList<AnimationKey> animationKeys = new ArrayList<>();
	private int pwmChannel;

	public abstract int getMaxTick();

	public abstract int getMinTick();

	public abstract double getMaxAngle();

	public abstract double getMinAngle();

	public abstract String getMotorId();

	public abstract int getSignalFrequency();

	public int getPwmChannel() {
		return pwmChannel;
	}

	public void setPwmChannel(int pwmChannel) {
		this.pwmChannel = pwmChannel;
	}

	public int getNumberOfKeys() {
		return animationKeys.size();
	}

	public int getFirstAnimationKeyIndex() {
		if (animationKeys.size() == 0) {
			return -1;
		}

		return 0;
	}

	public int getLastAnimationKeyIndex() {
		return animationKeys.size() - 1;
	}

	public int getTickAtIndex(int index) {
		return animationKeys.get(index).getTick();
	}

	public void setTickAtIndex(int index, int tick) {

		if (tick > getMaxTick()) {
			tick = getMaxTick();
		} else if (tick < getMinTick()) {
			tick = getMinTick();
		}

		animationKeys.get(index).setTick(tick);
	}

	public long getTimeAtIndex(int index) {
		return animationKeys.get(index).getTime();
	}

	public void setTimeAtIndex(int index, long time) {

		if (index > 0 && getTimeAtIndex(index - 1) >= time) {
			throw new IllegalArgumentException("Time specified must be greater than the time of the previous key.");
		} else if (index < (animationKeys.size() - 1) && getTimeAtIndex(index + 1) <= time) {
			throw new IllegalArgumentException("Time specified must be less than the time of the next key.");
		}

		animationKeys.get(index).setTime(time);
	}

	/**
	 * Set the rotation angle for the animation key at specified index.
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

	protected int getTicksPerMillisecond() {

		int millisecondRefresh = 1000 /  getSignalFrequency();
		int ticksPerMillisecond = 4096 / millisecondRefresh;
		return ticksPerMillisecond;
	}

	protected int convertAngleToTicks(double angle) {

		double angleDelta = getMaxAngle() - getMinAngle();
		int tickDelta = getMaxTick() - getMinTick();
		double fromMinAngleDelta = angle - getMinAngle();
		int ticksConverted = (int) (tickDelta * fromMinAngleDelta / angleDelta + getMinTick());
		return ticksConverted;
	}

	protected double convertTicksToAngle(int ticks) {

		int fromMinTickDelta = ticks - getMinTick();
		int tickDelta = getMaxTick() - getMinTick();
		double angleDelta = getMaxAngle() - getMinAngle();
		double angleConverted = fromMinTickDelta * angleDelta / tickDelta + getMinAngle();
		return angleConverted;
	}
}
