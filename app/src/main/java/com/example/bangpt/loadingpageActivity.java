package com.example.bangpt;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.PulseRing;

public class loadingpageActivity extends Dialog {
    public loadingpageActivity(@NonNull Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE); // title 없는 거
        setContentView(R.layout.loading);

        ProgressBar progressBar = findViewById(R.id.spin_kit);
        Sprite pulseRing = new PulseRing();
        progressBar.setIndeterminateDrawable(pulseRing);
    }
}
//로딩 다이얼로그 띄어줄 화면에서 아래 명령어 실행하기!
//loadingpageActivity loadingDialog = new loadingpageActivity(MyWriteActivity.this);
//loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//loadingDialog.setCancelable(false);
//loadingDialog.show();

// 처리될 메소드들이 다 처리되고 나면 이거 써서 종료하기
//loadingDialog.dismiss();