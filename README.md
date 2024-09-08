# Account Simulation Project

This Java project demonstrates a multi-threaded account simulation using concurrent programming concepts such as ExecutorService, Locks, and Conditions.

## Project Overview

The `AccountWithConditionUsers` class simulates a user account with two concurrent operations:
1. Adding money to the account
2. Removing money from the account

These operations are performed by two separate threads, demonstrating thread safety and coordination using `ReentrantLock` and `Condition`.

## Key Components

1. `UserAccount`: A class representing the user's account with methods to add and remove money.
2. `AddMoney`: A `Runnable` implementation that simulates adding random amounts of money to the account.
3. `RemoveMoney`: A `Runnable` implementation that simulates removing random amounts of money from the account.

## Features

- Concurrent execution of add and remove operations
- Thread-safe account balance management
- Condition-based waiting when attempting to remove more money than the current balance
- Random amounts for add and remove operations
- Timed execution (60 seconds) using `ExecutorService`

## How it Works

1. The program creates an `ExecutorService` with a fixed thread pool of 2 threads.
2. It submits `AddMoney` and `RemoveMoney` tasks to the executor.
3. The executor runs for 60 seconds, during which:
   - `AddMoney` thread adds random amounts to the account every 2 seconds.
   - `RemoveMoney` thread attempts to remove random amounts from the account every 2 seconds.
4. If the removal amount exceeds the current balance, the thread waits until sufficient funds are available.
5. After 60 seconds, the executor is shut down, and the program terminates.

## Running the Project

To run this project:

1. Ensure you have Java Development Kit (JDK) installed on your system.
2. Compile the Java file:
   ```
   javac org/vinam/AccountWithConditionUsers.java
   ```
3. Run the compiled class:
   ```
   java org.vinam.AccountWithConditionUsers
   ```

## Output

The program will output the add and remove operations, along with the current balance, for 60 seconds. Example output:

```
Added:   5		Current balance: 5
Removed: 3		Current balance: 2
Added:   7		Current balance: 9
Insufficient Balance while removing: 10
Added:   6		Current balance: 15
Removed: 10		Current balance: 5
```

## Notes

- This project is for demonstration purposes and simulates a simplified version of concurrent account operations.
- The use of `ReentrantLock` and `Condition` ensures thread-safe operations on the shared account balance.
- The project runs for a fixed duration of 60 seconds. You can modify this in the `main` method if needed.
