package org.pdb.db.repo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.cassandra.core.keyspace.CreateKeyspaceSpecification.createKeyspace;
import static org.springframework.data.cassandra.repository.support.BasicMapId.id;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
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
// @RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration
public class UserRepoTest {

    @Test
    public void bozo() {
    }

    /*
    protected static String CASSANDRA_CONFIG = "spring-cassandra.yaml";

    @Configuration
    @EnableCassandraRepositories(basePackageClasses = UserRepo.class)
    public static class Config extends AbstractCassandraConfiguration {

        public String keyspaceName = "ks" + UUID.randomUUID().toString().replace("-", "");

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
    UserRepo repo;

    @Autowired
    CassandraTemplate template;

    @BeforeClass
    public static void startCassandra() throws TTransportException, IOException, InterruptedException,
            ConfigurationException {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(CASSANDRA_CONFIG);
    }

    @Before
    public void beforeEach() {
        assertNotNull(repo);
        assertNotNull(template);
        deleteAllEntities();
    }

    @Test
    public void testSave() {
        String userId = "userId";
        String groupId = "groupId";
        String accessToken = "accessToken";
        User user = new User();
        user.setUserId(userId);
        user.setGroupId(groupId);
        user.setAccessToken(accessToken);

        repo.save(user);

        User found = repo.findOne(id().with("userId", userId).with("groupId", groupId));
        assertNotNull(found);

        assertEquals(userId, found.getUserId());
        assertEquals(groupId, found.getGroupId());
        assertEquals(accessToken, found.getAccessToken());
    }

    public void deleteAllEntities() {

        for (CassandraPersistentEntity<?> entity : template.getConverter().getMappingContext()
                .getPersistentEntities()) {
            template.truncate(entity.getTableName());
        }
    }
    */
}
