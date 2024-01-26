//
// Created by Emil on 26/01/2024.
//

#include <iostream>
#include <thread>

class Workers{
    private:
    

    public:
    Workers(int n) {
        // Create n internal threads
    }
    
    Workers event_loop(int n) {
        // Create n internal threads
    }

    void start() {
        // Start all the internal threads
    }
    void post(std::function<void()> task) {
        // Add task to the queue of one of the internal threads
    }
    void join() {
        // Join all the internal threads
    }
};

int main() {
    Workers worker_threads(4);
    Workers event_loop(1);
    worker_threads.start(); // Create 4 internal threads
    event_loop.start(); // Create 1 internal thread

    worker_threads.post([] {
        // Task A

        
    });

    worker_threads.post([] {
        // Task B
        // Might run in parallel with task A
    });

    event_loop.post([] {
        // Task C
        // Might run in parallel with task A and B
    });

    event_loop.post([] {
        // Task D
        // Will run after task C
        // Might run in parallel with task A and B
    });

    worker_threads.join(); // Calls join() on the worker threadsevent_loop.join(); // Calls join() on the event thread

    return 0;
}
