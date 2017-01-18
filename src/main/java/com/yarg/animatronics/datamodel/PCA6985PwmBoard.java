package com.yarg.animatronics.datamodel;

/**
 * This is an implementation of the Adafruit PCA6985 PWM Board used with the Raspberry PI.
 *
 * Datasheet: https://cdn-shop.adafruit.com/datasheets/PCA9685.pdf
 * Other related information: https://learn.adafruit.com/16-channel-pwm-servo-driver/downloads
 *
 * @author Ben Yarger benjamin.yarger@gmail.com
 */
public class PCA6985PwmBoard extends PwmBoard {

	private static final int hertz = 50;

	@Override
	public int getFrequency() {
		return hertz;
	}

}
