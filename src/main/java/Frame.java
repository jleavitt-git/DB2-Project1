import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

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
            sb.append((char) getContentAtIndex(recordIndex+i));
        }
        return sb.toString();
    }

    public void updateRecord(int record, String data){
        int recordIndex = record*40;
        char[] bytes = data.toCharArray();
        for(int i = 0; i < 40; i++){
            setContentAtIndex(recordIndex+i, (byte) bytes[i]);
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

    public void writeToFile() throws IOException {
        String filename = "F" + getBlockId() + ".txt";
        BufferedWriter file = new BufferedWriter(new FileWriter(filename));
        for(int i = 0; i < content.length; i++){
            file.write((char) content[i]);
        }
        file.close();
    }

    public void loadFromFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        content = Files.readAllBytes(path);
        //System.out.println("DEBUG " + path.toString().substring(1,2));
        setBlockId(Integer.parseInt(path.toString().substring(1,2)));
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
