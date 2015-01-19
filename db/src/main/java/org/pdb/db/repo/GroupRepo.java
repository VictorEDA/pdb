package org.pdb.db.repo;

import org.pdb.db.entities.Group;
import org.springframework.data.cassandra.repository.CassandraRepository;

/**
 * Repository for User entity.
 */
public interface GroupRepo extends CassandraRepository<Group> {

}
