package com.yarg.animatronics.log;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.yarg.animatronics.log.Logger.SEVERITY;

public class LoggerTest {

	// Store the original standard out before changing it.
	private final PrintStream originalStdout = System.out;
	private ByteArrayOutputStream consoleContent;


	@BeforeMethod(alwaysRun = true)
	public void beforeTest()
	{
		consoleContent = new ByteArrayOutputStream();

		// Redirect all System.out to consoleContent.
		System.setOut(new PrintStream(consoleContent));
	}

	@AfterMethod(alwaysRun = true)
	public void afterTest()
	{
		// Put back stdout.
		System.setOut(originalStdout);

		// Print what was captured.
		//		System.out.println(consoleContent.toString());
	}

	@Test(enabled=true, groups={"LoggerTests","unit"})
	public void logInfo() {

		String message = "Test info message.";
		Logger.log(SEVERITY.INFO, message);

		String expectedMessage = "[INFO] " + message + "\n";
		assertThat(consoleContent.toString(), is(equalTo(expectedMessage)));
	}

	@Test(enabled=true, groups={"LoggerTests","unit"})
	public void logWarning() {

		String message = "Test warning message.";
		Logger.log(SEVERITY.WARNING, message);

		String expectedMessage = "[WARNING] " + message + "\n";
		assertThat(consoleContent.toString(), is(equalTo(expectedMessage)));
	}

	@Test(enabled=true, groups={"LoggerTests","unit"})
	public void logDebug() {

		String message = "Test debug message.";
		Logger.log(SEVERITY.DEBUG, message);

		String expectedMessage = "[DEBUG] " + message + "\n";
		assertThat(consoleContent.toString(), is(equalTo(expectedMessage)));
	}

	@Test(enabled=true, groups={"LoggerTests","unit"})
	public void logError() {

		String message = "Test error message.";
		Logger.log(SEVERITY.ERROR, message);

		String expectedMessage = "[ERROR] " + message + "\n";
		assertThat(consoleContent.toString(), is(equalTo(expectedMessage)));
	}

	@Test(enabled=true, groups={"LoggerTests","unit"})
	public void logInfoWithoutWriteFlag() {

		String message = "Test info message.";
		Logger.log(SEVERITY.INFO, message, false);

		assertThat(consoleContent.toString().length(), is(equalTo(0)));
	}

	@Test(enabled=true, groups={"LoggerTests","unit"})
	public void logInfoWithWriteFlag() {

		String message = "Test info message.";
		Logger.log(SEVERITY.INFO, message, true);

		String expectedMessage = "[INFO] " + message + "\n";
		assertThat(consoleContent.toString(), is(equalTo(expectedMessage)));
	}

	@Test(enabled=true, groups={"LoggerTests","unit"})
	public void logInfoWithNullMessage() {

		String message = null;
		Logger.log(SEVERITY.INFO, message);

		String expectedMessage = "[INFO] " + message + "\n";
		assertThat(consoleContent.toString(), is(equalTo(expectedMessage)));
	}
}
