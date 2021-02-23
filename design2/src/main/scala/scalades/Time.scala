package scalades

object Time:
  opaque type Stamp = Double
  opaque type Duration = Double

  final class IllegalTimeException(msg: String)  extends Exception(msg)
  
  def Stamp(units: Double): Time.Stamp = 
    if units >= 0.0 then units 
    else throw new IllegalTimeException(s"negative timestamp $units")

  def Duration(units: Double): Time.Duration = 
    if units >= 0.0 then units 
    else throw new IllegalTimeException(s"negative duration $units")
 
  def init: Time.Stamp = 0.0
  def noDelay: Time.Duration = 0.0
 
  extension (s: Stamp)
    def units: Double = s
    def +(duration: Duration): Stamp = s + duration   
    def -(other: Stamp): Double = s - other   
    infix def dist(other: Stamp): Duration = math.abs(s - other)

  extension (d: Duration)
    def length: Double = d
