package io.metersphere.platform.impl;

import io.metersphere.platform.api.AbstractPlatformMetaInfo;

public class JiraPlatformMetaInfo extends AbstractPlatformMetaInfo {

    public static final String KEY = "Jira";

    public JiraPlatformMetaInfo() {
        super(JiraPlatformMetaInfo.class.getClassLoader());
    }

    @Override
    public String getVersion() {
        return "main";
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
