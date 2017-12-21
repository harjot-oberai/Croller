package com.sdsmdg.harjot.crollerTest;

public interface OnCrollerChangeListener {
    void onProgressChanged(Croller croller, int progress);

    void onTap(Croller croller);

    void onStartTrackingTouch(Croller croller);

    void onStopTrackingTouch(Croller croller);
}
