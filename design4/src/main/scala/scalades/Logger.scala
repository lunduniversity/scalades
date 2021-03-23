package scalades

open class Logger(val logFile: String = "scalades.log", var isDebug: Boolean = true, enc: String = "UTF-8"):
  import java.nio.file.StandardOpenOption.{APPEND, CREATE}
  import java.nio.file.{Path, Paths, Files}
  import scala.jdk.CollectionConverters.*

  val charSet = java.nio.charset.Charset.forName(enc)
  val logFilePath: Path = Paths.get(logFile) 

  def logPrefix(): String = java.util.Date().toString + " | "
  val clearLogMessage: String = s"CLEAR LOG $logFile"
  
  /** Append `lines` of text to file with path `p`.*/
  def append(p: Path, lines: String*): Unit = Files.write(p, lines.asJava, charSet, CREATE, APPEND)
  
  /** Delete the `logFile` if exists and log `clearLogMessage`. **/
  def clearLog(): Unit = 
    Files.deleteIfExists(logFilePath)
    log(clearLogMessage)

  /** Append `s` to `logFile` prefixed with `logPrefix()` */
  def log(s: String): Unit = append(logFilePath, logPrefix() + s)

  /** Print `s` to console */
  def show(s: String): Unit = println(s) 
  
  /** Print debug message`s` to console if `isDebug` is true */
  def dbg(s: String): Unit = if isDebug then show("*** DEBUG: " + s) 
  
  /** Print error message `s` to console and exit with error. */
  def err(s: String): Unit = 
    show("*** ERROR: " + s)
    System.exit(1) 
