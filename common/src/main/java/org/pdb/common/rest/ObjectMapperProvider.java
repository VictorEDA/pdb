package org.pdb.common.rest;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.pdb.common.Helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * This provider is needed for mapping JSON to Java objects when using Jersey REST with Jackson.
 */
@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

    /**
     * Generates string representation of objects.
     */
    private static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();

    static {
        DEFAULT_OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        DEFAULT_OBJECT_MAPPER.setDateFormat(Helper.DEFAULT_DATE_FORMAT);
    }

    /**
     * Constructor.
     */
    public ObjectMapperProvider() {
        // empty
    }

    @Override
    public ObjectMapper getContext(final Class<?> type) {
        return DEFAULT_OBJECT_MAPPER;
    }

}
