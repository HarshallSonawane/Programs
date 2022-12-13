import java.io.*;
import java.util.HashSet;

public class Phase2 {
    static char M[][];
    static char R[];
    static char IR[];
    static int IC;
    static int c, RA;
    static char buffer[] = new char[41];
    static String s;
    static BufferedReader br;
    static HashSet<Integer> set2 = new HashSet<>();

    class PCB {
        static int JID, SI, TI, PI = 0, TTC, LLC, TTL, TLL, PTR;
    }

    static void init() {
        M = new char[300][4];
        IR = new char[4];
        R = new char[4];
        PCB.SI = 0;
        PCB.TI = 0;
        PCB.PI = 0;
        PCB.TTC = 0;
        PCB.LLC = 0;
    }

    static void printMemory() {

        for (int i = 0; i < M.length; i++) {
            System.out.print("M[" + i + "][] ");

            for (int j = 0; j < 4; j++) {
                System.out.print(M[i][j]);
            }
            System.out.println();
        }
    }

    private static void startExecution() throws Exception {
        IC = 0;
        executeUserProgram();
    }

    public static int allocateMap(int VA) {

        if (VA > 100) {
            PCB.PI = 2;
        }
        int PTE = PCB.PTR + (VA / 10);

        if (M[PTE][0] == '*' && IR[0] == 'P' && IR[1] == 'D') {
            PCB.PI = 3;
            return -1;
        }
        // for valid page fault

        if (M[PTE][0] == '*') {
            allocateFrame((PTE));
        }

        RA = (Integer.parseInt(String.valueOf(M[PTE][0]))) * 10 + (VA % 10);
        return RA;
    }

    static void MOS() throws Exception {

        if (!((IR[0] == 'G' && IR[1] == 'D') || (IR[0] == 'P' && IR[1] == 'D') || (IR[0] == 'S' && IR[1] == 'R')
                || (IR[0] == 'L' && IR[1] == 'R') || (IR[0] == 'C' && IR[1] == 'R') || (IR[0] == 'B' && IR[1] == 'T')
                || (IR[0] == 'H'))) {
            PCB.TTC++;

            if (PCB.TTC > PCB.TTL) {
                PCB.TI = 2;
            }
            PCB.PI = 1;
        }

        if (IR[0] != 'H') {
            String ts = String.valueOf(IR[2]);
            ts += String.valueOf(IR[3]);

            try {
                int t1 = Integer.parseInt(ts);
            } catch (Exception e) {
                PCB.PI = 2;
                if (IR[0] == 'P' && IR[1] == 'D') {
                    PCB.LLC++;
                    if (PCB.LLC > PCB.TLL)
                        terminate(2);
                }
            }
        }

        if (PCB.SI == 1 && PCB.TI == 0) {
            readF(RA);
            PCB.SI = 0;
        }

        if (PCB.SI == 2 && PCB.TI == 0) {
            writeF(RA);
            PCB.SI = 0;
        }

        if (PCB.SI == 3 && PCB.TI == 0) {
            terminate(0);
        }

        if (PCB.SI == 1 && PCB.TI == 2) {
            terminate(3);
        }

        if (PCB.SI == 2 && PCB.TI == 2) {
            writeF(RA);
            terminate(3);
        }

        if (PCB.SI == 3 && PCB.TI == 2) {
            terminate(3);
        }

        if (PCB.TI == 0 && PCB.PI == 1) {
            terminate(4);
        }

        if (PCB.TI == 0 && PCB.PI == 2) {
            terminate(5);

        }

        if (PCB.TI == 0 && PCB.PI == 3) {
            terminate(6);
        }

        if (PCB.TI == 2 && PCB.PI == 1) {
            terminate(3);
            terminate(4);
        }

        if (PCB.TI == 2 && PCB.PI == 2) {
            terminate(3);
            terminate(5);
        }

        if (PCB.TI == 2 && PCB.PI == 3) {
            terminate(3);
        }

    }

    private static void executeUserProgram() throws Exception {

        int t1 = 0;
        String ts = "";

        while (IR[0] != 'H') {

            RA = allocateMap(IC);

            if (PCB.PI != 0) {
                System.exit(1);
            }

            for (int i = 0; i < 4; i++)
                IR[i] = M[RA][i];

            IC += 1;
            String ch = Character.toString(IR[0]);
            ch += Character.toString(IR[1]);

            if (IR[0] != 'H') {
                ts = String.valueOf(IR[2]);
                ts += String.valueOf(IR[3]);
                try {
                    t1 = Integer.parseInt(ts);
                } catch (Exception e) {
                    PCB.PI = 2;
                    PCB.TTC++;
                    if (PCB.TTC > PCB.TTL) {
                        PCB.TI = 2;
                    }
                    MOS();
                    System.exit(1);
                }
            }

            RA = allocateMap(t1);

            if (PCB.PI != 0) {
                MOS();
                System.exit(1);
            }

            switch (ch) {
                case "GD":
                    PCB.TTC += 2;
                    if (PCB.TTC > PCB.TTL) {
                        PCB.TI = 2;
                        MOS();
                        System.exit(0);
                    }
                    PCB.SI = 1;
                    break;
                case "PD":
                    PCB.TTC++;
                    PCB.LLC++;
                    PCB.SI = 2;
                    if (PCB.LLC > PCB.TLL) {
                        terminate(2);
                        if (PCB.TTC > PCB.TTL) {
                            PCB.TI = 2;
                            MOS();
                        }
                        System.exit(0);
                    }
                    if (PCB.TTC > PCB.TTL) {
                        PCB.TI = 2;
                        MOS();
                        System.exit(0);
                    }
                    break;

                case "LR":
                    PCB.TTC += 1;
                    if (PCB.TTC > PCB.TTL) {
                        PCB.TI = 2;
                        MOS();
                        System.exit(0);
                    }
                    for (int i = 0; i < 4; i++) {
                        R[i] = M[RA][i];
                    }
                    break;
                case "SR":
                    PCB.TTC += 2;
                    if (PCB.TTC > PCB.TTL) {
                        PCB.TI = 2;
                        MOS();
                        System.exit(0);
                    }
                    for (int i = 0; i < 4; i++) {
                        M[RA][i] = R[i];
                    }
                    break;
                case "CR":
                    PCB.TTC += 1;
                    if (PCB.TTC > PCB.TTL) {
                        PCB.TI = 2;
                        MOS();
                        System.exit(0);
                    }
                    for (int i = 0; i < 4; i++) {
                        if (R[i] == M[RA][i]) {
                            c = 1;
                        } else {
                            c = 0;
                            break;
                        }
                    }
                    break;
                case "BT":
                    PCB.TTC += 1;
                    if (PCB.TTC > PCB.TTL) {
                        PCB.TI = 2;
                        MOS();
                        System.exit(0);
                    }
                    if (c == 1) {
                        IC = RA;
                    }
                    break;
            }
            MOS();
        }

        if (IR[0] == 'H') {
            PCB.TTC += 1;
            PCB.SI = 3;

            if (PCB.TTC > PCB.TTL) {
                PCB.TI = 2;
                MOS();
                System.exit(0);
            }
        }
    }

    private static void readF(int t1) throws Exception {
        s = br.readLine();

        if (s.startsWith("$END")) {
            terminate(1);
        }

        int t = t1;
        int c = 0;

        for (int i = 0; i < s.length(); i++) {
            M[t1][c] = s.charAt(i);
            c++;

            if (c == 4) {
                c = 0;
                t1++;
            }

            if (t == (t1 + 10))
                break;
        }
    }

    private static void writeF(int t1) {
        int t = t1;
        int c = 0;
        String s = "";

        while (M[t1][c] != 0) {
            s += M[t1][c++];

            if (c == 4) {
                t1++;
                c = 0;
            }

            if (t == (t1 + 10))
                break;
        }

        try {
            FileWriter outputF = new FileWriter("Output.txt", true);
            outputF.write(s);
            outputF.write("\n");
            outputF.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void terminate(int n) {
        System.out.println("***********************************************************");

        switch (n) {
            case 0:
                System.out.println("No Error");
                break;
            case 1:
                System.out.println("Out of Data");
                break;
            case 2:
                System.out.println("Line Limit Exceeded");
                break;
            case 3:
                System.out.println("Time Limit Exceeded");
                break;
            case 4:
                System.out.println("Operation Code Error");
                break;
            case 5:
                System.out.println("Operand Error");
                break;
            case 6:
                System.out.println("Invalid Page Fault");
                break;
        }

        System.out.println("***********************************************************");
        System.out.println(" JID " + " TTL " + " TTC " + " TLL " + " LLC " + " SI " + " PI " + " TI " + " IC ");
        System.out.println(
                "  " + PCB.JID + "    " + PCB.TTL + "    " + PCB.TTC + "    " + PCB.TLL + "    " + PCB.LLC + "   "
                        + PCB.SI + "    " + PCB.PI + "   " + PCB.TI + "   " + IC);
        System.out.println("***********************************************************");

    }

    public static void allocatePTR() {
        int min = 0;
        int max = 9;
        PCB.PTR = ((int) Math.floor(Math.random() * (max - min + 1) + min)) * 10;
        int j = -1;

        if ((!(set2.contains(PCB.PTR))) && (!(set2.contains(PCB.PTR / 10)))) {
            set2.add(PCB.PTR);
        } else {
            allocatePTR();
        }
        for (int i = PCB.PTR; i < PCB.PTR + 10; i++) {
            while (j++ < 3)
                M[i][j] = '*';
            j = -1;
        }

    }

    public static int allocateFrame(int c) {

        int min = 0;
        int max = 9;
        int random_int;
        random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);

        if ((!(set2.contains(random_int))) && (!(set2.contains(random_int * 10)))) {
            set2.add(random_int);
        } else {
            allocateFrame(c);
        }
        while (M[c][0] != '*') {
            c++;
        }

        String s = Integer.toString(random_int);
        char f1 = ' ', f2 = ' ';

        if (s.length() > 1) {
            f1 = s.charAt(0);
            f2 = s.charAt(1);
            M[c][0] = f1;
            M[c][1] = f2;
            M[c][2] = ' ';
            M[c][3] = ' ';
        } else {
            M[c][0] = s.charAt(0);
            M[c][1] = ' ';
            M[c][2] = ' ';
            M[c][3] = ' ';
        }
        return Integer.valueOf(String.valueOf(M[c][0]));
    }

    public static void main(String[] args) throws Exception {

        FileReader file = new FileReader("Input.txt");
        br = new BufferedReader(file);
        s = br.readLine();
        buffer = s.toCharArray();

        while (s != null) {
            if (buffer[0] == '$' && buffer[1] == 'A' && buffer[2] == 'M' && buffer[3] == 'J') {

                init();
                allocatePTR();
                String t = String.valueOf(buffer[4]);
                t += String.valueOf(buffer[5]);
                t += String.valueOf(buffer[6]);
                t += String.valueOf(buffer[7]);
                PCB.JID = Integer.parseInt(t);
                String tl = String.valueOf(buffer[8]);
                tl += String.valueOf(buffer[9]);
                tl += String.valueOf(buffer[10]);
                tl += String.valueOf(buffer[11]);
                PCB.TTL = Integer.parseInt(tl);
                String tll = String.valueOf(buffer[12]);
                tll += String.valueOf(buffer[13]);
                tll += String.valueOf(buffer[14]);
                tll += String.valueOf(buffer[15]);
                PCB.TLL = Integer.parseInt(tll);

                int k = 0;
                s = br.readLine();
                int ct = 0;

                int temp;
                k = allocateFrame(PCB.PTR) * 10;
                temp = k;
                while (!(buffer[0] == '$' && buffer[1] == 'D' && buffer[2] == 'T' && buffer[3] == 'A')) {

                    while (ct != s.length()) {
                        for (int i = 0; i < 4; i++) {
                            if (ct == s.length())
                                break;
                            buffer[i] = s.charAt(ct);
                            ct++;
                        }
                        for (int i = 0; i < 4; i++) {
                            if (buffer[0] == 'H') {
                                M[k][0] = 'H';
                                break;
                            }
                            M[k][i] = buffer[i];
                        }
                        k++;
                        if (k > ((temp + 10) - 1)) {
                            k = allocateFrame(PCB.PTR) * 10;
                        }
                    }
                    s = br.readLine();
                    buffer = s.toCharArray();
                }
            }

            if (buffer[0] == '$' && buffer[1] == 'D' && buffer[2] == 'T' && buffer[3] == 'A') {
                startExecution();
            }

            if (buffer[0] == '$' && buffer[1] == 'E' && buffer[2] == 'N' && buffer[3] == 'D') {
                printMemory();
                try {
                    FileWriter outputF = new FileWriter("Output.txt", true);
                    outputF.write("\n\n");
                    outputF.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
                System.out.println("Program Executed Successfully!");
            }
            s = br.readLine();

            if (s == null) {
                break;
            }
            buffer = s.toCharArray();
        }
    }
}