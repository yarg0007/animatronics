package com.yarg.animatronics.datamodel;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PwmBoardTest {

	TestPwmBoard testBoard;

	@BeforeMethod(alwaysRun=true)
	public void setup() {
		testBoard = new TestPwmBoard();
	}

	// ------------------------------------------------------------------------
	// Add motor tests
	// ------------------------------------------------------------------------

	@Test(enabled=true, groups={"PwmBoardTests","unit"})
	public void addMotorToBoard() {

		TestPwmMotor testMotor = new TestPwmMotor();

		testBoard.addMotor(testMotor);
		List<PwmMotor> actualBoardMotors = testBoard.getAttachedMotors();

		ArrayList<PwmMotor> expectedBoardMotors = new ArrayList<>();
		expectedBoardMotors.add(testMotor);

		assertThat(actualBoardMotors, is(equalTo(expectedBoardMotors)));
	}

	@Test(enabled=true, groups={"PwmBoardTests","unit"}, expectedExceptions=ArrayStoreException.class)
	public void addMoreMotorsToBoardThanThereAreAvailableChannels() {

		TestPwmMotor testMotor1 = new TestPwmMotor();
		testMotor1.setPwmChannel(1);

		TestPwmMotor testMotor2 = new TestPwmMotor();
		testMotor2.setPwmChannel(2);

		TestPwmMotor testMotor3 = new TestPwmMotor();
		testMotor3.setPwmChannel(3);

		testBoard.addMotor(testMotor1);
		testBoard.addMotor(testMotor2);
		testBoard.addMotor(testMotor3);
	}

	@Test(enabled=true, groups={"PwmBoardTests","unit"}, expectedExceptions=ArrayStoreException.class)
	public void addMotorWithSamePwmChannelAsExistingMotor() {

		TestPwmMotor testMotor1 = new TestPwmMotor();
		testMotor1.setPwmChannel(1);

		TestPwmMotor testMotor2 = new TestPwmMotor();
		testMotor2.setPwmChannel(1);

		testBoard.addMotor(testMotor1);
		testBoard.addMotor(testMotor2);
	}

	@Test(enabled=true, groups={"PwmBoardTests","unit"}, expectedExceptions=ArrayStoreException.class)
	public void addMotorWithChannelGreaterThanLimitOfAvailableChannelsOnBoard() {

		TestPwmMotor testMotor1 = new TestPwmMotor();
		testMotor1.setPwmChannel(3);

		testBoard.addMotor(testMotor1);
	}

	@Test(enabled=true, groups={"PwmBoardTests","unit"}, expectedExceptions=ArrayStoreException.class)
	public void addMotorWithChannelLessThanOne() {

		TestPwmMotor testMotor1 = new TestPwmMotor();
		testMotor1.setPwmChannel(0);

		testBoard.addMotor(testMotor1);
	}

	/*
	 * Implementation of PwmBoard abstract class for testing purposes.
	 */
	class TestPwmBoard extends PwmBoard {

		@Override
		public int getFrequency() {
			return 50;
		}

		@Override
		public int getNumberOfChannels() {
			return 2;
		}

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
	}
}
