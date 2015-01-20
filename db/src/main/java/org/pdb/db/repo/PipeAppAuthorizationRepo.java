package org.pdb.db.repo;

import org.pdb.db.entities.PipeAppAuthorization;
import org.springframework.data.cassandra.repository.CassandraRepository;

/**
 * Repository for PipeAppAuthorization entity.
 */
public interface PipeAppAuthorizationRepo extends CassandraRepository<PipeAppAuthorization> {

}
