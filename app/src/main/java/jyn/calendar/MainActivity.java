package jyn.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends Activity {
    int mYear, mMonth, mDay, mDayseq;
    boolean mOkyn = false;
    boolean mDayyn = true;
    int[] mDayChk; //날짜 or 월 선택시 색설정

    TextView[] mDayNumber = new TextView[42];
    FrameLayout[] mDayBackground = new FrameLayout[42];

    FrameLayout[] mMonthBackground = new FrameLayout[12];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //당일 날짜검색
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar sCalendars1 = new GregorianCalendar(Locale.KOREA);
        sCalendars1.add(Calendar.MONTH, -1);
        String sClearDay = df.format(sCalendars1.getTime());

        //검색된 당일날짜 저장.
        mYear = Integer.valueOf(sClearDay.substring(0,4));
        mMonth = Integer.valueOf(sClearDay.substring(4,6));
        mMonth++;
        mDay = Integer.valueOf(sClearDay.substring(6,8));

        mDayChk = new int[2];
        mDayChk[0] = getResources().getColor(R.color.color_calendar_chk);
        mDayChk[1] = getResources().getColor(R.color.color_calendar_nonchk);

        Setting();
    }

    //화면설정
    private void Setting() {
        setContentView(R.layout.calendar_main);

        mDayyn = true;

        TextView txtYear = (TextView)findViewById(R.id.txtYear);
        TextView txtMonth = (TextView)findViewById(R.id.txtMonth);
        txtYear.setText(String.valueOf(mYear) + "년 ");
        txtMonth.setText(String.valueOf(mMonth) + "월");

        ImageButton imgbtnOk = (ImageButton)findViewById(R.id.imgbtnOk);
        imgbtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOkyn =  true;
                //dismiss();
            }
        });

        ImageButton imgbtnPrevious = (ImageButton)findViewById(R.id.imgbtnPrevious);
        imgbtnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMonth--;
                MonthChang();
            }
        });
        ImageButton imgbtnNext = (ImageButton)findViewById(R.id.imgbtnNext);
        imgbtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMonth++;
                MonthChang();
            }
        });

        LinearLayout llMonth = (LinearLayout)findViewById(R.id.llMonth);
        llMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.dialog_calendar_month);

                mDayyn = false;

                TextView txtYear_Month = (TextView)findViewById(R.id.txtYear_Month);
                txtYear_Month.setText(String.valueOf(mYear) + "년");

                ImageButton imgbtnNext_Month = (ImageButton)findViewById(R.id.imgbtnNext_Month);
                imgbtnNext_Month.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView txtYear_Month = (TextView)findViewById(R.id.txtYear_Month);
                        int sYear = Integer.valueOf(txtYear_Month.getText().toString().substring(0,4));
                        sYear++;
                        txtYear_Month.setText(String.valueOf(sYear) + "년");
                    }
                });
                ImageButton imgbtnPrevious_Month = (ImageButton)findViewById(R.id.imgbtnPrevious_Month);
                imgbtnPrevious_Month.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView txtYear_Month = (TextView)findViewById(R.id.txtYear_Month);
                        int sYear = Integer.valueOf(txtYear_Month.getText().toString().substring(0,4));
                        sYear--;
                        txtYear_Month.setText(String.valueOf(sYear) + "년");
                    }
                });

                String resName2 = "frmMonth";
                for (int i=0; i<12; i++) {
                    mMonthBackground[i] = (FrameLayout)findViewById(getResources().getIdentifier(resName2 + String.valueOf(i+1), "id", getPackageName()));
                    mMonthBackground[i].setTag(i+1);
                    mMonthBackground[i].setOnClickListener(frame_month_click);
                    mMonthBackground[i].setBackgroundColor(mDayChk[1]);
                    if ((i+1) == mMonth) {
                        mMonthBackground[i].setBackgroundColor(mDayChk[0]);
                    }
                }
            }
        });

        String resName1 = "txt", resName2 = "frm";
        int sDayweek = getDayweek(mYear, mMonth, 1);
        int sDaynum = 1; //날짜
        int sDaynum_Max = getMonth_Lastday(mYear, mMonth); //해당월 최종날짜

        for (int i=0; i < 42; i++) {
            mDayNumber[i] = (TextView)findViewById(this.getResources().getIdentifier(resName1 + String.valueOf(i+1), "id", this.getPackageName()));
            mDayBackground[i] = (FrameLayout)findViewById(this.getResources().getIdentifier(resName2 + String.valueOf(i+1), "id", this.getPackageName()));
            mDayBackground[i].setOnClickListener(frame_click);
            mDayNumber[i].setTag(i);
            mDayBackground[i].setTag(i);

            mDayBackground[i].setBackgroundColor(mDayChk[1]);
            mDayNumber[i].setText("");
            if (sDayweek <= i && sDaynum_Max >= sDaynum) {
                mDayNumber[i].setText(String.valueOf(sDaynum));
                if (mDay == sDaynum) {
                    mDayseq = i;
                    mDayBackground[i].setBackgroundColor(mDayChk[0]);
                }
                sDaynum++;
            }
        }
    }

    private void MonthChang() {
        //mMonth = mMonth + month;
        if (mMonth > 12) {
            mYear++;
            mMonth = 1;
        } else if (mMonth < 1) {
            mYear--;
            mMonth = 12;
        }

        int sDayweek = getDayweek(mYear, mMonth, 1);
        int sDaynum = 1; //날짜
        int sDaynum_Max = getMonth_Lastday(mYear, mMonth); //해당월 최종날짜

        if (mDay > sDaynum_Max) mDay = 1;

        for (int i=0; i < 42; i++) {
            mDayBackground[i].setBackgroundColor(mDayChk[1]);
            mDayNumber[i].setText("");
            if (sDayweek <= i && sDaynum_Max >= sDaynum) {
                mDayNumber[i].setText(String.valueOf(sDaynum));
                if (mDay == sDaynum) {
                    mDayseq = i;
                    mDayBackground[i].setBackgroundColor(mDayChk[0]);
                }
                sDaynum++;
            }
        }

        TextView txtYear = (TextView)findViewById(R.id.txtYear);
        TextView txtMonth = (TextView)findViewById(R.id.txtMonth);
        txtYear.setText(String.valueOf(mYear) + "년 ");
        txtMonth.setText(String.valueOf(mMonth) + "월");
    }

    private int getMonth_Lastday(int year, int month){
        int last_day = 0;
        switch (month) {
            case 1 :
                last_day = 31;
                break;
            case 2 :
                if (!IsLeapyear(year)) last_day = 28; else last_day = 29;
                break;
            case 3 :
                last_day = 31;
                break;
            case 4 :
                last_day = 30;
                break;
            case 5 :
                last_day = 31;
                break;
            case 6 :
                last_day = 30;
                break;
            case 7 :
                last_day = 31;
                break;
            case 8 :
                last_day = 31;
                break;
            case 9 :
                last_day = 30;
                break;
            case 10 :
                last_day = 31;
                break;
            case 11 :
                last_day = 30;
                break;
            case 12 :
                last_day = 31;
                break;
            default:
        }
        return last_day;
    }

    //윤년 계산하기
    boolean IsLeapyear(int year) {
        boolean IsLeapyear = false;
        if((year % 4 == 0) && (year % 100) != 0 || (year % 400) == 0) {
            IsLeapyear = true;
        }
        return IsLeapyear;
    }

    //날짜의 요일계산
    private int getDayweek(int y, int m, int d) {
        int k, j, h;

        if (m <= 2) {
            m += 12;
            y--;
        }

        k = y % 100;
        j = y / 100;
        h = 21 * j / 4 + 5 * k / 4 + 13 * (m + 1) / 5 + d - 1;

        if (h < 0) h += 7;

        return h % 7;
    }


    View.OnClickListener frame_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tag = Integer.valueOf(String.valueOf(v.getTag()));
            if (!mDayNumber[tag].getText().equals("")) {
                mDayBackground[mDayseq].setBackgroundColor(mDayChk[1]); //기존 배경색 변경 nonchk 로 변경
                mDayBackground[tag].setBackgroundColor(mDayChk[0]); //새로 선택한 날짜의 배경색 chk 로 변경

                mDayseq = tag; //날짜의 seq 값 변경
                mDay = Integer.valueOf(mDayNumber[mDayseq].getText().toString()); //저장된 날짜값 변경
            }
        }
    };

    View.OnClickListener frame_month_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //월 변경
            TextView txtYear_Month = (TextView)findViewById(R.id.txtYear_Month);
            int tag = Integer.valueOf(String.valueOf(v.getTag()));
            mYear = Integer.valueOf(txtYear_Month.getText().toString().substring(0,4));
            mMonth = tag;

            Setting();
            MonthChang();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (!mDayyn && keyCode == 4) {
                Setting();
                MonthChang();
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
