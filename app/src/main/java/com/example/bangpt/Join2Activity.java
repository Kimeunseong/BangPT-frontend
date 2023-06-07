package com.example.bangpt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Join2Activity extends AppCompatActivity {
    private Button btn_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join2);

        btn_register=findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            //회원 가입 후 메인화면으로
            public void onClick(View view){
                Intent intent=new Intent(Join2Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        Button galleryButton = findViewById(R.id.btn_upload);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivity(intent);
    }
}