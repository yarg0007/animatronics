package com.yarg.animatronics.datamodel;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PwmMotorTest {

	PwmMotor motor;

	@BeforeMethod(alwaysRun=true)
	public void setup() {
		motor = new TestPwmMotor();
	}

	// ------------------------------------------------------------------------
	// Get first animation key index tests
	// ------------------------------------------------------------------------

	@Test(enabled=true, groups={"PwmMotorTests","unit"})
	public void getFirstAnimationKeyIndexWithoutAnyKeys() {
		int index = motor.getFirstAnimationKeyIndex();
		assertThat(index, is(equalTo(-1)));
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"})
	public void getFirstAnimationKeyIndexWithOneKey() {
		motor.addAnimationKey();
		int index = motor.getFirstAnimationKeyIndex();
		assertThat(index, is(equalTo(0)));
	}

	// ------------------------------------------------------------------------
	// Add key tests
	// ------------------------------------------------------------------------

	@Test(enabled=true, groups={"PwmMotorTests","unit"})
	public void addKey() {
		motor.addAnimationKey();

		assertThat(motor.getNumberOfKeys(), is(equalTo(1)));
		assertThat(motor.getTimeAtIndex(0), is(equalTo(0L)));
		assertThat(motor.getTickAtIndex(0), is(equalTo(0)));
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"})
	public void addThreeKeys() {
		motor.addAnimationKey();
		motor.addAnimationKey();
		motor.setTickAtIndex(1, 300);
		motor.addAnimationKey();

		assertThat(motor.getNumberOfKeys(), is(equalTo(3)));
		assertThat(motor.getTimeAtIndex(0), is(equalTo(0L)));
		assertThat(motor.getTickAtIndex(0), is(equalTo(0)));
		assertThat(motor.getTimeAtIndex(1), is(equalTo(1000L)));
		assertThat(motor.getTickAtIndex(1), is(equalTo(300)));
		assertThat(motor.getTimeAtIndex(2), is(equalTo(2000L)));
		assertThat(motor.getTickAtIndex(2), is(equalTo(300)));
	}

	// ------------------------------------------------------------------------
	// Add key at index tests
	// ------------------------------------------------------------------------

	@Test(enabled=true, groups={"PwmMotorTests","unit"})
	public void addAnimationKeyToArrayLengthThreeAtIndexZero() {

		motor.addAnimationKey();
		motor.setTickAtIndex(0, 300);
		motor.addAnimationKey();
		motor.setTickAtIndex(1, 350);
		motor.addAnimationKey();
		motor.setTickAtIndex(2, 390);
		motor.addAnimationKeyAtIndex(0);

		assertThat(motor.getNumberOfKeys(), is(equalTo(3)));
		assertThat(motor.getTickAtIndex(0), is(equalTo(300)));
		assertThat(motor.getTimeAtIndex(0), is(equalTo(0L)));
		assertThat(motor.getTickAtIndex(1), is(equalTo(350)));
		assertThat(motor.getTimeAtIndex(1), is(equalTo(1000L)));
		assertThat(motor.getTickAtIndex(2), is(equalTo(390)));
		assertThat(motor.getTimeAtIndex(2), is(equalTo(2000L)));
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"})
	public void addAnimationKeyToArrayLengthThreeAtIndexFour() {

		motor.addAnimationKey();
		motor.setTickAtIndex(0, 300);
		motor.addAnimationKey();
		motor.setTickAtIndex(1, 350);
		motor.addAnimationKey();
		motor.setTickAtIndex(2, 390);
		motor.addAnimationKeyAtIndex(4);

		assertThat(motor.getNumberOfKeys(), is(equalTo(4)));
		assertThat(motor.getTickAtIndex(0), is(equalTo(300)));
		assertThat(motor.getTimeAtIndex(0), is(equalTo(0L)));
		assertThat(motor.getTickAtIndex(1), is(equalTo(350)));
		assertThat(motor.getTimeAtIndex(1), is(equalTo(1000L)));
		assertThat(motor.getTickAtIndex(2), is(equalTo(390)));
		assertThat(motor.getTimeAtIndex(2), is(equalTo(2000L)));
		assertThat(motor.getTickAtIndex(3), is(equalTo(390)));
		assertThat(motor.getTimeAtIndex(3), is(equalTo(3000L)));
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"})
	public void addAnimationKeyToArrayLengthThreeAtIndexNegativeOne() {

		motor.addAnimationKey();
		motor.setTickAtIndex(0, 300);
		motor.addAnimationKey();
		motor.setTickAtIndex(1, 350);
		motor.addAnimationKey();
		motor.setTickAtIndex(2, 390);
		motor.addAnimationKeyAtIndex(0);

		assertThat(motor.getNumberOfKeys(), is(equalTo(3)));
		assertThat(motor.getTickAtIndex(0), is(equalTo(300)));
		assertThat(motor.getTimeAtIndex(0), is(equalTo(0L)));
		assertThat(motor.getTickAtIndex(1), is(equalTo(350)));
		assertThat(motor.getTimeAtIndex(1), is(equalTo(1000L)));
		assertThat(motor.getTickAtIndex(2), is(equalTo(390)));
		assertThat(motor.getTimeAtIndex(2), is(equalTo(2000L)));
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"})
	public void addAnimationKeyToArrayLengthThreeAtIndexOne() {

		motor.addAnimationKey();
		motor.setTickAtIndex(0, 300);
		motor.addAnimationKey();
		motor.setTickAtIndex(1, 350);
		motor.addAnimationKey();
		motor.setTickAtIndex(2, 390);
		motor.addAnimationKeyAtIndex(1);

		assertThat(motor.getNumberOfKeys(), is(equalTo(4)));
		assertThat(motor.getTickAtIndex(0), is(equalTo(300)));
		assertThat(motor.getTimeAtIndex(0), is(equalTo(0L)));
		assertThat(motor.getTickAtIndex(1), is(equalTo(325)));
		assertThat(motor.getTimeAtIndex(1), is(equalTo(500L)));
		assertThat(motor.getTickAtIndex(2), is(equalTo(350)));
		assertThat(motor.getTimeAtIndex(2), is(equalTo(1000L)));
		assertThat(motor.getTickAtIndex(3), is(equalTo(390)));
		assertThat(motor.getTimeAtIndex(3), is(equalTo(2000L)));
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"})
	public void addAnimationKeyToArrayLengthThreeAtIndexTwo() {

		motor.addAnimationKey();
		motor.setTickAtIndex(0, 300);
		motor.addAnimationKey();
		motor.setTickAtIndex(1, 350);
		motor.addAnimationKey();
		motor.setTickAtIndex(2, 390);
		motor.addAnimationKeyAtIndex(2);

		assertThat(motor.getNumberOfKeys(), is(equalTo(4)));
		assertThat(motor.getTickAtIndex(0), is(equalTo(300)));
		assertThat(motor.getTimeAtIndex(0), is(equalTo(0L)));
		assertThat(motor.getTickAtIndex(1), is(equalTo(350)));
		assertThat(motor.getTimeAtIndex(1), is(equalTo(1000L)));
		assertThat(motor.getTickAtIndex(2), is(equalTo(370)));
		assertThat(motor.getTimeAtIndex(2), is(equalTo(1500L)));
		assertThat(motor.getTickAtIndex(3), is(equalTo(390)));
		assertThat(motor.getTimeAtIndex(3), is(equalTo(2000L)));
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"}, expectedExceptions=IllegalArgumentException.class)
	public void addAnimationKeyBetweenTwoKeysWithZeroTimeBetweenThem() {

		motor.addAnimationKey();
		motor.setTimeAtIndex(0, 0L);
		motor.addAnimationKey();
		motor.setTimeAtIndex(1, 1L);
		motor.addAnimationKeyAtIndex(1);
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"})
	public void addAnimationKeyToArrayLengthZeroAtIndexOne() {

		motor.addAnimationKeyAtIndex(1);

		assertThat(motor.getNumberOfKeys(), is(equalTo(1)));
		assertThat(motor.getTickAtIndex(0), is(equalTo(0)));
		assertThat(motor.getTimeAtIndex(0), is(equalTo(0L)));
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"})
	public void addAnimationKeyToArrayLengthZeroAtIndexZero() {

		motor.addAnimationKeyAtIndex(0);

		assertThat(motor.getNumberOfKeys(), is(equalTo(1)));
		assertThat(motor.getTickAtIndex(0), is(equalTo(0)));
		assertThat(motor.getTimeAtIndex(0), is(equalTo(0L)));
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"}, expectedExceptions=IllegalArgumentException.class)
	public void addAnimationKeyToArrayLengthZeroAtIndexNegativeOne() {

		motor.addAnimationKeyAtIndex(-1);
	}

	// ------------------------------------------------------------------------
	// Set tick tests
	// ------------------------------------------------------------------------

	@Test(enabled=true, groups={"PwmMotorTests","unit"})
	public void setTickLessThanTickMin() {

		motor.addAnimationKey();
		motor.setTickAtIndex(0, 0);
		assertThat(motor.getTickAtIndex(0), is(equalTo(motor.getMinTick())));
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"})
	public void setTickGreaterThanTickMax() {

		motor.addAnimationKey();
		motor.setTickAtIndex(0, 1000);
		assertThat(motor.getTickAtIndex(0), is(equalTo(motor.getMaxTick())));
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"}, expectedExceptions=IndexOutOfBoundsException.class)
	public void setTickWithNoKeys() {
		motor.setTickAtIndex(0, 300);
	}

	// ------------------------------------------------------------------------
	// Set time tests
	// ------------------------------------------------------------------------

	@Test(enabled=true, groups={"PwmMotorTests","unit"}, expectedExceptions=IndexOutOfBoundsException.class)
	public void setTimeWithNoKeys() {
		motor.setTimeAtIndex(0, 100L);
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"})
	public void setTimeWithOneKey() {
		motor.addAnimationKey();
		motor.setTimeAtIndex(0, 99L);
		assertThat(motor.getTimeAtIndex(0), is(equalTo(99L)));
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"}, expectedExceptions=IllegalArgumentException.class)
	public void setTimeLessThanPreviousKey() {
		motor.addAnimationKey();
		motor.setTimeAtIndex(0, 100L);
		motor.addAnimationKey();
		motor.setTimeAtIndex(1, 99L);
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"}, expectedExceptions=IllegalArgumentException.class)
	public void setTimeGreaterThanNextKey() {
		motor.addAnimationKey();
		motor.addAnimationKey();
		motor.setTimeAtIndex(1, 100L);
		motor.setTimeAtIndex(0, 101L);
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"}, expectedExceptions=IllegalArgumentException.class)
	public void setTimeEqualToPreviousKey() {
		motor.addAnimationKey();
		motor.setTimeAtIndex(0, 100L);
		motor.addAnimationKey();
		motor.setTimeAtIndex(1, 100L);
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"}, expectedExceptions=IllegalArgumentException.class)
	public void setTimeEqualToNextKey() {
		motor.addAnimationKey();
		motor.addAnimationKey();
		motor.setTimeAtIndex(1, 100L);
		motor.setTimeAtIndex(0, 100L);
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"}, expectedExceptions=IndexOutOfBoundsException.class)
	public void setTimeWhereIndexIsOutOfBounds() {
		motor.setTimeAtIndex(0, 100L);
	}

	// ------------------------------------------------------------------------
	// Get ticks per millisecond tests
	// ------------------------------------------------------------------------

	@Test(enabled=true, groups={"PwmMotorTests","unit"})
	public void calculateTicksPerMillisecond() {
		int ticksPerMillisecond = motor.getTicksPerMillisecond();
		assertThat(ticksPerMillisecond, is(equalTo(204)));
	}

	// ------------------------------------------------------------------------
	// Convert angle to ticks tests
	// ------------------------------------------------------------------------

	@Test(enabled=true, groups={"PwmMotorTests","unit"})
	public void convertAngleOfZeroDegreesToTicks() {

		int ticks = motor.convertAngleToTicks(0.0);
		assertThat(ticks, is(equalTo(307)));
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"})
	public void convertAngleOfNinetyDegreesToTicks() {

		int ticks = motor.convertAngleToTicks(90.0);
		assertThat(ticks, is(equalTo(410)));
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"})
	public void convertAngleOfNegativeNinetyDegressToTicks() {

		int ticks = motor.convertAngleToTicks(-90.0);
		assertThat(ticks, is(equalTo(204)));
	}

	// ------------------------------------------------------------------------
	// Convert ticks to angle tests
	// ------------------------------------------------------------------------

	@Test(enabled=true, groups={"PwmMotorTests","unit"})
	public void convert307TicksToZeroDegress() {

		double angle = motor.convertTicksToAngle(307);
		assertThat(angle, is(equalTo(0.0)));
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"})
	public void convert410TicksToNinetyDegrees() {

		double angle = motor.convertTicksToAngle(410);
		assertThat(angle, is(equalTo(90.0)));
	}

	@Test(enabled=true, groups={"PwmMotorTests","unit"})
	public void convert204TicksToNegativeNinetyDegrees() {

		double angle = motor.convertTicksToAngle(204);
		assertThat(angle, is(equalTo(-90.0)));
	}

	/*
	 * Implementation of the PwmMotor abstract class for testing purposes.
	 */
	class TestPwmMotor extends PwmMotor {

		@Override
		public int getMaxTick() {
			return 410;
		}

		@Override
		public int getMinTick() {
			return 204;
		}

		@Override
		public double getMaxAngle() {
			return 90;
		}

		@Override
		public double getMinAngle() {
			return -90;
		}

		@Override
		public String getMotorId() {
			return "Test Motor MG995R";
		}

		@Override
		public int getSignalFrequency() {
			return 50;
		}

	}
}
