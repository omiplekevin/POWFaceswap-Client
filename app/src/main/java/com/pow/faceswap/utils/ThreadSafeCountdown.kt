package com.pow.faceswap.utils

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicBoolean

class ThreadSafeCountdown(
	initialCount: Int,
	private val onTick: (Int) -> Unit,
	private val onComplete: () -> Unit
) {
	private val initialCount = initialCount
	private val count = AtomicInteger(initialCount)
	private val isRunning = AtomicBoolean(false)
	private var thread: Thread? = null
	
	@Synchronized
	fun start() {
		if (isRunning.compareAndSet(false, true)) {
			thread = Thread {
				try {
					while (count.get() > 0 && isRunning.get()) {
						onTick(count.get())
						Thread.sleep(1000)
						count.decrementAndGet()
					}
					if (isRunning.get()) {
						onComplete()
						isRunning.set(false)
					}
				} catch (e: InterruptedException) {
					// Thread was interrupted, isRunning already set to false in stop()
				} catch (e: Exception) {
					isRunning.set(false)
					// Optional: Log the exception
				}
			}
			thread?.start()
		}
	}
	
	@Synchronized
	fun stop() {
		if (isRunning.compareAndSet(true, false)) {
			thread?.interrupt()
			thread = null
		}
	}
	
	@Synchronized
	fun restart() {
		stop() // Stop any existing countdown
		count.set(initialCount) // Reset to initial count
		start() // Start new countdown
	}
	
	fun getCurrentCount(): Int = count.get()
	
	fun isRunning(): Boolean = isRunning.get()
}