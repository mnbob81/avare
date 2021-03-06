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

import java.util.LinkedHashMap;
import android.content.Context;

/**
 * @author zkhan
 * Gets entries from database
 * The class that actually does something is DataBaseImageHelper
 */
public class ImageDataSource {

    /**
     * 
     */
    private DataBaseImageHelper dbHelper;
    /**
     * 
     */
    private boolean mOpened;

    /**
     * @param context
     */
    public ImageDataSource(Context context) {
        mOpened = false;
        dbHelper = new DataBaseImageHelper(context);
    }

    /**
     * @return
     */
    public boolean isOpen() {
        return mOpened;
    }
      
    /**
     * @param name
     */
    public void open(String name) {
        try {
            dbHelper.openDataBase(name);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        mOpened = true;
    }

    /**
     * 
     */
    public void close() {
        dbHelper.close();
        mOpened = false;
    }
      
    /**
     * @param lon
     * @param lat
     * @param offset
     * @param p
     * @return
     */
    public Tile findClosest(double lon, double lat, double offset[], double p[]) {
        return(dbHelper.findClosest(lon, lat, offset, p));
    }

    /**
     * @param name
     * @return
     */
    public Tile findTile(String name) {
        return(dbHelper.findTile(name));
    }

    /**
     * @param lon
     * @param lat
     * @param offset
     * @param p
     * @return
     */
    public boolean isWithin(double lon, double lat, double offset[], double p[]) {
        return(dbHelper.isWithin(lon, lat, offset, p));
    }

    /**
     * @param name
     * @param params
     * @return
     */
    public boolean findDestination(String name, LinkedHashMap<String, String> params) {
        return(dbHelper.findDestination(name, params));
    }
    
    /**
     * 
     * @param lon
     * @param lat
     * @param airports
     */
    public void findClosestAirports(double lon, double lat, Airport[] airports) {
        dbHelper.findClosestAirports(lon, lat, airports);        
    }
    
    public String findClosestAirportID(double lon, double lat) {
        return(dbHelper.findClosestAirportID(lon, lat));
    }

}
