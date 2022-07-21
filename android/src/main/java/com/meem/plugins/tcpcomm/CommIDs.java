package com.meem.plugins.tcpcomm;

public class CommIDs {
    // packet IDs
    byte acknowledge = 0x00;
    byte failure = 0x01;
    byte header = 0x02;
    byte emptyOrCorrupted = 0x08;

    // commands
    byte network = 0x04;
    byte password = 0x05;
    byte finish = 0x09;
}
