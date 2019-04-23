/**
 *
 */
package org.xvolks.dofus.util

import org.apache.log4j.Logger
import org.xvolks.dofus.util.apache.commons.lang.StringEscapeUtils
import java.io.*
import java.util.*
import java.util.concurrent.Semaphore
import java.util.regex.Pattern


/**
 * @author xvolks - 12 oct. 06 $Id: ProcessRunner.java,v 1.1
 * 2006/10/12 11:00:16 xvolks Exp $
 */
object ProcessRunner {
    private val logger = Logger.getLogger(ProcessRunner::class.java!!)


    private val osName = System.getProperty("os.name").toLowerCase()

    internal var quoteSearcher = Pattern.compile("(?:\"([^\"]*)\")|(?:([^\\s]*))").matcher("")

    class ProcessOutput {
        /**
         * @return
         */
        var retVal: Int = 0
            internal set
        /**
         * @return the stdout
         */
        var stdout: List<String>? = null
            internal set
        /**
         * @return the stderr
         */
        var stderr: List<String>? = null
            internal set

        companion object {

            @Throws(IOException::class)
            internal fun byteArrayOutputStreamToList(bos: ByteArrayOutputStream): List<String> {
                var br: BufferedReader? = null
                try {
                    val list = ArrayList<String>()
                    br = BufferedReader(InputStreamReader(ByteArrayInputStream(bos.toByteArray())))
                    while (true) {
                        val line = br.readLine() ?: break
                        list.add(line)
                    }
                    return list
                } catch (e: IOException) {
                    e.printStackTrace()
                    throw e
                } finally {
                    if (br != null) {
                        try {
                            br.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }
                }
            }
        }
    }

    /**
     * For inheritance
     * @param name
     */
    open abstract class OutputStreamConsumerThread(name: String) : Thread(name) {
        lateinit var outputStream: OutputStream

        init {
            isDaemon = true
        }

        /* (non-Javadoc)
         * @see java.lang.Object#finalize()
         */
        @Throws(Throwable::class)
        protected fun finalize() {
            try {
                outputStream.close()
            } catch (e: Exception) {
            }

        }

        abstract override fun run()
    }

    open class InputStreamConsumerThread : Thread {

        var inputStream: InputStream? = null
            private set
        /**
         * @return
         */
        val outputStream: ByteArrayOutputStream?

        constructor(group: ThreadGroup?, name: String, `in`: InputStream?) : super(group, name) {
            isDaemon = true
            if (`in` == null) {
                throw IllegalArgumentException("InputStream argument cannot be null")
            }
            inputStream = `in`
            outputStream = ByteArrayOutputStream()
        }

        constructor(name: String, `in`: InputStream) : this(null, name, `in`) {}
        /**
         * For inheritance
         * @param name
         */
        constructor(name: String) : super(name) {
            isDaemon = true
            outputStream = ByteArrayOutputStream()
        }

        /**
         * Surcharger la methode run pour pouvoir lire les entrées, filtrer etc !
         */
        override fun run() {
            basicStreamCopy()
        }

        protected open fun bufferedCopy() {
            bufferedCopy(inputStream, outputStream)
        }

        protected open fun bufferedCopy(inputStream: InputStream?, outputStream: OutputStream?, closeOut: Boolean = true) {
            if (closeOut) {
                outputStream?.bufferedWriter().use { wr ->
                    inputStream?.bufferedReader()?.forEachLine { ln ->
                        wr?.write(ln)
                    }
                }
            } else {
                val wr = outputStream?.bufferedWriter()
                inputStream?.bufferedReader()?.forEachLine { ln ->
                    wr?.write(ln)
                }
                wr?.flush()
            }
        }

        /**
         *
         */
        protected fun basicStreamCopy() {
            bufferedCopy()
//            val buf = ByteArray(512)
//            var count = 0
//            try {
//                while ((count = inputStream!!.read(buf)) != -1) {
//                    outputStream?.write(buf, 0, count)
//                }
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }

        }

        fun setIn(`in`: InputStream) {
            inputStream = `in`
        }

        /* (non-Javadoc)
         * @see java.lang.Object#finalize()
         */
        @Throws(Throwable::class)
        protected fun finalize() {
            try {
                inputStream!!.close()
            } catch (e: Exception) {
            }

        }

    }

    @Throws(IOException::class, InterruptedException::class)
    @JvmOverloads
    fun runAndWait(cmd: String, connectStreams: Boolean = true): Int {
        val p = Runtime.getRuntime().exec(splitCommandLine(cmd))
        InputStreamConsumerThread(null, cmd, p.inputStream).start()
        InputStreamConsumerThread(null, cmd, p.errorStream).start()
        return p.waitFor()
    }

    @Throws(IOException::class, InterruptedException::class)
    @JvmOverloads
    fun runAndGetAnswer(cmd: String, out: InputStreamConsumerThread? = InputStreamConsumerThread(cmd), err: InputStreamConsumerThread? = InputStreamConsumerThread(cmd), `in`: OutputStreamConsumerThread? = null): ProcessOutput {
        var out = out
        var err = err
        var `in` = `in`
        val shell = splitCommandLine(cmd)
        for (s in shell) {
            logger.debug(s)
        }

        val p = Runtime.getRuntime().exec(shell)
        if (out == null) {
            out = InputStreamConsumerThread("dummy_out")
        }
        out.setIn(p.inputStream)
        out.start()
        if (err == null) {
            err = InputStreamConsumerThread("dummy_err")
        }
        err.setIn(p.errorStream)
        err.start()
        val input = `in` ?: object : OutputStreamConsumerThread("dummy_out") {
                override fun run() {
                    //Do nothing here
                }
            }
        input.outputStream = p.outputStream
        input.start()

        val output = ProcessOutput()

        output.retVal = p.waitFor()
        out.join(5000)
        err.join(5000)
        input.join(5000)

        output.stdout = ProcessOutput.byteArrayOutputStreamToList(out.outputStream!!)
        output.stderr = ProcessOutput.byteArrayOutputStreamToList(err.outputStream!!)
        return output
    }

    @JvmStatic
    fun main(args: Array<String>) {
        //	    String cmd = "Pouet \"Toto was here\" �a veut dire \"Toto �tait l�\" Tutut";
        //	    String cmd = "\"Pouet Toto was here\" �a veut dire Toto �tait l� Tutut";
        val cmd = "C:\\temp\\mysql-mxj\\bin\\mysql.exe -uroot < C:\\temp\\mysql-mxj\\data\\create.sql"
        val ret = unQuoteString(cmd)
        for (s in ret) {
            logger.debug(s)
        }
    }

    /**
     * @param cmd
     * @return
     */
    private fun unQuoteString(cmd: String): List<String> {
        var begin = 0
        logger.debug("Commande �$cmd�")
        val ret = ArrayList<String>()
        quoteSearcher.reset(cmd)
        while (begin > -1) {
            if (begin > -1) {
                //				System.err.println("begin "+ begin);
                if (quoteSearcher.find()) {
                    for (i in 0 until quoteSearcher.groupCount()) {
                        //						int start0 = quoteSearcher.start(i);
                        val start = quoteSearcher.start(i + 1)
                        //						System.err.println(start0 +" <> "+start);
                        val end = quoteSearcher.end(i + 1)
                        if (start < end) {
                            ret.add(cmd.substring(start, end))
                        }
                        begin = end + 1
                    }

                } else {
                    begin = -1
                }
            } else {
                begin = -1
            }
        }
        return ret
    }

    /**
     * @param cmd
     * @return
     */
    private fun splitCommandLine(cmd: String): Array<String?> {
        val shell: Array<String?>
        if (osName.indexOf("linux") != -1) {
            shell = arrayOf("/bin/sh", "-c", StringEscapeUtils.escapeJava(cmd, charArrayOf('$')))
            logger.debug(shell[0] + " " + shell[1] + " " + shell[2])
        } else if (osName.indexOf("windows 9") != -1 || osName.indexOf("windows me") != -1) {
            shell = commandToArray(cmd, "command.exe", "/C")
        } else if (osName.indexOf("windows") != -1) {
            shell = commandToArray(cmd, "cmd.exe", "/C")
        } else {
            shell = arrayOf(cmd)
            logger.error("OS non g�r� ($osName) : $cmd")
        }
        return shell
    }

    /**
     * @param cmd
     * @return
     */
    private fun commandToArray(cmd: String, vararg prefixes: String): Array<String?> {
        val shell: Array<String?>
        val unQuoteString = unQuoteString(cmd)

        shell = arrayOfNulls(prefixes.size + 1)
        var i = 0
        while (i < prefixes.size) {
            shell[i] = prefixes[i]
            i++
        }

        var quotedCMD = "\""
        for (s in unQuoteString) {
            val ss =
            if (s.indexOf(' ') > -1) {
                "\"" + s + "\""
            } else {
                s
            }
            quotedCMD += "$ss "
        }
        if (quotedCMD.endsWith(" "))
            quotedCMD = quotedCMD.substring(0, quotedCMD.length - 1)
        quotedCMD += "\""
        shell[i] = quotedCMD
        return shell
    }

    private class LineOrderedInputStreamConverter(private val i: InputStream) : Thread() {
        val orderedOut: LinkedHashMap<Long, String> = LinkedHashMap()
        private val lock = Semaphore(0)

        override fun run() {
            lock.release()
            i.bufferedReader().forEachLine { line ->
                orderedOut[System.nanoTime()] = line
            }
        }

        @Synchronized
        override fun start() {
            super.start()
            lock.acquire()
        }
    }

    class ProcessOutputOrdered(val retVal: Int, val stdout: LinkedHashMap<Long, String>, val stderr: LinkedHashMap<Long, String>) : Serializable {
        companion object {
            /**
             *
             */
            private const val serialVersionUID = -5238168025843816066L
        }
    }

    /**
     * Conserve l'ordre chronologique des �v�nements en fournissant un objet de type ProcessOutputOrdered
     * @param cmd
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Throws(IOException::class, InterruptedException::class)
    fun runAndGetAnswerOrdered(cmd: String): ProcessOutputOrdered {
        val splitCommandLine = splitCommandLine(cmd)
        val p = Runtime.getRuntime().exec(splitCommandLine)

        val inConv = LineOrderedInputStreamConverter(p.inputStream)
        val errConv = LineOrderedInputStreamConverter(p.errorStream)

        inConv.start()
        errConv.start()

        val retVal = p.waitFor()
        inConv.join()
        errConv.join()

        println(inConv.orderedOut)
        System.err.println(errConv.orderedOut)
        return ProcessOutputOrdered(retVal, inConv.orderedOut, errConv.orderedOut)
    }


    @Throws(IOException::class, InterruptedException::class)
    fun runAndForget(cmd: String, connectStreams: Boolean) {
        val p = Runtime.getRuntime().exec(splitCommandLine(cmd))
        object : InputStreamConsumerThread(null, cmd, p.inputStream) {
            override fun run() {
                bufferedCopy(inputStream, System.out, closeOut = false)
            }
        }.start()


        object : InputStreamConsumerThread(null, cmd, p.errorStream) {
            override fun run() {
                bufferedCopy(inputStream, System.err, closeOut = false)
            }
        }.start()
    }
}
