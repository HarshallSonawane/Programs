import java.io.*;

public class Phase1 {
    static char M[][];
    static char R[] = new char[4];
    static char IR[] = new char[4];
    static int IC;
    static int c, SI;
    static char buffer[] = new char[41];
    static String s;
    static BufferedReader br;

    static void init() {
        M = new char[100][4];
    }

    static void printMemory() {
        for (int i = 0; i < 100; i++) {
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

    private static void executeUserProgram() throws Exception {
        int t1 = 0;
        String ts = "";
        while (IR[0] != 'H') {
            for (int i = 0; i < 4; i++)
                IR[i] = M[IC][i];
            String ch = Character.toString(IR[0]);
            ch += Character.toString(IR[1]);
            if (IR[0] != 'H') {
                ts = String.valueOf(IR[2]);
                ts += String.valueOf(IR[3]);
                t1 = Integer.parseInt(ts);
            }
            IC += 1;
            switch (ch) {
                case "GD":
                    SI = 1;
                    break;
                case "PD":
                    SI = 2;

                    break;
                case "H":
                    SI = 3;

                    break;
                case "LR":
                    for (int i = 0; i < 4; i++) {
                        R[i] = M[t1][i];
                    }
                    break;
                case "SR":
                    for (int i = 0; i < 4; i++) {
                        M[t1][i] = R[i];
                    }
                    break;
                case "CR":
                    for (int i = 0; i < 4; i++) {
                        if (R[i] == M[t1][i]) {
                            c = 1;
                        } else {
                            c = 0;
                            break;
                        }
                    }
                    break;
                case "BT":
                    if (c == 1) {
                        IC = t1;
                    }
                    break;
            }

            if (SI == 1) {
                readF();
                SI = 0;
            }
            if (SI == 2) {
                writeF();
                SI = 0;
            }
            if (SI == 3) {
                terminate();
            }
        }
    }

    private static void readF() throws Exception {
        s = br.readLine();
        String ts = String.valueOf(IR[2]);
        ts += String.valueOf(IR[3]);
        int t1 = Integer.parseInt(ts);
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

    private static void writeF() {
        String ts = String.valueOf(IR[2]);
        ts += String.valueOf(IR[3]);
        int t1 = Integer.parseInt(ts);
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

    private static void terminate() {
        try {
            FileWriter outputF = new FileWriter("Output.txt", true);
            outputF.write("\n\n");
            outputF.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws Exception {
        FileReader file = new FileReader("Input.txt");
        br = new BufferedReader(file);
        s = br.readLine();
        buffer = s.toCharArray();
        while (s != null) {

            if (buffer[0] == '$' && buffer[1] == 'A' && buffer[2] == 'M' && buffer[3] == 'J') {
                init();
                int c = 0;
                int k = 0;
                s = br.readLine();
                int ct = 0;
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

                                break;
                            }
                            M[k][i] = buffer[i];
                        }
                        k++;
                    }
                    s = br.readLine();
                    buffer = s.toCharArray();
                }
                M[k][c] = 'H';
            }
            if (buffer[0] == '$' && buffer[1] == 'D' && buffer[2] == 'T' && buffer[3] == 'A') {
                startExecution();
            }

            if (buffer[0] == '$' && buffer[1] == 'E' && buffer[2] == 'N' && buffer[3] == 'D') {
                printMemory();
            }
            s = br.readLine();
            buffer = s.toCharArray();
        }
    }

}
