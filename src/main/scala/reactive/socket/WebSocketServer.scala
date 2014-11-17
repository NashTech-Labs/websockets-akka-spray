package reactive.socket

import akka.actor.ActorRef
import java.net.InetSocketAddress
import org.java_websocket.WebSocket
import org.java_websocket.framing.CloseFrame
import org.java_websocket.server.WebSocketServer
import org.java_websocket.handshake.ClientHandshake
import scala.collection.mutable.Map

object AppWebSocketServer {
  sealed trait AppWebSocketServerMessage
  case class Message(ws: WebSocket, msg: String)
    extends AppWebSocketServerMessage
  case class Open(ws: WebSocket, hs: ClientHandshake)
    extends AppWebSocketServerMessage
  case class Close(ws: WebSocket, code: Int, reason: String, external: Boolean)
    extends AppWebSocketServerMessage
  case class Error(ws: WebSocket, ex: Exception)
    extends AppWebSocketServerMessage
}

/**
 * Main WebSocket Server
 */
class AppWebSocketServer(val port: Int) extends WebSocketServer(new InetSocketAddress(port)) {
  private val webSocketConnections = Map[String, ActorRef]()

  final def forResource(descriptor: String, reactor: Option[ActorRef]) {
    reactor match {
      case Some(actor) => webSocketConnections += ((descriptor, actor))
      case None => webSocketConnections -= descriptor
    }
  }
  final override def onMessage(ws: WebSocket, msg: String) {
    if (null != ws) {
      webSocketConnections.get(ws.getResourceDescriptor) match {
        case Some(actor) => actor ! AppWebSocketServer.Message(ws, msg)
        case None => ws.close(CloseFrame.REFUSE)
      }
    }
  }
  final override def onOpen(ws: WebSocket, hs: ClientHandshake) {
    if (null != ws) {
      webSocketConnections.get(ws.getResourceDescriptor) match {
        case Some(actor) => actor ! AppWebSocketServer.Open(ws, hs)
        case None => ws.close(CloseFrame.REFUSE)
      }
    }
  }
  final override def onClose(ws: WebSocket, code: Int, reason: String, external: Boolean) {
    if (null != ws) {
      webSocketConnections.get(ws.getResourceDescriptor) match {
        case Some(actor) => actor ! AppWebSocketServer.Close(ws, code, reason, external)
        case None => ws.close(CloseFrame.REFUSE)
      }
    }
  }
  final override def onError(ws: WebSocket, ex: Exception) {
    if (null != ws) {
      webSocketConnections.get(ws.getResourceDescriptor) match {
        case Some(actor) => actor ! AppWebSocketServer.Error(ws, ex)
        case None => ws.close(CloseFrame.REFUSE)
      }
    }
  }
}
