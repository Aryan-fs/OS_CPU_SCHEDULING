import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class cpu_scheduling {
    
    static JFrame jf1, jf2;
    
    // Components For Input Window
    
    static JTextField tf1 ,tf2;
    static JLabel jl1, jl2, jl3, jl4;
    static JComboBox jcb;
    static JButton jb;
    static JPanel jp;

    // Components For Output Window
    
    static JLabel jl5, jl6, jl7, jl8, jl9, jl10, jl11, jl12;
    static JTable jt, jt1, jt2;
    static JTableHeader jth;

    // Variables For Calculations 
    static String data[][], data1[][], data2[][];
    static String column1[], column2[];
    static int aSize, bSize, Time;
    static ArrayList<Integer> arrivalTime, burstTime;
    static PCB[] pcb;
    static ArrayList<PCB> temp_pcb, ready_queue;
    static PCB temp, temp1, temp2;

    // Method to Count No. of Processes & Fetch Arrival Times of the Processes
    static public int processes(){
        String input = tf1.getText();
        String output[] = input.split(" ");
        aSize = output.length;
        arrivalTime = new ArrayList<>();
        int temp = aSize;
       
        for(int i = 0; i < temp; i++){
            try{
                Integer.parseInt(output[i]);
                arrivalTime.add(Integer.parseInt(output[i]));
            }
            catch(NumberFormatException e){
                aSize--;
            }
        }
        return aSize;
    }

    // Method To Print Arrival Time of the processes in JTable
    static public void printArrivalTime(){
        for(int i = 1; i <= aSize; i++){
            data[i][1] = "" + arrivalTime.get(i-1);
        }
    }

    // Method To Fetch Burst Time of the processes 
    static public void getBurstTime(){
        String input = tf2.getText();
        String[] output = input.split(" ");
        burstTime = new ArrayList<>();
        bSize = output.length;
        int temp = bSize;

        for(int i = 0; i< temp; i++){
            try{
                Integer.parseInt(output[i]);
                burstTime.add(Integer.parseInt(output[i]));
            }
            catch(NumberFormatException e){
                bSize--;
            }
        }
    }

    // Method To Print Burst Time of the processes
    static public void printBurstTime(){
        for(int i = 1; i <= bSize; i++){
            data[i][2] = "" + burstTime.get(i-1);
        }
    }

    // Method To Calculate Finish Time, Turnaround Time & Waiting Time
    static public void calculate(){
        String x = jcb.getSelectedItem().toString();
        if(x == "First Come First Serve, FCFS"){

            jl6.setText("FCFS");
            data1= new String[1][arrivalTime.size()];
            column1 = new String[arrivalTime.size()];
            data2 = new String[1][arrivalTime.size() + 1];
            column2 = new String[arrivalTime.size() + 1];

            for(int i = 0; i<arrivalTime.size(); i++){
                for(int j = 0; j<arrivalTime.size() -1; j++){
                    if(pcb[j].at > pcb[j+1].at){
                        temp = pcb[j];
                        pcb[j] = pcb[j+1];
                        pcb[j+1] = temp;
                    }
                }
            }
        
            Time = pcb[0].at;
            for(int i = 0; i<arrivalTime.size(); i++){
                Time += pcb[i].bt;
                pcb[i].tat = Time - pcb[i].at;
                pcb[i].ft = Time;
                pcb[i].wt = pcb[i].tat - pcb[i].bt;
            }

            for(int i = 0; i<arrivalTime.size(); i++){
                data1[0][i] = "P" + pcb[i].p_id;
            }

            data2[0][0] = "" + pcb[0].at;
            for(int i = 1; i<= arrivalTime.size(); i++){
                data2[0][i] = "" + pcb[i-1].ft;
            }

            for(int i = 0; i<arrivalTime.size(); i++){
                for(int j = 0; j<arrivalTime.size() -1; j++){
                    if(pcb[j].p_id > pcb[j+1].p_id){
                        temp = pcb[j];
                        pcb[j] = pcb[j+1];
                        pcb[j+1] = temp;
                    }
                }
            }

        }
        else if(x == "Shortest Job First, SJF"){

            jl6.setText("SJF");
            data1= new String[1][arrivalTime.size()];
            column1 = new String[arrivalTime.size()];
            data2 = new String[1][arrivalTime.size() + 1];
            column2 = new String[arrivalTime.size() + 1];

            temp_pcb = new ArrayList<>();
            for(int i = 0; i< arrivalTime.size(); i++){
                temp_pcb.add(pcb[i]);
            }
            
            ready_queue = new ArrayList<>();

            Time = 0;
            for(PCB p : temp_pcb){
                if(p.at < Time){
                    Time = p.at;
                }
            }
            while(!(temp_pcb.isEmpty() && ready_queue.isEmpty())){
                for(PCB p : temp_pcb){
                    if(p.at <= Time){
                        ready_queue.add(p);
                    }
                }
                for(PCB p : ready_queue){
                    temp_pcb.remove(p);
                }

                temp = ready_queue.get(0);
                for(PCB p : ready_queue){
                    if(p.bt < temp.bt){
                        temp = p;
                    }
                }

                Time += temp.bt;
                pcb[temp.p_id -1].ft = Time;
                pcb[temp.p_id -1].tat = Time - temp.at;
                pcb[temp.p_id -1].wt = temp.tat - temp.bt;

                ready_queue.remove(temp);

            }

            for(int i = 0; i<arrivalTime.size(); i++){
                for(int j = 0; j< arrivalTime.size() -1; j++){
                    if(pcb[j].ft > pcb[j+1].ft){
                        temp = pcb[j];
                        pcb[j] = pcb[j+1];
                        pcb[j+1] = temp;
                    }
                }
            }

            for(int i = 0; i< arrivalTime.size(); i++){
                data1[0][i] = "P" + pcb[i].p_id;
            }

            int min_at  = pcb[0].at;
            for(PCB p : pcb){
                if(p.at < min_at){
                    min_at = p.at;
                }
            }

            data2[0][0] = "" + min_at;
            for(int i = 1; i<= arrivalTime.size(); i++){
                data2[0][i] = "" + pcb[i - 1].ft;
            }

            for(int i = 0; i<arrivalTime.size(); i++){
                for(int j = 0; j<arrivalTime.size() -1; j++){
                    if(pcb[j].p_id > pcb[j+1].p_id){
                        temp = pcb[j];
                        pcb[j] = pcb[j+1];
                        pcb[j+1] = temp;
                    }
                }
            }
        }
        else if(x == "Shortest Remaining Time First, SRTF"){
            
            int Switch = 1;
            ArrayList<Integer> execution_sequence = new ArrayList<>(); 
            ArrayList<Integer> time_chart = new ArrayList<>();
            jl6.setText("SRTF");
            
            temp_pcb = new ArrayList<>();
            for(PCB p : pcb){
                temp = new PCB();
                temp.p_id = p.p_id;
                temp.at = p.at;
                temp.bt = p.bt;
                temp_pcb.add(temp);
            }

            ready_queue = new ArrayList<>();
            Time = (temp_pcb.get(0)).at;
            
            for(PCB p : temp_pcb){
                if(p.at < Time){
                    Time = p.at;
                }
            }

            while(!(ready_queue.isEmpty() & temp_pcb.isEmpty())){
    
                for(PCB p : temp_pcb){
                    if(p.at <= Time){
                        ready_queue.add(p);
                    }
                }
    
                for(PCB p : ready_queue){
                    temp_pcb.remove(p);
                }

                temp = ready_queue.get(0);

                for(PCB p : ready_queue){
                    if(p.bt < temp.bt){
                        temp = p;
                    }
                }

                if(temp2 == null){

                }
                else{
                    if(temp.p_id == temp2.p_id){
        
                    }
                    else{
                        execution_sequence.add(temp2.p_id);
                        time_chart.add(Time);
                        Switch++;
                    }
                }

                temp2 = temp;

                Time++;
                temp1 = temp;
                temp1.bt --;
                ready_queue.set(ready_queue.indexOf(temp), temp1);

                if(temp1.bt == 0){
                    pcb[temp.p_id - 1].ft = Time;
                    pcb[temp.p_id - 1].tat = Time - pcb[temp.p_id - 1].at;
                    pcb[temp.p_id - 1].wt = pcb[temp.p_id - 1].tat - pcb[temp.p_id - 1].bt;
                    
                    if(temp_pcb.isEmpty() && ready_queue.size() - 1 == 0 ){
                         execution_sequence.add((ready_queue.get(0).p_id));
                         time_chart.add(Time);
                    }
                    ready_queue.remove(temp1);
                }
            }
            data1= new String[1][Switch];
            column1 = new String[Switch];
            data2 = new String[1][Switch + 1];
            column2 = new String[Switch + 1];

            for(int i = 0; i< execution_sequence.size(); i++){
                data1[0][i] = "P" + execution_sequence.get(i); 
            }
            int min_at = arrivalTime.get(0) ;
            for(int i = 0; i<arrivalTime.size(); i++){
                if(min_at > arrivalTime.get(i)){
                    min_at = arrivalTime.get(i);
                }
            }
            data2[0][0] = "" + min_at;
            for(int i = 1; i<= time_chart.size(); i++){
                data2[0][i] = "" + time_chart.get(i - 1);
            }
        }
    }

    // Method to Print Finish Time, Turnaround Time & Waiting Time
    static public void printall(){

        for(int i = 1; i <= aSize; i++){
            data[i][3] = "" + pcb[i-1].ft;
        }

        for(int i = 1; i<= aSize; i++){
            data[i][4] = "" + pcb[i-1].tat;
        }

        for(int i = 1; i <= aSize; i++){
            data[i][5] = "" + pcb[i-1].wt;
        }
    }

    // Method to store process data in pcb
    static public void process_data(){
        pcb = new PCB[arrivalTime.size()];
        for(int i = 0; i< arrivalTime.size(); i++){
            pcb[i] = new PCB();
            pcb[i].p_id = i +1;
            pcb[i].at = arrivalTime.get(i);
            pcb[i].bt = burstTime.get(i);
        }
    }

    // Method to Print Average Turnaround Time & Waiting Time
    static public void print_avg_tat_at(){
        double sum_tat = 0;
        double sum_wt = 0;
        for(PCB p : pcb){
            sum_tat += p.tat;
            sum_wt += p.wt;
        }

        double avg_tat = sum_tat / arrivalTime.size();
        double avg_wt = sum_wt / arrivalTime.size();

        jl8.setText((int)sum_tat +"/"+ arrivalTime.size() +" = "+ String.format("%.2f" ,avg_tat));
        jl9.setText((int)sum_wt +"/"+ arrivalTime.size() +" = "+ String.format("%.2f" ,avg_wt));

    }
    
    //ALL RIGHTS RESERVED. COPYRIGHT - ARYAN SINGH
    //RESIZE THE FRAME IF THE COMPONENTS DOESN'T LOAD.
    //RECOMMENDED TO CLOSE THE PREVIOUS OUTPUT WINDOW(s) IF ANY BEFORE SOLVING ANOTHER PROBLEM.


    public static void main(String [] args){
        String Algorithms [] = {"First Come First Serve, FCFS", "Shortest Job First, SJF", "Shortest Remaining Time First, SRTF"};

        jf1 = new JFrame("CPU SCHEDULING");
        jf1.setSize(400,350);
        jf1.setVisible(true);
        jf1.setLayout(null);
        jf1.setDefaultCloseOperation(3);
        
        jl1 = new JLabel("INPUT");
        jl1.setBounds(20, 0, 100, 40);
        jf1.add(jl1);

        jl2 = new JLabel("Algorithm");
        jl2.setBounds(20, 30, 100, 40);
        jf1.add(jl2);

        jcb = new JComboBox<>(Algorithms);
        jcb.setBounds(20, 65, 220, 20);
        jf1.add(jcb);

        jl3 = new JLabel("Arrival Times");
        jl3.setBounds(20, 85, 100, 40);
        jf1.add(jl3);

        tf1 = new JTextField("e.g. 0 2 4 6 8");
        tf1.setBounds(20, 120, 220, 30);
        jf1.add(tf1);

        jl4 = new JLabel("Burst Times");
        jl4.setBounds(20, 150, 100, 40);
        jf1.add(jl4);

        tf2 = new JTextField("e.g. 2 4 6 8 10");
        tf2.setBounds(20, 185, 220, 30);
        jf1.add(tf2);

        jb = new JButton("Solve");
        jb.setBounds(20 , 240, 75, 30);
        jb.addActionListener(new Action1());
        jf1.add(jb);
    }

    static class Action1 implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            jf2 = new JFrame("OUTPUT WINDOW");
            jf2.setVisible(true);
            jf2.setSize(620, 400);
            jf2.setLayout(null);

            jl5 = new JLabel("OUTPUT");
            jl5.setBounds(20, 0, 100, 40);
            jf2.add(jl5);

            jl6 = new JLabel();
            jl6.setBounds(550, 0, 40, 40);
            jf2.add(jl6);

            jl7 = new JLabel("Average");
            jl7.setBounds(330, 330, 100, 40);
            jf2.add(jl7);

            jl8 = new JLabel();
            jl8.setBounds(400, 330, 100, 40);
            jf2.add(jl8);

            jl9 = new JLabel();
            jl9.setBounds(500, 330, 100, 40);
            jf2.add(jl9);

            jl10 = new JLabel("Gantt Chart");
            jl10.setBounds(250, 40, 100, 40);
            jf2.add(jl10);

            jl11 = new JLabel("Process ID :");
            jl11.setBounds(100, 66, 100, 40);
            jf2.add(jl11);

            jl12 = new JLabel("Time :");
            jl12.setBounds(100, 85, 100, 40);
            jf2.add(jl12);

            int z = processes();

            String column[] = {"Job", "Arrival Time", "Burst Time", "Finish Time", "Turnaround Time", "Waiting Time"};
            data = new String[12][6];
            data[0][0] = "Job";
            data[0][1] = "Arrival Time";
            data[0][2] = "Burst Time";
            data[0][3] = "Finish Time";
            data[0][4] = "Turnaround Time";
            data[0][5] = "Waiting Time";

            
            for(int i = 1; i <= z; i++){
                data[i][0] = "P" + i;
            }

            printArrivalTime();
            getBurstTime();
            printBurstTime();
            process_data();
            calculate();
            printall();
            print_avg_tat_at();

            jt = new JTable(data, column);
            jt.setBounds(15, 140, 570, 200);
            jf2.add(jt);

            
            
            for(int i = 0; i< column1.length; i++){
                column1[i] = "P" + i;
            }

            jt1  = new JTable(data1, column1);
            jt1.setBounds(180, 80, 200, 16);
            jf2.add(jt1);

            for(int i = 0; i< column2.length; i++){
                column2[i] = "T" + i;
            }

            jt2 = new JTable(data2, column2);
            jt2.setBounds(175, 100, 245, 16 );
            jf2.add(jt2);

        }
    }
}
class PCB{
    int p_id, at, bt, ft, wt, tat;
}
