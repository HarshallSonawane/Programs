import java.util.Scanner;

public class FirstComeFirstServeCPUSchedulingAlgorithm
{
    int burstTime[];
    int arrivalTime[];
    String[] processId;
    int numberOfProcess;

    void getProcessData(Scanner input) 
    {
        System.out.print("Enter the number of Process for Scheduling           : ");
        int inputNumberOfProcess = input.nextInt();
        numberOfProcess = inputNumberOfProcess;
        burstTime = new int[numberOfProcess];
        arrivalTime = new int[numberOfProcess];
        processId = new String[numberOfProcess];
        String st = "P";
        for (int i = 0; i < numberOfProcess; i++) 
        {
            processId[i] = st.concat(Integer.toString(i));
            System.out.print("Enter the burst time   for Process - " + (i) + " : ");
            burstTime[i] = input.nextInt();
            System.out.print("Enter the arrival time for Process - " + (i) + " : ");
            arrivalTime[i] = input.nextInt();
        }
    }

    void sortAccordingArrivalTime(int[] at, int[] bt, String[] pid)
    {
        boolean swapped;
        int temp;
        String stemp;
        for (int i = 0; i < numberOfProcess; i++) 
        {
            swapped = false;
            for (int j = 0; j < numberOfProcess - i - 1; j++) 
            {
                if (at[j] > at[j + 1]) 
                {
                    
                    temp = at[j];
                    at[j] = at[j + 1];
                    at[j + 1] = temp;

                    
                    temp = bt[j];
                    bt[j] = bt[j + 1];
                    bt[j + 1] = temp;

                    
                    stemp = pid[j];
                    pid[j] = pid[j + 1];
                    pid[j + 1] = stemp;

                   
                    swapped = true;
                }
            }
            if (swapped == false) 
            {
                break;
            }
        }
    }

    void firstComeFirstServeAlgorithm() 
    {
        int complitiontime[] = new int[numberOfProcess];
        int bt[] = burstTime.clone();
        int at[] = arrivalTime.clone();
        String pid[] = processId.clone();
        int waitingTime[] = new int[numberOfProcess];
        int turnAroundTime[] = new int[numberOfProcess];
        sortAccordingArrivalTime(at, bt, pid);

        
        complitiontime[0] = at[0] + bt[0];
        turnAroundTime[0] = complitiontime[0] - at[0];
        waitingTime[0] = turnAroundTime[0] - bt[0];
        for (int i = 1; i < numberOfProcess; i++) 
        {
            complitiontime[i] = bt[i] + complitiontime[i - 1];
            turnAroundTime[i] = complitiontime[i] - at[i];
            waitingTime[i] = turnAroundTime[i] - bt[i];
        }
        float sum = 0;
        for (int n : waitingTime) 
        {
            sum += n;
        }
        float averageWaitingTime = sum / numberOfProcess;

        sum = 0;
        for (int n : turnAroundTime) 
        {
            sum += n;
        }
        float averageTurnAroundTime = sum / numberOfProcess;

        
        System.out.println("FCFS Scheduling Algorithm : ");
        System.out.format("%20s%20s%20s%20s%20s%20s\n", "ProcessId", "BurstTime", "ArrivalTime", "complitiontime", "WaitingTime", "TurnAroundTime");
        for (int i = 0; i < numberOfProcess; i++) 
        {
            System.out.format("%20s%20d%20d%20d%20d%20d\n", pid[i], bt[i], at[i], complitiontime[i], waitingTime[i], turnAroundTime[i]);
        }
        System.out.format("%80s%20f%20f\n", "Average", averageWaitingTime, averageTurnAroundTime);
        System.out.println("Note : waiting time = Response time");
    }

    public static void main(String[] args) 
    {
        Scanner input = new Scanner(System.in);
        FirstComeFirstServeCPUSchedulingAlgorithm obj = new FirstComeFirstServeCPUSchedulingAlgorithm();
        obj.getProcessData(input);
        obj.firstComeFirstServeAlgorithm();
    }
}