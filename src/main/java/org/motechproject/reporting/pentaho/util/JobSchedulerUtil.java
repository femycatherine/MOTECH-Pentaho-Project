package org.motechproject.reporting.pentaho.util;

public final class JobSchedulerUtil {

    private JobSchedulerUtil() {}

    private static String dailyCronTemplate = "0 0 %s 1/1 * ? *";
    private static String weeklyCronTemplate = "0 0 %s ? * %s *";
    private static String monthlyCronTemplate = "0 0 %s %s 1/1 ? *";

    public static String getDailyCronExpression(String hour) {
        return String.format(dailyCronTemplate, hour);
    }

    public static String getWeeklyCronExpression(String hour, String day) {
        return String.format(weeklyCronTemplate, hour, day);
    }

    public static String getMonthlyCronExpression(String hour, String dayOfMonth) {
        return String.format(monthlyCronTemplate, hour, dayOfMonth);
    }
}
