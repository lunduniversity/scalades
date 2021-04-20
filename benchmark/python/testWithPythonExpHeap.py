# -*- coding: utf-8 -*-
import random
import time
import heapq
signalList = []

ARRIVAL = 1
READY = 2
MEASURE = 3
simTime = 0


class generator:
    def __init__(self, sendTo):
        self.sendTo = sendTo
        self.this = self
    def treatSignal(self, x):
        send(ARRIVAL, simTime, self.sendTo)
        send(READY, simTime + random.expovariate(0.8), self)  # time between arrivals
        
class queue:
    def __init__(self):
        self.numberInQueue = 0
        self.numberMeasurements = 0
        self.accumulated = 0
    def treatSignal(self, x):
        if x == ARRIVAL:
            if self.numberInQueue == 0:
                send(READY, simTime + random.expovariate(1.0), self) # service time
            self.numberInQueue = self.numberInQueue + 1
        elif x == READY:
            self.numberInQueue = self.numberInQueue - 1
            if self.numberInQueue > 0:
                send(READY, simTime + random.expovariate(1.0), self) # service time
        elif x == MEASURE:
            self.numberMeasurements = self.numberMeasurements + 1
            self.accumulated = self.accumulated + self.numberInQueue
            send(MEASURE, simTime + random.expovariate(0.1), self)
            
# class observer:
#     def __init__(self):
#         self.meanObserveTime = 10
#         self.numberMeasurements = 0
#         self.accumulated = 0
#     def treatSignal(self, x):
#         if x == ARRIVAL:
#             if self.numberInQueue == 0:
#                 send(READY, simTime + random.expovariate(1.0), self)
#             self.numberInQueue = self.numberInQueue + 1
#         elif x == READY:
#             self.numberInQueue = self.numberInQueue - 1
#             if self.numberInQueue > 0:
#                 send(READY, simTime + random.expovariate(1.0), self)
#         elif x == MEASURE:
#             self.numberMeasurements = self.numberMeasurements + 1
#             self.accumulated = self.accumulated + self.numberInQueue
#             send(MEASURE, simTime + 5, self)


def send(evType, evTime, destination):
    heapq.heappush(signalList, (evTime, evType, destination))

def fetchEvent():
    return heapq.heappop(signalList)

def printList():
    pass



simulationTime = 12345678

q = queue()
gen = generator(q)
send(READY, 0, gen)
send(MEASURE, 0, q)
print(signalList)
antal = 0
startTime = time.time()
while simTime < simulationTime:
    antal = antal + 1
    actEvent = heapq.heappop(signalList)
    simTime = actEvent[0]
    actEvent[2].treatSignal(actEvent[1])

print('Mean: ', 1.0*q.accumulated/q.numberMeasurements)
totalTid = time.time() - startTime
print('Elapsed time: ', totalTid)
print('Number of measurements: ', q.numberMeasurements)
print('Antal händelser: ', antal)
print('Tid per händelse: ', totalTid/antal)

    
    
