package org.impalaframework.command.listener;

import org.impalaframework.testrun.DynamicContextHolder;

public class StopCheckerListener implements TestCommandListener {

	private int maxInactiveSeconds = 300;

	private long lastAccessed;

	public void commandExecuted(String commandText) {
		setLastAccessed();
	}

	public void start() {
		System.out.println("Starting inactivity checker with maximum inactivity of " + maxInactiveSeconds
				+ " seconds");
		setLastAccessed();
		new Thread(new StopCheckerDelegate()).start();
	}

	private void setLastAccessed() {
		this.lastAccessed = System.currentTimeMillis();
	}

	final class StopCheckerDelegate implements Runnable {

		private boolean isStopped;

		public StopCheckerDelegate() {
			super();
		}

		public void run() {
			while (!isStopped) {
				if ((System.currentTimeMillis() - lastAccessed) > 1000 * maxInactiveSeconds) {
					System.out.println();
					System.out.println("Terminating test runner as it has been inactive for more than "
							+ maxInactiveSeconds + " seconds.");
					DynamicContextHolder.unloadRootModule();
					System.exit(0);
				}
				try {
					// sleep for 10 seconds before checking again
					Thread.sleep(10000);
				}
				catch (InterruptedException e) {
				}
			}
		}

		void stop() {
			this.isStopped = true;
		}
	}

	public void setMaxInactiveSeconds(int maxInactiveSeconds) {
		this.maxInactiveSeconds = maxInactiveSeconds;
	}

}
