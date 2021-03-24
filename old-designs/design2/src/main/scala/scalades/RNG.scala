package scalades

/** Random Number Generator (RNG) */
object RNG:
  /** Underlying thread-safe random number generator from Java JDK. */
  val underlying = java.util.concurrent.ThreadLocalRandom.current

  /** Random number with exponential distribution. 
   * 
   * Note on implementation: The general random transformation is 
   * `math.log(1 - underlying.nextDouble)/(-1 / mean)`
   * but as we know that underlying.nextDouble is between 0 and 1 we can
   * use the slightly faster `math.log(underlying.nextDouble)/(-1 / mean)`
   * which is equivalent from a statistical point of view.
  */
  def negExp(mean: Double) = math.log(underlying.nextDouble)/(-1 / mean)

  /** Random number with rectangular distribution. */
  def rect(from: Int, until: Int) = underlying.nextInt(from, until)

  /** If the seed is set then the pseudo-random sequence is repeatable. */
  def setSeed(seed: Long) = underlying.setSeed(seed)