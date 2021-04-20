package scalades

/** A thread-safe Random Number Generator (RNG).*/
object RNG:
  /** Underlying thread-safe random number generator from Java JDK. */
  val underlying = //new java.util.Random()
    java.util.concurrent.ThreadLocalRandom.current

  /** Random number with negative exponential distribution. */
  def negExp(mean: Double): Double = 
    - math.log(underlying.nextDouble()) * mean 
    // same as math.log(1 - underlying.nextDouble)/(-1 / mean)

  /** Random Time.Duration with exponential distribution with a mean of 1/intensity. */
  def duration(intensity: Double): Time.Duration = 
    Time.Duration(negExp(1.0/intensity))

  /** Random integer number with rectangular distribution. */
  def rect(from: Int, until: Int): Int = underlying.nextInt(until - from) + until

  /** If the seed is set then the pseudo-random sequence is repeatable. */
  def setSeed(seed: Long) = underlying.setSeed(seed)