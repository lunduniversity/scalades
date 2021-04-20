#include <iostream>
#include <queue>
#include <random>
#include <time.h>

using namespace std;

class Proc;

struct Message {
public:
    int typeOfSignal;
    double arrivalTime;
    Proc *destination;
};

class Proc {
public:
    virtual void treatSignal(Message) = 0;
};

struct LessThanByTime
{
  bool operator()(const Message& lhs, const Message& rhs) const
  {
    return lhs.arrivalTime > rhs.arrivalTime;
  }
};

class SQ {
public:
    void send(int signal, double arrivalTime, Proc *destination){
        Message x;
        x.typeOfSignal = signal;
        x.arrivalTime = arrivalTime;
        x.destination = destination;
        pq.push(x);
    }
    Message getMessage(){
        Message x = pq.top();
        pq.pop();
        return x;
    }
    priority_queue<Message, vector<Message>, LessThanByTime> pq;
};

default_random_engine gen;
// ex1 arrival 1/rate, ex2 service 1/rate, ex3 measurement 1/rate 
exponential_distribution<double> ex1(1.0), ex2(1.25), ex3(10.0);

SQ sq;  // TODO: re-implement this with single-linked hand-rolled queue, c.f. java & c
double simTime = 0;

const int ARRIVAL = 1;
const int READY = 2;
const int MEASURE = 3;


class Queue : public Proc {
public:
    int numberInQueue, noMeasurements, accumulated;
    void treatSignal(Message x){
        switch (x.typeOfSignal){
        case ARRIVAL:
            if (numberInQueue == 0){
                sq.send(READY, simTime + ex2(gen), this);
            }
            numberInQueue++;
            break;
        case READY:
            numberInQueue--;
            if (numberInQueue > 0){
                sq.send(READY, simTime + ex2(gen), this);
            }
            break;
        case MEASURE:
            accumulated = accumulated + numberInQueue;
            noMeasurements++;
            sq.send(MEASURE, simTime + ex3(gen), this);
            break;
        }
    }
};

class Generator : public Proc {
public:
    Proc *sendTo;
    void treatSignal(Message x){
        sq.send(ARRIVAL, simTime, sendTo);
        sq.send(READY, simTime + ex1(gen), this);
    }
};


int main()
{
    Queue q;
    q.numberInQueue = 0;
    q.accumulated = 0.0;
    q.noMeasurements = 0;
    Generator factory;
    factory.sendTo = &q;
    sq.send(READY, 0, &factory);
    sq.send(MEASURE, 0, &q);
    clock_t tic = clock();
    while (simTime < 12345678){  // NOTE: this is an order of magnitude less than 123456789
        Message m = sq.getMessage();
        simTime = m.arrivalTime;
        (*m.destination).treatSignal(m);
        // if (((int) simTime) % 100 == 0) {
        //     cout << simTime << endl;
        // }
        //cout << q.numberInQueue << endl;
    }
    clock_t toc = clock();
    cout << (double)(toc - tic)/CLOCKS_PER_SEC << endl;
    cout << (double)q.accumulated/q.noMeasurements;
    return 0;
}
