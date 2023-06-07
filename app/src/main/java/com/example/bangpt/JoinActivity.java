package com.example.bangpt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.bangpt.Request.CheckIDRequest;
import com.example.bangpt.Request.JoinRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class JoinActivity extends AppCompatActivity {

    private EditText et_id, et_pass, et_name, et_nickname;
    private Button btn_chkID, btn_confirm, btn_register;
    private AlertDialog dialog;
    private boolean validate=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        //아이디 값 찾기
        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        et_name = findViewById(R.id.et_name);
        et_nickname = findViewById(R.id.et_nickname);

        btn_chkID = findViewById(R.id.btn_chkID);
        //중복체크
        btn_chkID.setOnClickListener(new View.OnClickListener() { //id중복체크
            @Override
            public void onClick(View view) {
                String userID=et_id.getText().toString();
                if(validate)
                {
                    return;
                }
                if(userID.equals("")){
                    AlertDialog.Builder builder=new AlertDialog.Builder( JoinActivity.this );
                    dialog=builder.setMessage("아이디는 빈 칸일 수 없습니다")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse=new JSONObject(response);
                            boolean success=jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder=new AlertDialog.Builder( JoinActivity.this );
                                dialog=builder.setMessage("사용할 수 있는 아이디입니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                                et_id.setEnabled(false);
                                validate=true;
                                btn_chkID.setText("확인");
                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder( JoinActivity.this );
                                dialog=builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                CheckIDRequest checkidRequest=new CheckIDRequest(userID,responseListener);
                RequestQueue queue= Volley.newRequestQueue(JoinActivity.this);
                queue.add(checkidRequest);

            }
        });

        btn_confirm = findViewById(R.id.btn_confirm);
        // 인증하기 버튼 클릭시 수행
        btn_confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            //버튼을 누르면 트레이너 인증 화면으로
            public void onClick(View view){
                Intent intent=new Intent(JoinActivity.this,Join2Activity.class);
                startActivity(intent);
            }
        });

        //회원가입 버튼 클릭 시 수행
        btn_register=findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditText에 현재 입력되어있는 값을 가져옴
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();
                String userName = et_name.getText().toString();
                String userNick = et_nickname.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try { // 회원가입에 성공
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                Toast.makeText(getApplicationContext(), "회원 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            else{ // 회원가입에 실패
                                Toast.makeText(getApplicationContext(), "회원 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                JoinRequest joinRequest = new JoinRequest(userID, userPass, userName, userNick, responseListener);
                RequestQueue queue = Volley.newRequestQueue(JoinActivity.this);
                queue.add(joinRequest);
            }
        });

    }
}