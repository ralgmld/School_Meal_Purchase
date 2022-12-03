package com.cookandroid.myapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

    SingupActivity.myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    EditText edtLid, edtLpw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLid = (EditText) findViewById(R.id.LoginId);
        edtLpw = (EditText) findViewById(R.id.LoginPw);

        myHelper = new SingupActivity.myDBHelper(this);

        Button btnLogin = (Button)findViewById(R.id.login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = myHelper.getReadableDatabase();
                Cursor cursor;

                int id = Integer.parseInt(edtLid.getText().toString());
                String pw = edtLpw.getText().toString();
                String id_str = edtLid.getText().toString();

                /*if(pw.length() == 0) { // 아이디 공백체크는 에러뜸
                    Toast.makeText(getApplicationContext(), "비밀번호는 필수 입력사항입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(id_str.length() == 0) {
                    Toast.makeText(getApplicationContext(), "아이디는 필수 입력사항입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {*/
                    cursor = sqlDB.rawQuery("SELECT*FROM userinfo;", null);

                    while (cursor.moveToNext()) {
                        if((id == cursor.getInt(0)) && (pw.equals(cursor.getString(1))))
                        {
                            Toast.makeText(getApplicationContext(), "로그인 성공",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                            startActivity(intent);
                            break;
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "로그인 실패",Toast.LENGTH_SHORT).show();
                        }
                    }
                //}
                edtLid.setText(null);
                edtLpw.setText(null);
            }
        });

        Button btnSingup = (Button)findViewById(R.id.signup);
        btnSingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), SingupActivity.class);
                startActivity(intent);
            }

        });

        Button btnSearch = (Button)findViewById(R.id.search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);
            }

        });
    }
}
