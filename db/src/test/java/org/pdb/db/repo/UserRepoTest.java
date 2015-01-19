package org.pdb.db.repo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.data.cassandra.repository.support.BasicMapId.id;

import java.util.Collection;

import org.apache.commons.collections4.IteratorUtils;
import org.junit.Before;
import org.junit.Test;
import org.pdb.db.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Unit tests for UserRepo.
 */
public class UserRepoTest extends CassandraBaseTest {

    @Autowired
    UserRepo repo;

    @Override
    @Before
    public void beforeEach() {
        super.beforeEach();
        assertNotNull(repo);
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
        checkUser(found, userId, groupId, accessToken);

        User found2 = repo.findOne(id().with("userId", userId));
        checkUser(found2, userId, groupId, accessToken);
    }

    @Test
    public void testFindAll() {
        repo.save(new User("user1", "group1", "token1"));
        repo.save(new User("user2", "group2", "token2"));
        repo.save(new User("user3", "group1", "token3"));

        Iterable<User> iterable = repo.findAll();
        assertNotNull(iterable);
        Collection<User> users = IteratorUtils.toList(iterable.iterator());
        assertEquals(3, users.size());
    }

    /**
     * Check to make sure user is correct.
     * @param user The user to check.
     * @param userId Expected userId.
     * @param groupId Expected groupId.
     * @param accessToken Expected accessToken.
     */
    private void checkUser(User user, String userId, String groupId, String accessToken) {
        assertNotNull(user);
        assertEquals(userId, user.getUserId());
        assertEquals(groupId, user.getGroupId());
        assertEquals(accessToken, user.getAccessToken());
    }

}
