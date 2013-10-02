package org.motechproject.reporting.pentaho.util;

public final class JobSchedulerUtil {

    private JobSchedulerUtil() {}

    private static String dailyCronTemplate = "0 %s %s 1/1 * ? *";
    private static String weeklyCronTemplate = "0 %s %s ? * %s *";
    private static String monthlyCronTemplate = "0 %s %s %s 1/1 ? *";

    public static String getDailyCronExpression(String minute, String hour) {
        return String.format(dailyCronTemplate, minute, hour);
    }

    public static String getWeeklyCronExpression(String minute, String hour, String day) {
        return String.format(weeklyCronTemplate, minute, hour, day);
    }

    public static String getMonthlyCronExpression(String minute, String hour, String dayOfMonth) {
        return String.format(monthlyCronTemplate, minute, hour, dayOfMonth);
    }
}
