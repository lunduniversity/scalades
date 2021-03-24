package scalades

/** Write text to a log file and to the console. */
open class Logger(
  val logFile: String = "scalades.log", 
  var isDebug: Boolean = false, 
  enc: String = "UTF-8"
):
  import java.nio.file.StandardOpenOption.{APPEND, CREATE}
  import java.nio.file.{Path, Paths, Files}
  import scala.jdk.CollectionConverters.*

  protected val charSet = java.nio.charset.Charset.forName(enc)
  protected val logFilePath: Path = Paths.get(logFile) 
  
  /** Prefix in `log`. Default is `java.util.Date`. Override to customize.*/
  def logPrefix(): String = java.util.Date().toString + " | "

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
    
  /** Append `lines` of text to file with path `p`.*/
  def append(p: Path, lines: String*): Unit = 
    Files.write(p, lines.asJava, charSet, CREATE, APPEND)

  /** The message logged by the default clearLog. */
  def clearLogMessage: String = s"CLEAR LOG $logFile"
  
  /** Delete the `logFile` if exists and log `clearLogMessage`. **/
  final def clearLog(): Unit = 
    Files.deleteIfExists(logFilePath)
    log(clearLogMessage)
