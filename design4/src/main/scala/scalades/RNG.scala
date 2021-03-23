package scalades

/** Random Number Generator (RNG) */
object RNG:
  /** Underlying thread-safe random number generator from Java JDK. */
  val underlying = java.util.concurrent.ThreadLocalRandom.current

  /** Random number with negative exponential distribution. */
  inline def negExp(mean: Double): Double = 
    math.log(underlying.nextDouble)/(-1 / mean) 
    // same as math.log(1 - underlying.nextDouble)/(-1 / mean)

  /** Random number with Poisson distribution. */
  inline def poisson(intensity: Double): Double = RNG.negExp(1.0/intensity)

  /** Random Time.Duration with Poisson distribution. */
  inline def duration(intensity: Double): Time.Duration = Time.Duration(poisson(intensity))

  /** Random integer number with rectangular distribution. */
  inline def rect(from: Int, until: Int): Int = underlying.nextInt(from, until)

  /** If the seed is set then the pseudo-random sequence is repeatable. */
  inline def setSeed(seed: Long) = underlying.setSeed(seed)