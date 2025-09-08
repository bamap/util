package ir.bamap.blu.util

import ir.bamap.blu.exception.BluException
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.security.InvalidParameterException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class FileUtil {
    companion object {

        @JvmStatic
        fun extractZipFile(zipFile: File, destinationDirectory: File) {
            val buffer = ByteArray(2014)
            val zipStream = ZipInputStream(FileInputStream(zipFile))
            var zipEntry = try {
                zipStream.nextEntry
            } catch (exception: RuntimeException) {
                throw BluException(500, "{blu.notSupportedCompress}")
            }

            while (zipEntry != null) {
                val zipEntryFile = extractZipEntry(zipEntry, destinationDirectory)
                var length: Int = zipStream.read(buffer)

                // It's directory
                if (length < 0) {
                    zipEntryFile.mkdirs()
                } else {
                    val fos = FileOutputStream(zipEntryFile)
                    while (length > 0) {
                        fos.write(buffer, 0, length)
                        length = zipStream.read(buffer)
                    }
                    fos.close()
                }
                zipEntry = zipStream.nextEntry
            }
            zipStream.closeEntry()
            zipStream.close()
        }

        @JvmStatic
        fun extractZipFile(zipFile: File, destinationDirectory: String) {
            val directory = File(destinationDirectory)
            directory.mkdirs()

            extractZipFile(zipFile, directory)
        }

        @JvmStatic
        fun extractZipEntry(zipEntry: ZipEntry, destinationDirectory: File): File {
            val destFile = File(destinationDirectory, zipEntry.name)

            val desDirPath = destinationDirectory.canonicalPath
            val destFilePath = destFile.canonicalPath

            if (!destFilePath.startsWith("$desDirPath${File.separator}"))
                throw IOException("Entry is outside of the target dir: ${zipEntry.name}")

            return destFile
        }

        @JvmStatic
        fun getFileType(fileName: String): String {
            val index = fileName.lastIndexOf('.')
            return fileName.substring(index + 1, fileName.length)
        }

        @JvmStatic
        fun renameAllFolderDirectories(folderPath: String, newName: String) {
            val directory = File("$folderPath")
            if (!directory.exists())
                return

            if (directory.isFile)
                throw InvalidParameterException("$folderPath must be folder")

            val directories = directory.listFiles() ?: return

            directories.forEach {
                val renameFile = File("$folderPath/$newName.${it.extension}")
                it.renameTo(renameFile)

                if (it.isDirectory)
                    renameAllFolderDirectories(it.path, newName)
            }
        }

        @JvmStatic
        fun getTempDirectory(vararg directory: String): String {
            val baseTempPath = System.getProperty("java.io.tmpdir")
            if(directory.isEmpty())
                return baseTempPath

            return baseTempPath + directory.joinToString(File.separator, File.separator)
        }
    }
}
