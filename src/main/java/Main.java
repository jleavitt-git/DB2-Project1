import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Initializing...");
        BufferPool buf = new BufferPool();

        if(args.length != 1){
            System.out.println("Improper input arguments. Expects number for amount of buffers");
        }
        BufferedReader input = new BufferedReader(
                new InputStreamReader(System.in)
        );

        buf.initialize(Integer.parseInt(args[0]));
        System.out.println("Initialized " + args[0] + " buffers.\n");


        while(true){
            System.out.println("Ready for next command...");
            String[] in = input.readLine().split(" ");

            switch(in[0].toUpperCase()){
                case "GET":
                    get(Integer.parseInt(in[1]));
                    break;
                case "SET":
                    set(Integer.parseInt(in[1]), in[2]);
                    break;
                case "PIN":
                    pin(Integer.parseInt(in[1]));
                    break;
                case "UNPIN":
                    unpin(Integer.parseInt(in[1]));
                    break;
                default:
                    System.out.println("Not a command. Options are [GET, SET, PIN, UNPIN");
            }
        }
    }

    public static void get(int bid){

    }

    public static void set(int bid, String s){

    }

    public static void pin(int bid){

    }

    public static void unpin(int bid){

    }
}
