package org.pdb.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * Base class for all jUnit tests.
 */
public abstract class BaseTest {

    /**
     * The logger to use.
     */
    protected static final Logger LOGGER = LogManager.getLogger("JUnit");

    /**
     * Add logging for tests.
     */
    @Rule
    public TestRule watchman = new TestWatcher() {
        @Override
        public void starting(Description description) {
            LOGGER.info("RUNNING Test " + description);
        }

        @Override
        public void succeeded(Description description) {
            LOGGER.info("PASSED Test " + description);
        }

        @Override
        public void failed(Throwable ex, Description description) {
            LOGGER.info("FAILED Test " + description + " with " + ex);
        }
    };

    /**
     * Default constructor.
     */
    public BaseTest() {
        // empty
    }

}
