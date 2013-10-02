package org.motechproject.reporting.pentaho.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.commons.couchdb.dao.MotechBaseRepository;
import org.motechproject.reporting.pentaho.domain.PentahoExecuteTransInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllPentahoTransformations extends MotechBaseRepository<PentahoExecuteTransInstance> {

    @Autowired
    protected AllPentahoTransformations(@Qualifier("pentahoApplicationDatabaseConnector") CouchDbConnector db) {
        super(PentahoExecuteTransInstance.class, db);
    }

}
