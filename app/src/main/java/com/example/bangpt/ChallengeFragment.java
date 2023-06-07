package com.example.bangpt;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.bangpt.Request.ChallengeRequest;
import com.example.bangpt.Request.JoinRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChallengeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChallengeFragment extends Fragment {
    private Button button;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChallengeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChallengeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChallengeFragment newInstance(String param1, String param2) {
        ChallengeFragment fragment = new ChallengeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_challenge, container, false);

        button = view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EditText에 현재 입력되어있는 값을 가져옴
                Bundle bundle = getArguments();
                String participantId = bundle.getString("userID");

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try { // 참가자 등록에 성공
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                Context context = getContext();
                                Toast.makeText(context, "챌린지 참여가 완료되었습니다!", Toast.LENGTH_SHORT).show();
                            }
                            else{ // 회원가입에 실패
                                Context context = getContext();
                                Toast.makeText(context, "챌린지 참여에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                ChallengeRequest challengeRequest = new ChallengeRequest(participantId, responseListener);
                Context context = getContext();
                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(challengeRequest);

            }
        });

        return view;
    }
}