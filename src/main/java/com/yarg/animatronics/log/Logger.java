package com.yarg.animatronics.log;

public class Logger {

	public static enum SEVERITY {
		INFO("INFO"),
		WARNING("WARNING"),
		DEBUG("DEBUG"),
		ERROR("ERROR");

		private final String severity;

		private SEVERITY(String severity) {
			this.severity = severity;
		}

		public String getSeverityString() {
			return severity;
		}
	}

	/**
	 * Write a log entry if writeToLog is set to true. Allows for easy command line configuration of logging. All log
	 * entries are written to stdout.
	 * @param severity Severity of the log message.
	 * @param message Message to write to the log.
	 * @param writeToLog Message to write to the log.
	 */
	public static void log(SEVERITY severity, String message, boolean writeToLog) {

		if (writeToLog) {
			log(severity, message);
		}
	}

	/**
	 * Write a log entry. All log entries are written to stdout.
	 * @param severity Severity of the log message.
	 * @param message Message to write to the log.
	 */
	public static void log(SEVERITY severity, String message) {
		String logMessage = String.format("[%s] %s", severity.getSeverityString(), message);
		System.out.println(logMessage);
	}
}
