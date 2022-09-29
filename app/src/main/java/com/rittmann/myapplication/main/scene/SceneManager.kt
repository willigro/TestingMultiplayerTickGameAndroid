package com.rittmann.myapplication.main.scene

import android.graphics.Canvas
import android.view.MotionEvent
import com.rittmann.myapplication.main.entity.Player
import com.rittmann.myapplication.main.entity.Position
import com.rittmann.myapplication.main.entity.server.PlayerMovementResult

class SceneManager {
    private val scene: Scene

    fun receiveTouch(motionEvent: MotionEvent) {
        scene.receiveTouch(motionEvent)
    }

    fun update() {
        scene.update()
    }

    fun draw(canvas: Canvas) {
        scene.draw(canvas)
    }

    fun ownPlayerCreated(player: Player) {
        scene.ownPlayerCreated(player)
    }

    fun newPlayerConnected(player: Player) {
        scene.newPlayerConnected(player)
    }

    fun setJoystickLeftValues(angle: Double, strength: Double) {
        scene.setJoystickLeftValues(angle, strength)
    }

    fun getPlayerPosition(): Position {
        return scene.getPlayerPosition()
    }

    fun playerMovement(playerMovementResult: PlayerMovementResult) {
        scene.playerMovement(playerMovementResult)
    }

    init {
        scene = SceneMain()
    }
}