package com.cookandroid.myapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SingupActivity extends AppCompatActivity {

    myDBHelper myHelper;
    EditText edtId, edtPw, edtPwconfirm, edtName, edtBirth, edtGender, edtPhoneNum;
    Button btnJoin;
    SQLiteDatabase sqlDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtId = (EditText) findViewById(R.id.InputId);
        edtPw = (EditText) findViewById(R.id.InputPw);
        edtPwconfirm = (EditText) findViewById(R.id.InputConfirmPw);
        edtName = (EditText) findViewById(R.id.InputName);
        edtBirth = (EditText) findViewById(R.id.InputBirth);
        edtGender= (EditText) findViewById(R.id.Gender);
        edtPhoneNum = (EditText) findViewById(R.id.PhoneNumber);

        btnJoin = (Button) findViewById(R.id.Joinbtn);

        myHelper = new myDBHelper(this);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sqlDB = myHelper.getReadableDatabase();
                Cursor cursor;
                cursor = sqlDB.rawQuery("SELECT*FROM userinfo;", null);

                int id = Integer.parseInt(edtId.getText().toString());
                String pwd = edtPw.getText().toString();
                String pwd_confirm = edtPwconfirm.getText().toString();
                int count = 0; // 아이디 중복 검사

                while (cursor.moveToNext()) {
                    if ( id == cursor.getInt(0) ) {
                        count ++;
                    }
                }

                if( count == 0) {
                    if(pwd.equals(pwd_confirm)) {
                        sqlDB = myHelper.getWritableDatabase();

                        sqlDB.execSQL("INSERT INTO userinfo VALUES ( '"
                                + edtId.getText().toString() + "' , '"
                                + edtPw.getText().toString() + "' , '"
                                + edtName.getText().toString() + "' , '"
                                + edtBirth.getText().toString() + "' , '"
                                + edtGender.getText().toString() + "' , "
                                + edtPhoneNum.getText().toString() + ");");
                        sqlDB.close();
                        Toast.makeText(getApplicationContext(), "회원가입 완료",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "중복된 아이디입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public static class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {
            super(context, "userDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE  userinfo ( id INTEGER, password CHAR(20), name CHAR(20), brith INTEGER, gender CHAR(5), phonenum INTEGER);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS userinfo");
            onCreate(db);
        }
    }

}
