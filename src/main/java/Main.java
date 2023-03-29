import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.System.exit;

public class Main {

    private static BufferPool buf;

    public static void main(String[] args) throws IOException {
        System.out.println("Initializing...");

        buf = new BufferPool();

        //Check for proper command line arguments
        if(args.length != 1){
            System.out.println("Improper input arguments. Expects number for amount of buffers");
        }
        //Read in buffer size and init the bufferPool
        BufferedReader input = new BufferedReader(
                new InputStreamReader(System.in)
        );
        buf.initialize(Integer.parseInt(args[0]));
        System.out.println("Initialized " + args[0] + " buffers.\n");

        //Program functionality here
        while(true){
            System.out.println("Ready for next command...");
            String[] in = input.readLine().split(" ");
            String setArgs = "";
            if(in.length > 2){
                for(int i = 2; i < in.length; i++){
                    setArgs+=in[i] + " ";
                }
            }

            //Switch on command or error on unknown command, automatically makes all commands uppercase
            switch (in[0].toUpperCase()) {
                case "GET" -> get(Integer.parseInt(in[1]));
                //SET command takes 2, 3, 4, 5 because of record layout being split by space
                case "SET" -> set(Integer.parseInt(in[1]), setArgs);
                case "PIN" -> pin(Integer.parseInt(in[1]));
                case "UNPIN" -> unpin(Integer.parseInt(in[1]));
                case "EXIT" -> quitApp();
                default -> System.out.println("Not a command. Options are [GET, SET, PIN, UNPIN");
            }
        }
    }

    /**
     * Quits the application (breaks while true loop)
     */
    private static void quitApp() {
        System.out.println("Quitting out...");
        exit(1);
    }

    /**
     *
     * @param k record number
     * @throws IOException
     *
     * prints data if successful otherwise prints error
     */
    public static void get(int k) throws IOException {
        if(1 > k|| k > 699){
            System.out.println("Error, no record at that location");
            return;
        }
        String record = buf.get(k);
        if(record == null){
            System.out.println("The corresponding block #" + (int) Math.floor((double)k / 100.00) + " cannot be accessed from disk because the memory buffers are full");
        }
        else{
            System.out.println("Record at #" + k + ":\n" + record);
        }
    }
    /**
     *
     * @param k record number
     * @param s String of bytes to be set at record k
     * @throws IOException
     *
     * prints data if successful otherwise prints error
     */
    public static void set(int k, String s) throws IOException {
        //System.out.println("Data: " + s);
        if(1 > k|| k > 699){
            System.out.println("Error, no record at that location");
            return;
        }
        if(s.length() != 43){
            System.out.println("Error, provided data is not 40 bytes");
            System.out.println("LENG: " + s.length() + " DATA: " + s);
            return;
        }
        else{
            s = s.substring(1, 41);
        }
        boolean set = buf.set(k, s);
        if(!set){
            System.out.println("The corresponding block #" + (int) Math.floor((double)k / 100.00) + " cannot be accessed from disk because the memory buffers are full");
        }
        else{
            System.out.println("Record successfully updated at #" + k);
        }
    }

    /**
     *
     * @param bid block id (file ID)
     * @throws IOException
     *
     * prints whether pin was successful
     */
    public static void pin(int bid) throws IOException {
        if(1 > bid|| bid > 7){
            System.out.println("Error, no record at that location");
            return;
        }
        boolean pinned = buf.pin(bid);
        if(pinned){
            System.out.println("Block #" + bid + " has been pinned");
        }
        else{
            System.out.println("The corresponding block #" + bid + " cannot be accessed from disk because the memory buffers are full");
        }
    }

    /**
     *
     * @param bid block id (file ID)
     * @throws IOException
     *
     * prints whether unpin was successful
     */
    public static void unpin(int bid) throws IOException {
        if (1 > bid || bid > 7) {
            System.out.println("Error, no record at that location");
            return;
        }
        boolean pinned = buf.unpin(bid);
        if (pinned) {
            System.out.println("Block #" + bid + " has been unpinned");
        } else {
            System.out.println("The corresponding block #" + bid + " cannot be accessed from disk because the memory buffers are full");
        }
    }
}
