package scalades

/** Random Number Generator (RNG) */
object RNG:
  /** Underlying JDK thread-safe random number generator. */
  val underlying = java.util.concurrent.ThreadLocalRandom.current

  /** RNG with exponential distribution. */
  def negExp(mean: Double)  = math.log(1 - underlying.nextDouble)/(-1 / mean)

  /** RNG with rectangular distribution. */
  def rect(from: Int, until: Int) = underlying.nextInt(from, until)

  /** Set the seed for repeatable pseudo-random sequence. */
  def setSeed(seed: Long) = underlying.setSeed(seed)