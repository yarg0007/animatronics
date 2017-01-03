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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class AnimationKey {

	private int tick;
	private long time;

	public AnimationKey(int tick, long time) {
		this.tick = tick;
		this.time = time;
	}

	public int getTick() {
		return tick;
	}

	public AnimationKey setTick(int tick) {
		this.tick = tick;
		return this;
	}

	public long getTime() {
		return time;
	}

	public AnimationKey setTime(long time) {
		this.time = time;
		return this;
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(13, 33)
				.append(tick)
				.append(time)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		} else if (!(obj instanceof AnimationKey)) {
			return false;
		}

		AnimationKey compareObj = (AnimationKey) obj;
		return new EqualsBuilder()
				.append(getTick(), compareObj.getTick())
				.append(getTime(), compareObj.getTime())
				.isEquals();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this)
				.append("tick", getTick())
				.append("time", getTime())
				.toString();
	}

}
