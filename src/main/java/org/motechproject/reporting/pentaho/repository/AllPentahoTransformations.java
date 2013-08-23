package org.motechproject.reporting.pentaho.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.commons.couchdb.dao.MotechBaseRepository;
import org.motechproject.reporting.pentaho.domain.PentahoExecuteTransInstance;

public class AllPentahoTransformations extends MotechBaseRepository<PentahoExecuteTransInstance> {

    protected AllPentahoTransformations(
            Class<PentahoExecuteTransInstance> type, CouchDbConnector db) {
        super(type, db);
        // TODO Auto-generated constructor stub
    }

}
