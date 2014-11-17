package com.knoldus.hub

import org.java_websocket.handshake.ServerHandshake
import org.java_websocket.client.WebSocketClient
import org.scalatest.FunSuite
import reactive.socket.AppWebSocketServer
import reactive.socket.BootWebSocketService
import com.knoldus.util.ActorHelper
import java.net.URI
import reactive.socket.DashBoardActor
import akka.actor.Props

class HubWebSocketsTest extends FunSuite with ActorHelper {
  test("Websocket connection") {
    implicit val system = hubActorSystem

    // =======
    // Server
    // =======
    val dashboardActor = system.actorOf(Props[DashBoardActor])
    val budgetWebSocketServer = new AppWebSocketServer(Configuration.portWs)
    budgetWebSocketServer.forResource("/dashboard/ws", Some(dashboardActor))
    budgetWebSocketServer.forResource("/flashboard/ws", Some(dashboardActor))
    budgetWebSocketServer.start

    // ==========
    // Client One
    // ==========

    var websocketMessage = ""
    // This could be your AngularJS or Dart Client making a websocket connection with the server
    val webSocketDashboard = new WebSocketClient(URI.create(s"ws://localhost:${Configuration.portWs}/flashboard/ws")) {
      override def onMessage(msg: String) {
        websocketMessage = msg
      }
      override def onOpen(hs: ServerHandshake) {}
      override def onClose(code: Int, reason: String, intentional: Boolean) {}
      override def onError(ex: Exception) { println(ex.getMessage) }
    }
    webSocketDashboard.connect
    Thread.sleep(1000L)

    // ==========
    // Client Two
    // ==========

    var websocketMessage2 = ""
    // This could be your AngularJS or Dart Client making a websocket connection with the server
    val webSocketDashboard2 = new WebSocketClient(URI.create(s"ws://localhost:${Configuration.portWs}/flashboard/ws")) {
      override def onMessage(msg: String) {
        websocketMessage2 = msg
      }
      override def onOpen(hs: ServerHandshake) {}
      override def onClose(code: Int, reason: String, intentional: Boolean) {}
      override def onError(ex: Exception) { println(ex.getMessage) }
    }
    webSocketDashboard2.connect
    Thread.sleep(1000L) 
    
    // ==========
    // Client Three
    // ==========

    var websocketMessage3 = ""
    // This could be your AngularJS or Dart Client making a websocket connection with the server
    val webSocketDashboard3 = new WebSocketClient(URI.create(s"ws://localhost:${Configuration.portWs}/flashboard/ws")) {
      override def onMessage(msg: String) {
        websocketMessage3 = msg
      }
      override def onOpen(hs: ServerHandshake) {}
      override def onClose(code: Int, reason: String, intentional: Boolean) {}
      override def onError(ex: Exception) { println(ex.getMessage) }
    }
    webSocketDashboard3.connect
    Thread.sleep(1000L)

    // =======================
    // Websocket communication
    // =======================
    webSocketDashboard.send("Hello")
    Thread.sleep(1000L)
    // Assert that both the clients are getting notified of the changes
    assert(websocketMessage === "Hello")
    assert(websocketMessage2 === "Hello")
    assert(websocketMessage3 === "Hello")
  }
}


