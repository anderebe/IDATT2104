#include <iostream>
#include <thread>
#include <vector>
#include <functional>
#include <list>
#include <mutex>
#include <condition_variable>

using namespace std;

int task_number = 0;

// A class that manages a set of worker threads. The threads will be created when the start() method is called. 
class Workers {
private:
    int num_threads;
    vector<thread> threads;
    list<function<void()>> tasks;
    mutex task_mutex;
    condition_variable task_cv;
    bool stop_threads = false;

public:
    Workers(int num_threads) : num_threads(num_threads) {}

    void start() {
        for (int i = 0; i < num_threads; i++) {
            threads.emplace_back([this] {
                while (true) {
                    function<void()> task;
                    {
                        unique_lock<mutex> lock(task_mutex);
                        task_cv.wait(lock, [this] { return stop_threads || !tasks.empty(); });
                        if (stop_threads && tasks.empty()) {
                            return;
                        }
                        task = *tasks.begin();
                        tasks.pop_front();
                    }
                    task();
                }
            });
        }
    }

    // Post a task to the worker threads. Will be executed asynchronously.
    void post(function<void()> task) {
        if(stop_threads) {
            return;
        }
        unique_lock<mutex> lock(task_mutex);
        tasks.emplace_back(task);
        task_cv.notify_one();
    }

    // Stop the worker threads. Will wait for any ongoing tasks to finish.
    void stop() {
        stop_threads = true;
        task_cv.notify_all();
    }

    // Join the worker threads. Waits for all threads to finish.
    void join() {
        for (auto& thread : threads) {
            thread.join();
        }
    }

    void post_timeout(){
        // TODO
    }
};

// Example task
void printHello() {
    int task_id = ++task_number;
    cout << "Task:" << task_id << " starting." << endl;
    this_thread::sleep_for(chrono::milliseconds(1000));
    cout << "Task:" << task_id << " says hello!" << endl;
    this_thread::sleep_for(chrono::milliseconds(1000));
    cout << "Task:" << task_id << " done!" << endl;
}   

int main() {
    
    Workers worker_threads(4);
    Workers event_loop(1);

    worker_threads.start(); // Create 4 internal threads
    event_loop.start(); // Create 1 internal thread

    worker_threads.post([] { printHello(); }); // Task A
    worker_threads.post([] { printHello(); }); // Task B
    worker_threads.post([] { printHello(); });
    worker_threads.post([] { printHello(); });
    worker_threads.post([] { printHello(); });

    event_loop.post([] { printHello(); }); // Task C
    event_loop.post([] { printHello(); }); // Task D

    worker_threads.stop(); // Stops the worker threads
    event_loop.stop(); // Stops the event thread

    worker_threads.join(); // Calls join() on the worker threads
    event_loop.join(); // Calls join() on the event thread

    cout << endl << "All threads finished the " << task_number << " tasks." << endl;

    return 0;
}
