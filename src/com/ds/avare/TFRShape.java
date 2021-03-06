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

/**
 * 
 * @author zkhan
 *
 */
public class TFRShape extends Shape {

    private boolean mVisible;

    /**
     * Distance from current location for draw
     */
    private static final int MAXDISTANCE = 100;
   
    /**
     * 
     */
    public TFRShape(String text) {
        super(text);
    }
    
    /**
     * 
     * @return
     */
    public boolean isVisible() {
        return mVisible;
    }
    
    /**
     * Update from display bound the visibility of this TFR shape,
     * and if visible, draw in a bitmap.
     * @param lonl
     * @param lonr
     * @param latu
     * @param latl
     */
    public void prepareIfVisible(double lon, double lat) {
        mVisible = false;
        Projection p;
        
        /*
         * If TFR is within max distance range, draw it
         */
        p = new Projection(super.mLonMin, super.mLatMin, lon, lat);
        if(p.getDistance() < MAXDISTANCE) {
            mVisible = true;
        }
        p = new Projection(super.mLonMax, super.mLatMax, lon, lat);
        if(p.getDistance() < MAXDISTANCE) {
            mVisible = true;
        }
        p = new Projection(super.mLonMin, super.mLatMax, lon, lat);
        if(p.getDistance() < MAXDISTANCE) {
            mVisible = true;
        }
        p = new Projection(super.mLonMax, super.mLatMin, lon, lat);
        if(p.getDistance() < MAXDISTANCE) {
            mVisible = true;
        }        
    }
}
