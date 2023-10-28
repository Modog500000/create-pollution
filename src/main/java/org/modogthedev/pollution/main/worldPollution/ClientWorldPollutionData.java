package org.modogthedev.pollution.main.worldPollution;

public class ClientWorldPollutionData {

    private static int chunkPollution;
    private static int chunkCurrentPollution;

    public static void set(int newChunkPollution) {
        ClientWorldPollutionData.chunkPollution = newChunkPollution;
    }
    public static void setCurrentPollution(int newChunkPollution) { ClientWorldPollutionData.chunkCurrentPollution = newChunkPollution; }
    public static int getChunkPollution() {
        return chunkPollution;
    }
    public static int getChunkCurrentPollution() { return chunkCurrentPollution; }
}
