/*
Copyright (c) 2012, Zubair Khan (governer@gmail.com) 
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
    *     * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
    *
    *     THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.ds.avare;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

/**
 * @author zkhan
 * Main storage service. It stores all states so when activity dies,
 * we dont start from no state.
 * This is especially important for start up functions that take time,
 * one of which is databse un-zipping.
 * 
 * Also sends intent to display warning, since its too intrusive to show a 
 * warning every time activity starts.
 */
public class StorageService extends Service {

    /**
     * The Sqlite database
     */
    private ImageDataSource mImageDataSource;
    /**
     * Store this
     */
    private Destination mDestination;
    /**
     * Store this
     */
    private GpsParams mGpsParams;
    /**
     * Store this
     */
    private Movement mMovement;

    /**
     * Store this
     */
    private Pan mPan;

    /**
     * Area around us
     */
    private Area mArea;
    
    /**
     * TFR list
     */
    private TFRFetcher mTFRFetcher;

    /*
     * For performing periodic activities.
     */
    private Timer mTimer;
    
    
    /**
     * Local binding as this runs in same thread
     */
    private final IBinder binder = new LocalBinder();

    /*
     * When to show warning
     */
    private Boolean mShouldWarn = true;

    /**
     * @author zkhan
     *
     */
    public class LocalBinder extends Binder {
        /**
         * @return
         */
        StorageService getService() {
            return StorageService.this;
        }
    }
    /* (non-Javadoc)
     * @see android.app.Service#onBind(android.content.Intent)
     */
    @Override
    public IBinder onBind(Intent arg0) {
        return binder;
    }
    
    /* (non-Javadoc)
     * @see android.app.Service#onUnbind(android.content.Intent)
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    /* (non-Javadoc)
     * @see android.app.Service#onCreate()
     */
    @Override
    public void onCreate() {
          
        super.onCreate();

        mImageDataSource = new ImageDataSource(getApplicationContext());
        
        mArea = new Area(getApplicationContext(), mImageDataSource);
        
        mTFRFetcher = new TFRFetcher(getApplicationContext());
        mTimer = new Timer();
        TimerTask tfrTime = new UpdateTask();
        
        /*
         * Monitor TFR every hour.
         */
        mTimer.scheduleAtFixedRate(tfrTime, 0, Preferences.TFR_UPDATE_PERIOD_MS);
    }
        
    /* (non-Javadoc)
     * @see android.app.Service#onDestroy()
     */
    @Override
    public void onDestroy() {
          super.onDestroy();
    }
    
    /*
     * Get/Set (state), Get (resources, state) functions for activity
     */

    /**
     * @return
     */
    public Boolean shouldWarn() {
        if(mShouldWarn) {
            mShouldWarn = false;
            return true;
        }
        return false;
    }

    /**
     * @return
     */
    public LinkedList<TFRShape> getTFRShapes() {
        return mTFRFetcher.getShapes();
    }

    /**
     * @return
     */
    public ImageDataSource getDBResource() {
        return mImageDataSource;
    }
    
    /**
     * @return
     */
    public Destination getDestination() {
        return mDestination;
    }

    /**
     * @param destination
     */
    public void setDestination(Destination destination) {
        mDestination = destination;
    }

    /**
     * @return
     */
    public GpsParams getGpsParams() {
        return mGpsParams;
    }

    /**
     * @param params
     */
    public void setGpsParams(GpsParams params) {
        mGpsParams = params;
    }

    /**
     * @param m
     */
    public void setMovement(Movement m) {
        mMovement = m;
    }

    /**
     * @return
     */
    public Movement getMovement() {
        return mMovement;
    }

    
    /**
     * @param m
     */
    public void setPan(Pan p) {
        mPan = p;
    }

    /**
     * @return
     */
    public Pan getPan() {
        return mPan;
    }

    /**
     * @return
     */
    public Area getArea() {
        return mArea;
    }
   
    /**
     * @author zkhan
     *
     */
    private class UpdateTask extends TimerTask {
        
        private int counter = -1;
        
        /* (non-Javadoc)
         * @see java.util.TimerTask#run()
         */
        public void run() {

            /*
             * Comes here every TFR_UPDATE_PERIOD_MS (1 minute)
             * Try to fetch more quickly when we dont have TFRs
             * When we have TFRs, then fetch slowly for update only.
             */
            counter++;
            Message msg = mHandler.obtainMessage();
            msg.what = (Integer)0;
            if(mTFRFetcher.isFromFile() || (mTFRFetcher.getShapes() == null)) {
                mHandler.sendMessage(msg);
                return;
            }
            else if (mTFRFetcher.getShapes().isEmpty()) {
                mHandler.sendMessage(msg);
                return;
            }
            else if(((counter % Preferences.TFR_GET_PERIOD_MIN) == 0)) {
                mHandler.sendMessage(msg);
                return;
            }
        }
    }
    
    /**
     * This leak warning is not an issue if we do not post delayed messages, which is true here.
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mTFRFetcher.fetch();            
        }
    };

}