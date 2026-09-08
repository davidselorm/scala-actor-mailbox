package com.cluster.mailbox

import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger

sealed trait Envelope
case class PriorityMessage(id: String, payload: Any) extends Envelope
case class StandardMessage(id: String, payload: Any) extends Envelope

class ActorMailbox(val capacity: Int = 10000) {
  private val priorityQueue = new ConcurrentLinkedQueue[PriorityMessage]()
  private val standardQueue = new ConcurrentLinkedQueue[StandardMessage]()
  private val sizeCounter = new AtomicInteger(0)

  def enqueue(msg: Envelope): Boolean = {
    if (sizeCounter.get() >= capacity) {
      return false // Backpressure / drop
    }

    msg match {
      case p: PriorityMessage => priorityQueue.offer(p)
      case s: StandardMessage => standardQueue.offer(s)
    }
    sizeCounter.incrementAndGet()
    true
  }

  def dequeue(): Option[Envelope] = {
    // 1. Process priority control frames first
    val prio = priorityQueue.poll()
    if (prio != null) {
      sizeCounter.decrementAndGet()
      return Some(prio)
    }

    // 2. Process standard payload envelopes
    val std = standardQueue.poll()
    if (std != null) {
      sizeCounter.decrementAndGet()
      return Some(std)
    }

    None
  }

  def size: Int = sizeCounter.get()
  def isEmpty: Boolean = sizeCounter.get() == 0
}
