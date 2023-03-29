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
    private byte[] content; //Data
    private boolean dirty; //Dirty flag
    private boolean pinned; //Pinned flag
    private int  blockId; //Block id of this frame

    /**
     * Init empty Frame
     */
    public Frame(){
        blockId = -1;
        content = new byte[4096];
        dirty = false;
        pinned = false;
    }
    /**
     * Init empty Frame
     */
    public void init(){
        blockId = -1;
        content = new byte[4096];
        dirty = false;
        pinned = false;
    }

    /**
     * Takes the record number (not record and file number) and returns a string of the record
     * @param record record number (1-100) of this frame.
     * @return String of data from that record
     */
    public String getRecord(int record){
        StringBuilder sb = new StringBuilder();
        int recordIndex = record*40;
        for(int i = 0; i < 40; i++){
            sb.append((char) getContentAtIndex(recordIndex+i));
        }
        return sb.toString();
    }

    /**
     * Takes the record number (not record and file number) and updates the record with new data.
     * @param record record number (1-100) of this frame
     * @param data String of data to update record with
     */
    public void updateRecord(int record, String data){
        int recordIndex = record*40;
        char[] bytes = data.toCharArray();
        for(int i = 0; i < 40; i++){
            setContentAtIndex(recordIndex+i, (byte) bytes[i]);
        }
        dirty = true;
    }

    /**
     * Return the data at a given index of the content array
     * @param index index to access of the content array
     * @return the data within the content array at the given index
     */
    public byte getContentAtIndex(int index){
        return content[index];
    }

    /**
     * Set the given data to the passed index of the content array
     * @param index index to update
     * @param data byte of data to update with
     */
    public void setContentAtIndex(int index, byte data){
        content[index] = data;
    }

    /**
     * Write the entire contents of this frame to disk (file) of the given blockID as chars (not bytes)
     * @throws IOException
     */
    public void writeToFile() throws IOException {
        String filename = "F" + getBlockId() + ".txt";
        BufferedWriter file = new BufferedWriter(new FileWriter(filename));
        for(int i = 0; i < content.length; i++){
            file.write((char) content[i]);
        }
        file.close();
    }

    /**
     * Load the entire contents of a file (Block) from disk into this frame. Also set blockId
     * @param fileName name of file
     * @throws IOException
     */
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
