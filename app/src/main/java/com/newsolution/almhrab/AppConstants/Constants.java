package com.newsolution.almhrab.AppConstants;

public interface Constants {
    // REQUEST CODES
    int REQUEST_CHECK_SETTINGS = 101;
    int REQUEST_ONBOARDING = 102;
    int REQUEST_LOCATION = 103;
    int REQUEST_WRITE_EXTERNAL = 104;
    int REQUEST_SET_ALARM = 105;
    int RESULT_LOAD_IMAGE = 1;


    int ALARM_ID = 1010;
    int IQAMA_ALARM_ID = 1010;
    int PASSIVE_LOCATION_ID = 1011;
    int PRE_SUHOOR_ALARM_ID = 1012;
    int PRE_IFTAR_ALARM_ID = 1013;

    long ONE_MINUTE = 60000;
    long FIVE_MINUTES = ONE_MINUTE * 1;

    //EXTRAS
    String EXTRA_ALARM_INDEX = "alarm_index";
    String EXTRA_LAST_LOCATION = "last_location";
    String EXTRA_ACTION = "action";
    String EXTRA_PRAYER_NAME = "prayer_name";
    String PRAYER_NAME = "prayer_name";
    String EXTRA_PRAYER_TIME = "prayer_time";
    String EXTRA_IQAMA_NAME = "iqama_name";
    String EXTRA_IQAMA_TIME = "iqama_time";
    String EXTRA_PRE_ALARM_FLAG = "pre_alarm_flag";

    String CONTENT_FRAGMENT = "content_fragment";
    String TIMES_FRAGMENT = "times_fragment";
    String CONFIG_FRAGMENT = "config_fragment";
    String LOCATION_FRAGMENT = "location_fragment";

    int NOTIFICATION_ID = 2010;

    public static String Main_URL = "http://almihrab.newsolutions.ps/APIs/";
    public static String IMAGE_URL = "http://almihrab.newsolutions.ps";
    public static String PACKAGE_NAME="com.newsolution.almhrab";
}
