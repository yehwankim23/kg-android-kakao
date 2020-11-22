package com.example.sam.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MemberList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);

        final Context ctx = MemberList.this;

        final ListView memberList = findViewById(R.id.lv_memberList);

        final ItemList query = new ItemList(ctx);

        ArrayList<Member> mList = (ArrayList<Member>) new Main.ListService() {
            @Override
            public List<?> perform() {
                return query.execute();

            }

        }.perform();

        memberList.setAdapter(new MemberAdapter(ctx, mList));

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, MemberDetail.class));

            }

        });

        // Details
        memberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View v, int i, long l) {
                Member m = (Member) memberList.getItemAtPosition(i);
                Log.d("선택한 ID : ", m.seq + "");

                Intent intent = new Intent(ctx, MemberDetail.class);
                intent.putExtra("seq", m.seq + "");
                startActivity(intent);

            }

        });

        // Delete
        memberList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;

            }

        });

    } // onCreate() ends

    /*
    Database related part starts
    */
    private class ListQuery extends Main.SQLiteHelper {
        SQLiteOpenHelper helper;

        public ListQuery(Context ctx) {
            super(ctx);
            helper = new Main.SQLiteHelper(ctx);

        }

        //        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();

        }

    }

    private class ItemList extends ListQuery {
        public ItemList(Context ctx) {
            super(ctx);

        }

        public ArrayList<Member> execute() {
            ArrayList<Member> ls = new ArrayList<>();

            Cursor c = this.getDatabase().rawQuery(
                    " SELECT * FROM MEMBER ", null

            );

            Member m = null;

            if (c != null) {
                while (c.moveToNext()) {
                    m = new Member();

                    m.setSeq(Integer.parseInt(c.getString(c.getColumnIndex(DatabaseInfo.MEMBER_SEQ))));
                    m.setName(c.getString(c.getColumnIndex(DatabaseInfo.MEMBER_NAME)));
                    m.setPass(c.getString(c.getColumnIndex(DatabaseInfo.MEMBER_PASS)));
                    m.setEmail(c.getString(c.getColumnIndex(DatabaseInfo.MEMBER_EMAIL)));
                    m.setPhone(c.getString(c.getColumnIndex(DatabaseInfo.MEMBER_PHONE)));
                    m.setAddr(c.getString(c.getColumnIndex(DatabaseInfo.MEMBER_ADDR)));
                    m.setPhoto(c.getString(c.getColumnIndex(DatabaseInfo.MEMBER_PHOTO)));

                    ls.add(m);

                    Log.d("등록된 회원 : ", ls.size() + "명");

                }

            } else {
                Log.d("등록된 회원 : ", "없음");

            }

            return ls;

        }

    }
    /*
    Database related part ends
    */

    private class MemberAdapter extends BaseAdapter {
        Context ctx;
        ArrayList<Member> ls;
        LayoutInflater inflater;

        public MemberAdapter(Context ctx, ArrayList<Member> ls) {
            this.ctx = ctx;
            this.ls = ls;
            this.inflater = LayoutInflater.from(ctx);

        }

        @Override
        public int getCount() {
            return ls.size();

        }

        @Override
        public Object getItem(int i) {
            return ls.get(i);

        }

        @Override
        public long getItemId(int i) {
            return i;

        }

        @Override
        public View getView(int i, View v, ViewGroup g) {
            ViewHolder holder;

            if (v == null) {
                v = inflater.inflate(R.layout.member_item, null);
                holder = new ViewHolder();

                holder.name = v.findViewById(R.id.tv_name);
                holder.photo = v.findViewById(R.id.iv_photo);
                holder.phone = v.findViewById(R.id.tv_phone);

                v.setTag(holder);

            } else {
                holder = (ViewHolder) v.getTag();

            }

            holder.name.setText(ls.get(i).getName());
            holder.phone.setText(ls.get(i).getPhone());

            final ItemPhoto query = new ItemPhoto(ctx);
            query.seq = ls.get(i).seq + "";

            String s = ((String) new Main.ObjectService() {
                @Override
                public Object perform() {
                    return query.execute();

                }

            }.perform()).toLowerCase();
            Log.d("파일명 : ", s);

            holder.photo.setImageDrawable(getResources().getDrawable(getResources().getIdentifier(ctx.getPackageName() + ":drawable/" + s, null, null), ctx.getTheme()));

            return v;

        }

    }

    static class ViewHolder {
        ImageView photo;
        TextView name, phone;

    }

    private class PhotoQuery extends Main.QueryFactory {
        Main.SQLiteHelper helper;

        public PhotoQuery(Context ctx) {
            super(ctx);
            helper = new Main.SQLiteHelper(ctx);

        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();

        }

    }

    private class ItemPhoto extends PhotoQuery {
        String seq;

        public ItemPhoto(Context ctx) {
            super(ctx);

        }

        public String execute() {
            Cursor c = getDatabase().rawQuery(String.format(
                    " SELECT %s FROM %s WHERE %s LIKE '%s' ",

                    DatabaseInfo.MEMBER_PHOTO,
                    DatabaseInfo.MEMBER_TABLE,
                    DatabaseInfo.MEMBER_SEQ,
                    seq

            ), null);

            String result = "";

            if (c != null) {
                if (c.moveToNext()) {
                    result = c.getString(c.getColumnIndex(DatabaseInfo.MEMBER_PHOTO));

                }

            }

            return result;

        }

    }

}
