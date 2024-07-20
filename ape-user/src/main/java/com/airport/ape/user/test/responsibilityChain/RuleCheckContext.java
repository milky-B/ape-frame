package com.airport.ape.user.test.responsibilityChain;

import org.apache.commons.collections4.CollectionUtils;

public class RuleCheckContext {

    /**
     * 规则检查结果
     */
    private RuleCheckResult ruleCheckResult = new RuleCheckResult();

    public RuleCheckResult getRuleCheckResult() {
        return ruleCheckResult;
    }

    public boolean hasError() {
        return CollectionUtils.isNotEmpty(ruleCheckResult.getFailedMsgList());
    }

}