program project1;

uses DateUtils, SysUtils;

const ARRIVAL = 0;
      READY = 1;
      MEASURE = 2;

type  Proc = interface;
      //procedure send(eType: integer; eTime: double; dest: Proc );

     Signal2 = record
        signalType: integer;
        arrivalTime: real;
        destination: Proc;
     end;

     signalList = object
     private
        store: array [1..1000] of Signal2;
        lastElement: integer;
     public
        procedure sendSignal(sign: signalType; dest: integer; arrival: real);
        function fetchEvent(): signal;
     end;

     Signal = record
        eventTime: double;
        eventType: integer;
        destination: Proc;
        next: ^Signal;
     end;

     Proc = interface
       procedure treatSignal(m: Signal);
     end;

     MM1 = class(TInterfacedObject, Proc)
     public
        numberInQueue, accumulated, noMeasurements: integer;
        procedure treatSignal(m: Signal);
     end;

     Generator = class(TInterfacedObject, Proc)
     public
        sendTo: Proc;
        procedure treatSignal(m: Signal);
     end;

var actSignal: Signal;
    eventList: record
         first:^Signal;
         last: ^Signal;
    end;
    time: double;
    startTime, stopTime: TDateTime;
    DiffMilliSeconds: integer;
    Q: MM1;
    G: Generator;

procedure send(eType: integer; eTime: double; dest: Proc );
var
    newEvent: ^Signal;
    dummy, preDummy: ^Signal;
begin
  new(newEvent);
  newEvent^.eventType := eType;
  newEvent^.eventTime := eTime;
  newEvent^.destination := dest;
  predummy := eventList.first;
  dummy := predummy^.next;
  while (dummy^.eventTime < eTime) and (dummy <> eventList.last) do
  begin
    predummy := dummy;
    dummy := predummy^.next;
  end;
  predummy^.next := newEvent;
  newEvent^.next := dummy;
end;

procedure MM1.treatSignal(m: Signal);
begin
  case m.eventType of
     ARRIVAL: begin
       if numberInQueue = 0 then
          send(READY, time - ln(random()), Self);
       numberInQueue := numberInQueue + 1;
     end;
     READY: begin
       numberInQueue := numberInQueue - 1;
       if numberInQueue > 0 then
          send(READY, time - ln(random()), Self);
     end;
     MEASURE: begin
       accumulated := accumulated + numberInQueue;
       noMeasurements := noMeasurements + 1;
       send(MEASURE, time + 5, Self);
     end;
  end;
end;

procedure Generator.treatSignal(m: Signal);
begin
  send(ARRIVAL, time, sendTo);
  send(READY, time - 2*ln(random()), Self);
end;

function fetchEvent(): Signal;
var dummy: ^Signal;
begin
  dummy := eventList.first^.next;
  eventList.first^.next := dummy^.next;
  dummy^.next := nil;
  fetchEvent := dummy^;
  dispose(dummy);
end;

begin
  new(eventList.last);
  new(eventList.first);
  eventList.first^.next := eventlist.last;
  Randomize;

  Q := MM1.create;
  Q.numberInQueue := 0;
  Q.accumulated := 0;
  Q.noMeasurements := 0;
  G := Generator.create;
  G.sendTo := Q;

  send(ARRIVAL, 0, G);
  send(MEASURE, 0, Q);
  time := 0;
  startTime := Now;
  while time < 123456789 do
  begin
    actSignal := fetchEvent();
    time := actSignal.eventTime;
    actSignal.destination.treatSignal(actSignal);
  end;
  stopTime := Now;
  DiffMilliSeconds := MilliSecondsBetween(stopTime, startTime);

  writeln();
  writeln('The mean is: ', 1.0*Q.accumulated/Q.noMeasurements:1:4);
  writeln('The time elapsed: ', DiffMilliSeconds/1000.0:6:2, ' seconds');
  writeln('Number of measurements: ', Q.noMeasurements);
  readln();
end.


