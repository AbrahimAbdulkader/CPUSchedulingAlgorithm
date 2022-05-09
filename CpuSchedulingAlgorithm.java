import java.util.*;
import java.io.*;

public class CpuSchedulingAlgorithm {

       public static int check(int rem_bt[]) {

            int length = rem_bt.length;
            for(int i = 0; i < length; i++) {
                if(rem_bt[i] != 0)
                    return 0 ; //means still processes are their
            }
            return 1;
        }

        public static void main(String []args) {
            int length = args.length;
            String scheduleType = "";
            int timeQuantum = 0;
            String filePath = "";

            System.out.println("----------------------------------------------");
            System.out.println("             CPU Scheduling Simulation        ");
            System.out.println("----------------------------------------------");


            if (length == 3) { //means RR as we are passing the time slice as well
                scheduleType = args[0];
                filePath = args[1];
                timeQuantum = Integer.parseInt(args[2]);
            }

            else if (length == 2) {  //means FCFS
                scheduleType = args[0];
                filePath = args[1];
            }



            ArrayList<String> lines = new ArrayList<String>(); //an arraylist to store the lines which are read from the file

            //reading from file
            try {
                File file = new File(filePath);
                BufferedReader br = new BufferedReader(new FileReader(file));
                String temp;
                while ((temp = br.readLine()) != null) {
                    lines.add(temp); //add lines to the arrayList;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            int number = lines.size();
            int pid[] = new int[number];  //processes
            int at[] = new int[number];   //arrival times
            int bt[] = new int[number];  //burst times
            int wt[] = new int[number]; //waiting time
            int ct[] = new int[number]; //complition time
            int tat[] = new int[number]; //turn around time
            int rem_bt[] = new int[number]; //remaining burst time for RR scheduling
            int rt[] = new int[number]; //response time


            for (int i = 0; i < number; i++) {

                String arr[] = lines.get(i).split(" ");
                String id = arr[0];
                String arrival_time = arr[1];
                String burst_time = arr[2];


                // System.out.println(id+" "+arrival_time+" "+burst_time);
                char char_arr[] = id.toCharArray();
                int index = char_arr[1] - 48; //to get the pid ex: if its P2 , 2 is store in char_arr[1] so it's id is 2

                pid[index] = index;
                at[index] = Integer.parseInt(arrival_time);
                bt[index] = Integer.parseInt(burst_time);
                rem_bt[index] = bt[index];

            }

            //sorting the data based on arrival time

            for (int i = 0; i < number; i++) {
                for (int j = 0; j < number - (i + 1); j++) {
                    if (at[j] > at[j + 1]) {
                        int temp = at[j];  //swapping arrival times
                        at[j] = at[j + 1];
                        at[j + 1] = temp;

                        temp = bt[j];  //swapping burst times
                        bt[j] = bt[j + 1];
                        bt[j + 1] = temp;

                        temp = pid[j];  //swapping process ids
                        pid[j] = pid[j + 1];
                        pid[j + 1] = temp;

                        temp = rem_bt[j];  //swapping remaining burst times
                        rem_bt[j] = rem_bt[j+1];
                        rem_bt[j+1] = temp;
                    }
                }
            }

            if (scheduleType.equalsIgnoreCase("fcfs")) {

                System.out.println("----------------------------------------------");
                System.out.println("            FIRST COME FIRST SERVE SCHEDULING       ");
                System.out.println("----------------------------------------------");


                for (int i = 0; i < number; i++) {
                    if (i == 0) { //for the first process

                        wt[i] = 0;
                        ct[i] = at[i] + bt[i];
                        tat[i] = ct[i] - at[i];
                        if (at[0] != 0)
                            System.out.println("[0-" + at[i] + "] CPU idle");

                        System.out.println("[" + at[0] + "-" + ct[0] + "] P" + pid[0] + " running");
                        rt[0] = 0;

                    } else {
                        if (at[i] > ct[i - 1]) { // if there is no one in ready queue

                            wt[i] = 0;
                            ct[i] = at[i] + bt[i];
                            tat[i] = ct[i] - at[i];
                            System.out.println("[" + ct[i - 1] + "-" + at[i] + "] CPU idle");
                            System.out.println("[" + at[i] + "-" + ct[i] + "] P" + pid[i] + " running");
                            rt[i] = 0;
                        }

                        else {
                            wt[i] = ct[i - 1] - at[i];
                            ct[i] = ct[i - 1] + bt[i];
                            tat[i] = ct[i] - at[i];
                            System.out.println("[" + ct[i - 1] + "-" + ct[i] + "] P" + pid[i] + " running");
                            rt[i] = ct[i-1]-at[i];

                        }
                    }
                }

                double tat_avg; //average turn around time
                int tat_sum = 0;

                double wt_avg;  //average waiting time
                int wt_sum = 0;

                double rt_avg; //response time average
                int rt_sum = 0;

                System.out.println("Turn around times: ");

                for (int i = 0; i < number; i++) {
                    System.out.println("P[" + pid[i] + "] = " + tat[i]);
                    tat_sum = tat_sum + tat[i];
                }

                tat_avg = (double) tat_sum / number;

                System.out.println(); //to print empty line

                System.out.println("Waiting times: ");

                for (int i = 0; i < number; i++) {
                    System.out.println("P[" + pid[i] + "] = " + wt[i]);
                    wt_sum = wt_sum + wt[i];
                }
                wt_avg = (double) wt_sum / number;

                System.out.println(); //to print empty line
                System.out.println("Response times: ");

                for (int i = 0; i < number; i++) {
                    System.out.println("P[" + pid[i] + "] = " + rt[i]);
                    rt_sum = rt_sum + rt[i];
                }

                rt_avg = (double) rt_sum / number;

                System.out.println(); //to print empty line
                System.out.println("Average turn around time: " + tat_avg);
                System.out.println("Average waiting time: " + wt_avg);
                System.out.println("Average response time: " + rt_avg);

            }

            //if Round Robin
            else if(scheduleType.equalsIgnoreCase("RR")) {

                System.out.println("----------------------------------------------");
                System.out.println("            ROUND ROBIN SCHEDULING       ");
                System.out.println("----------------------------------------------");


                Queue<Integer> q = new LinkedList<>(); //a Queue to store the processes

                Arrays.fill(rt,-1); //fill all values of response time with -1

                int timer = 0;
                q.add(0);  //initially adding index zero to the Queue

                int flag =0 ; //if flag ==1 means all the processes are completed
                int round = 0; // just  a variable to see which iteration is it, round =0 siginifies first iteration
                int last_index = 0; //to keep track of last index which was processed
                int k = 0;
                do {

                    if(q.isEmpty())
                    {
                        System.out.println("[" + timer + "-"+ at[last_index + 1] + "] CPU idle");
                        timer = at[last_index + 1];
                        q.add(last_index + 1);
                    }

                    int i = q.poll();

                    last_index = Math.max(i, last_index);

                    if (at[0] != 0 && round == 0) { //if it's first iteration and arrival time doesn't start with zero
                        System.out.println("[0-" + at[i] + "] CPU idle");
                        timer = timer + at[i];
                        round++;
                    }


                    if(rem_bt[i] > timeQuantum ) {
                        System.out.println("[" + timer + "-" + (timer + timeQuantum) + "] P" + pid[i] + " running");
                        if(rt[i] == -1) //if -1 only then we update it
                            rt[i] = timer - at[i];

                        rem_bt[i] = rem_bt[i] - timeQuantum;
                        timer = timer + timeQuantum;

                        //update the queue
                        for(int j = last_index + 1; j < number; j++) {

                            if(timer >= at[j]) { //if the processes have already arrived by that time we add them to queue
                                q.add(j);
                                last_index = Math.max(j, last_index);
                                //     System.out.println("Pushing "+"P["+pid[j]+"]");
                            }
                            else
                                break;  //as input is already sorted on arrival time
                        }

                        q.add(i);

                    }
                    else {
                        System.out.println("[" + timer + "-" + (timer + rem_bt[i]) + "] P" + pid[i] + " running");
                        if(rt[i] == -1) //if -1 only then we update it
                            rt[i] = timer - at[i];
                        timer = timer + rem_bt[i];
                        rem_bt[i] = 0;
                        ct[i] = timer;

                    }

                    flag = check(rem_bt); // a function to check if all processes are done

                }

                while(flag != 1);


                double tat_avg; //average turn around time
                int tat_sum = 0;

                double wt_avg;  //average waiting time
                int wt_sum = 0;

                double rt_avg;
                int rt_sum = 0;

                System.out.println("Turn around times: ");

                for (int i = 0; i < number; i++) {
                    tat[i] = ct[i]-at[i];
                    System.out.println("P[" + pid[i] + "] = " + tat[i]);
                    tat_sum = tat_sum + tat[i];
                }

                tat_avg = (double) tat_sum / number;

                System.out.println(); //to print empty line

                System.out.println("Waiting times: ");

                for (int i = 0; i < number; i++) {
                    wt[i] = tat[i]- bt[i];
                    System.out.println("P[" + pid[i] + "] = " + wt[i]);
                    wt_sum = wt_sum + wt[i];
                }
                wt_avg = (double) wt_sum / number;

                System.out.println(); //to print empty line
                System.out.println("Response times: ");

                for (int i = 0; i < number; i++) {
                    System.out.println("P[" + pid[i] + "] = " + rt[i]);
                    rt_sum = rt_sum + rt[i];
                }

                rt_avg = (double) rt_sum / number;

                System.out.println(); //to print empty line
                System.out.println("Average turn around time: " + tat_avg);
                System.out.println("Average waiting time: " + wt_avg);
                System.out.println("Average response time: " + rt_avg);




            }
        }

    }

