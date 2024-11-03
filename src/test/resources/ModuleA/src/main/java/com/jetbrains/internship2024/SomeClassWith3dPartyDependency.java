package com.jetbrains.internship2024;

import org.apache.commons.io.FilenameUtils;

public class SomeClassWith3dPartyDependency {

    public String doSomething3dParty() {
        return FilenameUtils.concat("qwerty", "asdfg");
    }
}
