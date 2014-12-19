package redis.clients.util;

import redis.clients.jedis.*;

public class ClusterNodeInformationParser
{
    private static final String SLOT_IMPORT_IDENTIFIER = "-<-";
    private static final String SLOT_IN_TRANSITION_IDENTIFIER = "[";
    public static final int SLOT_INFORMATIONS_START_INDEX = 8;
    public static final int HOST_AND_PORT_INDEX = 1;
    
    public ClusterNodeInformation parse(final String nodeInfo, final HostAndPort current) {
        final String[] nodeInfoPartArray = nodeInfo.split(" ");
        final HostAndPort node = this.getHostAndPortFromNodeLine(nodeInfoPartArray, current);
        final ClusterNodeInformation info = new ClusterNodeInformation(node);
        if (nodeInfoPartArray.length >= 8) {
            final String[] slotInfoPartArray = this.extractSlotParts(nodeInfoPartArray);
            this.fillSlotInformation(slotInfoPartArray, info);
        }
        return info;
    }
    
    private String[] extractSlotParts(final String[] nodeInfoPartArray) {
        final String[] slotInfoPartArray = new String[nodeInfoPartArray.length - 8];
        for (int i = 8; i < nodeInfoPartArray.length; ++i) {
            slotInfoPartArray[i - 8] = nodeInfoPartArray[i];
        }
        return slotInfoPartArray;
    }
    
    public HostAndPort getHostAndPortFromNodeLine(final String[] nodeInfoPartArray, final HostAndPort current) {
        final String stringHostAndPort = nodeInfoPartArray[1];
        final String[] arrayHostAndPort = stringHostAndPort.split(":");
        return new HostAndPort(arrayHostAndPort[0].isEmpty() ? current.getHost() : arrayHostAndPort[0], arrayHostAndPort[1].isEmpty() ? current.getPort() : Integer.valueOf(arrayHostAndPort[1]));
    }
    
    private void fillSlotInformation(final String[] slotInfoPartArray, final ClusterNodeInformation info) {
        for (final String slotRange : slotInfoPartArray) {
            this.fillSlotInformationFromSlotRange(slotRange, info);
        }
    }
    
    private void fillSlotInformationFromSlotRange(final String slotRange, final ClusterNodeInformation info) {
        if (slotRange.startsWith("[")) {
            final int slot = Integer.parseInt(slotRange.substring(1).split("-")[0]);
            if (slotRange.contains("-<-")) {
                info.addSlotBeingImported(slot);
            }
            else {
                info.addSlotBeingMigrated(slot);
            }
        }
        else if (slotRange.contains("-")) {
            final String[] slotRangePart = slotRange.split("-");
            for (int slot2 = Integer.valueOf(slotRangePart[0]); slot2 <= Integer.valueOf(slotRangePart[1]); ++slot2) {
                info.addAvailableSlot(slot2);
            }
        }
        else {
            info.addAvailableSlot(Integer.valueOf(slotRange));
        }
    }
}
