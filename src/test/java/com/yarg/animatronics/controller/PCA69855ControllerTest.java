package com.yarg.animatronics.controller;

import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.yarg.animatronics.datamodel.PwmBoard;
import com.yarg.animatronics.datamodel.PwmMotor;

public class PCA69855ControllerTest {

	private PCA69855Controller controller;

	@BeforeMethod(alwaysRun = true)
	public void beforeTest() throws UnsupportedBusNumberException, IOException {
		controller = new PCA69855Controller();
	}

	@Test(enabled=true, groups={"PCA69855ControllerTests","unit"})
	public void addBoard() throws IOException, InterruptedException {

		TestPwmBoard board = new TestPwmBoard();
		controller.addBoard(board);
	}

	@Test(enabled=true, groups={"PCA69855ControllerTests","unit"}, expectedExceptions=UnsupportedBusNumberException.class)
	public void instantiateControllerWithInvalidBusNumber() throws UnsupportedBusNumberException, IOException {
		controller = new PCA69855Controller(-1, false);
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
