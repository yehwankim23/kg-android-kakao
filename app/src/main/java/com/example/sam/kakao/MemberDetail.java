package com.example.sam.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MemberDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);

        final Context ctx = MemberDetail.this;

        Intent intent = this.getIntent();
        String seq = intent.getExtras().getString("seq");

        final ItemDetail query = new ItemDetail(ctx);
        query.seq = seq;

        Member m = (Member) new Main.ObjectService() {
            @Override
            public Object perform() {
                return query.execute();

            }

        }.perform();

        Log.d("선택한 멤버 정보 : ", m.toString());

        final String spec = m.seq + "/" + m.name + "/" + m.pass + "/" + m.email + "/" + m.phone + "/" + m.addr + "/" + m.photo;

        /*
        * Should start MemberUpdate with the following line.
        * String[] arr = spec.split("/");
        * */

        findViewById(R.id.updateBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, MemberUpdate.class);
                intent.putExtra("spec", spec);
                startActivity(intent);

            }

        });

    } // onCreate() ends

    /*
     * Database related part starts
     * */
    private class DetailQuery extends Main.QueryFactory {
        Main.SQLiteHelper helper;

        public DetailQuery(Context ctx) {
            super(ctx);
            helper = new Main.SQLiteHelper(ctx);

        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();

        }

    }

    private class ItemDetail extends DetailQuery {
        String seq;

        public ItemDetail(Context ctx) {
            super(ctx);

        }

        public Member execute() {
            Member m = null;

            Cursor c = this.getDatabase().rawQuery(String.format(
                    " SELECT * FROM %s " +
                            " WHERE %s LIKE '' %s ",

                    DatabaseInfo.MEMBER_TABLE,
                    DatabaseInfo.MEMBER_SEQ, seq

            ), null);

            if (c != null) {
                if (c.moveToNext()) {
                    m = new Member();

                    m.setSeq(Integer.parseInt(c.getString(c.getColumnIndex(DatabaseInfo.MEMBER_SEQ))));
                    m.setName(c.getString(c.getColumnIndex(DatabaseInfo.MEMBER_NAME)));
                    m.setPass(c.getString(c.getColumnIndex(DatabaseInfo.MEMBER_PASS)));
                    m.setEmail(c.getString(c.getColumnIndex(DatabaseInfo.MEMBER_EMAIL)));
                    m.setPhone(c.getString(c.getColumnIndex(DatabaseInfo.MEMBER_PHONE)));
                    m.setAddr(c.getString(c.getColumnIndex(DatabaseInfo.MEMBER_ADDR)));
                    m.setPhoto(c.getString(c.getColumnIndex(DatabaseInfo.MEMBER_PHOTO)));

                    Log.d("검색된 회원 : ", m.getName());

                }

            } else {
                Log.d("검색된 회원 : ", "없음");

            }

            return m;

        }

    }
    /*
     * Database related part ends
     * */

}
