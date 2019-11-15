package io.github.zwieback.familyfinance.util

import java.io.File
import java.io.IOException

object FileUtils {

    /**
     * Creates the specified [destinationFile] as a byte for byte copy of the
     * [sourceFile]. If [destinationFile] already exists, then it will be
     * replaced with a copy of [sourceFile].
     *
     * Note: [sourceFile] and [destinationFile] will be closed by this method.
     *
     * @param sourceFile      file to copy from
     * @param destinationFile file to copy to
     */
    @Throws(IOException::class)
    fun copyFile(sourceFile: File, destinationFile: File) {
        sourceFile.copyTo(destinationFile, overwrite = true)
    }
}
