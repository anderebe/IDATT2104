#include <iostream>
#include <vector>
#include <mutex>
#include <thread>

using namespace std;

vector<int> primes;
mutex prime_mutex;

int isPrime(int n){
    if(n <= 1) return false;
    if(n <= 3) return true;

    if(n % 2 == 0 || n % 3 == 0) return false;

    for ( int i = 5; i * i <= n; i = i + 6){
        if (n % i == 0 || n % (i + 2) == 0) return false;
    }

    return true;
} struct isPrime;

void primeNumbers(int start, int end){
    for(int i = start; i <= end; i++){
        if(isPrime(i)){
            lock_guard<mutex> guard(prime_mutex);
            primes.push_back(i);
        }
    }
} struct primeNumbers;

int main() {
    int threads_num, num_start, num_end;
    cout << "Enter number of threads: ";
    cin >> threads_num;
    cout << "Enter start number: ";
    cin >> num_start;
    cout << "Enter end number: ";
    cin >> num_end;

    if(num_end < num_start){
        cout << "End number is less than start number" << endl << "flipping numbers..." << endl;
        int placeholder = num_end;
        num_end = num_start;
        num_start = placeholder;
    }
    if(num_start == num_end){
        cout << "Numbers are the same" << endl << "adding 1 to end number..." << endl;
        num_end++;
    }

    int num_total = num_end - num_start;

    vector<thread> threads;
    int workload = num_total / threads_num;
    int workload_rest = num_total % threads_num;

    int current_num = num_start;

    for(int i = 0; i < threads_num; i++){
        int start = current_num;
        int end = start + workload + (i < workload_rest ? 1 : 0);
        current_num = end;

        threads.emplace_back(primeNumbers, start, end);
    }

    for(auto& thread : threads){
        thread.join();
    }

    sort(primes.begin(), primes.end());

    cout << endl << "Prime numbers between " << num_start << " and " << num_end << ": " << endl;
    int count = 0;
    for(auto prime: primes){
        count++;
        cout << prime << " ";
        if(count % 10 == 0){
            cout << endl;
        }

    }

    return 0;
}