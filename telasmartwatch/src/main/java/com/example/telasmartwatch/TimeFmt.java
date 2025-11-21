package com.example.telasmartwatch;

import com.google.firebase.Timestamp;

public class TimeFmt {
    public static String relative(Timestamp ts) {
        if (ts == null) return "";
        long now = System.currentTimeMillis();
        long t = ts.toDate().getTime();
        long diff = Math.max(0, now - t);
        long m = diff / 60000L;
        if (m < 1) return "agora";
        if (m < 60) return "há " + m + " min";
        long h = m / 60;
        return "há " + h + " h";
    }
}
