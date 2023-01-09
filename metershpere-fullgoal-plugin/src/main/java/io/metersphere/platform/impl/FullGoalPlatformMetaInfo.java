package io.metersphere.platform.impl;

import io.metersphere.platform.api.AbstractPlatformMetaInfo;

/**
 * 效能平台元数据
 */
public class FullGoalPlatformMetaInfo extends AbstractPlatformMetaInfo {

    public static final String KEY = "fullGoal";

    public FullGoalPlatformMetaInfo() {
        super(FullGoalPlatformMetaInfo.class.getClassLoader());
    }

    @Override
    public String getVersion() {
        return "2.4.0";
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public boolean isThirdPartTemplateSupport() {
        return true;
    }
}
