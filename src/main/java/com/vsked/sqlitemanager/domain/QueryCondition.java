package com.vsked.sqlitemanager.domain;

public class QueryCondition {

    private QueryConditionName queryConditionName;

    private QueryConditionValue queryConditionValue;

	public QueryCondition(QueryConditionName queryConditionName, QueryConditionValue queryConditionValue) {
		this.queryConditionName = queryConditionName;
		this.queryConditionValue = queryConditionValue;
	}

	public QueryConditionName getConditionName() {
		return queryConditionName;
	}

	public QueryConditionValue getConditionValue() {
		return queryConditionValue;
	}

}
