package com.btb.nixorstudentapplication.Autentication.nsp_web;

import android.util.Log;

import com.btb.nixorstudentapplication.Misc.common_util;

import org.jsoup.nodes.Document;

import static com.btb.nixorstudentapplication.Autentication.nsp_web.portal_async.base_url;

/**
 * Created by Hassan Abbasi on 25/05/2018.
 */

public class ExtractData {
     static String TAG ="ExtractData";

    //Gets the PhotoURL
    public static String Extract_Display_Photo(Document logged_user_data) {


        common_util.encode_text("Extracting Picture URL");
        String profile_image_url = logged_user_data.getElementsByClass("profile-image")
                .html().replace("<img src=\"", "").replace("\" alt=\"Not Found\">", "");
        profile_image_url = base_url + profile_image_url;
        System.out.println(profile_image_url);
        return profile_image_url;
    }

    //Gets the Student Details
    public static StudentDetails Extract_Basic_Information(Document logged_user_data) {


        //table table-profile
        //<td>Email Address : </td>
        //<td>Student Name :</td>
        //<td>House :</td>
        // <td>Graduating Class :</td>
        //<td>Student ID :</td>


        String table = logged_user_data.getElementsByClass("table table-profile").toString();
        common_util.encode_text("Extracting Basic Details");
        String Student_name = Format_Student_BasicDetails(table, "<td>Student Name :</td>");
        String Student_ID = Format_Student_BasicDetails(table, "<td>Student ID :</td>");
        String Student_email = Format_Student_BasicDetails(table, "<td>Email Address : </td>");
        String Student_year = Format_Student_BasicDetails(table, "<td>Graduating Class :</td>");
        String Student_house = Format_Student_BasicDetails(table, "<td>House :</td>");
        //student_name, String student_ID, String student_email, String student_year, String student_house
        StudentDetails student = new StudentDetails(Student_name, Student_ID, Student_email, Student_year, Student_house, null, null);
        return student;
    }

    //Formats the Student Details
    public static String Format_Student_BasicDetails(String table, String type) {
        //System.out.println(type);
        //Error handling Required
        try {
            String[] first_break = table.split(type);
            String[] second_break = first_break[1].split("</tr>");
            String student_detail = second_break[0].replace("<td>", "").replace("</td>", "").replaceAll("(?m)^[ \t]*\r?\n", "").trim();
            System.out.println(student_detail);
            return student_detail;
        } catch (Exception e) {
            System.out.println("Couldn't get: " + type);
            return null;
        }


    }


    public static String Get_Student_GUID(Document logged_user_data) {
        String GUID = logged_user_data.location();
        GUID = GUID.substring(43, 79);
        return GUID;
    }


    //Generates a Student Object
    public static StudentDetails getStudentObject(Document logged_user_data) {
        StudentDetails student = Extract_Basic_Information(logged_user_data);
        student.setStudent_profileUrl(Extract_Display_Photo(logged_user_data));
        student.setStudent_guid(Get_Student_GUID(logged_user_data));
        Log.i(TAG, logged_user_data.location());
        return student;
    }




}
