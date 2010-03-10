/*
 * Copyright 2007-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.interactive.command.listener;

import org.impalaframework.facade.Impala;

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
                    Impala.unloadRootModule();
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
