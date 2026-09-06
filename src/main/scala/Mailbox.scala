package devpulse.actor
import java.util.concurrent.ConcurrentLinkedQueue

class Mailbox[M]:
  private val queue = new ConcurrentLinkedQueue[M]()
  def enqueue(msg: M): Unit = queue.offer(msg)
  def dequeue(): Option[M] = Option(queue.poll())
  def isEmpty: Boolean = queue.isEmpty
