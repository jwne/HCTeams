package com.mongodb;

enum BSONBinarySubType
{
    Binary((byte)0), 
    Function((byte)1), 
    OldBinary((byte)2), 
    UuidLegacy((byte)3), 
    UuidStandard((byte)4), 
    MD5((byte)5), 
    UserDefined((byte)(-128));
    
    private final byte value;
    
    private BSONBinarySubType(final byte value) {
        this.value = value;
    }
    
    public byte getValue() {
        return this.value;
    }
}
