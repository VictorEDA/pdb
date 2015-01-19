package org.pdb.db.init;

import java.io.IOException;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This method initializes Cassandra connections in the web application.
 */
public class CassandraInitiator {

    static Logger LOGGER = LoggerFactory.getLogger(CassandraInitiator.class);

    protected static String CASSANDRA_CONFIG = "spring-cassandra.yaml";

    public CassandraInitiator() throws ConfigurationException, TTransportException, IOException,
            InterruptedException {
        startCassandra();
    }

    public static void startCassandra() throws TTransportException, IOException, InterruptedException,
            ConfigurationException {
        LOGGER.info("Starting embedded Cassandra using " + CASSANDRA_CONFIG);
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(CASSANDRA_CONFIG);
    }

}
