package com.example.sam.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final Context ctx = Login.this;

        final EditText etID = findViewById(R.id.et_id);
        final EditText etPass = findViewById(R.id.et_pw);

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etID.getText().length() != 0 && etPass.getText().length() != 0) {
                    String id = etID.getText().toString();
                    String pass = etPass.getText().toString();

                    final ItemExist query = new ItemExist(ctx);
                    query.id = id;
                    query.pw = pass;

                    new Main.ExecuteService() {
                        @Override
                        public void perform() {
                            if(query.execute()) {
                                startActivity(new Intent(ctx, MemberList.class));

                            } else {
                                startActivity(new Intent(ctx, Login.class));

                            }

                        }
                    }.perform();

                } else {
                    startActivity(new Intent(ctx, Login.class));

                }

            }

        });

        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //

            }
        });
    } // onCreate() ends

    private class LoginQuery extends Main.QueryFactory {
        SQLiteOpenHelper helper;

        public LoginQuery(Context ctx) {
            super(ctx);
            helper = new Main.SQLiteHelper(ctx);

        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();

        }

    } // LoginQuery() ends

    private class ItemExist extends LoginQuery {
        String id, pw;

        public ItemExist(Context ctx) {
            super(ctx);

        }

        public boolean execute() {
            return super.getDatabase().rawQuery(String.format(
                    "SELECT * FROM %s " +
                            " WHERE %s LIKE '%s' AND %s LIKE '%s' ",

                    DatabaseInfo.MEMBER_TABLE,
                    DatabaseInfo.MEMBER_SEQ, id, DatabaseInfo.MEMBER_PASS, pw

            ), null).moveToNext();

        }

    }

}
