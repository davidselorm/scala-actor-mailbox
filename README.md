# scala-actor-mailbox

High-performance lock-free concurrent actor mailbox with priority queues and dead-letter routing in Scala.

## Architecture
- **Dual-Queue Priority Scheduling**: Control and system messages preempt standard message queues.
- **Lock-Free Concurrency**: Leverages CAS `ConcurrentLinkedQueue` for maximum multi-threaded throughput.
- **Dead-Letter Routing**: Captures undeliverable envelopes with timestamp metadata.
