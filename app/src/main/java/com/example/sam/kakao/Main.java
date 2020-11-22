package com.example.sam.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Context ctx = Main.this;

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteHelper helper = new SQLiteHelper(ctx);
                startActivity(new Intent(ctx, Login.class));

            }

        });

    } // onCreate() ends

    static interface ExecuteService {
        public void perform();

    }

    static interface ListService {
        public List<?> perform();

    }

    static interface ObjectService {
        public Object perform();

    }

    static abstract class QueryFactory {
        Context ctx;

        public QueryFactory(Context ctx) {
            this.ctx = ctx;

        }

        public abstract SQLiteDatabase getDatabase();

    }

    static class SQLiteHelper extends SQLiteOpenHelper {
        public SQLiteHelper(Context context) {
            super(context, DatabaseInfo.DATABASE_NAME, null, 1);
            this.getWritableDatabase();

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = String.format(
                    " CREATE TABLE IF NOT EXISTS %s " +
                            " ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT ) ",

                    DatabaseInfo.MEMBER_TABLE,
                    DatabaseInfo.MEMBER_SEQ, DatabaseInfo.MEMBER_NAME, DatabaseInfo.MEMBER_PASS, DatabaseInfo.MEMBER_EMAIL, DatabaseInfo.MEMBER_PHONE, DatabaseInfo.MEMBER_ADDR, DatabaseInfo.MEMBER_PHOTO

            );

            Log.d("실핼할 쿼리 :: ", sql);

            db.execSQL(sql);
            Log.d("========================= ", "쿼리 실행");

            String[] names = {"강동원", "윤아", "임수정", "박보검", "송중기"};
            String[] emails = {"kang@naver.com", "yun@naver.com", "lim@gmail.com", "park@gmail.com", "song@gmail,com"};
            String[] addrs = {"강동구", "강남구", "종로", "강북", "서초구"};

            for (int i = 0; i < names.length; i++) {
                Log.d("입력하는 이름 :: ", names[i]);
                db.execSQL(String.format(
                        " INSERT INTO %s ( %s , %s , %s , %s , %s , %s ) " +
                                " VALUES ( '%s', '%s', '%s', '%s', '%s', '%s' ) ",

                        DatabaseInfo.MEMBER_TABLE,
                        DatabaseInfo.MEMBER_NAME, DatabaseInfo.MEMBER_PASS, DatabaseInfo.MEMBER_EMAIL, DatabaseInfo.MEMBER_PHONE, DatabaseInfo.MEMBER_ADDR, DatabaseInfo.MEMBER_PHOTO,
                        names[i], '1', emails[i], "010-1234-567" + i, addrs[i], "photo_" + (i + 1)

                ));

            }

            Log.d("*************************", "친구 등록 완료");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(" DROP TABLE IF EXISTS " + DatabaseInfo.MEMBER_TABLE);
            onCreate(db);

        }

    }

}
