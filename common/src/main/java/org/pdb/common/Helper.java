package org.pdb.common;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * This class contains helper utility methods.
 */
public class Helper {

    /**
     * The default date format to use for JSON conversion.
     */
    public static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

    /**
     * The name of the class.
     */
    private static final String CLASS_NAME = Helper.class.getName();

    /**
     * The logger to use for logging.
     */
    private static final Logger LOGGER = LogManager.getLogger(Helper.class.getName());

    /**
     * Generates string representation of objects.
     */
    private static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();

    /**
     * The error message when an object cannot be converted to JSON string.
     */
    public static final String MESSAGE_JSON_ERROR = "Cannot convert to json string.";

    /**
     * Represents the error message.
     */
    private static final String MESSAGE_ERROR = "[Error in method '%s']";

    /**
     * Represents the method entry message.
     */
    private static final String MESSAGE_METHOD_ENTRY = "[Method entry '{}'] Input parameters[{}]";

    /**
     * Represents the method entry message.
     */
    private static final String MESSAGE_METHOD_ENTRY_NO_PARAMS = "[Method entry '{}']";

    /**
     * Represents the method exit message.
     */
    private static final String MESSAGE_METHOD_EXIT = "[Method exit '{}'] Output parameter[{}]";

    /**
     * Represents the method exit message without output parameter.
     */
    private static final String MESSAGE_METHOD_EXIT_NO_RESULT = "[Method exit '{}']";

    /**
     * Should not be null suffix.
     */
    private static final String NOT_NULL = "' should not be null.";

    /**
     * Should not be empty suffix.
     */
    private static final String NOT_EMPTY = "' should not be empty.";

    static {
        DEFAULT_OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        DEFAULT_OBJECT_MAPPER.setDateFormat(DEFAULT_DATE_FORMAT);
    }

    /**
     * Hidden constructor for utility class.
     */
    private Helper() {
        //
    }

    /**
     * Validates the value of a variable. The value can not be <code>null</code>.
     * @param value the value of the variable to be validated.
     * @param name the name of the variable to be validated.
     * @throws AppConfigurationException if the value of the variable is <code>null</code>.
     */
    public static void checkNullConfig(Object value, String name) {
        if (value == null) {
            throw new AppConfigurationException("'" + name + NOT_NULL);
        }
    }

    /**
     * Validates the value of an input variable. The value can not be <code>null</code>.
     * @param logger the logger to use
     * @param methodName the name of the calling method
     * @param value the value of the variable to be validated.
     * @param name the name of the variable to be validated.
     * @throws IllegalArgumentException if the value of the variable is <code>null</code>.
     */
    public static void checkNull(Logger logger, String methodName, Object value, String name) {
        if (value == null) {
            throw logException(logger, methodName, new IllegalArgumentException("'" + name + NOT_NULL));
        }
    }

    /**
     * Validates the value of an input variable. The value can not be null or empty.
     * @param logger the logger to use
     * @param methodName the name of the calling method
     * @param value the value of the variable to be validated.
     * @param name the name of the variable to be validated.
     * @throws IllegalArgumentException if the value of the variable is null or empty.
     */
    public static void checkNullOrEmpty(Logger logger, String methodName, String value, String name) {
        checkNull(logger, methodName, value, name);
        if (value.trim().isEmpty()) {
            throw logException(logger, methodName, new IllegalArgumentException("'" + name + NOT_EMPTY));
        }
    }

    /**
     * Validates the value of a string. The value can not be <code>null</code> or empty.
     * @param value the value of the variable to be validated.
     * @param name the name of the variable to be validated.
     * @throws AppConfigurationException if the value of the variable is <code>null</code> or empty
     */
    public static void checkNullOrEmptyConfig(String value, String name) {
        checkNullConfig(value, name);
        if (value.trim().isEmpty()) {
            throw new AppConfigurationException("'" + name + NOT_EMPTY);
        }
    }

    /**
     * Validates the value of an input variable. The value can not be 0 or negative.
     * @param logger the logger to use
     * @param methodName the name of the calling method
     * @param value the value of the variable to be validated.
     * @param name the name of the variable to be validated.
     * @throws IllegalArgumentException if the value of the variable is 0 or negative.
     */
    public static void checkZeroOrNegative(Logger logger, String methodName, long value, String name) {
        if (value < 1) {
            throw logException(logger, methodName, new IllegalArgumentException("'" + name
                    + "' should not be zero or negative."));
        }
    }

    /**
     * Converts the object to JSON string.
     * @param obj the object to convert
     * @return the string representation of objects in JSON format.
     */
    public static String toJsonString(Object obj) {
        final String signature = CLASS_NAME + "#toJSONString";
        try {
            return DEFAULT_OBJECT_MAPPER.writeValueAsString(obj);
        } catch (IOException e) {
            logException(LOGGER, signature, e);
        }
        return MESSAGE_JSON_ERROR;
    }

    /**
     * Log the method entrance message at DEBUG level.
     * @param logger the logger to use for logging, cannot be null
     * @param methodName the method name
     * @param paramsNameValues The String names and corresponding values of method parameters, like:
     *            <code>"id", 5, "city", "Austin"</code>
     */
    public static void logEntrance(Logger logger, String methodName, Object... paramsNameValues) {
        if (logger.isDebugEnabled()) {
            // Create a map of parameters
            Map<String, Object> params = new HashMap<>();
            for (int i = 0; i < paramsNameValues.length; i += 2) {
                params.put((String) paramsNameValues[i], paramsNameValues[i + 1]);
            }
            logger.debug(MESSAGE_METHOD_ENTRY, methodName, params);
        }
    }

    /**
     * Log the method entrance message at DEBUG level. The method entrance is logged without params.
     * @param logger the logger to use for logging, cannot be null
     * @param methodName the method name
     */
    public static void logEntrance(Logger logger, String methodName) {
        logger.debug(MESSAGE_METHOD_ENTRY_NO_PARAMS, methodName);
    }

    /**
     * Log the method exit message at DEBUG level.
     * @param <T> the result type
     * @param logger the logger to use for logging, cannot be null
     * @param methodName the method name
     * @param result the method result
     * @return the result
     */
    public static <T> T logExit(Logger logger, String methodName, T result) {
        logger.debug(MESSAGE_METHOD_EXIT, methodName, result);
        return result;
    }

    /**
     * Log the method exit message at DEBUG level without a result.
     * @param logger the logger to use for logging, cannot be null
     * @param methodName the method name
     */
    public static void logExit(Logger logger, String methodName) {
        logger.debug(MESSAGE_METHOD_EXIT_NO_RESULT, methodName);
    }

    /**
     * Logs the given exception and message at <code>ERROR</code> level.
     * @param <T> the exception type
     * @param logger the logger object, cannot be null
     * @param signature the signature of the method to log.
     * @param exception the exception to log.
     * @return the passed in exception.
     */
    public static <T extends Throwable> T logException(Logger logger, String signature, T exception) {
        logger.error(String.format(MESSAGE_ERROR, signature), exception);
        return exception;
    }

}
