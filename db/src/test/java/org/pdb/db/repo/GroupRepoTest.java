package org.pdb.db.repo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.data.cassandra.repository.support.BasicMapId.id;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.pdb.db.entities.Group;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Unit tests for GroupRepo.
 */
public class GroupRepoTest extends CassandraBaseTest {

    @Autowired
    GroupRepo repo;

    @Override
    @Before
    public void beforeEach() {
        super.beforeEach();
        assertNotNull(repo);
    }

    @Test
    public void testSave() {
        String groupId = "groupId";
        String accessToken = "token";
        Group group = new Group(groupId, accessToken);

        repo.save(group);
        Group found = repo.findOne(id().with("groupId", groupId));
        checkGroup(found, groupId, accessToken, new HashSet<String>());
    }

    @Test
    public void testSaveWithUsers() {
        String groupId = "groupId";
        String accessToken = "token";
        Set<String> userIds = new HashSet<>(Arrays.asList("user1", "user2", "user3"));
        Group group = new Group(groupId, accessToken);
        group.setUserIds(userIds);

        repo.save(group);
        Group found = repo.findOne(id().with("groupId", groupId));
        checkGroup(found, groupId, accessToken, userIds);
    }

    /**
     * Check to make sure group is correct.
     * @param group The group to check.
     * @param groupId Expected userId.
     * @param accessToken Expected accessToken.
     * @param userIds Expected userIds.
     */
    private void checkGroup(Group group, String groupId, String accessToken, Set<String> userIds) {
        assertNotNull(group);
        assertEquals(groupId, group.getGroupId());
        assertEquals(accessToken, group.getAccessToken());
        assertEquals(userIds, group.getUserIds());
    }

}
