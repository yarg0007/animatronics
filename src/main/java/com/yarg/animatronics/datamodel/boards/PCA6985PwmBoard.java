package com.yarg.animatronics.datamodel.boards;

import com.yarg.animatronics.datamodel.PwmBoard;

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
	private static final int numberOfChannels = 16;

	public static final int MODE1 = 0x00; // Mode register 1
	public static final int MODE2 = 0x01; // Mode register 2
	public static final int SUBADR1 = 0x02; // I2C Bus sub-address 1
	public static final int SUBADR2 = 0x03; // I2C Bus sub-address 2
	public static final int SUBADR3 = 0x04; // I2C Bus sub-address 3
	public static final int ALLCALLADR = 0x05;	// LED All Call I2C-bus address

	public static final int LED0_ON_L = 0x06;	// LED0 output and brightness control byte 0
	public static final int LED0_ON_H = 0x07;	// LED0 output and brightness control byte 1
	public static final int LED0_OFF_L = 0x08;	// LED0 output and brightness control byte 2
	public static final int LED0_OFF_H = 0x09;	// LED0 output and brightness control byte 3

	public static final int LED1_ON_L = 0x0A;	// LED1 output and brightness control byte 0
	public static final int LED1_ON_H = 0x0B;	// LED1 output and brightness control byte 1
	public static final int LED1_OFF_L = 0x0C;	// LED1 output and brightness control byte 2
	public static final int LED1_OFF_H = 0x0D;	// LED1 output and brightness control byte 3

	public static final int LED2_ON_L = 0x0E;	// LED2 output and brightness control byte 0
	public static final int LED2_ON_H = 0x0F;	// LED2 output and brightness control byte 1
	public static final int LED2_OFF_L = 0x10;	// LED2 output and brightness control byte 2
	public static final int LED2_OFF_H = 0x11;	// LED2 output and brightness control byte 3

	public static final int LED3_ON_L = 0x12;	// LED3 output and brightness control byte 0
	public static final int LED3_ON_H = 0x13;	// LED3 output and brightness control byte 1
	public static final int LED3_OFF_L = 0x14;	// LED3 output and brightness control byte 2
	public static final int LED3_OFF_H = 0x15;	// LED3 output and brightness control byte 3

	public static final int LED4_ON_L = 0x16;	// LED4 output and brightness control byte 0
	public static final int LED4_ON_H = 0x17;	// LED4 output and brightness control byte 1
	public static final int LED4_OFF_L = 0x18;	// LED4 output and brightness control byte 2
	public static final int LED4_OFF_H = 0x19;	// LED4 output and brightness control byte 3

	public static final int LED5_ON_L = 0x1A;	// LED5 output and brightness control byte 0
	public static final int LED5_ON_H = 0x1B;	// LED5 output and brightness control byte 1
	public static final int LED5_OFF_L = 0x1C;	// LED5 output and brightness control byte 2
	public static final int LED5_OFF_H = 0x1D;	// LED5 output and brightness control byte 3

	public static final int LED6_ON_L = 0x1E;	// LED6 output and brightness control byte 0
	public static final int LED6_ON_H = 0x1f;	// LED6 output and brightness control byte 1
	public static final int LED6_OFF_L = 0x20;	// LED6 output and brightness control byte 2
	public static final int LED6_OFF_H = 0x21;	// LED6 output and brightness control byte 3

	public static final int LED7_ON_L = 0x22;	// LED7 output and brightness control byte 0
	public static final int LED7_ON_H = 0x23;	// LED7 output and brightness control byte 1
	public static final int LED7_OFF_L = 0x24;	// LED7 output and brightness control byte 2
	public static final int LED7_OFF_H = 0x25;	// LED7 output and brightness control byte 3

	public static final int LED8_ON_L = 0x26;	// LED8 output and brightness control byte 0
	public static final int LED8_ON_H = 0x27;	// LED8 output and brightness control byte 1
	public static final int LED8_OFF_L = 0x28;	// LED8 output and brightness control byte 2
	public static final int LED8_OFF_H = 0x29;	// LED8 output and brightness control byte 3

	public static final int LED9_ON_L = 0x2A;	// LED9 output and brightness control byte 0
	public static final int LED9_ON_H = 0x2B;	// LED9 output and brightness control byte 1
	public static final int LED9_OFF_L = 0x2C;	// LED9 output and brightness control byte 2
	public static final int LED9_OFF_H = 0x2D;	// LED9 output and brightness control byte 3

	public static final int LED10_ON_L = 0x2E;	// LED10 output and brightness control byte 0
	public static final int LED10_ON_H = 0x2F;	// LED10 output and brightness control byte 1
	public static final int LED10_OFF_L = 0x30;	// LED10 output and brightness control byte 2
	public static final int LED10_OFF_H = 0x31;	// LED10 output and brightness control byte 3

	public static final int LED11_ON_L = 0x32;	// LED11 output and brightness control byte 0
	public static final int LED11_ON_H = 0x33;	// LED11 output and brightness control byte 1
	public static final int LED11_OFF_L = 0x34;	// LED11 output and brightness control byte 2
	public static final int LED11_OFF_H = 0x35;	// LED11 output and brightness control byte 3

	public static final int LED12_ON_L = 0x36;	// LED12 output and brightness control byte 0
	public static final int LED12_ON_H = 0x37;	// LED12 output and brightness control byte 1
	public static final int LED12_OFF_L = 0x38;	// LED12 output and brightness control byte 2
	public static final int LED12_OFF_H = 0x39;	// LED12 output and brightness control byte 3

	public static final int LED13_ON_L = 0x3A;	// LED13 output and brightness control byte 0
	public static final int LED13_ON_H = 0x3B;	// LED13 output and brightness control byte 1
	public static final int LED13_OFF_L = 0x3C;	// LED13 output and brightness control byte 2
	public static final int LED13_OFF_H = 0x3D;	// LED13 output and brightness control byte 3

	public static final int LED14_ON_L = 0x3E;	// LED14 output and brightness control byte 0
	public static final int LED14_ON_H = 0x3F;	// LED14 output and brightness control byte 1
	public static final int LED14_OFF_L = 0x40;	// LED14 output and brightness control byte 2
	public static final int LED14_OFF_H = 0x41;	// LED14 output and brightness control byte 3

	public static final int LED15_ON_L = 0x42;	// LED15 output and brightness control byte 0
	public static final int LED15_ON_H = 0x43;	// LED15 output and brightness control byte 1
	public static final int LED15_OFF_L = 0x44;	// LED15 output and brightness control byte 2
	public static final int LED15_OFF_H = 0x45;	// LED15 output and brightness control byte 3

	public static final int ALL_LED_ON_L = 0xFA; // Load all the LEDn_ON registers byte 0
	public static final int ALL_LED_ON_H = 0xFB; // Load all the LEDn_ON registers byte 1
	public static final int ALL_LED_OFF_L = 0xFC; // Load all the LEDn_OFF registers byte 0
	public static final int ALL_LED_OFF_H = 0xFD; // Load all the LEDn_OFF registers byte 1

	public static final int PRE_SCALE = 0xFE; // [1] Pre-scaler for PWM output frequency
	public static final int TEST_MODE = 0xFF; // [2]

	/*
	[1] Writes to PRE_SCALE register are blocked when SLEEP bit is logic 0 (MODE 1).
	[2] Reserved. Writes to this register may cause unpredictable results.
	Remark: Auto Increment past register 69 will point to MODE1 register (register 0).
	Auto Increment also works from register 250 to register 254, then rolls over to register 0.
	 */

	@Override
	public int getFrequency() {
		return hertz;
	}

	@Override
	public int getNumberOfChannels() {
		return numberOfChannels;
	}
}
