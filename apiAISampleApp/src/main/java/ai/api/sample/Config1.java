package ai.api.sample;


public class Config1 {

    //Address of our scripts of the CRUD
    public static final String URL_GET_REPLY = "http://192.168.0.108/api.php?intentName=";

    //Keys that will be used to send the request to php scripts
    public static final String KEY_EMP_ID = "id";
    public static final String KEY_EMP_NAME = "name";
    public static final String KEY_EMP_DESG = "desg";
    public static final String KEY_EMP_SAL = "salary";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_SPEECH = "speech";
    public static final String TAG_DISPLAY_TEXT = "displayText";
    public static final String TAG_IMAGE_URL = "imageURL";

    public static final String TAG_WEBSITE_URL = "websiteURL";
    public static final String TAG_LOCATION = "location";

    //employee id to pass with intent
    public static final String EMP_ID = "emp_id";
}