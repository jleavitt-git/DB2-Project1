Jake Leavitt, #########

Section 1:

    1. Place data txt files into the top level directory of this project
    2. Compile the java files in the /src/main/java package (I used intelliJ IDEA to do this automatically)
    3. Run Main.java providing a number as the initial bufferPool size.
    4. input 'exit' to quit the program.

Section 2.
    NOTE: Test output looks different, but all information is printed and all tests were successful
    NOTE2: Frame# is zero indexed, unlike the Block (File) indexing which starts at 1.


    Test Output running /java Main.java 3
    Initializing...
    Initialized 3 buffers.

    Ready for next command...
    Set 430 "F05-Rec450, Jane Do, 10 Hill Rd, age020."
    File is not in buffer, need to load it
    Setting data from File #5 into frame #0
    Record successfully updated at #430
    Ready for next command...
    Get 430
    File is not in buffer, need to load it
    Loading data from File #5, frame #0
    Record at #430:
    F05-Rec430, Name430, address430, age430.
    Ready for next command...
    Get 20
    File is not in buffer, need to load it
    Loading data from File #1, frame #1
    Record at #20:
    F01-Rec020, Name020, address020, age020.
    Ready for next command...
    Set 430 "F05-Rec450, John Do, 23 Lake Ln, age056."
    Setting data from File #5 into frame #0
    Record successfully updated at #430
    Ready for next command...
    Pin 5
    Pinning block in frame #0
    Block #5 has been pinned
    Ready for next command...
    Unpin 3
    File is not in buffer, need to load it
    Block #3 is already unpinned
    Block #3 has been unpinned
    Ready for next command...
    Get 430
    Loading data from File #5, frame #0
    Record at #430:
    F05-Rec450, John Do, 23 Lake Ln, age056.
    Ready for next command...
    Pin 5
    Block #5 is already pinned
    Block #5 has been pinned
    Ready for next command...
    Get 646
    File is not in buffer, need to load it
    Overwrote block #1
    Loading data from File #7, frame #1
    Record at #646:
    F07-Rec646, Name646, address646, age646.
    Ready for next command...
    Pin 3
    Pinning block in frame #2
    Block #3 has been pinned
    Ready for next command...
    Set 10 "F01-Rec010, Tim Boe, 09 Deer Dr, age009."
    File is not in buffer, need to load it
    Overwrote block #7
    Setting data from File #1 into frame #1
    Record successfully updated at #10
    Ready for next command...
    Unpin 1
    File is not in buffer, need to load it
    Block #7 is dirty, writing to disk
    Overwrote block #7
    Block #1 is already unpinned
    Block #1 has been unpinned
    Ready for next command...
    Get 355
    File is not in buffer, need to load it
    Overwrote block #1
    Loading data from File #4, frame #1
    Record at #355:
    F04-Rec355, Name355, address355, age355.
    Ready for next command...
    Pin 2
    File is not in buffer, need to load it
    Overwrote block #4
    Pinning block in frame #1
    Block #2 has been pinned
    Ready for next command...
    Get 156
    Loading data from File #2, frame #1
    Record at #156:
    F02-Rec156, Name156, address156, age156.
    Ready for next command...
    Set 10 "F01-Rec010, No Work, 31 Hill St, age100."
    File is not in buffer, need to load it
    The corresponding block #0 cannot be accessed from disk because the memory buffers are full
    Ready for next command...
    Pin 7
    File is not in buffer, need to load it
    The corresponding block #7 cannot be accessed from disk because the memory buffers are full
    Ready for next command...
    Get 10
    File is not in buffer, need to load it
    The corresponding block #0 cannot be accessed from disk because the memory buffers are full
    Ready for next command...
    Unpin 3
    Unpinning block in frame #2
    Block #3 has been unpinned
    Ready for next command...
    Unpin 2
    Unpinning block in frame #1
    Block #2 has been unpinned
    Ready for next command...
    Get 10
    File is not in buffer, need to load it
    Overwrote block #3
    Loading data from File #1, frame #2
    Record at #10:
    F01-Rec010, Name010, address010, age010.
    Ready for next command...
    Pin 6
    File is not in buffer, need to load it
    Overwrote block #2
    Pinning block in frame #1
    Block #6 has been pinned
    Ready for next command...
    exit
    Quitting out...

    Process finished with exit code 1

Section 3.
    I followed the outline given by the homework pretty closely, but used a few tactics to consolidate by code.
    All of the Frame.java class runs without validation, validation is done in the Maine or BufferPool so that
    base functionality of a Frame can be simplified.

    Eviction round robin is handled with the nextToEvict integer in BufferPool.java, which keeps track of the last
    buffer removed. This skips pinned frames so if frame #2 was pinned and #3 was evicted, then #2 was unpinned,
    it would still be evicted after 4,5,6,0,1.

    The main class is designed to pass all logic to the BufferPool class, leaving the only logic in main to parse
    inputs and announce if the logic asked for was completely properly.