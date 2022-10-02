package com.rittmann.myapplication.main.scene

import android.graphics.Canvas
import android.util.Log
import android.view.MotionEvent
import com.rittmann.myapplication.main.components.Joystick
import com.rittmann.myapplication.main.entity.Collidable
import com.rittmann.myapplication.main.entity.Player
import com.rittmann.myapplication.main.entity.Position
import com.rittmann.myapplication.main.entity.server.PlayerMovementWrapResult
import com.rittmann.myapplication.main.entity.verifyCollisions
import com.rittmann.myapplication.main.match.MatchEvents
import com.rittmann.myapplication.main.match.screen.GLOBAL_TAG
import com.rittmann.myapplication.main.utils.INVALID_ID
import com.rittmann.myapplication.main.utils.Logger

class SceneMain(
    private val matchEvents: MatchEvents,
) : Scene, Logger {
    private var player: Player? = null
    private var enemies: ArrayList<Player> = arrayListOf()

    private var joystickMovement: Joystick = Joystick()
    private var joystickAim: Joystick = Joystick()

    private val collidables: ArrayList<Collidable> = arrayListOf()

    override fun update() {
        player?.also { player ->
            if (joystickMovement.isWorking) {
                player.move(
                    joystickMovement.angle,
                    joystickMovement.strength,
                )
            }

            if (joystickAim.isWorking) {
                player.aim(
                    joystickAim.angle,
                )

                // create a const
                if (joystickAim.strength > 80f) {
                    player.shot()?.also { bullet ->
                        matchEvents.shoot(player, bullet)
                    }
                }
            }

            player.update()

            enemies.forEach {
                it.update()
            }

            collidables.verifyCollisions()
        }
    }

    override fun draw(canvas: Canvas) {
//        canvas.drawColor(StardardColors.INSTANCE.getBackground());
        player?.draw(canvas)
        enemies.forEach { it.draw(canvas) }
    }

    override fun terminate() {}

    override fun receiveTouch(motionEvent: MotionEvent) {

    }

    override fun ownPlayerCreated(player: Player) {
        this.player = player

        collidables.add(player)
    }

    override fun newPlayerConnected(player: Player) {
        enemies.add(player)

        collidables.add(player)
    }

    override fun playerDisconnected(id: String) {
        val index = enemies.indexOfFirst { it.playerId == id }

        if (index != INVALID_ID) {
            enemies.removeAt(index)
        }
    }

    override fun onJoystickMovementChanged(angle: Double, strength: Double) {
        joystickMovement.set(angle, strength)
    }

    override fun onJoystickAimChanged(angle: Double, strength: Double) {
        joystickAim.set(angle, strength)
    }

    override fun getPlayerPosition(): Position {
        return player?.position ?: Position()
    }

    override fun playerMovement(playerMovementWrapResult: PlayerMovementWrapResult) {
        val movedEnemy = enemies.firstOrNull { it.playerId == playerMovementWrapResult.id }

        movedEnemy?.keepTheNextPlayerMovement(playerMovementWrapResult)
    }

    init {
        Log.i(GLOBAL_TAG, "Cena principal criada")
    }
}