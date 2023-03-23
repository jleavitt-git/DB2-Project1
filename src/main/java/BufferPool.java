public class BufferPool {
    private Frame[] buffers;

    public BufferPool(int bufferSize){
        buffers = new Frame[bufferSize];
    }
}
