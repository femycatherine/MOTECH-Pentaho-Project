package org.motechproject.pentaho.icappr;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.util.EnvUtil;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            KettleEnvironment.init();
            EnvUtil.environmentInit();
            TransMeta transMeta = new TransMeta("sideEffectReports.ktr");
            Trans trans = new Trans(transMeta);

            trans.execute(null);
            trans.waitUntilFinished();
            if (trans.getErrors() > 0) {
                throw new RuntimeException("There were errors");
            }
        } catch (KettleException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
