package com.jetbrains.internship2024.another;

import com.jetbrains.internship2024.SomeClassWith3dPartyDependency;

public class ClassA {
    public SomeClassWith3dPartyDependency doAnotherA() {
        SomeClassWith3dPartyDependency someClassWith3dPartyDependency = new SomeClassWith3dPartyDependency();
        someClassWith3dPartyDependency.doSomething3dParty();
        return someClassWith3dPartyDependency;
    }
}
