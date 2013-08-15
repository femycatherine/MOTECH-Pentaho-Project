package org.motechproject.reporting.pentaho.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class PentahoExecuteTransRequest extends PentahoRequest {

    private Map<String, String> params;
    private String transName;

    @Override
    public String toQueryString() {
        List<String> queryParams = new ArrayList<>();

        queryParams.add(concat("trans", transName));

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (xml != null) {
                    queryParams.add(concat(entry.getKey(), entry.getValue()));
                }
            }
        }

        return StringUtils.join(queryParams, "&");
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName;
    }
}
