package reactive.socket

import reactive.socket.AppWebSocketServer._
import akka.actor.{ Actor, ActorLogging }
import scala.collection._
import org.java_websocket.WebSocket

object DashBoardActor {
  sealed trait FindMessage
  case object Clear extends FindMessage
  case class Unregister(ws: WebSocket) extends FindMessage
}

class DashBoardActor extends Actor {
  import DashBoardActor._

  val clients = mutable.ListBuffer[WebSocket]()

  def receive: Receive = {

    case Message(ws, msg) =>
      for (client <- clients) {
        client.send(msg)
      }
    case Open(ws, hs) =>
      clients += ws
      ws.send("Opened a new connection")
    case Close(ws, code, reason, ext) => self ! Unregister(ws)
    case Error(ws, ex) => self ! Unregister(ws)
    case Unregister(ws) => {
      if (null != ws) {
        clients -= ws
      }
    }
  }
}
