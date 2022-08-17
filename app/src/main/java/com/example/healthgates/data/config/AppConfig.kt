package com.example.healthgates.data.config

class AppConfig {

    companion object {
        const val DB = "healthgates.pulseiehr.com"
//        const val DB = "healthgates-copy.pulseiehr.com"
        const val JSON_RPC = "2.0"
        const val METHOD = "call"
        private const val SERVER_BASE_URL = "https://healthgates.pulseiehr.com/"
//        private const val SERVER_BASE_URL = "https://healthgates-copy.pulseiehr.com/"
        const val AGORA_APP_ID = "e6e27f6339e143f2b9450f8b1d53edf8"
        const val LOGIN_URL = SERVER_BASE_URL + "web/session/authenticate"
        const val SIGNUP_URL = SERVER_BASE_URL + "api/signup"
        const val RESET_PASSWORD_URL = SERVER_BASE_URL + "api/reset/password"
        const val SEARCH_LIMIT = 80
        const val PATIENT_DETIALS_URL = SERVER_BASE_URL + "api/get/patient/details"
        const val SEARCH_READ_SPECIALTY_URL = SERVER_BASE_URL + "api/web/dataset/search_read"
        const val SEARCH_READ_PATIENT_DETAILS_URL = SERVER_BASE_URL + "api/web/dataset/search_read"
        const val SEARCH_READ_DOCTOR_DETAILS_URL = SERVER_BASE_URL + "api/web/dataset/search_read"
        const val GET_BOOKING_SLOTS_URL = SERVER_BASE_URL + "api/get/slot"
        const val GET_ALL_BRANCHES_URL = SERVER_BASE_URL + "api/get/all/branches"
        const val READ_PATIENT_DETAILS_URL = SERVER_BASE_URL + "api/web/dataset/call_kw"
        const val BOOK_APPOINTMENT_URL = SERVER_BASE_URL + "api/web/dataset/call_kw"
        const val READ_DOCTOR_DETAILS_URL = SERVER_BASE_URL + "api/web/dataset/call_kw"
        const val SEARCH_READ_APPOINTMENT_DETAILS_URL = SERVER_BASE_URL + "api/web/dataset/search_read"
        const val GET_APPOINTMENT_DETAILS_URL = SERVER_BASE_URL + "api/get/appointment/details"
        const val GET_ALL_LAB_TEST_RESULTS = SERVER_BASE_URL + "api/get/all/lab/test/results"
        const val GET_LAB_TEST_RESULTS = SERVER_BASE_URL + "api/get/lab/test/results"
        const val GET_ALL_RAD_TEST_RESULTS = SERVER_BASE_URL + "api/get/all/rad/test/results"
        const val GET_ALL_PRESCRIPTIONS = SERVER_BASE_URL + "api/get/all/prescription/order"
        const val UPDATE_CHANNEL_ID_PATIENT_ID_URL = SERVER_BASE_URL + "update/channel/patient/token/id"
        const val INCOMING_CALL_TO_DOCTOR_URL = SERVER_BASE_URL + "incoming/call/to/doctor"
        const val GENERATE_TOKEN_CHANNEL_ID_URL = SERVER_BASE_URL + "generate/token/channel/id"
        const val DISCONNECT_CALL = SERVER_BASE_URL + "disconnect/call"
    }

}