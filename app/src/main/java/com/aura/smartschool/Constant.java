package com.aura.smartschool;

public final class Constant {
	public static final String HOST = "https://aurasystem.kr:9000";
	
	public static final String API_SIGNIN = "/api/signInOfMobile";
	public static final String API_SIGNUP = "/api/signUp";
	public static final String API_GET_HOMEBYNUMBER = "/api/getHomeByNumber";
	public static final String API_GET_MODIFYHOME = "/api/modifyHome";
	public static final String API_ADD_MEMBER = "/api/addMember";
	public static final String API_REMOVE_MEMBER = "/api/removeMember";
	public static final String API_UPDATE_MEMBER = "/api/updateMember";
	public static final String API_GET_MEMBERLIST= "/api/getMemberList";
	public static final String API_ADD_LOCATION = "/api/addLocation";
	public static final String API_ADD_GEOFENCE = "/api/addGeofence";
	public static final String API_GET_SCHOOLLIST = "/api/getSchoolList";
	public static final String API_GET_LASTLOCATION = "/api/getLastLocation";
	public static final String API_GET_LOCATIONLIST = "/api/getLocationList";
	public static final String API_GET_GEOFENCELIST = "/api/getGeofenceList";

	public static final String API_GET_MEASURESUMMARY = "/api/getMeasureSummary";
	public static final String API_GET_HEIGHT = "/api/getHeight";
	public static final String API_GET_WEIGHT = "/api/getWeight";

	public static final String API_ADD_AREA = "/api/addArea";
	public static final String API_GET_AREA = "/api/getArea";
	public static final String API_GET_AREALIST = "/api/getAreaList";

	public static final String API_GET_SCHOOL_NOTI_LIST = "/admin/api/getSchoolNotiList";
	public static final String API_GET_SCHOOL_NOTI_LIST_BY_MEMBER = "/api/getSchoolNotiListByMember";

	public static final String API_GET_CONSULT_LIST = "/api/getConsultList";
	public static final String API_ADD_CONSULT = "/api/addConsult";
	public static final String API_RATE_CONSULT = "/api/rateConsult";
	public static final String API_GET_CONSULT_HISTORY = "/api/getConsultHistory";

	public static final String API_GET_APP_NOTI_LIST = "/api/getNotiList";
	public static final String API_GET_BOARD_LIST = "/api/getBoardList";
	public static final String API_ADD_BOARD = "/api/addBoard";
	public static final String API_ADD_NOTI_BOOKMARK = "/api/addNotiBookmark";
	public static final String API_REMOVE_NOTI_BOOKMARK = "/api/removeNotiBookmark";

	public static final String API_ADD_ACTIVITY = "/api/addActivity";
	public static final String API_GET_ACTIVITY_LIST = "/api/getActivityList";

	//비디오 리스트 가져오기
	public static final String API_GET_VIDEOLIST_BY_MASTERID = "/api/getVideoListByMasterGradeId";
	public static final String API_GET_VIDEOLIST_BY_SECTION = "/api/getVideoListByInfoType";
	public static final String API_GET_VIDEOTIME_OF_MEMBER = "/api/getVideoTimeOfMember";

	public static final String API_ADD_DINING = "/api/addDining";
	public static final String API_GET_DINING_LIST = "/api/getDiningList";
	public static final String API_GET_DINING_IMAGE = "/images/dining/";

	public static final String API_GET_OS_INFO = "/api/getOsInfo";

	public static final String API_FILE = "/upload/";

	public static final int NOTIFICATION_STEP = 1001;
	public static final int NOTIFICATION_SCHOOL_ALIMI = 1002;
	public static final int NOTIFICATION_CONSULT = 1003;
	public static final int NOTIFICATION_QNA = 1004;
	public static final int NOTIFICATION_NOTICE = 1005;
	public static final int NOTIFICATION_GEOFENCE = 1006;

	public static final String NOTIFCATION_DESTINATION_FRAGMENT = "des_fragment";
	public static final String CATEGORY = "category";
	public static final String SCHOOL_ID = "school_id";

	public static final int CATEGORY_LETTER = 1;
	public static final int CATEGORY_NOTI = 2;
	public static final int CATEGORY_SCHEDULE = 3;
}
