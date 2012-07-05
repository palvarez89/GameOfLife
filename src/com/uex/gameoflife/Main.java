package com.uex.gameoflife;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class Main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void onJugarClick(View button){
    	Intent intent = new Intent();
    	intent.setComponent(new ComponentName(this,Game.class));
    	startActivity(intent);
    	
    }
}