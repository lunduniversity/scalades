using DataStructures

mutable struct Signal
    typeOfEvent::Int
    timeOfEvent::Float64
    destination::Any
end

mutable struct Queue
    numberOfCustomers::Int
    accumulated::Int
    noMeasurements::Int
end

mutable struct Generator
    sendTo::Queue
end

function treatSignal(x::Queue, m::Signal)
    if m.typeOfEvent == ARRIVAL
        if x.numberOfCustomers == 0
            send(READY, time - log(rand()), x)
            # send(READY, time + 1.0, x)
        end
        x.numberOfCustomers = x.numberOfCustomers + 1
    elseif m.typeOfEvent == READY
        x.numberOfCustomers = x.numberOfCustomers - 1
        if x.numberOfCustomers > 0
            send(READY, time - log(rand()), x)
        end
    elseif m.typeOfEvent == MEASURE
        x.accumulated = x.accumulated + x.numberOfCustomers
        x.noMeasurements = x.noMeasurements + 1
        send(MEASURE, time - 10.0*log(rand()), x)
    end
end

function treatSignal(x::Generator, m::Signal)
    if m.typeOfEvent == READY
        send(ARRIVAL, time, x.sendTo)
        send(READY, time - 1.25 * log(rand()), x)
    end
end

signalList = PriorityQueue()

function send(evType, evTime, dest)
    newEvent = Signal(evType, evTime, dest)
    signalList[newEvent] = evTime
end

function fetchSignal()
    return dequeue!(signalList)
end

time = 0.0
ARRIVAL = 1
READY = 2
MEASURE = 3

simulationTime = 12345678

q = Queue(0, 0, 0)
gen = Generator(q)

send(READY, 0, gen)
send(MEASURE, 0, q)

startTid = time_ns()


while time < simulationTime
    actSignal = fetchSignal()
    global time = actSignal.timeOfEvent
    treatSignal(actSignal.destination, actSignal)
end
println()
println("Mean: ", q.accumulated/q.noMeasurements)
println("Execution time: ", (time_ns() - startTid) / 1000000000, " seconds")
println("Number of measurements: ", q.noMeasurements)
