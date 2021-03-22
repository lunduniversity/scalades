package scalades

object Time:
  import math.BigDecimal.double2bigDecimal
  type Underlying = BigDecimal // change to Double if performance is more important than precision
  opaque type Stamp = Underlying 
  opaque type Duration = Double

  final class IllegalTimeException(msg: String)  extends Exception(msg)
  
  def Stamp(units: Underlying): Time.Stamp = 
    if units >= 0.0 then units 
    else throw new IllegalTimeException(s"negative timestamp $units")

  def Duration(units: Double): Time.Duration = 
    if units >= 0.0 then units 
    else throw new IllegalTimeException(s"negative duration $units")
 
  val Zero: Time.Stamp = 0.0
  val NoDelay: Time.Duration = 0.0
 
  extension (s: Stamp)
    def units: Underlying = s
    def +(duration: Duration): Stamp = s + duration   
    def -(other: Stamp): Stamp = s - other   
    infix def dist(other: Stamp): Duration = math.abs((s - other).toDouble)

  extension (d: Duration)
    def length: Double = d
