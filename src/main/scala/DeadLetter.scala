package devpulse.actor

case class DeadLetter[M](msg: M, recipient: String, timestamp: Long)

object DeadLetterPublisher:
  private var listeners: List[DeadLetter[?] => Unit] = Nil
  def subscribe(listener: DeadLetter[?] => Unit): Unit = listeners = listener :: listeners
  def publish(dl: DeadLetter[?]): Unit = listeners.foreach(_(dl))
