/* 
 * File:   main.c
 * Author: tts-cny
 *
 * Created on den 10 december 2014, 10:27
 */

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
    #define ARRIVAL  1
    #define MEASURE  2
    #define READY    3

int main(int argc, char** argv) {
    
    struct event{
        double timeOfEvent;
        int typeOfEvent;
        struct event *next;
    };
    
    struct event *first = malloc(sizeof *first);
    struct event *last = malloc(sizeof *last);
    struct event *actEvent;
    
    
    void insertEvent(int typeEv, double timeEv){
        struct event *newEvent = malloc(sizeof *newEvent);
        newEvent -> timeOfEvent = timeEv;
        newEvent -> typeOfEvent = typeEv;
        struct event *predummy;
        struct event *dummy;
        predummy = first;
        dummy = predummy->next;
        while ((dummy -> timeOfEvent < timeEv) && (dummy != last)){
            predummy = dummy;
            dummy = predummy -> next;
        }
        predummy -> next = newEvent;
        newEvent -> next = dummy;
    }
    
    void printEventQueue(){
        struct event *dummy = first->next;
        while (dummy != last){
            printf("E: %i   T: %f\n", dummy->typeOfEvent, dummy->timeOfEvent);
            dummy = dummy->next;
        }
        printf("************************************************************\n");
        printf("\n");
    }
    
    struct event *fetchEvent(){
        struct event *dummy; 
        dummy = first -> next;
        first -> next = dummy -> next;
        dummy -> next = NULL;
        return dummy;
    }
    
    double uni(){
        int a = rand();
        while (a == 0)
            a = rand();
        return (double)a/(double)RAND_MAX;
    }
    
    first -> next = last;
    

    long noInQueue = 0;
    long noMeasurements = 0;
    long accumulated = 0;
    double time = 0;
    insertEvent(ARRIVAL, 0);
    insertEvent(MEASURE, 0);
    
    clock_t tic = clock();
    
    while (time < 123456789){
        actEvent = fetchEvent();
        time = actEvent->timeOfEvent;
        switch (actEvent->typeOfEvent){
            case ARRIVAL:{
                if (noInQueue == 0)
                    insertEvent(READY, time - log(uni()));
                noInQueue++;
                insertEvent(ARRIVAL, time - 2.0*log(uni()));
            }
            break;
            case READY: {
                noInQueue--;
                if (noInQueue > 0)
                    insertEvent(READY, time - log(uni()));
            }
            break;
            case MEASURE: {
                noMeasurements++;
                accumulated = accumulated + noInQueue;
                insertEvent(MEASURE, time + 5);
                }
            }
        free(actEvent);
    }
    clock_t toc = clock();
    printf("Elapsed: %f seconds\n", (double)(toc - tic) / CLOCKS_PER_SEC);

    printf("%f\n", (double)accumulated/(double)noMeasurements);
    printf("Antal mätningar: %li", noMeasurements);
        


    return (EXIT_SUCCESS);
}

