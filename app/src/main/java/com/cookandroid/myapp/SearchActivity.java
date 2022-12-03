package com.cookandroid.myapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends Activity{

    SingupActivity.myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    Button search_idbtn, search_pwdbtn;
    EditText edtname, edtbirth, edtid_pwd, edtname_pwd, edtbirth_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        myHelper = new SingupActivity.myDBHelper(this);

        edtname = (EditText)findViewById(R.id.SearchName); // 아이디 찾기 이름
        edtbirth = (EditText)findViewById(R.id.SearchBirth); // 아이디 찾기 생년월일
        search_idbtn = (Button)findViewById(R.id.SearchIdbtn); // 아이디 찾기 버튼

        edtid_pwd = (EditText)findViewById(R.id.SearchId);  // 비밀번호 찾기 아이디
        edtname_pwd = (EditText)findViewById(R.id.SearchNamePw); // 비밀번호 찾기 이름
        edtbirth_pwd = (EditText)findViewById(R.id.SearchBirthPw); // 비밀번호 찾기 생년월일
        search_pwdbtn = (Button)findViewById(R.id.SearchPwbtn); // 비밀번호 찾기 버튼

        // 아이디 찾기
        search_idbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = myHelper.getReadableDatabase();
                Cursor cursor;

                String name = edtname.getText().toString();
                int birth = Integer.parseInt(edtbirth.getText().toString());
                String birth2 = String.valueOf(birth);

                cursor = sqlDB.rawQuery("SELECT*FROM userinfo;", null);

                while (cursor.moveToNext()) {
                   if(name.equals(cursor.getString(2)) && (birth == cursor.getInt(3)))
                   {
                       AlertDialog.Builder dlg = new AlertDialog.Builder(SearchActivity.this);
                       dlg.setTitle("아이디 찾기"); //제목
                       dlg.setMessage(cursor.getString(2)+"님의 아이디는 "+cursor.getString(0)+"입니다."); // 메시지
                       dlg.setIcon(R.drawable.buy_logo); // 아이콘 설정
                       dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                           public void onClick(DialogInterface dialog, int which) {
                               return;
                           }
                       });
                       dlg.show();
                       break;
                   }
                   else
                   {
                       Toast.makeText(SearchActivity.this,"일치하는 회원 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                   }
                }
                edtname.setText(null);
                edtbirth.setText(null);
            }
        });

        // 비밀번호 찾기
        search_pwdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = myHelper.getReadableDatabase();
                Cursor cursor;

                int id_pwd = Integer.parseInt(edtid_pwd.getText().toString());
                String name_pwd = edtname_pwd.getText().toString();
                int birth_pwd = Integer.parseInt(edtbirth_pwd.getText().toString());

                cursor = sqlDB.rawQuery("SELECT*FROM userinfo;", null);

                while (cursor.moveToNext()) {
                    if( (id_pwd == cursor.getInt(0)) && (name_pwd.equals(cursor.getString(2)))
                            && (birth_pwd == cursor.getInt(3)))
                    {
                        //Toast.makeText(getApplicationContext(), cursor.getString(1),Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder dlg = new AlertDialog.Builder(SearchActivity.this);
                        dlg.setTitle("비밀번호 찾기"); //제목
                        dlg.setMessage(cursor.getString(2)+"님의 비밀번호는 "+cursor.getString(1)+"입니다."); // 메시지
                        dlg.setIcon(R.drawable.buy_logo); // 아이콘 설정
                        dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                        dlg.show();
                        break;
                    }
                    else
                    {
                        Toast.makeText(SearchActivity.this,"일치하는 회원 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                edtid_pwd.setText(null);
                edtname_pwd.setText(null);
                edtbirth_pwd.setText(null);
            }
        });
    }

}
