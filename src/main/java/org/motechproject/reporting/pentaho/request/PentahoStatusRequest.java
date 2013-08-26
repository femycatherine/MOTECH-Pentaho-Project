package org.motechproject.reporting.pentaho.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class PentahoStatusRequest extends PentahoRequest {

    @Override
    public String toQueryString() {
        List<String> queryParams = new ArrayList<>();

        if (this.getXml() != null) {
            queryParams.add(concat("xml", this.getXml()));
        }

        return StringUtils.join(queryParams, "&");
    }
}
