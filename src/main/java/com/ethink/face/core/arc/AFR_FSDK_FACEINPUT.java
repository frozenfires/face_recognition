package com.ethink.face.core.arc;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class AFR_FSDK_FACEINPUT extends Structure {
    
    public MRECT rcFace;
    public int lOrient;
    
    @Override
    protected List getFieldOrder() { 
        return Arrays.asList(new String[] { 
             "rcFace", "lOrient"
        });
    }
}
