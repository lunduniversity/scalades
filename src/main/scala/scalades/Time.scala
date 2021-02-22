package scalades

object Time:
  opaque type Stamp = Double
  opaque type Duration = Double
  
  def Stamp(units: Double): Time.Stamp = 
    if units >= 0.0 then units 
    else throw new Simulation.IllegalTimeException(s"negative timestamp $units")

  def Duration(units: Double): Time.Duration = 
    if units >= 0.0 then units 
    else throw new Simulation.IllegalTimeException(s"negative duration $units")
 
  def zero: Time.Stamp = 0.0
 
  extension (s: Stamp)
    def units: Double = s
    def +(duration: Duration): Stamp = s + duration   
    infix def diff(other: Stamp): Duration = math.abs(s - other)

  extension (d: Duration)
    def length: Double = d
