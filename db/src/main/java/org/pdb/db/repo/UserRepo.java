package org.pdb.db.repo;

import org.pdb.db.entities.User;
import org.springframework.data.cassandra.repository.CassandraRepository;

/**
 * Repository for User entity.
 */
public interface UserRepo extends CassandraRepository<User> {

}
