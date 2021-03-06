package com.criptext.monkeykitui.input.recorder

import android.content.Context
import android.os.Vibrator
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout

/**
 * Created by gesuwall on 4/25/16.
 */

open class RecorderTouchListener : View.OnTouchListener {
    var blocked : Boolean = false
    var lastHit : Long = 0L
    var startTime : Long = 0L

    var startX : Float = -1f

    var recordingAnimations : RecorderSlideAnimator? = null

    val maxLength = 80

    lateinit var dragger : ViewDraggerFadeOut

    fun vibrate(ctx: Context){
        val vibrator = ctx.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(50)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.pointerCount > 1 || blocked)
            return false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                startTime = System.nanoTime()
                startX = event.rawX


                dragger = ViewDraggerFadeOut(v)
                recordingAnimations?.dragger = dragger
                val started = recordingAnimations?.revealRecorder() ?: true
                if (!started){
                    startX = -1f
                    return true
                }
                vibrate(v.context)

            }
            MotionEvent.ACTION_UP -> {
                if(startX == -1f)
                    return true

                recordingAnimations?.hideRecorder(false)


            }
            MotionEvent.ACTION_MOVE -> {
                if(startX == -1f)
                    return true

                val reachedEnd = dragger.drag((startX - event.rawX).toInt())
                if (reachedEnd) {
                    recordingAnimations?.hideRecorder(true)
                    startX = -1f
                }


            }
        }
        return true
    }
}