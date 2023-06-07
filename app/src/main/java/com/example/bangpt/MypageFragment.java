package com.example.bangpt;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MypageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MypageFragment extends Fragment {
    ViewGroup viewGroup;
    TextView name, name2;
    String userid;


    //private Button btn_badge;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MypageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MypageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MypageFragment newInstance(String param1, String param2) {
        MypageFragment fragment = new MypageFragment();
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
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_mypage, container, false);

        name = rootView.findViewById(R.id.name);
        name2 = rootView.findViewById(R.id.name2);

        Bundle bundle = getArguments();
        String userID = bundle.getString("userID");
        String userPass = bundle.getString("userPass");
        name.setText(userID);


        Button btnID = rootView.findViewById(R.id.btnID);
        btnID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyInfoActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("userPass", userPass);
                startActivity(intent);
            }
        });
        Button btn_write = rootView.findViewById(R.id.btn_write);
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyWriteActivity.class);
                startActivity(intent);
            }
        });

        Button btn_badge = rootView.findViewById(R.id.btn_badge);
        btn_badge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyChallActivity.class);
                startActivity(intent);
            }
        });

        Button btn_result = rootView.findViewById(R.id.btn_result);
        btn_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExerciseResultList.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
        myinfo();
        return rootView;
    }
    private void myinfo() {
        SharedPreferences settings = getActivity().getSharedPreferences("Login", 0);
        userid = settings.getString("userID", ""); // Use the global userid variable

        String serverUrl = "http://172.20.10.8:821/user/" + userid;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, serverUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String user_id = response.getString("user_id");
                            String trainer = response.getString("trainer");

                            // Update TextViews with retrieved information
                            name2.setText(trainer);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jsonObjectRequest);
    }
}