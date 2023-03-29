import java.io.IOException;

public class BufferPool {
    private Frame[] buffers;

    private int nextToEvict;

    public BufferPool(){
    }

    public void initialize(int poolSize){
        buffers = new Frame[poolSize];
        nextToEvict = 0;
        for(int i = 0; i < buffers.length; i++){
            Frame frame = new Frame();
            buffers[i] = frame;
        }
    }

    //Given K record, first find loaded frame, else load frame
    public String get(int k) throws IOException {
        int file = (int) Math.floor((double)k / 100.00);
        file++; //Record 001 is file 1
        int record = (k % 100)-1;
        int frameNum = -1;

        for(int i = 0; i < buffers.length; i++){
            if(buffers[i].getBlockId() == file){
                frameNum = i;
            }
        }
        //Frame not loaded
        if(frameNum == -1){
            System.out.println("Need to get frame");
            frameNum = getEmptyFrame();
            if(frameNum == -1){
                //No available frame
                return null;
            }
            String fileName = "F" + file + ".txt";
            //System.out.println("Loading into frame #" + frameNum);
            buffers[frameNum].loadFromFile(fileName);
        }
        System.out.println("Loading data from frame #" + frameNum);
        return buffers[frameNum].getRecord(record);
    }

    public boolean set(int k, String data) throws IOException {
        int file = (int) Math.floor((double)k / 100.00);
        file++; //Record 001 is file 1
        int record = (k % 100)-1;
        int frameNum = -1;

        for(int i = 0; i < buffers.length; i++){
            if(buffers[i].getBlockId() == file){
                frameNum = i;
            }
        }
        //Frame not loaded
        if(frameNum == -1){
            System.out.println("Need to get frame");
            frameNum = getEmptyFrame();
            if(frameNum == -1){
                //No available frame
                return false;
            }
        }
        System.out.println("Setting data into frame #" + frameNum);
        buffers[frameNum].updateRecord(record, data);
        return true;
    }

    public boolean pin(int bid) throws IOException {
        for(int i = 0; i < buffers.length; i++){
            if(buffers[i].getBlockId() == bid){
                if(buffers[i].isPinned()){
                    System.out.println("Block #" + buffers[i].getBlockId() + " is already pinned");
                }
                else {
                    buffers[i].setPinned(true);
                }
                return true;
            }
        }
        //Block is not in memory so load it
        System.out.println("Need to get frame");
        int frameNum = getEmptyFrame();
        if(frameNum == -1){
            //No available frame
            return false;
        }
        String fileName = "F" + bid + ".txt";
        buffers[frameNum].loadFromFile(fileName);
        if(buffers[frameNum].isPinned()){
            System.out.println("Block #" + buffers[frameNum].getBlockId() + " is already unpinned");
        }
        else {
            buffers[frameNum].setPinned(true);
        }
        return true;
    }

    public boolean unpin(int bid) throws IOException {
        for(int i = 0; i < buffers.length; i++){
            if(buffers[i].getBlockId() == bid){
                if(buffers[i].isPinned()){
                    System.out.println("Block #" + buffers[i].getBlockId() + " is already pinned");
                }
                else {
                    buffers[i].setPinned(false);
                }
                return true;
            }
        }
        //Block is not in memory so load it
        System.out.println("Need to get frame");
        int frameNum = getEmptyFrame();
        if(frameNum == -1){
            //No available frame
            return false;
        }
        String fileName = "F" + bid + ".txt";
        buffers[frameNum].loadFromFile(fileName);
        if(buffers[frameNum].isPinned()){
            System.out.println("Block #" + buffers[frameNum].getBlockId() + " is already pinned");
        }
        else {
            buffers[frameNum].setPinned(false);
        }
        return true;
    }

    public int getEmptyFrame() throws IOException {
        for(int i = 0; i < buffers.length; i++){
            if(buffers[i].getBlockId() == -1){
                return i;
            }
        }
        for(int i = 0; i < buffers.length; i++){
            int toEvict = (nextToEvict+i) % buffers.length;
            if(buffers[toEvict].isPinned()){
                continue;
            }
            if (buffers[toEvict].isDirty()) {
                System.out.println("Block #" + buffers[toEvict].getBlockId() + " is dirty, writing to disk");
                buffers[toEvict].writeToFile();
                buffers[toEvict].setDirty(false);
            }
            System.out.println("Overwrote block #" + buffers[toEvict].getBlockId());
            nextToEvict = (toEvict+1)%buffers.length;
            //System.out.println("Next frame to be evicted is #" + (nextToEvict+1));
            return toEvict;
        }

        return -1; //All blocks pinned, none can be removed
    }

}
