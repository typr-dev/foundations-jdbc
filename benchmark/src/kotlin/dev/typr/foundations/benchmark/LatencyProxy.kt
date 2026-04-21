package dev.typr.foundations.benchmark

import java.io.InputStream
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A simple TCP proxy that adds configurable one-way latency to simulate network delay.
 * Each byte transferred is delayed by [latencyMs] in each direction, giving a full RTT
 * of 2 * [latencyMs].
 */
class LatencyProxy(
    private val listenPort: Int,
    private val targetHost: String,
    private val targetPort: Int,
    private val latencyMs: Long,
) : AutoCloseable {

    private val running = AtomicBoolean(true)
    private val serverSocket = ServerSocket(listenPort)
    private val threads = mutableListOf<Thread>()

    fun start() {
        val acceptThread = Thread.ofPlatform().daemon().name("proxy-accept").start {
            while (running.get()) {
                try {
                    val client = serverSocket.accept()
                    handleConnection(client)
                } catch (_: Exception) {
                    if (!running.get()) return@start
                }
            }
        }
        threads.add(acceptThread)
    }

    private fun handleConnection(client: Socket) {
        val upstream = Socket(targetHost, targetPort)
        upstream.tcpNoDelay = true
        client.tcpNoDelay = true

        val t1 = Thread.ofPlatform().daemon().name("proxy-c2s").start {
            relay(client.inputStream, upstream.outputStream)
        }
        val t2 = Thread.ofPlatform().daemon().name("proxy-s2c").start {
            relay(upstream.inputStream, client.outputStream)
        }
        threads.add(t1)
        threads.add(t2)
    }

    private fun relay(src: InputStream, dst: OutputStream) {
        val buf = ByteArray(8192)
        try {
            while (running.get()) {
                val n = src.read(buf)
                if (n < 0) break
                if (latencyMs > 0) Thread.sleep(latencyMs)
                dst.write(buf, 0, n)
                dst.flush()
            }
        } catch (_: Exception) {
        }
    }

    override fun close() {
        running.set(false)
        serverSocket.close()
        threads.forEach { it.interrupt() }
    }
}
