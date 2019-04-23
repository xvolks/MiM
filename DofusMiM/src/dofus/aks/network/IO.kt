package dofus.aks.network

import java.io.IOException

/**
 * This interface manages the input/output **socket**
 * @author xvolks
 * @since 1.1
 */
interface IO {

    @Throws(IOException::class)
    fun close()

    @Throws(IOException::class)
    fun read(): Int

    @Throws(IOException::class)
    fun send(sData: String, bWaiting: Boolean)

}
