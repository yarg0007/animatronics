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

/**
 * Implementation of the TowerPro MG995R Servo.
 *
 * @author Ben Yarger benjamin.yarger@gmail.com
 */
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
}
