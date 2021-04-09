package com.example.pianoman

import android.graphics.*
import java.util.*
import android.graphics.RectF


class Note (private val speed: Float, private val piano: Piano, private val view: PianoView, private val pitch: Int, position: Int, private val duree: Int){

    private val epaisseur: Float = 50f
    private var x1: Float = 0f
    private var x2: Float = 0f
    private val longueur = 150f
    private var y: Float = - (longueur + 10f) * position

    private val randomColor = Paint()
    private val random = Random()
    private var noteOnScreen = true

    private var rect: RectF = RectF()

    init {
        randomColor.color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))
    }

    fun draw(canvas: Canvas) {
        if (rect.top > -10 && noteOnScreen) canvas.drawRoundRect(rect, 10f, 10f, randomColor)
    }

    fun update(interval: Double) {
        if (noteOnScreen) {
            rect.top += (interval * speed).toFloat()
            rect.bottom += (interval * speed).toFloat()
        }
        if (rect.top - longueur * duree > piano.pianoTop) {
            view.score.resetMultiplier()
            deleteNote()
        }
    }

    private fun deleteNote() {
        noteOnScreen = false
    }

    fun detectPlay(playedPitch: Int) {
        if(rect.top >= piano.pianoTop - 50f && rect.bottom <= piano.pianoTop + 50f && noteOnScreen) if (playedPitch == pitch) {
            view.score.increaseScore((longueur*duree).toInt() - kotlin.math.abs(piano.pianoTop - rect.top).toInt())
            deleteNote()
            view.score.increaseMultiplier()
        }
        else (view.score.resetMultiplier())
    }

    fun setNote() {
        x1 = when(pitch) {
            in 1..5 -> (pitch * piano.whiteKeyWidth - epaisseur) / 2
            in 6..12 -> ((pitch + 1) * piano.whiteKeyWidth - epaisseur) / 2
            in 13..17 -> ((pitch + 2) * piano.whiteKeyWidth - epaisseur) / 2
            in 18..24 -> ((pitch + 3) * piano.whiteKeyWidth - epaisseur) / 2
            25 -> ((pitch + 4) * piano.whiteKeyWidth - epaisseur) / 2
            else -> (piano.whiteKeyWidth - epaisseur) / 2
        }

        x2 = x1 + epaisseur
        rect = RectF(x1, y, x2, (y - longueur * duree))
    }
}
