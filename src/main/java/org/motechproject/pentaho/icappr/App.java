package org.motechproject.pentaho.icappr;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.util.EnvUtil;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

public class App 
{

    public static final String START_DATE_KEY = "startDate";
    public static final String END_DATE_KEY = "endDate";
    public static final String FILE_NAME = "fileName";

    public static void main( String[] args )
    {

        try {
            KettleEnvironment.init();
            EnvUtil.environmentInit();

            String reportToRun = args[0];

            TransMeta transMeta = new TransMeta(reportToRun);
            Trans trans = new Trans(transMeta);

            if (args.length > 3) {
                trans.setParameterValue(START_DATE_KEY, args[1]);
                trans.setParameterValue(END_DATE_KEY, args[2]);
                trans.setParameterValue(FILE_NAME, args[3]);
            }

            trans.execute(null);
            trans.waitUntilFinished();

            if (trans.getErrors() > 0) {
                throw new RuntimeException("There were errors");
            }

        } catch (KettleException e) {
            System.exit(1);
        }

        System.exit(0);
    }
}
