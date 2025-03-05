package com.vsked.sqlitemanager.domain;

public class VQueryCondition {

    private VQueryConditionName VQueryConditionName;

    private VQueryConditionValue VQueryConditionValue;

	public VQueryCondition(VQueryConditionName VQueryConditionName, VQueryConditionValue VQueryConditionValue) {
		this.VQueryConditionName = VQueryConditionName;
		this.VQueryConditionValue = VQueryConditionValue;
	}

	public VQueryConditionName getConditionName() {
		return VQueryConditionName;
	}

	public VQueryConditionValue getConditionValue() {
		return VQueryConditionValue;
	}

}
