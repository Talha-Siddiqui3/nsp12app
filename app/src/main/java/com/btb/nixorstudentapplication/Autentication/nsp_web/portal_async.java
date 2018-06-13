package com.btb.nixorstudentapplication.Autentication.nsp_web;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.btb.nixorstudentapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.btb.nixorstudentapplication.Misc.common_util;
import static com.btb.nixorstudentapplication.Autentication.nsp_web.ExtractData.getStudentObject;

public class portal_async extends AsyncTask<String,String,String> {

    common_util common_util;
    private static int timeout=10000;
    private static String asp_verification_token;
    private static String login_url;
    private static String logout_url;
    private static StudentDetails Student_Profile;
    public static String base_url;
    private static String loadingText;
    private Context context;
    String TAG = "portal_async";
    public portal_async (Context context_rec){
        context = context_rec;
    }

    @Override
    protected String doInBackground(String... strings) {
        common_util = new common_util();

        return  getCookie(strings[0],strings[1],true);

    }

    @Override
    protected void onPreExecute() {
        base_url = context.getString(R.string.base_url);
        login_url = context.getString(R.string.login_url);
        logout_url = context.getString(R.string.logout_url);
        loadingText = context.getString(R.string.portalLoginLoadingText);
        common_util = new common_util();
        common_util.progressDialogShow(context,loadingText);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
       common_util.progressDialogHide();

        try {
            Thread.sleep(500); } catch (InterruptedException e) {
            e.printStackTrace(); }

        common_util.encode_text(result);
        portal_login superObj = new portal_login();
        switch(result){
            case "logged":
                String FirebaseUID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                superObj.postStudentDetails(FirebaseUID,Student_Profile,context);break;
            case "invalidcreds":superObj.invalidCredentials(context); break;
            case "sitedown":superObj.siteDown(context); break;
            default:superObj.unknownError(context); break;

        }
        super.onPostExecute(result);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }


    private static String getCookie(String username_log, String password_log, Boolean login){
        try{
            Connection.Response loginForm = Jsoup.connect(login_url)
                    .method(Connection.Method.GET)
                    .timeout(timeout)
                    .execute();
            Document homepage_html_code = loginForm.parse();
            asp_verification_token = homepage_html_code.select("input[name=__RequestVerificationToken]").attr("value");
            //Using token to login.
            // System.out.println(homepage_html_code);
            if(login){
             return  initiateLogin(loginForm, asp_verification_token, username_log,password_log);
            }else{
                logoff(loginForm);
            }
            //initiateLogin(loginForm, asp_verification_token, username_log,password_log);}

        }catch(Exception e){
            System.out.println("Site probably down");

            e.printStackTrace();
            return "sitedown";
        }
      return null;
    }
    //Attempts a login using a post request
    public  static String initiateLogin(Connection.Response cookie_file, String asp_token, String username_log, String password_log ){
        try{
            Connection.Response logged = Jsoup.connect(login_url)
                    .method(Connection.Method.POST)
                    .timeout(timeout)
                    .cookies(cookie_file.cookies())
                    .data("Email",username_log )
                    .data("Password", password_log)
                    .data("RememberMe", "false")
                    .data("__RequestVerificationToken", asp_verification_token)
                    .data("btn btn-default", "Log in")
                    .followRedirects(true)
                    .execute();
            Document userid = logged.parse();
            //   System.out.println(userid);

            // System.out.println(username_log+" "+password_log);

            if(userid.toString().contains("Invalid login attempt")){

                incorrectCreds(username_log,password_log);
                return "invalidcreds";
            }else{
                if(userid.toString().contains("Student Details")){

                    encode_text("Login Successful");
                    ExtractData(userid);
                    return "logged";
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    return null;
    }
    //On Successful login, Data Extraction Methods are called
    public static void ExtractData(Document logged_user_data){
        //Gets the Basic Student Profile. For more information check StudentDetails Object.
        Student_Profile = getStudentObject(logged_user_data);

        getCookie(null,null,false);
    }
    //Error Handling for Incorrect CREDS
    public static void incorrectCreds(String username_failed, String password_failed){
        encode_text("Invalid Credentials");
    }
    //Shashkay
    public static String encode_text(String text){
        text = "------"+text+"------";
        System.out.println(text);
        return text;
    }

    public static void logoff(Connection.Response cookie_file){

        try{
            Connection.Response logged = Jsoup.connect(logout_url)
                    .method(Connection.Method.POST)
                    .timeout(timeout)
                    .cookies(cookie_file.cookies())
                    .data("__RequestVerificationToken", asp_verification_token)
                    .data("form-control", "Log Out")
                    .followRedirects(true)
                    .execute();
            Document userid = logged.parse();
            encode_text("Logout Successful");
            // System.out.println(userid);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
