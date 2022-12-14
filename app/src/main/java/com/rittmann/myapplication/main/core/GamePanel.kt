package com.rittmann.myapplication.main.core

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.rittmann.myapplication.main.draw.DrawObject
import com.rittmann.myapplication.main.entity.Player
import com.rittmann.myapplication.main.match.MatchEvents
import com.rittmann.myapplication.main.scene.SceneManager
import com.rittmann.myapplication.main.entity.server.WorldState
import com.rittmann.myapplication.main.server.ConnectionControlListeners

class GamePanel(
    context: Context,
) : SurfaceView(context), DrawObject, SurfaceHolder.Callback {

    private var gameMainThread: GameMainThread? = null
    private lateinit var sceneManager: SceneManager

    init {
        holder.addCallback(this)
        isFocusable = true
    }

    fun build(matchEvents: MatchEvents): GamePanel {
        sceneManager = SceneManager(matchEvents)
        return this
    }

    override fun update(deltaTime: Double) {
        sceneManager.update(deltaTime)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        sceneManager.draw(canvas)
    }

    override fun free() {}

    override fun surfaceCreated(holder: SurfaceHolder) {
        gameMainThread = GameMainThread(getHolder(), this)
        gameMainThread?.setRunning(true)
        gameMainThread?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        try {
            gameMainThread?.setRunning(false)
            gameMainThread?.join()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        sceneManager.receiveTouch(event)
        return true
    }

    fun onGameStarted(tick: Int) {
        sceneManager.onGameStarted(tick)
    }

    fun ownPlayerCreated(player: ConnectionControlListeners.NewPlayerConnected) {
        sceneManager.ownPlayerCreated(player)
    }

    fun newPlayerConnected(player: ConnectionControlListeners.NewPlayerConnected) {
        sceneManager.newPlayerConnected(player)
    }

    fun onJoystickMovementChanged(angle: Double, strength: Double) {
        sceneManager.onJoystickMovementChanged(angle, strength)
    }

    fun onJoystickAimChanged(angle: Double, strength: Double) {
        sceneManager.onJoystickAimChanged(angle, strength)
    }

    fun getPlayer(): Player? {
        return sceneManager.getPlayer()
    }

    fun getEnemies(): List<Player> {
        return sceneManager.getEnemies()
    }

    fun playerDisconnected(id: String) {
        sceneManager.playerDisconnected(id)
    }

    fun onPlayerUpdate(worldState: List<WorldState>) {
        sceneManager.onPlayerUpdate(worldState)
    }

    fun onGameDisconnected() {
        sceneManager.onGameDisconnected()
    }
}