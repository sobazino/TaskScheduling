# TaskScheduling

**<p align="center">Task scheduling in cloud environment</p>**

<p align="center">
<img src="https://img.shields.io/github/languages/count/sobazino/TaskScheduling">
<img src="https://img.shields.io/badge/Roadmap-2024-yellowgreen.svg">
<img src="https://img.shields.io/badge/Author-Mehran%20Nosrati-blue.svg">
</p>

</br>

### Pseudocode:

```
1. Sort the list of n tasks using SJF.
2. Set selected to 0.
3. For i from 1 to n:
    1. If selected is 0:
        1. Select a task from the top of the list
        2. Set selected to 1.
    2. Else if selected is 1:
        1. Select a task from the center of the list
        2. Set selected to 2.
    3. Else:
        1. Select a task from the bottom of the list
        2. Set selected to 0.
4. For j from 1 to m:
    1. Find the VM with the minimum completion time.
    2. If two VMs have the same minimum completion time:
        1. Select the VM with the highest MIPS.
5. Assign the task to the VM with the shortest completion time.
6. Move to the next task.
```

---

### Core:

[Constants.java](https://github.com/sobazino/TaskScheduling/blob/main/Project/SobaZino/Constants.java)
</br>
[Simulation.java](https://github.com/sobazino/TaskScheduling/blob/main/Project/SobaZino/Simulation.java)
</br>
[DatacenterBroker.java](https://github.com/sobazino/TaskScheduling/blob/main/sources/org/cloudbus/cloudsim/DatacenterBroker.java)

### Dataset:

```
HPC2N-2002-2.2-cln
```

### Performance Metrics:

```
1. Average Makespan
2. Mean of total average response time
3. Actual Execution Time
4. Average Resource Utilization
5. Throughput
```
