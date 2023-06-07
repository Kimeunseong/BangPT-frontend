package com.example.bangpt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.bangpt.Request.GetDatesRequest;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;

import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {
    private MaterialCalendarView mCalendarView;

    private ArrayList<String> itemList; //운동한 날짜들 저장


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
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
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        itemList = new ArrayList<>();

        // MaterialCalendarView 초기화
        mCalendarView = view.findViewById(R.id.calView);
        mCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY) // 일요일부터 시작
                .setMinimumDate(CalendarDay.from(2002, 0, 1)) // 최소 날짜
                .setMaximumDate(CalendarDay.from(2032, 11, 31)) // 최대 날짜
                .commit();
        mCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new OneDayDecorator());
        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                // 클릭한 날짜를 가져옵니다.
                int year = date.getYear();
                int month = date.getMonth() + 1; // 월은 0부터 시작하므로 1을 더해줍니다.
                int day = date.getDay();
                Bundle bundle = getArguments();
                String userID = bundle.getString("userID");

                // 클릭한 날짜를 액티비티로 전달합니다.
                Intent intent = new Intent(getActivity(), CalendarMemoActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
        /**/
        CalendarDay today = CalendarDay.today();

        int year = today.getYear();
        int month = today.getMonth() + 1; // 월은 0부터 시작하므로 1을 더해줍니다.
        Bundle bundle = getArguments();
        String userID = bundle.getString("userID");
        itemList.clear();

        Response.Listener<String> responseListener=new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i=0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String date = jsonObject.optString("diary_date");
                        itemList.add(date);
                    }

                    // itemList에 저장된 날짜들을 이벤트로 표시하는 EventDecorator 생성
                    List<CalendarDay> eventDates = new ArrayList<>();

                    for (int i = 0; i < itemList.size(); i++) {

                        String item = itemList.get(i);
                        String[] dateParts = item.split("-");
                        int itemYear = Integer.parseInt(dateParts[0]);
                        int itemMonth = Integer.parseInt(dateParts[1]);
                        int itemDay = Integer.parseInt(dateParts[2]);
                        eventDates.add(CalendarDay.from(itemYear, itemMonth-1, itemDay));
                    }


// 새로운 이벤트 데코레이터를 추가하여 반영
                    EventDecorator eventDecorator = new EventDecorator(Color.RED, eventDates);
                    mCalendarView.addDecorator(eventDecorator);
                    //

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        String URL = GetDatesRequest.GetURL(userID, Integer.toString(year), Integer.toString(month));
        GetDatesRequest getDatesRequest=new GetDatesRequest(userID, URL,responseListener);
        Context context = getContext();
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(getDatesRequest);
        /**/

        mCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {
                int year = date.getYear();
                int month = date.getMonth() + 1; // 월은 0부터 시작하므로 1을 더해줍니다.
                Bundle bundle = getArguments();
                String userID = bundle.getString("userID");
                itemList.clear();

                Response.Listener<String> responseListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for(int i=0 ; i < jsonArray.length() ; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String date = jsonObject.optString("diary_date");
                                itemList.add(date);
                            }

                            // itemList에 저장된 날짜들을 이벤트로 표시하는 EventDecorator 생성
                            List<CalendarDay> eventDates = new ArrayList<>();

                            for (int i = 0; i < itemList.size(); i++) {

                                String item = itemList.get(i);
                                String[] dateParts = item.split("-");
                                int itemYear = Integer.parseInt(dateParts[0]);
                                int itemMonth = Integer.parseInt(dateParts[1]);
                                int itemDay = Integer.parseInt(dateParts[2]);
                                eventDates.add(CalendarDay.from(itemYear, itemMonth-1, itemDay));
                            }
// 기존의 이벤트 데코레이터들을 모두 제거
                            //mCalendarView.removeDecorators();

// 새로운 이벤트 데코레이터를 추가하여 반영
                            EventDecorator eventDecorator = new EventDecorator(Color.RED, eventDates);
                            mCalendarView.addDecorator(eventDecorator);
                            //

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                String URL = GetDatesRequest.GetURL(userID, Integer.toString(year), Integer.toString(month));
                GetDatesRequest getDatesRequest=new GetDatesRequest(userID, URL,responseListener);
                Context context = getContext();
                RequestQueue queue= Volley.newRequestQueue(context);
                queue.add(getDatesRequest);

            }
        });

        Log.d("TAG", "1"); // 디버그 메시지 출력


        return view;
    }



    public class SundayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();
        public SundayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }

    public class SaturdayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public SaturdayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }

    public class OneDayDecorator implements DayViewDecorator {

        private CalendarDay date;

        public OneDayDecorator() {
            date = CalendarDay.today();
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return date != null && day.equals(date);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new StyleSpan(Typeface.BOLD));
            view.addSpan(new RelativeSizeSpan(1.4f));
            view.addSpan(new ForegroundColorSpan(Color.GREEN));
        }

        public void setDate(Date date) {
            this.date = CalendarDay.from(date);
        }
    }
    public class EventDecorator implements DayViewDecorator {
        private final int color;
        private final List<CalendarDay> eventDates;

        public EventDecorator(int color, Collection<CalendarDay> eventDates) {
            this.color = color;
            this.eventDates = new ArrayList<>(eventDates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return eventDates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, color));
        }
    }
}