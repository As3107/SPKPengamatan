package com.ggpc.spkpengamatan;

public class EndPoints {

    public static String server = "https://ai.gg-foods.com/spk_pengamatan/api_spk_pengamatan.php";
    public static String server2 = "https://ggp-pis.com/spk_pengamatan/Api.php";
    public static String serverSAP = "https://ai.gg-foods.com/spk_pengamatan/spk_sap/get_spk_sqlsrv.php";
    public static String key = "?key=oBs_2398!-spk";
    public static String apicall = "&apicall=";

    public static final String GET_SPK_HEADER = server + key + apicall + "get_spk_header";
    public static final String GET_SPK_HEADER_MANDOR = server + key + apicall + "get_spk_header_mandor";
    public static final String GET_SPK_ACTIVITY = server + key + apicall + "get_activity";
    public static final String GET_SPK_ACTIVITY_TK = server + key + apicall + "get_activity_observer";
    public static final String SEND_OBSERVER = server + key + apicall + "update_spk";
    public static final String UPDATE_STATUS_SPK = server + key + apicall + "update_status_spk";
    public static final String GET_TOTAL_ACTIVITY = server + key + apicall + "get_total_activity";
    public static final String SEND_IMAGES = server2 + key + apicall+ "send_images";
    public static final String GET_CHOPPER_URL = server + key + apicall + "get_chopper";
    public static final String SEND_CHOPPER_URL = server + key + apicall + "set_chopper";
    public static final String DELETE_DATA_CHOPPER = server + key + apicall + "delete_chopper";
    public static final String GET_RIDGERS_URL = server + key + apicall + "get_ridger_single";
    public static final String SEND_RIDGERS_URL = server + key + apicall + "set_ridger_single";
    public static final String DELETE_DATA_RIDGERS = server + key + apicall + "delete_ridger_single";

    //API SAP
    public static final String UPLOAD_CHOPPER_SRV = serverSAP + key + apicall + "set_chopper2";
    public static final String GET_SPK_HEADER_SRV = serverSAP + key + apicall + "get_spk_header";
    public static final String GET_SPK_HEADER_MANDOR_SRV = serverSAP + key + apicall + "get_spk_header_mandor";
    public static final String GET_SPK_ACTIVITY_SRV = serverSAP + key + apicall + "get_activity";
    public static final String GET_SPK_ACTIVITY_TK_SRV = serverSAP + key + apicall + "get_activity_observer";
    public static final String SEND_OBSERVER_SRV = serverSAP + key + apicall + "update_spk";
    public static final String UPDATE_STATUS_SPK_SRV = serverSAP + key + apicall + "update_status_spk";
    public static final String GET_TOTAL_ACTIVITY_SRV = serverSAP + key + apicall + "get_total_activity";
    public static final String GET_CHOPPER_URL_SRV = serverSAP + key + apicall + "get_chopper_spl";
    public static final String SEND_CHOPPER_URL_SRV = serverSAP + key + apicall + "set_chopper_spl";
    public static final String DELETE_DATA_CHOPPER_SRV = serverSAP + key + apicall + "delete_chopper_spl";
    public static final String UPDATE_DATA_CHOPPER_SRV = serverSAP + key + apicall + "update_chopper_spl";
    public static final String GET_RIDGERS_URL_SRV = serverSAP + key + apicall + "get_ridger_sr";
    public static final String SEND_RIDGERS_URL_SRV = serverSAP + key + apicall + "set_ridger_sr";
    public static final String DELETE_DATA_RIDGERS_SRV = serverSAP + key + apicall + "delete_ridger_sr";
    public static final String UPDATE_DATA_RIDGERS_SRV = serverSAP + key + apicall + "update_ridger_sr";
    public static final String UPLOAD_RIDGERS_SRV = serverSAP + key + apicall + "set_ridger_total";

    public static final String LOGIN_URL = "https://ai.gg-foods.com/spk_pengamatan/Api.php?apicall=login&key=oBs_2398!-spk";

    public static final String UPDATE_CHOPPER_URL = "https://ai.gg-foods.com/spk_pengamatan/Api.php?apicall=update_chopper";
    public static final String GET_SPK_MANDOR_URL = "https://ai.gg-foods.com/spk_pengamatan/Api.php?apicall=get_spk_mandor";
    public static final String GET_RESULT_SPK = "https://ai.gg-foods.com/spk_pengamatan/Api.php?apicall=get_result_spk";
    public static final String GET_AGREE_SPK = "https://ai.gg-foods.com/spk_pengamatan/Api.php?apicall=get_agree_spk";
    public static final String GET_AGREE_SPK_KASIE = "https://ai.gg-foods.com/spk_pengamatan/Api.php?apicall=get_agree_spk_kasie";
    public static final String GET_PROFILE_TK_URL = "https://ai.gg-foods.com/spk_pengamatan/Api.php?apicall=get_profile_tk";
    public static final String GET_PROFILE_MANDOR_URL = "https://ai.gg-foods.com/spk_pengamatan/Api.php?apicall=get_profile_mandor";
    public static final String GET_PROFILE_KASIE_URL = "https://ai.gg-foods.com/spk_pengamatan/Api.php?apicall=get_profile_kasie";
    public static final String GET_LIST_TK_URL = "https://ai.gg-foods.com/spk_pengamatan/Api.php?apicall=get_list_tk";
    public static final String GET_LIST_TK_URL_NEW = "https://ai.gg-foods.com/spk_pengamatan/Api.php?apicall=get_list_tk_new";
    public static final String SEND_DATA_MANDOR_URL = "https://ai.gg-foods.com/spk_pengamatan/Api.php?apicall=update_status_spk";
    public static final String UPDATE_STATUS_SPK_URL = "https://ai.gg-foods.com/spk_pengamatan/Api.php?apicall=update_status_spk";
    public static final String SET_NOTES_URL = "https://ai.gg-foods.com/spk_pengamatan/Api.php?apicall=set_notes";
    public static final String GET_NOTES_URL = "https://ai.gg-foods.com/spk_pengamatan/Api.php?apicall=get_notes";
    public static final String UPDATE_TOKEN_MANDOR = "https://ai.gg-foods.com/spk_pengamatan/Api.php?apicall=update_token_mandor";
    public static final String UPDATE_TOKEN_TK = "https://ai.gg-foods.com/spk_pengamatan/Api.php?apicall=update_token_tk";
    public static final String UPDATE_TOKEN_KASIE = "https://ai.gg-foods.com/spk_pengamatan/Api.php?apicall=update_token_kasie";
    public static final String SEND_NOTIF_MANDOR = "https://ai.gg-foods.com/spk_pengamatan/notifikasi/send_mandor.php";
    public static final String SEND_NOTIF_TK = "https://ai.gg-foods.com/spk_pengamatan/notifikasi/send_tk.php";
    public static final String SEND_NOTIF_KASIE = "https://ai.gg-foods.com/spk_pengamatan/notifikasi/send_kasie.php";



}
