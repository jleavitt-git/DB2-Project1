import static java.lang.System.exit;

public class Frame {
    private byte[] content;
    private boolean dirty;
    private boolean pinned;
    private int  blockId;

    public Frame(int bid){
        content = new byte[4096];
        blockId = bid;
        dirty = false;
        pinned = false;
    }

    public Frame(int bid, byte[] content){
        if(content.length != 4096){
            System.out.println("Error: Content is not of size [4096]");
            exit(1);
        }
        blockId = bid;
        this.content = content;
        dirty = false;
        pinned = false;
    }

    public Frame(){
        blockId = -1;
        content = new byte[4096];
        dirty = false;
        pinned = false;
    }

    public String getRecord(int record){
        StringBuilder sb = new StringBuilder();
        int recordIndex = record*40;
        for(int i = 0; i < 40; i++){
            sb.append(getContentAtIndex(recordIndex));
            recordIndex++;
        }
        return sb.toString();
    }

    public void updateRecord(int record, byte[] data){
        int recordIndex = record*40;
        for(int i = 0; i < 40; i++){
            setContentAtIndex(recordIndex, data[i]);
            recordIndex++;
        }
        dirty = true;
    }

    public byte getContentAtIndex(int index){
        return content[index];
    }
    public byte[] getContent() {
        return content;
    }

    public void setContentAtIndex(int index, byte data){
        content[index] = data;
    }
    public void setContent(byte[] content) {
        this.content = content;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

}
