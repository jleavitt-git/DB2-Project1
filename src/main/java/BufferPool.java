public class BufferPool {
    private Frame[] buffers;

    public BufferPool(){
    }

    public void initialize(int poolSize){
        buffers = new Frame[poolSize];
        for(Frame frame : buffers){
            frame = new Frame();
        }
    }

    public int getBufferId(int bid){
        for(int i = 0; i < buffers.length; i++){
            if(buffers[i].getBlockId() == bid){
                return i;
            }
        }
        return -1;
    }

    public int getEmptyFrame(){
        for(int i = 0; i < buffers.length; i++){
            if(buffers[i].getBlockId() == -1){
                return i;
            }
        }
        return -1; //No free block
    }

    public byte[] getContentFromBlock(int bid){
        return buffers[getBufferId(bid)].getContent();
    }
}
