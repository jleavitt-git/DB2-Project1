import java.io.IOException;

public class BufferPool {
    private Frame[] buffers; //Array of buffers

    private int nextToEvict; //Number of the frame to be evicted next, Round-Robin style.

    public BufferPool(){
    }

    /**
     * Initializes the bufferPool with a variable amount of Frames
     * @param poolSize amount of Frames
     */
    public void initialize(int poolSize){
        buffers = new Frame[poolSize];
        nextToEvict = 0;
        for(int i = 0; i < buffers.length; i++){
            Frame frame = new Frame();
            buffers[i] = frame;
        }
    }

    /**
     * Attempt to find frame in the buffer containing the data, otherwise get an empty (unpinned) frame
     * to load the data into. Then return the data. return null if data is not in a frame and the buffer is full.
     *
     * @param k record number
     * @return String if successfull, else null
     * @throws IOException
     */
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
            System.out.println("File is not in buffer, need to load it");
            frameNum = getEmptyFrame();
            if(frameNum == -1){
                //No available frame
                return null;
            }
            String fileName = "F" + file + ".txt";
            //System.out.println("Loading into frame #" + frameNum);
            buffers[frameNum].loadFromFile(fileName);
        }
        System.out.println("Loading data from File #"+ file +", frame #" + frameNum);
        return buffers[frameNum].getRecord(record);
    }

    /**
     * Attempt to find Frame with the block already loaded, else find an empty (unpinned) frame to load the data into,
     * then update the record. Also set flag to dirty, so it will be written out next time it's evicted.
     *
     * @param k record number
     * @param data String of data to be set at k record number
     * @return True if record update was successful, else False
     * @throws IOException
     */
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
            System.out.println("File is not in buffer, need to load it");
            frameNum = getEmptyFrame();
            if(frameNum == -1){
                //No available frame
                return false;
            }
        }
        System.out.println("Setting data from File #"+ file +" into frame #" + frameNum);
        buffers[frameNum].updateRecord(record, data);
        return true;
    }

    /**
     * Attempt to pin passed block number. If the frame is in the bufferPool then we check if it's already pinned,
     * and print if so and return True. If it's not pinned we pin it and return true.
     * If it's not in the bufferPool it is loaded in if possible, otherwise return false. If it is loaded in then
     * do the same logic as before.
     * false.
     * @param bid block number
     * @return
     * @throws IOException
     */
    public boolean pin(int bid) throws IOException {
        for(int i = 0; i < buffers.length; i++){
            if(buffers[i].getBlockId() == bid){
                if(buffers[i].isPinned()){
                    System.out.println("Block #" + buffers[i].getBlockId() + " is already pinned");
                }
                else {
                    System.out.println("Pinning block in frame #" + i);
                    buffers[i].setPinned(true);
                }
                return true;
            }
        }
        //Block is not in memory so load it
        System.out.println("File is not in buffer, need to load it");
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
            System.out.println("Pinning block in frame #" + frameNum);
            buffers[frameNum].setPinned(true);
        }
        return true;
    }

    /**
     * Attempt to pin passed block number. If the frame is in the bufferPool then we check if it's already unpinned,
     * and print if so and return True. If it's not unpinned we unpin it and return true.
     * If it's not in the bufferPool it is loaded in if possible, otherwise return false. If it is loaded in then
     * do the same logic as before.
     * false.
     * @param bid block number
     * @return
     * @throws IOException
     */
    public boolean unpin(int bid) throws IOException {
        for(int i = 0; i < buffers.length; i++){
            if(buffers[i].getBlockId() == bid){
                if(!buffers[i].isPinned()){
                    System.out.println("Block #" + buffers[i].getBlockId() + " is already unpinned");
                }
                else {
                    System.out.println("Unpinning block in frame #" + i);
                    buffers[i].setPinned(false);
                }
                return true;
            }
        }
        //Block is not in memory so load it
        System.out.println("File is not in buffer, need to load it");
        int frameNum = getEmptyFrame();
        if(frameNum == -1){
            //No available frame
            return false;
        }
        String fileName = "F" + bid + ".txt";
        buffers[frameNum].loadFromFile(fileName);
        if(!buffers[frameNum].isPinned()){
            System.out.println("Block #" + buffers[frameNum].getBlockId() + " is already unpinned");
        }
        else {
            System.out.println("Unpinning block in frame #" + frameNum);
            buffers[frameNum].setPinned(false);
        }
        return true;
    }

    /**
     * Attempts to find a frame that can be overwritten. Round-robin style of eviction with the nextToEvict variable.
     * If all blocks in the buffer are full then return -1.
     * If the next to be evicted file is unpinned and dirty, we first write the Frame to disk before returning the
     * frame number.
     *
     * @return id of block that can be overwritten
     * @throws IOException
     */
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
