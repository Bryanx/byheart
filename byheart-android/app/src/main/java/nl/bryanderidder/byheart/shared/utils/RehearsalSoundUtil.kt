package nl.bryanderidder.byheart.shared.utils

import android.content.Context
import android.media.MediaPlayer
import nl.bryanderidder.byheart.R

/**
 * Facade for managing rehearsal sounds
 * @author Bryan de Ridder
 */
class RehearsalSoundUtil(context: Context) {
    private val correctSound = MediaPlayer.create(context, R.raw.correct)
    private val incorrectSound = MediaPlayer.create(context, R.raw.incorrect)

    private fun stopIncorrect() {
        if (!incorrectSound.isPlaying) return
        incorrectSound.stop()
        incorrectSound.prepare()
    }

    private fun stopCorrect() {
        if (!correctSound.isPlaying) return
        correctSound.stop()
        correctSound.prepare()
    }

    fun playCorrect() {
        stopCorrect()
        correctSound.start()
    }

    fun playFalse() {
        stopIncorrect()
        incorrectSound.start()
    }

    fun destroy() {
        correctSound.stop()
        correctSound.release()
        incorrectSound.stop()
        incorrectSound.release()
    }
}