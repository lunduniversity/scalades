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

func main() {
	makeEventList()
	var actEvent Event
	var tid float64
	var no, acc, totMea int64
	tid = 1.0
	insertEvent(ARRIVAL, 0.0)
	insertEvent(MEASURE, 0.0)
	start := time.Now()
	rand.Seed(12300)
	for tid < 1234567890 {
		actEvent = fetchEvent()
		tid = actEvent.eventTime
		switch {
		case actEvent.eventType == ARRIVAL:
			{
				if no == 0 {
					insertEvent(READY, tid-math.Log(rand.Float64()))
				}
				no = no + 1
				insertEvent(ARRIVAL, tid-2*math.Log(rand.Float64()))
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
				insertEvent(MEASURE, tid+5)
			}
		}
	}
	elapsed := time.Since(start)
	fmt.Println("Tiden det tar är : ", elapsed)
	fmt.Println(float64(acc) / float64(totMea))
}
