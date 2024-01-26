#include <iostream>
#include <thread>
#include <functional>
#include <queue>
#include <mutex>
#include <condition_variable>

class Workers {
private:
    std::vector<std::thread> threads;
    std::queue<std::function<void()>> tasks;
    std::mutex mutex;
    std::condition_variable cv;
    bool stop = false;

public:
    Workers(int n) {
        for (int i = 0; i < n; i++) {
            threads.emplace_back([this]() {
                while (true) {
                    std::function<void()> task;
                    {
                        std::unique_lock<std::mutex> lock(mutex);
                        cv.wait(lock, [this]() { return stop || !tasks.empty(); });
                        if (stop && tasks.empty()) {
                            return;
                        }
                        task = std::move(tasks.front());
                        tasks.pop();
                    }
                    task();
                }
            });
        }
    }

    Workers event_loop(int n) {
        return Workers(n);
    }

    void start() {
        // No additional implementation needed
    }

    void post(std::function<void()> task) {
        {
            std::lock_guard<std::mutex> lock(mutex);
            tasks.push(task);
        }
        cv.notify_one();
    }

    void join() {
        {
            std::lock_guard<std::mutex> lock(mutex);
            stop = true;
        }
        cv.notify_all();
        for (auto& thread : threads) {
            thread.join();
        }
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

    worker_threads.join(); // Calls join() on the worker threads
    event_loop.join(); // Calls join() on the event thread

    return 0;
}
