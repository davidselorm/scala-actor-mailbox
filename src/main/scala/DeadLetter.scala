package com.cluster.mailbox

import java.util.concurrent.ConcurrentLinkedQueue

case class DeadLetter(envelope: Envelope, recipient: String, reason: String, timestamp: Long)

object DeadLetterQueue {
  private val queue = new ConcurrentLinkedQueue[DeadLetter]()

  def publish(msg: Envelope, recipient: String, reason: String): Unit = {
    queue.offer(DeadLetter(msg, recipient, reason, System.currentTimeMillis()))
  }

  def count: Int = queue.size()
  def drain(): List[DeadLetter] = {
    var list = List.empty[DeadLetter]
    var item = queue.poll()
    while (item != null) {
      list = item :: list
      item = queue.poll()
    }
    list.reverse
  }
}
