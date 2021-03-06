package com.example.yuichi_oba.ecclesia.tools;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.yuichi_oba.ecclesia.activity.AddMemberActivity;
import com.example.yuichi_oba.ecclesia.activity.ReserveListActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.yuichi_oba.ecclesia.tools.NameConst.*;

public class Util {

    public static final int COLUMN_INDEX = 1;
    public static final String DATE_PATTERN = "yyyy/MM/dd HH:mm";

    /***
     * 項目引数に渡すと、その項目のインデックスを返すUtilityメソッド
     * @param spinner   スピナー
     * @param item      インデックスを検索したい文字列
     * @return 引数の文字列がそのスピナーの何番目にあるかを返す
     */
    public static int setSelection(Spinner spinner, String item) {
        SpinnerAdapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(item)) {
                return i;
            }
        }
        // なければ０を返す
        return 0;
    }

  /***
   * 簡単・みやすいログの出力メソッド
   * @param args
   */
  public static void easyLog(String args) {
    Log.d(CALL, "_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/");
    Log.d(CALL, "_/");
    Log.d(CALL, "_/                   " + args);
    Log.d(CALL, "_/");
    Log.d(CALL, "_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/");
  }

  /***
   * 部署IDから、部署名をDB検索してリターンするメソッド
   * @param dep_id    部署ID
   * @return 検索した部署名（ヒットなしは 空文字を返す ）
   */
  public static String returnDepartName(String dep_id) {
    Log.d(CALL, "call returnDepartName()");
    Log.d(CALL, dep_id);
    SQLiteOpenHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
    SQLiteDatabase db = helper.getReadableDatabase();

    Cursor c = db.rawQuery("SELECT * FROM m_depart where dep_id = ?",
        new String[]{dep_id});
    String depName = "";
    if (c.moveToNext()) {
      depName = c.getString(COLUMN_INDEX); //*** 「部署名」**//
    }
    c.close();

    return depName; //*** 部署名を返す ***//
  }

    /***
     *
     * @param depName
     * @return
     */
    public static String returnDepartId(String depName) {
        SQLiteOpenHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM m_depart where dep_name = ?",
                new String[]{depName});
        String depId = "";
        if (c.moveToNext()) {
            depId = c.getString(0); //*** 「部署ID」**//
        }
        c.close();

        return depId;   //*** 部署ＩＤを返す ***//
    }

    /***
     *  役職IDから、役職名をMyHelper検索してリターンするメソッド
     * @param pos_id    役職ID
     * @return 検索した役職名（ヒットなしは 空文字 ヲ返す ）
     */
    public static AddMemberActivity.Position returnPostionName(String pos_id) {
        SQLiteOpenHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM m_position", null);
        String posName = "";
        AddMemberActivity.Position p = new AddMemberActivity.Position();
        if (c.moveToNext()) {
            posName = c.getString(COLUMN_INDEX);//*** 「役職名」 ***//
            p.posId = c.getString(0);
            p.posName = c.getString(1);
            p.posPriority = c.getString(2);
        }
        c.close();

        return p; //*** 役職情報のインスタンスを返す ***//
    }

    /***
     *
     * @param posName
     * @return
     */
    public static AddMemberActivity.Position returnPositionId(String posName) {
        SQLiteOpenHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM m_position where pos_name = ?",
                new String[]{posName});
        AddMemberActivity.Position p = new AddMemberActivity.Position();
        if (c.moveToNext()) {
            p.posId = c.getString(0);
            p.posName = c.getString(1);
            p.posPriority = c.getString(2);
        }
        c.close();

        return p;   //*** 役職情報のインスタンスを返す ***//
    }

    /***
     * 会議室名から、会議室ＩＤをＤＢ検索してリターンするメソッド
     * @param roomName  会議室名
     * @return 検索した会議室ＩＤ（ヒットなしは 空文字を返す ）
     */
    public static String returnRoomId(String roomName) {
        SQLiteOpenHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.rawQuery("select * from m_room where room_name = ?",
                new String[]{roomName});

        String roomId = "";
        if (c.moveToNext()) {
            roomId = c.getString(0);    //*** 会議室ID ***//
        }
        c.close();

        return roomId;  //*** 会議室ＩＤを返す ***//
    }

    /***
     *
     * @param roomId
     * @return
     */
    public static String returnRoomName(String roomId) {
        SQLiteOpenHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.rawQuery("select * from m_room where room_id = ?",
                new String[]{roomId});

        String roomName = "";
        if (c.moveToNext()) {
            roomName = c.getString(0);    //*** 会議室ID ***//
        }
        c.close();

        return roomName;//*** 会議室名を返す ***//
    }

  /***
   * 予約テーブルの予約IDの最大値＋１をMyHelper検索して、書式指定して返すメソッド
   * @return
   */
  public static String returnMaxReserveId() {
        Log.d(CALL, "call Util.returnMaxReserveId()");
        SQLiteOpenHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        String maxReserveId = "";
        //*** 予約テーブルの予約IDの最大値＋１をMyHelper検索 ***//
        Cursor c = db.rawQuery("select max(re_id) + 1 from t_reserve", null);
        if (c.moveToNext()) {
          maxReserveId = c.getString(0);          //*** 検索結果の代入（ここでは、未だ０埋めされていない） ***//
          Log.d(CALL, maxReserveId);
        }
        c.close();

        Log.d(CALL, String.format("新しい、予約ID : %04d", Integer.valueOf(maxReserveId)));
        return String.format("%04d", Integer.valueOf(maxReserveId));        //*** 書式指定付きで、０埋めして返す (ex: 0018) ***//
  }
    /***
     * 引数の社員名から、その社員の社員IDをMyHelper検索して値を返すメソッド
     * @param empName
     * @return
     */
    public static String returnEmpId(String empName) {
        Cursor c = rawQuery("select emp_id from t_emp where emp_id = ?", new String[]{empName});
        String empId = "";
        if (c.moveToNext()) {
            empId = c.getString(0); //***  ***//
        }
        return empId;               //***  ***//
    }

    /***
     * 引数の社員名から、その社員の社員IDをMyHelper検索して値を返すメソッド
     * @param empName
     * @return
     */
    public static String returnOutEmpId(String empName) {
        Cursor c = rawQuery("select out_id from m_out where out_name = ?", new String[]{empName});
        String outId = "";
        if (c.moveToNext()) {
            outId = c.getString(0);
        }
        //*** 社外者 ***//
        return outId;
    }

    /***
     * MyHelper簡単SQL発行メソッド
     * @param args
     * @param strArray
     * @return
     */
    public static Cursor rawQuery(String args, String[] strArray) {
        SQLiteOpenHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        return db.rawQuery(args, strArray);
    }

    /***
     * 引数の文字列を、Calenderクラスのインスタンス化したものを返すメソッド
     * 引数の渡し方 yyyy/MM/dd HH:mm となるように、args に String.formatで整形して渡す
     * @param args
     * @return
     * @throws ParseException
     */
    public static Calendar convertCalenderString(String args) throws ParseException {
        Date date = new Date((new SimpleDateFormat(DATE_PATTERN).parse(args).getTime()));
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c;   //***  ***//
    }

    //    public static long insertReserve(Reserve reserve, float priorityAverage) {
    //
    //        ContentValues c = new ContentValues();
    //        c.put("re_id", reserve.getRe_id());                 //***  ***//
    //        c.put("re_overview", reserve.getRe_name());         //***  ***//
    //        c.put("re_startday", reserve.getRe_startDay());     //***  ***//
    //        c.put("re_endday", reserve.getRe_endDay());         //***  ***//
    //        c.put("re_starttime", reserve.getRe_startTime());   //***  ***//
    //        c.put("re_endtime", reserve.getRe_endTime());       //***  ***//
    //        c.put("re_switch", reserve.getRe_switch());         //***  ***//
    //        c.put("re_fixture", reserve.getRe_fixtures());      //***  ***//
    //        c.put("re_remarks", reserve.getRe_remarks());       //***  ***//
    //        c.put("re_priority", priorityAverage);              //***  ***//
    //        c.put("com_id", "");                                //***  ***//
    //        c.put("emp_id", reserve.getRe_applicant());         //***  ***//
    //        c.put("room_id", reserve.getRe_room_id());          //***  ***//
    //        c.put("pur_id", reserve.getRe_purpose_id());        //***  ***//
    //        c.put("re_applicant", reserve.getRe_applicant());    //***  ***//
    //
    //        //***  ***//
    //        SQLiteOpenHelper helper = new MyHelper(ReserveListActivity.getInstance().getApplicationContext());
    //
    //        return helper.getWritableDatabase().insertOrThrow("t_reserve", null, c);  //***  ***//
    //    }
    public static String returnPurposeId(String purName) {
        MyHelper helper = new MyHelper(ReserveListActivity.getInstance().getBaseContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from m_purpose where pur_name = ?", new String[]{purName});
        if (c.moveToNext()) {
            //*** 会議目的IDを返す ***//
            return c.getString(0);
        }
        c.close();

        return "";
    }

    /**
     * 指定した日時のカレンダーを取得します。
     *
     * @param y   :年
     * @param m   :月
     * @param d   :日
     * @param h   :時間
     * @param min :分
     */
    public static Calendar getCalender(int y, int m, int d, int h, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.MONTH, m);
        calendar.set(Calendar.DAY_OF_MONTH, d);
        calendar.set(Calendar.HOUR, h);
        calendar.set(Calendar.MINUTE, min);

        return calendar;

    }

    //*** 管理者認証済みか否かを返す ***//
    public static boolean isAuthAdmin(String authFlg) {
        if (authFlg.contains("1")) {
            return true;
        }
        return false;
    }


}
