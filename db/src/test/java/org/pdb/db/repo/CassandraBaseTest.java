package org.pdb.db.repo;

import static org.junit.Assert.assertNotNull;
import static org.springframework.cassandra.core.keyspace.CreateKeyspaceSpecification.createKeyspace;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.pdb.common.BaseTest;
import org.pdb.db.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.keyspace.CreateKeyspaceSpecification;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.mapping.CassandraPersistentEntity;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Unit tests for UserRepo.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public abstract class CassandraBaseTest extends BaseTest {

    protected static String CASSANDRA_CONFIG = "spring-cassandra.yaml";

    @Configuration
    @EnableCassandraRepositories(basePackages = "org.pdb.db.repo")
    public static class Config extends AbstractCassandraConfiguration {

        public static final SpringCassandraBuildProperties PROPS = new SpringCassandraBuildProperties();
        public static final int PORT = PROPS.getCassandraPort();

        public String keyspaceName = "ks" + UUID.randomUUID().toString().replace("-", "");

        @Override
        protected int getPort() {
            return PORT;
        }

        @Override
        public SchemaAction getSchemaAction() {
            return SchemaAction.RECREATE;
        }

        @Override
        protected String getKeyspaceName() {
            return keyspaceName;
        }

        @Override
        protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
            return Arrays.asList(createKeyspace().name(getKeyspaceName()).withSimpleReplication());
        }

        @Override
        public String[] getEntityBasePackages() {
            return new String[] {User.class.getPackage().getName()};
        }
    }

    @Autowired
    CassandraTemplate template;

    @BeforeClass
    public static void startCassandra() throws TTransportException, IOException, InterruptedException,
            ConfigurationException {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(CASSANDRA_CONFIG);
    }

    @Before
    public void beforeEach() {
        assertNotNull(template);
        deleteAllEntities();
    }

    public void deleteAllEntities() {

        for (CassandraPersistentEntity<?> entity : template.getConverter().getMappingContext()
                .getPersistentEntities()) {
            template.truncate(entity.getTableName());
        }
    }
}
