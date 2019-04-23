package org.xvolks.dofus.util.swf

import java.io.File
import java.io.IOException

import org.xvolks.dofus.util.ProcessRunner
import org.xvolks.dofus.util.ProcessRunner.ProcessOutput

object SWFUtils {
    @Throws(IOException::class, InterruptedException::class)
    fun decompileSWFToList(file: File): List<String>? {
        var exec = "swfdump"
        if (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1) {
            exec = "win32\\$exec"
        }
        val swfDump = File(exec)
        val output = ProcessRunner.runAndGetAnswer(swfDump.canonicalPath + " -D " + file.absolutePath)
        if (output.retVal != 0) {
            throw IOException("swfdump n'a pas fonctionné.")
        }
        return output.stdout
    }
}
