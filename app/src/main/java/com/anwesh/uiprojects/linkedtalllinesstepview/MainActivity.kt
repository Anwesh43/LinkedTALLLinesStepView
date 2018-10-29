package com.anwesh.uiprojects.linkedtalllinesstepview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.talllinesstepview.TAllLinesStepView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAllLinesStepView.create(this)
    }
}
