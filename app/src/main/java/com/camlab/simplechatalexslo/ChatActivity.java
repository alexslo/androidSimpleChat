package com.camlab.simplechatalexslo;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class ChatActivity extends Activity {

    private ListView mListViewChat;
    private MyArrayAdapter <String> msgList;
    private EditText mEditTextMessage;

    private Random mRandom;

    private MsgDataBase sqh;
    private SQLiteDatabase sqdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mListViewChat = (ListView)findViewById(R.id.listView);
        mEditTextMessage = (EditText)findViewById(R.id.editText);

        msgList = new MyArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);

        mListViewChat.setAdapter(msgList);

        mRandom = new Random();
        //chat’s server emulator:
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, getRandomInterval());
        //DataBase:
        sqh = new MsgDataBase(this);
        sqdb = sqh.getWritableDatabase();
        //Update from DB
        updateViewFromDB();

    }

    @Override
    protected void onStop() {
        super.onStop();
        sqdb.close();
        sqh.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSendButtonClick(View view) {
        String OutputMessage = "You:" + mEditTextMessage.getText().toString() +'\n';
        viewAndSaveNewMsg(OutputMessage);
    }

    private void TimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);
    }


    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            String InputMessage = "Him:" + "Just a test text, random:"+ getRandomInterval() +'\n';
            viewAndSaveNewMsg(InputMessage);
        }
    };

    private int getRandomInterval () {
        return (mRandom.nextInt(30000) +2000);
    }
    private void viewAndSaveNewMsg (String _msg) {
        insertMsgToDB(_msg);
        viewNewMsg(_msg);
    }
    private void viewNewMsg (String _msg) {
        Boolean isAutoScrollApply;
        int saveScrollPosition = 0;
        if (mListViewChat.getLastVisiblePosition() != (msgList.getCount() - 1))
        {
            isAutoScrollApply = false;
            saveScrollPosition = mListViewChat.getLastVisiblePosition();
        }
        else isAutoScrollApply = true;
        msgList.add(_msg);
        mListViewChat.setAdapter(msgList);
        //scroll down
        if (isAutoScrollApply) mListViewChat.setSelection(msgList.getCount() - 1);
        else  mListViewChat.setSelection(saveScrollPosition);
    }

    private void insertMsgToDB (String _msg) {
        // INSERT
        ContentValues cv = new ContentValues();
        cv.put(MsgDataBase.MSQ_VALUE, _msg);
        sqdb.insert(MsgDataBase.TABLE_NAME, MsgDataBase.MSQ_VALUE, cv);

    }


    private void updateViewFromDB () {
        //get from DB
        Cursor cursor = sqdb.query(MsgDataBase.TABLE_NAME, new String[] {
                        MsgDataBase._ID, MsgDataBase.MSQ_VALUE },
                        null, null, null, null,null );
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(MsgDataBase._ID));
            String msg = cursor.getString(cursor
                    .getColumnIndex(MsgDataBase.MSQ_VALUE));
            //UpdateView:
            viewNewMsg (msg);
            //Log.i("LOG_TAG", "ROW " + id + " HAS NAME " + msg);
        }
        cursor.close();

    }




}
