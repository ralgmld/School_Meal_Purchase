package com.cookandroid.myapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class HomeActivity extends AppCompatActivity implements OnClickListener {
    String url = "https://www.mokpo.ac.kr/index.9is?contentUid=4a94e3927e9b080e017f4f09d5562c3f";
    TextView textView, result;
    String msg;
    int count =0;
    final Bundle bundle = new Bundle();
    private final HomeActivity.MyHandler handler = new HomeActivity.MyHandler(this);

    private class MyHandler extends Handler {
        private final WeakReference<HomeActivity> weakReference;

        public MyHandler(HomeActivity activity) {
            this.weakReference = new WeakReference<HomeActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            weakReference.get().textView.setText(bundle.getString("message"));
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_count:

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
                        HomeActivity.this);
                alertBuilder.setTitle("항목중에 하나를 선택하세요.");

                // List Adapter 생성
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        HomeActivity.this,
                        android.R.layout.select_dialog_singlechoice);
                adapter.add("1");
                adapter.add("2");
                adapter.add("3");
                adapter.add("4");
                adapter.add("5");

                // 버튼 생성
                alertBuilder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //count += 5000;
                            }
                });

                // Adapter 셋팅
                alertBuilder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                // AlertDialog 안에 있는 AlertDialog
                                String strName = adapter.getItem(id); //수량
                                int strcount = Integer.parseInt(strName) * count;

                                result = (TextView)findViewById(R.id.result_text);
                                if(count == 4500) {
                                    result.setText("구매내역 : 학생 " + count + ", 수량"+ strName +"\n 총 결제금액 = "+ strcount + "("+ count +"X"+strName+")");
                                }
                                else
                                {
                                    result.setText("구매내역 : 교직원 " + count + ", 수량"+ strName +"\n 총 결제금액 = "+ strcount + "("+ count +"X"+strName+")"  );
                                }

                                AlertDialog.Builder innBuilder = new AlertDialog.Builder(
                                        HomeActivity.this);
                                innBuilder.setMessage(strName);
                                innBuilder.setTitle("당신이 선택한 것은 ");
                                innBuilder
                                        .setPositiveButton(

                                                "확인",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                innBuilder.show();
                            }
                        });
                alertBuilder.show();
                break;
            case R.id.btn_buy:
                AlertDialog.Builder alertBuilder1 = new AlertDialog.Builder(
                        HomeActivity.this);
                alertBuilder1.setTitle("항목중에 하나를 선택하세요.");

                // List Adapter 생성
                final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                        HomeActivity.this,
                        android.R.layout.select_dialog_singlechoice);
                adapter1.add("학생");
                adapter1.add("교직원");


                // 버튼 생성
                alertBuilder1.setPositiveButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //count += 4500;
                            }
                        });

                // Adapter 셋팅
                alertBuilder1.setAdapter(adapter1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                // AlertDialog 안에 있는 AlertDialog
                                String strName = adapter1.getItem(id);
                                AlertDialog.Builder innBuilder = new AlertDialog.Builder(
                                        HomeActivity.this);
                                innBuilder.setMessage(strName);
                                innBuilder.setTitle("당신이 선택한 것은 ");
                                innBuilder
                                        .setPositiveButton(

                                                "확인",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                innBuilder.show();
                                if(strName.equals("학생")) {
                                    count = 4500;
                                }
                                else {
                                    count = 5000;
                                }
                            }
                        });
                alertBuilder1.show();
            default:
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        textView = findViewById(R.id.textview);

        new Thread(() -> {
            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();
                msg = "학식메뉴";
                for(int i=0;i<=4;i++) {
                    Element elements = doc.select("article.menu_wrap dt").get(i);
                    Element elements1 = doc.select("article.menu_wrap dd").get(i);
                    msg += "\n"+elements.text() + "\n "+ elements1.text() ; //body 값 불러와 저장

                }
                bundle.putString("message",msg);
                Message msg = handler.obtainMessage();
                msg.setData(bundle);
                handler.sendMessage(msg);

            } catch (IOException e){
                e.printStackTrace();
            }
        }).start();

        Button buy = (Button) findViewById(R.id.btn_buy);
        buy.setOnClickListener(this);

        Button count = (Button) findViewById(R.id.btn_count);
        count.setOnClickListener(this);

    }
}