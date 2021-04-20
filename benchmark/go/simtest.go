// test
package main

import (
	"fmt"
	"math"
	"math/rand"
	"time"
)

const (
	ARRIVAL = 1
	READY   = 2
	MEASURE = 3
)

type Event struct {
	eventTime float64
	eventType int
	next      *Event
}

var first, last *Event

func insertEvent(kind int, time float64) {
	var dummy, predummy, newEvent *Event
	newEvent = new(Event)
	newEvent.eventTime = time
	newEvent.eventType = kind
	predummy = first
	dummy = first.next
	for (dummy.eventTime < newEvent.eventTime) && (dummy != last) {
		predummy = dummy
		dummy = dummy.next
	}
	predummy.next = newEvent
	newEvent.next = dummy
}

func fetchEvent() Event {
	var dummy *Event
	dummy = first.next
	first.next = dummy.next
	return *dummy
}

func makeEventList() {	
    first = new(Event)
	last = new(Event)
	first.next = last
}

func main() {
	makeEventList()
	var actEvent Event
	var tid float64
	var no, acc, totMea int64
	tid = 1.0
	insertEvent(ARRIVAL, 0.0)
	insertEvent(MEASURE, 0.0)
	start := time.Now()
	//rand.Seed(12300)
	for tid < 12345678 {
		actEvent = fetchEvent()
		tid = actEvent.eventTime
		switch {
		case actEvent.eventType == ARRIVAL:
			{
				if no == 0 {
					insertEvent(READY, tid-math.Log(rand.Float64()))
				}
				no = no + 1
				insertEvent(ARRIVAL, tid-(1.25)*math.Log(rand.Float64()))
			}
		case actEvent.eventType == READY:
			{
				no = no - 1
				if no > 0 {
					insertEvent(READY, tid-math.Log(rand.Float64()))
				}
			}
		case actEvent.eventType == MEASURE:
			{
				acc = acc + no
				totMea = totMea + 1
				insertEvent(MEASURE, tid-(10.0)*math.Log(rand.Float64()))
			}
		}
	}
	elapsed := time.Since(start)
	fmt.Println("Elapsed time: ", elapsed)
	fmt.Println(float64(acc) / float64(totMea))
}
